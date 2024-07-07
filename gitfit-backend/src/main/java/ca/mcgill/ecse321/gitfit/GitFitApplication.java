package ca.mcgill.ecse321.gitfit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.gitfit.dao.SportCenterRepository;
import ca.mcgill.ecse321.gitfit.model.SportCenter;

import java.sql.Time;
import java.util.Iterator;

import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@SpringBootApplication
public class GitFitApplication {
	public static void main(String[] args) {
		SpringApplication.run(GitFitApplication.class, args);
	}

	@RequestMapping("/")
	public String greeting() {
		return "Hello world!";
	}
}