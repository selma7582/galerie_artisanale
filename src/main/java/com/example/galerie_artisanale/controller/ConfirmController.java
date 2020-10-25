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

            Address shippingAddress = new Address();
            if (shippingAddress.getUser() instanceof User) {

            }
            shippingAddress = ordered.getShippingAddress();
            Address billingAddress = new Address();
            billingAddress = ordered.getBillingAddress();

            model.addAttribute("shippingAddress", ordered.getShippingAddress());
            model.addAttribute("billingAddress", ordered.getBillingAddress());
            model.addAttribute("cartItemList", cartItemList);
            // model.addAttribute("shoppingCart",ordered);
            List<City> cityList = cityService.findAll();
            model.addAttribute("stateList", cityList);

            model.addAttribute("classActiveShipping", true);

            if (missingRequiredField) {
                model.addAttribute("missingRequiredField", true);
            }

        }else {

            throw new IllegalStateException("You should be authenticated to be able to confirm your Shopping cart");

        }

        Ordered shoppingCart = orderService.findShoppingCart((User) authentication.getPrincipal());

        model.addAttribute(SHOPPING_CART_MODEL, shoppingCart);
        return "confirm";
    }


    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
    public String confirmShoppingCartPost(Model model, HttpSession session,
                                          @ModelAttribute("shippingAddress")Address shippingAddress,
                                          @ModelAttribute("billingAddress")Address billingAddress){

        model.addAttribute("categories", categoryService.findAllCategoryNames());

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof User) {
            Ordered ordered = orderService.findShoppingCart((User) authentication.getPrincipal());
            if (ordered.getId() == null) {
                throw new IllegalStateException("Shopping cart should be persisted at this level: id shouldn't be null");
            }

            // TODO: update the stock

            /*ordered.getCartItemList()
                    .forEach(item -> item.getProduct().setInStockNumber());*/

            // model.addAttribute("notEnoughStock",true);

            ordered.setStatus(OrderedStatus.VALID);
            ordered.setOrderDate(new Date());
            ordered.getCartItemList()
                    .forEach(item -> item.setBuyinPrice(item.getProduct().getPrice()));



            ordered = orderService.save(ordered);

            User connectedUser = (User) authentication.getPrincipal();

            User user = userService.findByEmail(connectedUser.getEmail());

            mailSender.send(mailConstructor.constructOrderConfirmationEmail(user, ordered, Locale.FRENCH));

            //
            fillFullURLOfFirstImage(ordered);
            model.addAttribute(SHOPPING_CART_MODEL, ordered);
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

/*    @GetMapping("/all")
    public String view(Model model) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof User) {


            List<Ordered> validOrdered = orderService.findByUserIdAndValidTrue((User) authentication.getPrincipal());

            model.addAttribute("ordered", validOrdered);
        } else {
            throw new IllegalArgumentException("This service requires identification");
        }
        return "/CustumerOrderedList";
    }*/

    @GetMapping("/orderedList")
    public String viewAll(Model model) {
        Collection<Ordered> orderedList = orderService.findAll();
        model.addAttribute("orderedList", orderedList);
        return"/admin/orderedList";
    }



    @ModelAttribute("address")
    public Address newAddress(){ return new Address(); }

    @ModelAttribute("Addresses")
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

  /*  @ModelAttribute("ordered")
    public Ordered newOrdered(){return new Ordered();}
*/
   /* @ModelAttribute("ordereds")
    Collection<Ordered> findAllOrdered(){
        Collection<Ordered> ordereds = orderService.findAll();
        return ordereds;

    }*/



}
