package com.example.galerie_artisanale.controller;

import com.example.galerie_artisanale.entity.Product;
import com.example.galerie_artisanale.entity.Provider;
import com.example.galerie_artisanale.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/provider")
public class ProviderController {

    @Autowired
    private ProviderService providerService;

    @RequestMapping(value="/addProvider", method = RequestMethod.POST)
    public String addProviderPost(@ModelAttribute("provider")Provider provider, HttpServletRequest request){

        this.providerService.save(provider);
        return ("admin/providerList");

    }

    @RequestMapping("/addProvider")
    public String addInformation(Model model){
        List<Provider> providerList = providerService.findAll();
        model.addAttribute("providerList",providerList);
        return "admin/addProvider";

    }


    @ModelAttribute("provider")
    public Provider newProvider(){
        return new Provider();
    }

    @ModelAttribute("providers")
    Collection<Provider> findAllProviders(){
        Collection<Provider> providers = (Collection<Provider>) providerService.findAll();
        return providers;
    }

    @RequestMapping("/providerList")
    public String providerList(Model model){
        List<Provider> providerList = providerService.findAll();
        model.addAttribute("providerList",providerList);
        return "admin/providerList";
    }

  /*  @GetMapping(value = "/edit/{providerId}")
    public String edit(Model model, @PathVariable Long providerId) {
        Provider provider= providerService.findById(providerId);
        if (provider != null) {
            model.addAttribute("provider", provider.getId());
            model.addAttribute("providerList", providerService.findByRemovedFalse());

            return "admin/addProduct";
        } else {
            throw new IllegalArgumentException();
        }
    }
*/

    @GetMapping(value = "/delete/{providerId}")
    public String delete(Model model, @PathVariable Long providerId) {
        final Provider provider = providerService.findById(providerId);
        if (provider!=null) {
            provider.setRemoved(true);
            providerService.delete(provider);
            return "admin/productList";
        } else {
            throw new IllegalArgumentException();
        }
    }

    @GetMapping(value = "/restore/{providerId}")
    public String restore(Model model, @PathVariable Long providerId) {
        final Provider provider = providerService.findById(providerId);
        if (provider!= null) {
            provider.setRemoved(false);
            providerService.save(provider);
            return "admin/productList";
        } else {
            throw new IllegalArgumentException();
        }
    }




}
