package com.shafiya.fileparser.service;

import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

public interface CsvUploadService {
    Mono<String> uploadCsv(FilePart file);
}
