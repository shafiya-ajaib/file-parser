package com.shafiya.fileparser.controller;

import com.shafiya.fileparser.service.CsvUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


@RestController
public class CsvUploadController {
    @Autowired
    private CsvUploadService csvUploadService;

    @PostMapping(path = "/upload", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public Mono<ResponseEntity<String>> uploadCsv(@RequestPart(value="file") FilePart file) {
        return csvUploadService.uploadCsv(file).map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    String errorMessage = e.getMessage();
                    return Mono.just(ResponseEntity.internalServerError().body(errorMessage));
                });
    }
}
