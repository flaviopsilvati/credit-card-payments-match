package br.com.iamflaviops.creditcardpaymentsmatch.matcher;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class StatementsMatcher {

    public List<String> matchStatements(Map<String, Long> statement1, Map<String, Long> statement2) {
        var valuesOk = new HashSet<String>();
        var result = new ArrayList<String>();

        statement1.forEach((k, v) -> {
            if (statement2.containsKey(k) && statement2.get(k).equals(v)) {
                valuesOk.add(k);
            } else {
                if (statement2.containsKey(k)) {
                    result.add(String.format("%s - Found in both files with different counts (Bradesco: %d, Organizze: %d)",
                        k, v, statement2.get(k)));
                } else {
                    result.add(String.format("%s (%d occurrences) - Found only in Bradesco", k, v));
                }
            }
        });

        statement2.keySet().forEach(k -> {
            if (!valuesOk.contains(k) && !statement1.containsKey(k)) {
                result.add(String.format("%s (%d occurrences) - Found only in Organizze", k, statement2.get(k)));
            }
        });

        return result;
    }
}
