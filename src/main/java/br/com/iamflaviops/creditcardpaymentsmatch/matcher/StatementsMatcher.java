package br.com.iamflaviops.creditcardpaymentsmatch.matcher;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class StatementsMatcher {

    public Set<String> matchStatements(Map<String, Long> statement1, Map<String, Long> statement2) {
        var valuesOk = new HashSet<String>();
        var result = new HashSet<String>();

        statement1.forEach((k, v) -> {
            if (statement2.containsKey(k) && statement2.get(k).equals(v)) {
                valuesOk.add(k);
            } else {
                result.add(k);
            }
        });

        statement2.keySet().forEach(k -> {
            if (!valuesOk.contains(k)) {
                result.add(k);
            }
        });

        return result;
    }
}
