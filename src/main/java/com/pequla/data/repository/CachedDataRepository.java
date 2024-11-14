package com.pequla.data.repository;

import com.pequla.data.entity.CachedData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CachedDataRepository extends JpaRepository<CachedData, Integer> {

    Optional<CachedData> findByDiscordId(String discordId);

    Page<CachedData> findAllByGuildId(String guildId, Pageable pageable);

    Optional<CachedData> findByUuid(String uuid);

    Optional<CachedData> findByNameIgnoreCase(String name);

    boolean existsByDiscordId(String id);

    void deleteByDiscordId(String id);

    boolean existsByUuid(String uuid);

    void deleteByUuid(String uuid);
}
