package com.example.galerie_artisanale.controller;

import com.example.galerie_artisanale.entity.*;
import com.example.galerie_artisanale.repository.CartItemRepository;
import com.example.galerie_artisanale.repository.ProductRepository;
import com.example.galerie_artisanale.security.MailConstructor;
import com.example.galerie_artisanale.service.*;
import com.example.galerie_artisanale.util.Container;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.galerie_artisanale.controller.HomeController.fileToPath;

@Controller
public class ConfirmController {

    public static final String SHOPPING_CART_SESSION = "SHOPPING_CART";
    public static final String CART_ITEM_LIST = "cartItemList";
    public static final String SHOPPING_CART_MODEL = "shoppingCart";

    @Autowired
    private UserService userService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CityService cityService;

    @Autowired
    private CategoryService categoryService;


    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private StorageService storageService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailConstructor mailConstructor;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AddressService addressService;

    @GetMapping("/confirm")
    public String confirmShoppingCart(@RequestParam("id")Long cartId,
                                      @RequestParam(value = "missingRequiredField", required = false)boolean missingRequiredField,
                                      Model model, Principal principal, HttpSession session) {
        model.addAttribute("categories", categoryService.findAllCategoryNames());

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof User) {
            Ordered ordered = orderService.findShoppingCart((User) authentication.getPrincipal());
            if( ordered == null){
                // cad que le panier est dans la session et l'utilisateur n'a pas encore un panier en cours qui n'est pas encore validé
                ordered =(Ordered) session.getAttribute(ShoppingCartController.SHOPPING_CART_SESSION);
                ordered = orderService.save(ordered);
            }

            if (ordered.getId() == null) {
                throw new IllegalStateException("Shopping cart should be persisted at this level: id shouldn't be null");
            }
            List<CartItem> cartItemList = ordered.getCartItemList();
            if (cartItemList.size() == 0) {
                model.addAttribute("emptyCart", true);
                return "forward:/shoppingCart/view";
            }
            for (CartItem cartItem : cartItemList) {
                if (cartItem.getProduct().getInStockNumber() < cartItem.getQty()) {
                    model.addAttribute("notEnoughStock", true);
                    return "forward:/shoppingCart/view";
                }
            }

            // GET THE ADDRESS  of the connected user

            User user  = (User)authentication.getPrincipal() ;
            List<Address> addresses = addressService.findByUser(user);
            //Address shippingAddress = orderService.
            if (addresses != null && !addresses.isEmpty()){
                ordered.setShippingAddress(addresses.get(addresses.size()-1));
                ordered.setBillingAddress(ordered.getShippingAddress());
            }
            model.addAttribute("addresses", addresses);
            model.addAttribute("cartItemList", cartItemList);
            // model.addAttribute("shoppingCart",ordered);
            List<City> cityList = cityService.findAll();
            model.addAttribute("cityList", cityList);

            model.addAttribute("classActiveShipping", true);
            if (ordered != null) {
                fillFullURLOfFirstImage(ordered);
            }

            if (missingRequiredField) {
                model.addAttribute("missingRequiredField", true);
            }
            // Validate Order !
            model.addAttribute(SHOPPING_CART_MODEL, ordered);

        }else {

            throw new IllegalStateException("You should be authenticated to be able to confirm your Shopping cart");

        }

