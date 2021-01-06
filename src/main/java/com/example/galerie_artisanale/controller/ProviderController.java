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

        /*address.setProvider(provider);
        addressService.save(address);


        List<Address> addresses = addressService.findByProvider(provider);

        if (addresses.size() == 0) {

            address.setProvider(provider);
            Address persestideAddress1 = addressService.save(address);
        }
        if (addresses != null && !addresses.isEmpty()) {

            Address address1 = addressService.findOne(addresses.get(addresses.size() - 1).getId());
            Address persestideAddress = addressService.save(address1);
        }

        model.addAttribute("addresses", addresses);
        List<City> cityList = cityService.findAll();
        model.addAttribute("cityList", cityList);*/


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
        /*List<Address> addressList = addressService.findAll();
        model.addAttribute("addressList",addressList);*/
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
        /*Address address = provider.getAddress();
        model.addAttribute("address",address);*/
       // List<Address> addressList = addressService.findByProvider(provider);
       // model.addAttribute("addressList",addressList);

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



        /*Provider persistedProvider = providerService.save(provider);

        List<Address> addressList = addressService.findByProvider(persistedProvider);

        Address address = addressService.findOne((addressList.get(addressList.size()-1)).getId()); //.g.get(addresses.si
        address.setId((addressList.get(addressList.size()-1)).getId());*/
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
    /*@GetMapping(value = "/updateProviderAddress")
    public String updateProviderAddressGet(Model model, @RequestParam("id") Long id) {
        Address address1 = addressService.findOne(id);

        List<Address> addressList = addressService.findByProvider(address1.getProvider());

        Address address = addressService.findOne((addressList.get(addressList.size()-1)).getId()); //.g.get(addresses.size() - 1)).getId());
        //Address address = addressService.findOne((addressList.get(addresses.size() - 1)).getId());//addresses.size() - 1

        model.addAttribute("address", address);
        Provider provider = address.getProvider();

        Address address1 = addressService.findOne(id);

        model.addAttribute("provider",provider);
        return "admin/updateProviderAddress";
    }


    @PostMapping("/updateProviderAddress")
    public String updateProviderAddress(@ModelAttribute Address address, Model model) {

        Provider provider = address.getProvider();

        address.setProvider(provider);
        addressService.save(address);

        return "redirect:../provider/providerList";
    }*/

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
