package com.example.blockbuster;

import com.example.blockbuster.apiCall.DataCall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import javax.servlet.http.HttpSession;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class BlockBusterApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlockBusterApplication.class, args);
	}

}
