package com.shafiya.fileparser.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CsvUploadServiceImpl implements CsvUploadService {
    Logger logger = LoggerFactory.getLogger(CsvUploadService.class);
    public Mono<String> uploadCsv(FilePart file) {
        return Mono.just(file)
                .filter(filePart -> filePart.filename().endsWith(".csv"))
                .flatMap(path -> Mono.fromCallable(() -> {
                    return "CSV data parsed successfully";
                }))
                .switchIfEmpty(Mono.defer(() -> {
                    String errorMessage = "File is not a CSV";
                    logger.error(errorMessage);
                    return Mono.error(new IllegalStateException(errorMessage));
                }));
    }
}
