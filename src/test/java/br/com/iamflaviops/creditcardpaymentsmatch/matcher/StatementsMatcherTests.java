package br.com.iamflaviops.creditcardpaymentsmatch.matcher;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class StatementsMatcherTests {

    @Autowired
    private StatementsMatcher matcher;

    @Test
    public void statementsMatcherGivenTwoIdenticalStatementsShouldReportNoDifferences() {
        Map<String, Long> statement1 = Map.of(
            "10,00", 1L,
            "100,00", 2L,
            "27,90", 2L
        );

        Map<String, Long> statement2 = Map.of(
            "10,00", 1L,
            "100,00", 2L,
            "27,90", 2L
        );

        Set<String> notMatchingValues = matcher.matchStatements(statement1, statement2);

        assertTrue(notMatchingValues.isEmpty());
    }

    @Test
    public void statementsMatcherGivenStatementsWithDifferentCountOfValuesShouldReportTheValuesWhoseCountAreNotMatching_v1() {
        Map<String, Long> statement1 = Map.of(
                "10,00", 1L,
                "100,00", 2L
        );

        Map<String, Long> statement2 = Map.of(
                "10,00", 1L,
                "100,00", 1L,
                "27,90", 2L
        );

        Set<String> notMatchingValues = matcher.matchStatements(statement1, statement2);

        assertEquals(2, notMatchingValues.size());
        assertTrue(notMatchingValues.contains("100,00"));
        assertTrue(notMatchingValues.contains("27,90"));
    }

    @Test
    public void statementsMatcherGivenStatementsWithDifferentCountOfValuesShouldReportTheValuesWhoseCountAreNotMatching_v2() {
        Map<String, Long> statement1 = Map.of(
            "10,00", 1L,
            "100,00", 1L,
            "27,90", 2L
        );

        Map<String, Long> statement2 = Map.of(
            "10,00", 1L,
            "100,00", 2L
        );

        Set<String> notMatchingValues = matcher.matchStatements(statement1, statement2);

        assertEquals(2, notMatchingValues.size());
        assertTrue(notMatchingValues.contains("100,00"));
        assertTrue(notMatchingValues.contains("27,90"));
    }
}
