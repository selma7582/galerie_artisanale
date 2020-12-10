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
        String name = provider.getFirstName();
        String nameMaj = name.replaceFirst(".",(name.charAt(0)+"").toUpperCase());
        provider.setFirstName(nameMaj);
        String lastName = provider.getLastName();
        String lastNameMaj = lastName.replaceFirst(".",(lastName.charAt(0)+"").toUpperCase());
        provider.setLastName(lastNameMaj);

        provider = providerService.save(provider);
        address.setProvider(provider);
        List<Address> list = addressService.findAll();


        addressService.save(address);
        model.addAttribute("addSuccess", true);
        //return "admin/addProvider";
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

    @GetMapping(value = "/updateProvider")
    public String updateProviderGet(Model model, @RequestParam("id") Long id) {
        Provider provider = providerService.findOne(id);
        model.addAttribute("provider", provider);
        List<Address> addressList = addressService.findByProvider(provider);
        model.addAttribute("addressList",addressList);

        return "admin/updateProvider";
    }

    @PostMapping("/updateProvider")
    public String updateProvider( Provider provider, BindingResult result) {
        String name = provider.getFirstName();
        String nameMaj = name.replaceFirst(".",(name.charAt(0)+"").toUpperCase());
        provider.setFirstName(nameMaj);
        String lastName = provider.getLastName();
        String lastNameMaj = lastName.replaceFirst(".",(lastName.charAt(0)+"").toUpperCase());
        provider.setLastName(lastNameMaj);
         providerService.save(provider);
        return "redirect:../provider/providerList";

    }
/* Address address1 = addressService.findOne(address.getId());

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User connectedUser = (User) authentication.getPrincipal();
        Ordered ordered = orderService.findShoppingCart((User) authentication.getPrincipal());
        address1.setUser(connectedUser);
        address1.setStreet(address.getStreet());
        address1.setNumber(address.getNumber());
        address1.setCity(cityService.findById(address.getCity().getId()));
        Address persestideAddress = addressService.save(address1);
        ordered.setShippingAddress(persestideAddress);
        ordered.setBillingAddress(persestideAddress);
        return "redirect:/confirm?id=" + ordered.getId();*/
    @GetMapping(value = "/updateProviderAddress")
    public String updateProvidAddress(Model model, @RequestParam("id") Long id) {
        Address address = addressService.findOne(id);
        model.addAttribute("address", address);
        model.addAttribute("provider",address.getProvider());
        return "admin/updateProviderAddress";
    }

    @PostMapping("/updateProviderAddress")
    public String updateProviderAddress(Address address,Provider provider,City city) {
        Address add = addressService.findOne(address.getId());

        add.setProvider(address.getProvider());
        add.setProvider(provider);
        add.setCity(cityService.findById(address.getCity().getId()));
        add.setStreet(address.getStreet());
        add.setNumber(address.getNumber());
        addressService.save(add);
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
