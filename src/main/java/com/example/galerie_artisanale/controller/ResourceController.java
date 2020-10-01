package com.example.galerie_artisanale.controller;

import com.example.galerie_artisanale.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class ResourceController {

    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/shape/removeList", method = RequestMethod.POST)
    public String removeList(
            @RequestBody ArrayList<String> shapeIdList, Model model){

        for (String id : shapeIdList ) {
            String shapeId =id.substring(8);
            productService.removeOne(Long.parseLong(shapeId));
        }

        return "delete success";

    }





}
