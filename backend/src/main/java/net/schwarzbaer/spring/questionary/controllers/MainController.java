package net.schwarzbaer.spring.questionary.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@RequestMapping("/api")
public class MainController {

    @PostMapping("/setquestionary") 
    public void receiveQuestionary(@RequestBody String data) {
        System.out.printf("[POST] /api/setquestionary : receiveQuestionary( data:\"%s\" )%n", data);
    }
}
