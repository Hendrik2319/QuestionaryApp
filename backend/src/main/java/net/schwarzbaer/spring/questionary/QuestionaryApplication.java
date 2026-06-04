package net.schwarzbaer.spring.questionary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import net.schwarzbaer.spring.questionary.gui.ControlWindow;

@SpringBootApplication
public class QuestionaryApplication {

	public static void main(String[] args) {
		ControlWindow.getInstance().start();
		SpringApplication.run(QuestionaryApplication.class, args);
	}

}
