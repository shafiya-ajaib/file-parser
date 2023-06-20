package com.shafiya.fileparser.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CsvUploadService {
    Logger logger = LoggerFactory.getLogger(CsvUploadService.class);
    public void uploadCsv(FilePart file) {
        // checking if file is a csv
        String fileName = file.filename();
        Optional<String> fileExtension = getExtensionByStringHandling(fileName);

        if (fileExtension.isEmpty() || !fileExtension.get().equals("csv")) {
            logger.error("File is not a CSV file");
        }
    }

    private Optional<String> getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
                .filter(name -> name.contains("."))
                .map(name -> name.substring(name.lastIndexOf('.') + 1));
    }
}
