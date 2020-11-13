package com.example.galerie_artisanale.controller;

import com.example.galerie_artisanale.entity.Product;
import com.example.galerie_artisanale.entity.User;
import com.example.galerie_artisanale.service.CategoryService;
import com.example.galerie_artisanale.service.ProductService;
import com.example.galerie_artisanale.service.StorageService;
import com.example.galerie_artisanale.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.nio.file.Path;
import java.security.Principal;
import java.util.List;

@Controller
public class SearchController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/searchProduct")
    public String searchBook(
            @ModelAttribute("keyword") String keyword,
            Principal principal, Model model
    ) {
        model.addAttribute("categories", categoryService.findAllCategoryNames());

        if(principal!=null) {
            String username = principal.getName();
            User user = userService.findByUsername(username);
            model.addAttribute("user", user);
        }

        List<Product> productList = productService.blurrySearch(keyword);

        if (productList.isEmpty()) {
            model.addAttribute("emptyList", true);
            return "galerie";
        }

        productList.stream()
                .filter(product -> !product.getImagesList().isEmpty())
                .map(product -> product.getImagesList().get(0))
                .forEach(img -> img.setFullURL((fileToPath(storageService.load(img.getUrl_image())))));

        model.addAttribute("productList", productList);


        return "galerie";
    }


    // localhost:8080/imgs/prouct_4>7.jpeg
    public static String fileToPath(Path path) {
        return MvcUriComponentsBuilder.fromMethodName(HomeController.class,
                "loadImageFromServer", path.toString().replaceAll("\\\\", ">")).build().toString();
    }

}
