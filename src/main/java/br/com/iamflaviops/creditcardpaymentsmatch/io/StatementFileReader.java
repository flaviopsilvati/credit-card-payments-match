package br.com.iamflaviops.creditcardpaymentsmatch.io;

import java.nio.file.Path;
import java.util.Map;

public interface StatementFileReader {

    Map<String, Long> read(Path path);
}
