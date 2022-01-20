package br.com.iamflaviops.creditcardpaymentsmatch;

import br.com.iamflaviops.creditcardpaymentsmatch.io.StatementFileReader;
import br.com.iamflaviops.creditcardpaymentsmatch.io.StatementFileReaderBradesco;
import br.com.iamflaviops.creditcardpaymentsmatch.io.StatementFileReaderOrganizze;
import br.com.iamflaviops.creditcardpaymentsmatch.matcher.StatementsMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;

@SpringBootApplication
public class CreditCardPaymentsMatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(CreditCardPaymentsMatchApplication.class, args);
	}

	private final Logger logger = LoggerFactory.getLogger(CreditCardPaymentsMatchApplication.class);

	private final StatementFileReader statementFileReaderBradesco;

	private final StatementFileReader statementFileReaderOrganizze;

	private final StatementsMatcher matcher;

	public CreditCardPaymentsMatchApplication(
			StatementFileReaderBradesco statementFileReaderBradesco,
			StatementFileReaderOrganizze statementFileReaderOrganizze,
			StatementsMatcher matcher) {
		this.statementFileReaderBradesco = statementFileReaderBradesco;
		this.statementFileReaderOrganizze = statementFileReaderOrganizze;
		this.matcher = matcher;
	}

	@Bean
	public CommandLineRunner commandLineRunner() {
		return args -> {
			Map<String, Long> statementBradesco = statementFileReaderBradesco.read(
				Paths.get("src", "main", "resources", "bradesco.txt")
			);

			Map<String, Long> statementOrganizze = statementFileReaderOrganizze.read(
				Paths.get("src", "main", "resources", "organizze.txt")
			);

			Set<String> result = matcher.matchStatements(statementBradesco, statementOrganizze);

			logger.info("---------Results---------");
			result.forEach(logger::info);
		};
	}
}
