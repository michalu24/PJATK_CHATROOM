package org.example.pjatk_chatroom.controllers;

import org.example.pjatk_chatroom.service.MessageService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

@Controller
public class MainController {

    private final MessageService messageService;

    public MainController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/")
    public ModelAndView chat() {
        /**
         * 1) Pobierz nazwę zalogowanego użytkownika:
         *    String loggedUserName = SecurityContextHolder.getContext().getAuthentication().getName();
         * 2) Utwórz mapę modelu: var model = new HashMap<String, Object>();
         * 3) Dodaj do modelu parę ("loggedUserName", loggedUserName).
         * 4) Pobierz listę wiadomości użytkownika (limit 100):
         *    var messages = messageService.lastForActiveUser(100, loggedUserName);
         * 5) Dodaj do modelu parę ("messages", messages).
         * 6) Zwróć nowy widok: new ModelAndView("index", model).
         */
        String loggedUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        var model = new HashMap<String, Object>();
        model.put("loggedUserName", loggedUserName);
        var messages = messageService.lastForActiveUser(100, loggedUserName);
        model.put("messages", messages);
        return new ModelAndView("index", model);
    }
}
