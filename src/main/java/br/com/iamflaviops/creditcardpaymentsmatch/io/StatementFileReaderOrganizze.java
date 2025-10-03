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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class StatementFileReaderOrganizze implements StatementFileReader {

    private final Logger logger = LoggerFactory.getLogger(StatementFileReaderOrganizze.class);

    @Override
    public Map<String, Long> read(Path path) {
        try (var reader = Files.newBufferedReader(path)) {
            var lineCounter = new AtomicInteger();
            return reader.lines()
            .peek(line -> logger.info("Line#{} -> {}", lineCounter.addAndGet(1), line))
            .filter(Objects::nonNull)
            .map(String::trim)
            .filter(StringUtils::hasLength)
            .filter(line -> line.startsWith("-"))
            .map(line -> line.split("-"))
            .map(chunks -> chunks[1])
            .peek(extractedValue -> logger.info("Line#{} -> {}", lineCounter.get(), extractedValue))
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
