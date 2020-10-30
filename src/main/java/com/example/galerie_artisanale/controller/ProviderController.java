package com.example.galerie_artisanale.controller;

import com.example.galerie_artisanale.entity.*;
import com.example.galerie_artisanale.service.AddressService;
import com.example.galerie_artisanale.service.CityService;
import com.example.galerie_artisanale.service.ProductService;
import com.example.galerie_artisanale.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/provider")
public class ProviderController {

    @Autowired
    private ProviderService providerService;

    @Autowired
    private CityService cityService;

    @Autowired
    private ProductService productService;

    @Autowired
    private AddressService addressService;

    @RequestMapping(value="/addProvider", method = RequestMethod.POST)
    public String addProviderPost(@ModelAttribute("provider")Provider provider,@ModelAttribute("address")Address address,City city, Model model){


        model.addAttribute("email",provider.getEmail());
        if(providerService.findByEmail(provider.getEmail()) !=null){
            model.addAttribute("emailExists",true);
            return "admin/addProvider";
        }

        Address address1 = new Address();
        //address1.setId(address.getId());
        address1.setNumber(address.getNumber());
        address1.setStreet(address.getStreet());


        provider = providerService.save(provider);
        address1.setProvider(provider);

        addressService.save(address1);

        model.addAttribute("addSuccess", true);


        return "admin/addProvider";

/*
        return "redirect:providerList";
*/



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



    @GetMapping(value = "/delete/{provider}")
    public String deleteProvider(@PathVariable("provider") Long id, Model model) {

        Provider provider = providerService.findById(id);

        if(provider.getProductList().size() == 0){
            providerService.removeOne(id);
            model.addAttribute("providerList", providerService.findAll());
            model.addAttribute("deleteSuccess", true);
        }
        else {
            model.addAttribute("providerList", providerService.findAll());
            model.addAttribute("notSuccess", true);
        }
        return "admin/providerList";

    }

    @RequestMapping("/updateProvider")
    public String updateProvider(@RequestParam("id") Long id, Model model) {
        Provider provider = providerService.findOne(id);
        model.addAttribute("provider", provider);


        return "admin/updateProvider";
    }


    @ModelAttribute("city")
    public City newCity() {
        return new City();
    }

    @ModelAttribute("cities")
    Collection<City> findAllCity() {
        Collection<City> cities = cityService.findAll();
        return cities;
    }

    @ModelAttribute("address")
    public Address newAddress(){ return new Address(); }

    @ModelAttribute("Addresses")
    Collection<Address>findAllAddress(){
        Collection<Address> addresses = addressService.findAll();
        return addresses;
    }






}
