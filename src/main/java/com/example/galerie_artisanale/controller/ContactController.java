package com.example.galerie_artisanale.controller;

import com.example.galerie_artisanale.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContactController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/contact")
    public String contactForm(Model model) {
        model.addAttribute("categories", categoryService.findAllCategoryNames());

        return ("contact");


    }
}
