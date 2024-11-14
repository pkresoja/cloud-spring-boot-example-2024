package com.pequla.data.service;

import com.pequla.data.entity.CachedData;
import com.pequla.data.ex.BackendException;
import com.pequla.data.model.AccountModel;
import com.pequla.data.model.DataModel;
import com.pequla.data.model.PagedDataModel;
import com.pequla.data.model.UserModel;
import com.pequla.data.repository.CachedDataRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class DataService {

    private final BackendService service;
    private final CachedDataRepository repository;
    private LocalDateTime cachedCreatedAt;
    private LocalDateTime cachedDeletedAt;

    public Page<CachedData> getData(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Optional<CachedData> getById(Integer id) {
        // Update cache
        Optional<CachedData> optional = repository.findById(id);
        if (optional.isEmpty()) {
            updateDataById(id);
            return Optional.empty();
        }

        // Is cache expired
        CachedData data = optional.get();
        LocalDateTime now = LocalDateTime.now();
        if (data.getCachedAt().plusHours(1).isBefore(now)) {
            log.info("Cache expired for data id " + data.getId());
            updateDataById(id);
        }

        // Respond
        return repository.findById(id);
    }

    public Optional<CachedData> getByDiscordId(String discordId) {
        validateCache(repository.findByDiscordId(discordId));
        return repository.findByDiscordId(discordId);
    }

    public Page<CachedData> getAllByGuildId(String guildId, Pageable pageable) {
        return repository.findAllByGuildId(guildId, pageable);
    }

    public Optional<CachedData> getByUuid(String uuid) {
        // UUID contains dashes
        if (uuid.contains("-"))
            uuid = uuid.replace("-", "");
        validateCache(repository.findByUuid(uuid));
        return repository.findByUuid(uuid);
    }

    public Optional<CachedData> getByName(String name) {
        validateCache(repository.findByNameIgnoreCase(name));
        return repository.findByNameIgnoreCase(name);
    }

    public void syncNow() {
        try {
            int total = 5;
            for (int i = 0; i < total; i++) {
                PagedDataModel page = service.getData(i, 30);
                total = page.getTotalPages();

                for (DataModel model : page.getContent()) {
                    log.info("Syncing data for ID " + model.getId());
                    try {
                        // Exists for UUID
                        if (repository.existsByUuid(model.getUuid()))
                            repository.deleteByUuid(model.getUuid());

                        // Exists for Discord ID
                        String discord = model.getUser().getDiscordId();
                        if (repository.existsByDiscordId(discord))
                            repository.deleteByDiscordId(discord);

                        saveDataCommon(model);
                    } catch (BackendException e) {
                        if (e.getStatus() == 404) {
                            log.error("No data for id " + model.getId() + " found on backend");
                            if (repository.existsById(model.getId())) repository.deleteById(model.getId());
                            return;
                        }
                        log.error("Backend failed for id: " + model.getId() + " [UUID: " + model.getUuid() + "] HTTP STATUS:" + e.getStatus() + " BACKEND MESSAGE:" + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void syncCreated() throws IOException, InterruptedException {
        List<DataModel> data = service.getCreatedAfter(manageSyncTime(cachedCreatedAt));
        for (DataModel model : data) {
            try {
                log.info("Adding new data for ID " + model.getId());
                saveDataCommon(model);
            } catch (Exception e) {
                log.error("Failed to cache data for ID " + model.getId());
                if (e.getMessage().contains("GOAWAY")) {
                    try {
                    log.error("Attempting once again to cache data for  " + model.getId());
                    saveDataCommon(model);
                    } catch (Exception ex) {
                        log.error("Failed again, aborting caching for ID  " + model.getId());
                    }
                }
            }
        }
    }

    public void syncDeleted() throws IOException, InterruptedException {
        List<DataModel> data = service.getDeletedAfter(manageSyncTime(cachedDeletedAt));
        for (DataModel model : data) {
            try {
                log.info("Removing old data for ID " + model.getId());
                repository.deleteById(model.getId());
            } catch (Exception e) {
                log.error("Failed to delete data for ID " + model.getId());
            }
        }
    }

    private LocalDateTime manageSyncTime(LocalDateTime syncTime) {
        if (syncTime == null)
            return LocalDateTime.now().minusDays(1L);

        syncTime = LocalDateTime.now();
        return syncTime.minusMinutes(6L);
    }

    private void validateCache(Optional<CachedData> optional) {
        if (optional.isEmpty()) return;

        // Is cache expired
        CachedData data = optional.get();
        LocalDateTime now = LocalDateTime.now();
        if (data.getCachedAt().plusHours(1).isBefore(now)) {
            log.info("Cache expired for data id " + data.getId());
            updateDataById(data.getId());
        }
    }

    private void updateDataById(Integer id) {
        try {
            DataModel model = service.getData(id);

            // Exists for UUID
            if (repository.existsByUuid(model.getUuid()))
                repository.deleteByUuid(model.getUuid());

            // Exists for Discord ID
            String discord = model.getUser().getDiscordId();
            if (repository.existsByDiscordId(discord))
                repository.existsByDiscordId(discord);

            saveDataCommon(model);
        } catch (BackendException e) {
            if (e.getStatus() == 404) {
                log.error("No data for id " + id + " found on backend");
                if (repository.existsById(id)) repository.deleteById(id);
                return;
            }
            log.error("Something is wrong with backend");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void saveDataCommon(DataModel model) throws IOException, InterruptedException {
        try {
            UserModel user = service.getUser(model.getUuid());
            AccountModel account = service.getAccount(model.getUuid());
            CachedData data = repository.save(CachedData.builder()
                    .id(model.getId())
                    .name(account.getName())
                    .uuid(account.getId())
                    .discordId(user.getId())
                    .tag(user.getName())
                    .avatar(user.getAvatar())
                    .guildId(model.getGuild().getDiscordId())
                    .createdAt(model.getCreatedAt())
                    .cachedAt(LocalDateTime.now())
                    .build());
            log.info("Successfully saved data for player: " + data.getName());
        } catch (DataIntegrityViolationException die) {
            repository.deleteByUuid(model.getUuid());
            repository.deleteByDiscordId(model.getUser().getDiscordId());
            saveDataCommon(model);
        }
    }
}
