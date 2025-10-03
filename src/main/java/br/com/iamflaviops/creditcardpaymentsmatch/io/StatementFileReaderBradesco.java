package br.com.iamflaviops.creditcardpaymentsmatch.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
        try (var reader = Files.newBufferedReader(path)) {
            var lineCounter = new AtomicInteger();
            var shouldSkipNext = new AtomicBoolean(false);
            return reader.lines()
            .peek(line -> logger.info("Line#{} -> {}", lineCounter.addAndGet(1), line))
            .filter(Objects::nonNull)
            .map(String::trim)
            .filter(StringUtils::hasLength)
            .peek(line -> {
                // Modern switch expression (Java 14+, enhanced in Java 17)
                if (switch (line) {
                    case "SALDO ANTERIOR", "PAGTO. POR DEB EM C/C" -> true;
                    default -> false;
                }) {
                    shouldSkipNext.set(true);
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
