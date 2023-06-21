package com.shafiya.fileparser.service;

import com.shafiya.fileparser.entity.IpoOrder;
import com.shafiya.fileparser.kafka.CreateIpoOrderPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CsvUploadServiceImpl implements CsvUploadService {

    private final CreateIpoOrderPublisher createIpoOrderPublisher;

    private final Logger logger = LoggerFactory.getLogger(CsvUploadService.class);

    public CsvUploadServiceImpl(CreateIpoOrderPublisher createIpoOrderPublisher) {
        this.createIpoOrderPublisher = createIpoOrderPublisher;
    }

    public Mono<String> uploadCsv(FilePart file) {
        return Mono.just(file)
                .filter(filePart -> filePart.filename().endsWith(".csv"))
                .flatMap(filePart -> {
                    return DataBufferUtils.join(filePart.content())
                            .flatMap(dataBuffer -> {
                                try (InputStream inputStream = dataBuffer.asInputStream();
                                     BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

                                    List<String> lines = reader.lines().collect(Collectors.toList());

                                    return Flux.fromIterable(lines)
                                            .skip(1) // Skip the header line
                                            .flatMap(line -> {
                                                String[] columns = line.split(",");
                                                String investorName = columns[0];
                                                String sid = columns[1];
                                                BigDecimal bookPrice = parseBigDecimal(columns[2]);
                                                BigDecimal bookQty = parseBigDecimal(columns[3]);
                                                BigDecimal bookTotal = parseBigDecimal(columns[4]);
                                                BigDecimal offeringPrice = parseBigDecimal(columns[5]);
                                                BigDecimal offeringQty = parseBigDecimal(columns[6]);
                                                BigDecimal offeringTotal = parseBigDecimal(columns[7]);
                                                BigDecimal allotmentQty = parseBigDecimal(columns[8]);
                                                BigDecimal allotmentTotal = parseBigDecimal(columns[9]);

                                                IpoOrder ipoOrder = new IpoOrder();
                                                ipoOrder.setInvestorName(investorName);
                                                ipoOrder.setSid(sid);
                                                ipoOrder.setBookPrice(bookPrice);
                                                ipoOrder.setBookQty(bookQty);
                                                ipoOrder.setBookTotal(bookTotal);
                                                ipoOrder.setOfferingPrice(offeringPrice);
                                                ipoOrder.setOfferingQty(offeringQty);
                                                ipoOrder.setOfferingTotal(offeringTotal);
                                                ipoOrder.setAllocatedQty(allotmentQty);
                                                ipoOrder.setAllocatedTotal(allotmentTotal);

                                                return this.createIpoOrderPublisher.publish(ipoOrder)
                                                        .thenReturn("CSV data parsed successfully");
                                            })
                                            .collectList()
                                            .thenReturn("CSV data parsed successfully");
                                } catch (IOException e) {
                                    return Mono.error(new IllegalStateException("Error reading CSV file", e));
                                }
                            });
                })
                .switchIfEmpty(Mono.defer(() -> {
                    String errorMessage = "File is not a CSV";
                    this.logger.error(errorMessage);
                    return Mono.error(new IllegalStateException(errorMessage));
                }));
    }

    private BigDecimal parseBigDecimal(String value) {
        if (value != null && !value.isEmpty()) {
            return new BigDecimal(value);
        }
        return null;
    }
}
