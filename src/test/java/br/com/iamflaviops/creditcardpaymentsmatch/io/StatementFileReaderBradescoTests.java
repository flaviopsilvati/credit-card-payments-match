package br.com.iamflaviops.creditcardpaymentsmatch.io;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Paths;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class StatementFileReaderBradescoTests {

    @Autowired
    private StatementFileReaderBradesco reader;

    @Test
    public void statementFileReaderBradescoGivenValidFileShouldProduceMapOfMonetaryValuesAndHowManyTimesTheyOccur() {
        Map<String, Long> result = reader.read(
            Paths.get("src", "test", "resources", "io", "bradesco.txt")
        );

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(6, result.keySet().size());
        assertEquals(1, result.get("268,27"));
        assertEquals(1, result.get("27,90"));
        assertEquals(1, result.get("11,00"));
        assertEquals(2, result.get("100,00"));
        assertEquals(1, result.get("17,00"));
        assertEquals(1, result.get("15,94"));
    }
}
