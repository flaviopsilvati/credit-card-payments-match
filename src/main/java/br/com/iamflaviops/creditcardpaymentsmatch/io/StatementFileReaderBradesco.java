package br.com.iamflaviops.creditcardpaymentsmatch.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class StatementFileReaderBradesco implements StatementFileReader {

    private final Logger logger = LoggerFactory.getLogger(StatementFileReaderBradesco.class);

    @Override
    public Map<String, Long> read(Path path) {
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            AtomicInteger lineCounter = new AtomicInteger();
            AtomicBoolean shouldSkipNext = new AtomicBoolean(false);
            return reader.lines()
            .peek(line -> logger.info("Line#{} -> {}", lineCounter.addAndGet(1), line))
            .filter(Objects::nonNull)
            .map(String::trim)
            .filter(StringUtils::hasLength)
            .peek(line -> {
                switch (line) {
                    case "SALDO ANTERIOR":
                    case "PAGTO. POR DEB EM C/C":
                        shouldSkipNext.set(true);
                        break;
                }
            })
            .filter(line -> line.contains(","))
            .filter(line -> {
                if (shouldSkipNext.get()) {
                    shouldSkipNext.set(false);
                    return false;
                }
                return true;
            })
            .peek(extractedValue -> logger.info("Line#{} -> {}", lineCounter.get(), extractedValue))
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
