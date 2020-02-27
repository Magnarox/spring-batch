package com.magnarox.batch;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class EntryPoint {

    @GetMapping("start")
    public ResponseEntity<String> start() {
        return ResponseEntity.ok("started");
    }
}
