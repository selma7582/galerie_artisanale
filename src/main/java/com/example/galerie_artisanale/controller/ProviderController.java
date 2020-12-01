package com.example.galerie_artisanale.controller;

import com.example.galerie_artisanale.entity.*;
import com.example.galerie_artisanale.repository.AddressRepository;
import com.example.galerie_artisanale.service.AddressService;
import com.example.galerie_artisanale.service.CityService;
import com.example.galerie_artisanale.service.ProductService;
import com.example.galerie_artisanale.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String addProviderPost(@ModelAttribute("provider")Provider provider,@ModelAttribute("address")Address address, Model model){


        model.addAttribute("email",provider.getEmail());
        if(providerService.findByEmail(provider.getEmail()) !=null){
            model.addAttribute("emailExists",true);
            return "admin/addProvider";
        }

        provider = providerService.save(provider);
        address.setProvider(provider);
        List<Address> list = addressService.findAll();


        addressService.save(address);

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
        List<Address> addressList = addressService.findAll();
        model.addAttribute("addressList",addressList);


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

        return "redirect:/provider/providerList";

    }

    /*@RequestMapping("/updateProvider")
    public String updateProvider(@RequestParam("id") Long id, Model model) {
        Provider provider = providerService.findOne(id);
        model.addAttribute("provider", provider);

        return "admin/updateProvider";
    }*/


    @GetMapping(value = "/updateProvider")
    public String updateProviderGet(Model model, @RequestParam("id") Long id) {
        Provider provider = providerService.findOne(id);
        model.addAttribute("provider", provider);
        List<Address> addressList = addressService.findByProvider(provider);
        model.addAttribute("addressList",addressList);

        return "admin/updateProvider";
    }



    @PostMapping("/updateProvider")
    public String updateProvider(@ModelAttribute  Provider provider, BindingResult result) {

        provider = providerService.save(provider);
        return "redirect:../provider/providerList";

    }

    @GetMapping(value = "/updateProviderAddress")
    public String updateProviderAddress(Model model, @RequestParam("id") Long id) {

        Address address = addressService.findOne(id);

        model.addAttribute("address", address);
        return "admin/updateProviderAddress";
    }

    @PostMapping("/updateProviderAddress")
    public String updateProviderAddress(@ModelAttribute  Address address,City city, BindingResult result) {
        Address add = addressService.findOne(address.getId());
        address.setProvider(add.getProvider());
        address.setCity(cityService.findById(address.getCity().getId()));
        addressService.save(address);
        return "redirect:../provider/providerList";

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
