package jacc.expensesmanager.restservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "jacc.expensesmanager")
@SpringBootApplication
public class ExpensesManagerRestServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpensesManagerRestServiceApplication.class, args);
	}

}
