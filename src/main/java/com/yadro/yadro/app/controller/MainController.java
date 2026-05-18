package com.yadro.yadro.app.controller;

import com.yadro.yadro.app.service.MainService;
import com.yadro.yadro.app.model.Person;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final MainService service;

    @GetMapping({"/", "/homepage"})
    public String homepage(@RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "20") int size,
                           Model model) {
        Page<Person> personsPage = service.getPersonsPage(page, size);
        model.addAttribute("persons", personsPage);
        return "index";
    }

    @PostMapping("/load")
    public String loadPersons(@RequestParam int count) {
        service.loadAndSavePersons(count);
        return "redirect:/homepage";
    }

    @GetMapping("/{id}")
    public String personPage(@PathVariable Long id, Model model) {
        Person person = service.getPersonById(id);
        model.addAttribute("person", person);
        model.addAttribute("random", false);
        return "person";
    }

    @GetMapping("/random")
    public String randomPerson(Model model) {
        Person person = service.getRandomPerson();
        model.addAttribute("person", person);
        model.addAttribute("random", true);
        return "person";
    }

    @GetMapping("/count")
    @ResponseBody
    public String count() {
        return "Total persons: " + service.getPersonsPage(0, Integer.MAX_VALUE).getTotalElements();
    }
}