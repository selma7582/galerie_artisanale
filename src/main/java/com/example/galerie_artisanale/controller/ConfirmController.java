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
import static com.example.galerie_artisanale.controller.ShoppingCartController.SHOPPING_CART_MODEL;

@Controller
public class ConfirmController {

    public static final String SHOPPING_CART_SESSION = "SHOPPING_CART";
    public static final String CART_ITEM_LIST = "cartItemList";

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
    public String confirmShoppingCart(@RequestParam("id") Long cartId,
                                      @RequestParam(value = "missingRequiredField", required = false) boolean missingRequiredField,
                                      Model model, Principal principal, HttpSession session) {
        model.addAttribute("categories", categoryService.findAllCategoryNames());

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof User) {
            Ordered ordered = orderService.findShoppingCart((User) authentication.getPrincipal());
            if (ordered == null) {
                // cad que le panier est dans la session et l'utilisateur n'a pas encore un panier en cours qui n'est pas encore validé
                ordered = (Ordered) session.getAttribute(ShoppingCartController.SHOPPING_CART_SESSION);
                //ordered.setUser();
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

            User user = (User) authentication.getPrincipal();
            List<Address> addresses = addressService.findByUser(user);
            //Address shippingAddress = orderService.
            if (addresses != null && !addresses.isEmpty()) {
                ordered.setShippingAddress(addresses.get(addresses.size() - 1));
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

        } else {

            throw new IllegalStateException("You should be authenticated to be able to confirm your Shopping cart");

        }

        return "confirm";
    }


    @PostMapping(value = "/confirm")
    public String confirmShoppingCartPost(Model model, HttpSession session,
                                          @ModelAttribute("address") Address address) {

        model.addAttribute("categories", categoryService.findAllCategoryNames());

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof User) {
            Ordered ordered = orderService.findShoppingCart((User) authentication.getPrincipal());
            // TODO : verifier ici si tu dois faire la meme correciton à la ligne 79
            if (ordered == null) {
                // cad que le panier est dans la session et l'utilisateur n'a pas encore un panier en cours qui n'est pas encore validé
                ordered = (Ordered) session.getAttribute(ShoppingCartController.SHOPPING_CART_SESSION);
                ordered = orderService.save(ordered);
            }
            if (ordered.getId() == null) {
                throw new IllegalStateException("Shopping cart should be persisted at this level: id shouldn't be null");
            }

            // TODO: update the stock

            ordered.getCartItemList()
                    .forEach(cartItem -> cartItem.getProduct().setInStockNumber(cartItem.getProduct().getInStockNumber() - cartItem.getQty()));

            ordered.setStatus(OrderedStatus.EN_ATTENTE);
            ordered.setOrderDate(new Date());
            ordered.getCartItemList()
                    .forEach((item->item.setQty(item.getQty())));
            ordered.getCartItemList()
                    .forEach(item -> item.setBuyinPrice(item.getProduct().getPrice()));
            User connectedUser = (User) authentication.getPrincipal();
            User user = userService.findByEmail(connectedUser.getEmail());

            List<Address> addresses = addressService.findByUser(connectedUser);

            if (addresses.size() == 0) {

                address.setUser((User) authentication.getPrincipal());
                Address persestideAddress1 = addressService.save(address);
                ordered.setBillingAddress(persestideAddress1);
                ordered.setShippingAddress(persestideAddress1);

            }
            if (addresses != null && !addresses.isEmpty()) {
               // ordered.setShippingAddress(addresses.get(addresses.size() - 1));

                Address address1 = addressService.findOne(addresses.get(addresses.size() - 1).getId());
                Address persestideAddress = addressService.save(address1);
                ordered.setShippingAddress(persestideAddress);
                ordered.setBillingAddress(persestideAddress);

            }
            ordered = orderService.save(ordered);

            mailSender.send(mailConstructor.constructOrderConfirmationEmail(user, ordered, Locale.FRENCH));


            if (ordered != null) {
                fillFullURLOfFirstImage(ordered);
            }
            /*fillFullURLOfFirstImage(ordered);*/
            model.addAttribute(SHOPPING_CART_MODEL, ordered);
            model.addAttribute("address", new Address());
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


    @GetMapping("/orderedList")
    public String viewAll(Model model) {
        Collection<Ordered> orderedList = orderService.findAll();
        model.addAttribute("orderedList", orderedList);

        return "/admin/orderedList";
    }


    @ModelAttribute("address")
    public Address newAddress() {
        return new Address();
    }

    @ModelAttribute("addresses")
    Collection<Address> findAllAddress() {
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
    public String decreaseStockOf(@PathVariable Long id, @PathVariable int qty) {

        Product product = productRepository.getOne(id);
        product.setInStockNumber(product.getInStockNumber() - qty);
        productRepository.saveAndFlush(product);
        return "redirect: ..";
    }

    @RequestMapping("/deleteItemC/{cartItem}")
    public String removeItem(@PathVariable("cartItem") Long id, Model model) {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Ordered ordered = orderService.findShoppingCart((User) authentication.getPrincipal());

        CartItem persistedCartItem = cartItemService.findById(id);

        cartItemService.remove(persistedCartItem);

        model.addAttribute("shoppingCart", cartItemService.findAll());

        if (cartItemService.findAll() == null)

            return "redirect:/shoppingCart/view";

        // return "/confirm";
        return "redirect:/confirm?id=" + ordered.getId();

    }

    @GetMapping(value = "/updateUserAddress")
    public String updateUserAddress(Model model, @RequestParam("id") Long id) {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof User) {
            User connectedUser = (User) authentication.getPrincipal();
            List<Address> addresses = addressService.findByUser(connectedUser);

            Ordered ordered = orderService.findShoppingCart((User) authentication.getPrincipal());
            model.addAttribute("ordered", ordered);
            List<Address> addressList = addressService.findByUser(connectedUser); //.findOne(ordered.getShippingAddress().getId());
            Address address = addressService.findOne((addressList.get(addresses.size() - 1)).getId());//addresses.size() - 1

            model.addAttribute("address", address);
            return "updateUserAddress";

        } else {
            throw new IllegalStateException("You should be authenticated to be able to confirm your Shopping cart");
        }
    }

    @PostMapping("/updateUserAddress")
    public String updateUserAddress(@ModelAttribute("address")Address address, City city, Model model) {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User connectedUser = (User) authentication.getPrincipal();
        List<Address> addresses = addressService.findByUser(connectedUser);
        Ordered ordered = orderService.findShoppingCart(connectedUser);

        if (addresses.size() == 0) {

            address.setUser(connectedUser);
            Address persestideAddress1 = addressService.save(address);

        }
        if (addresses != null && !addresses.isEmpty()) {
            Address address1 = newAddress();
            address1.setUser(connectedUser);
            address1.setCity(address.getCity());
            address1.setStreet(address.getStreet());
            address1.setNumber(address.getNumber());
            City city1 = address.getCity();
            address1.setCity(city1);
            Address persestideAddress1 =addressService.save(address1);
            ordered.setShippingAddress(persestideAddress1);
            addresses.add(persestideAddress1);
        }
       // model.addAttribute("address", new Address());
        return "redirect:/confirm?id=" + ordered.getId();
    }


    @RequestMapping("/updateCartItem")
    public String updateShoppingCart(@ModelAttribute("qty") int qty, @ModelAttribute("itemID") long itemID) {
        CartItem persistedCartItem = cartItemService.findById(itemID);
        persistedCartItem.setQty(qty);
        cartItemService.save(persistedCartItem);
        return "redirect:/shoppingCart/view";
        // return "forward:/shoppingCart/cart";

    }






}
