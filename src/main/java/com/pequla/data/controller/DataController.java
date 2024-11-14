package com.pequla.data.controller;

import com.pequla.data.entity.CachedData;
import com.pequla.data.model.DataModel;
import com.pequla.data.service.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(path = "/api/data")
public class DataController {

    private final DataService service;

    @GetMapping
    public Page<CachedData> getData(Pageable pageable) {
        return service.getData(pageable);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CachedData> getDataById(@PathVariable Integer id) {
        return ResponseEntity.of(service.getById(id));
    }

    @GetMapping(path = "/discord/{id}")
    public ResponseEntity<CachedData> getDataByDiscordId(@PathVariable String id) {
        return ResponseEntity.of(service.getByDiscordId(id));
    }

    @GetMapping(path = "/guild/{id}")
    public Page<CachedData> getDataByGuildId(@PathVariable String id, Pageable pageable) {
        return service.getAllByGuildId(id, pageable);
    }

    @GetMapping(path = "/uuid/{uuid}")
    public ResponseEntity<CachedData> getDataByUuid(@PathVariable String uuid) {
        return ResponseEntity.of(service.getByUuid(uuid));
    }

    @GetMapping(path = "/name/{name}")
    public ResponseEntity<CachedData> getDataByName(@PathVariable String name) {
        return ResponseEntity.of(service.getByName(name));
    }

    @PostMapping(path = "/sync")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void syncNowWithMaster() {
        service.syncNow();
    }
}