        return "confirm";
    }


    @PostMapping(value = "/confirm")
    public String confirmShoppingCartPost(Model model, HttpSession session,
                                          @ModelAttribute("address")Address address){

        model.addAttribute("categories", categoryService.findAllCategoryNames());

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof User) {
            Ordered ordered = orderService.findShoppingCart((User) authentication.getPrincipal());
            // TODO : verifier ici si tu dois faire la meme correciton à la ligne 79
            if( ordered == null){
                // cad que le panier est dans la session et l'utilisateur n'a pas encore un panier en cours qui n'est pas encore validé
                ordered =(Ordered) session.getAttribute(ShoppingCartController.SHOPPING_CART_SESSION);
                ordered = orderService.save(ordered);
            }
            if (ordered.getId() == null) {
                throw new IllegalStateException("Shopping cart should be persisted at this level: id shouldn't be null");
            }

            // TODO: update the stock

            ordered.getCartItemList()
                    .forEach(cartItem -> cartItem.getProduct().setInStockNumber(cartItem.getProduct().getInStockNumber()-cartItem.getQty()));

            ordered.setStatus(OrderedStatus.VALID);
            ordered.setOrderDate(new Date());
            ordered.getCartItemList()
                    .forEach(item -> item.setBuyinPrice(item.getProduct().getPrice()));

            address.setUser((User) authentication.getPrincipal() );
            Address persestideAddress = addressService.save(address);

            ordered.setBillingAddress(persestideAddress);
            ordered.setShippingAddress(persestideAddress);

            ordered = orderService.save(ordered);

            User connectedUser = (User) authentication.getPrincipal();

            User user = userService.findByEmail(connectedUser.getEmail());

            mailSender.send(mailConstructor.constructOrderConfirmationEmail(user, ordered, Locale.FRENCH));

            //
            if (ordered != null) {
                fillFullURLOfFirstImage(ordered);
            }
            /*fillFullURLOfFirstImage(ordered);*/
            model.addAttribute(SHOPPING_CART_MODEL, ordered);
            model.addAttribute("address",new Address());
            // try again to clear the session (may be itsnt necessary )
            session.removeAttribute(SHOPPING_CART_SESSION);

        } else {
            throw new IllegalStateException("You should be authenticated to be able to confirm your Shopping cart");
        }
        return "orderConfirm";
    }


    private void fillFullURLOfFirstImage(Ordered shoppingCart) {
        List<CartItem> cartItems = shoppingCart.getCartItemList();

        cartItems.stream()
                .map(CartItem::getProduct)
                .filter(product -> !product.getImagesList().isEmpty())
                .map(product -> product.getImagesList().get(0))
                .forEach(img -> img.setFullURL((fileToPath(storageService.load(img.getUrl_image())))));
    }

    @GetMapping("/myOrderedList")
    public String view(Model model) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof User) {


            List<Ordered> myOrderedList = orderService.findByUserAndStatus((User) authentication.getPrincipal(),OrderedStatus.VALID);

            model.addAttribute("myOrderedList", myOrderedList);
        } else {
            throw new IllegalArgumentException("This service requires identification");
        }
        return  "redirect:/myOrderedList";
    }

    @GetMapping("/orderedList")
    public String viewAll(Model model) {
        Collection<Ordered> orderedList = orderService.findAll();
        model.addAttribute("orderedList", orderedList);
        return"/admin/orderedList";
    }


    @ModelAttribute("address")
    public Address newAddress(){ return new Address(); }

    @ModelAttribute("addresses")
    Collection<Address> findAllAddress(){
        Collection<Address> addresses = addressService.findAll();
        return addresses;
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

    @RequestMapping("/decrease/{id}/{qty}")
    public String decreaseStockOf(@PathVariable Long id,@PathVariable int qty) {

        Product product = productRepository.getOne(id);
        product.setInStockNumber(product.getInStockNumber() - qty);
        productRepository.saveAndFlush(product);
        return "redirect: ..";
    }
    @RequestMapping("/deleteItemC/{cartItem}")
    public String removeItem(@PathVariable("cartItem") Long id,Model model) {


        CartItem persistedCartItem = cartItemService.findById(id);

        cartItemService.remove(persistedCartItem);

        model.addAttribute("shoppingCart", cartItemService.findAll());
        if(cartItemService.findAll() == null)
            return "redirect:/shoppingCart/view";

        return "/confirm";
    }



    @GetMapping(value = "/updateUserAddress")
    public String updateProviderAddress(Model model, @RequestParam("id") Long id) {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof User){
            User connectedUser = (User) authentication.getPrincipal();
            List<Address> addressList =  addressService.findByUser(connectedUser); //.findOne(ordered.getShippingAddress().getId());
            Address address = addressService.findOne((addressList.get(0)).getId());

            model.addAttribute("address",address );
            return "updateUserAddress";
        }else{
            throw new IllegalStateException("You should be authenticated to be able to confirm your Shopping cart");

        }

    }

    @PostMapping("/updateUserAddress")
    public String updateProviderAddress(@ModelAttribute  Address address,City city, BindingResult result) {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        /*if (authentication.getPrincipal() instanceof User) {
            User connectedUser = (User) authentication.getPrincipal();
            List<Address> addressList = addressService.findByUser(connectedUser); //.findOne(ordered.getShippingAddress().getId());

            Address add = addressService.findOne((addressList.get(0)).getId());
            address.setUser(add.getUser());
            address.setCity(cityService.findById(address.getCity().getId()));
            addressService.save(address);
            Ordered ordered = orderService.findShoppingCart((User) authentication.getPrincipal());

            return "redirect:/confirm?id="+ordered.getId();

        }else{
            throw new IllegalStateException("You should be authenticated to be able to confirm your Shopping cart");

        }*/
        Ordered ordered = orderService.findShoppingCart((User) authentication.getPrincipal());

        Address add = addressService.findOne(address.getId());
        /*address.setUser(add.getUser());
        address.setNumber(add.getNumber());
        address.setStreet(add.getStreet());
        address.setCity(cityService.findById(address.getCity().getId()));*/
        addressService.save(add);
        return "redirect:/confirm?id="+ordered.getId();

    }

}
