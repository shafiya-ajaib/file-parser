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
                .flatMap(filePart -> DataBufferUtils.join(filePart.content())
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
                                            BigDecimal bookPrice;
                                            BigDecimal bookQty;
                                            BigDecimal bookTotal;
                                            BigDecimal offeringPrice;
                                            BigDecimal offeringQty;
                                            BigDecimal offeringTotal;
                                            BigDecimal allotmentQty;
                                            BigDecimal allotmentTotal;

                                            try {
                                                bookPrice = parseBigDecimal(columns[2]);
                                                bookQty = parseBigDecimal(columns[3]);
                                                bookTotal = parseBigDecimal(columns[4]);
                                                offeringPrice = parseBigDecimal(columns[5]);
                                                offeringQty = parseBigDecimal(columns[6]);
                                                offeringTotal = parseBigDecimal(columns[7]);
                                                allotmentQty = parseBigDecimal(columns[8]);
                                                allotmentTotal = parseBigDecimal(columns[9]);
                                            } catch (NumberFormatException e) {
                                                return Mono.empty();
                                            }

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
                                                    .thenReturn(1);
                                        })
                                        .reduce(0, Integer::sum)
                                        .map(count -> "Successfully uploading " + count + " data");
                            } catch (IOException e) {
                                return Mono.error(new IllegalStateException("Error reading CSV file", e));
                            }
                        }))
                .switchIfEmpty(Mono.defer(() -> {
                    String errorMessage = "File is not a CSV";
                    this.logger.error(errorMessage);
                    return Mono.error(new IllegalStateException(errorMessage));
                }));
    }

    private BigDecimal parseBigDecimal(String value) throws NumberFormatException {
        if (value != null && !value.isEmpty()) {
            try {
                return new BigDecimal(value);
            } catch (NumberFormatException e) {
                throw new NumberFormatException("Invalid numeric value: " + value);
            }
        }
        return null;
    }
}
