package com.example.galerie_artisanale.service;

import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;

public interface ShoppingCartHeaderFiller {

    void fill(Model model, HttpSession session) ;
}
