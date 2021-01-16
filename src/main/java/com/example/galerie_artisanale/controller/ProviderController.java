package com.example.galerie_artisanale.controller;

import com.example.galerie_artisanale.entity.*;
import com.example.galerie_artisanale.repository.AddressRepository;
import com.example.galerie_artisanale.service.*;
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

    @Autowired
    private UserService userService;

    @RequestMapping(value="/addProvider", method = RequestMethod.POST)
    public String addProviderPost(@ModelAttribute("provider")Provider provider,@ModelAttribute("address")Address address, Model model){


        model.addAttribute("email",provider.getEmail());
        if(providerService.findByEmail(provider.getEmail()) !=null){
            model.addAttribute("emailExists",true);
            return "admin/addProvider";
        }
        String name = provider.getFirstName();
        String nameMaj = name.replaceFirst(".",(name.charAt(0)+"").toUpperCase());
        provider.setFirstName(nameMaj);
        String lastName = provider.getLastName();
        String lastNameMaj = lastName.replaceFirst(".",(lastName.charAt(0)+"").toUpperCase());
        provider.setLastName(lastNameMaj);

        address = addressService.save(address);
        provider.setAddress(address);
        providerService.save(provider);




        model.addAttribute("addSuccess", true);
        return "redirect:../provider/providerList";

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
        return "redirect:/provider/providerList";
    }

    @GetMapping(value = "/updateProvider")
    public String updateProviderGet(Model model, @RequestParam("id") Long id) {
        Provider provider = providerService.findOne(id);
        model.addAttribute("provider", provider);

        return "admin/updateProvider";
    }

    @PostMapping("/updateProvider")
    public String updateProvider(@ModelAttribute Provider provider, BindingResult result) {
        String name = provider.getFirstName();
        String nameMaj = name.replaceFirst(".",(name.charAt(0)+"").toUpperCase());
        provider.setFirstName(nameMaj);
        String lastName = provider.getLastName();
        String lastNameMaj = lastName.replaceFirst(".",(lastName.charAt(0)+"").toUpperCase());
        provider.setLastName(lastNameMaj);

        Address address = addressService.save(provider.getAddress());
        provider.setAddress(address);
        provider = providerService.save(provider);

        return "redirect:../provider/providerList";

    }


    @RequestMapping("/updateProduct")
    public String updateProductGet(@RequestParam("id") Long id, Model model) {
        Product product = productService.findOne(id);
        model.addAttribute("product", product);
        return "admin/updateProduct";
    }

    @PostMapping("/updateProduct")
    public String updateProduct(@ModelAttribute Product product, BindingResult result, Model model) {

        String name = product.getProductName();
        String nameMaj = name.replaceFirst(".",(name.charAt(0)+"").toUpperCase());
        product.setProductName(nameMaj);
        product = productService.save(product);
        return "redirect:../product/productInfo?id=" + product.getId();
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
