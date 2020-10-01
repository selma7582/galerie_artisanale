package com.example.galerie_artisanale.controller;

import com.example.galerie_artisanale.entity.*;
import com.example.galerie_artisanale.repository.CartItemRepository;
import com.example.galerie_artisanale.repository.ProductRepository;
import com.example.galerie_artisanale.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.galerie_artisanale.controller.HomeController.fileToPath;

@Controller
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    public static final String SHOPPING_CART = "SHOPPING_CART";
    public static final String CART_ITEM_LIST= "cartItemList";

    @Autowired
    private UserService userService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private StorageService storageService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductRepository productRepository;


    @PostMapping("/cart")
    public String shoppingCart(CartItem cartItem, HttpSession session) {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof User) {
            User connectedUser = (User) authentication.getPrincipal();
            ShoppingCart shoppingCart = shoppingCartService.findByUser(connectedUser);
            if (shoppingCart == null) {
                shoppingCart = new ShoppingCart();
                shoppingCart.setOrdered(new Ordered());
            }
            if (shoppingCart.getOrdered().getCartItemList() == null) {
                shoppingCart.getOrdered().setCartItemList(new ArrayList<>());
            }
            shoppingCart.getOrdered().getCartItemList().add(cartItem);
            shoppingCart.setUser(connectedUser);
            shoppingCartService.save(shoppingCart);
            session.removeAttribute(SHOPPING_CART);
            // travailler avec la base de donnée (doit etre sauvegarder dans la base de donnee pour avoir la possibilité de recuperer tout les items suite a  une nouvelle connexion )
        } else {

            Object shoppingCart = session.getAttribute(SHOPPING_CART);
            if (shoppingCart == null) {
                ShoppingCart cart = new ShoppingCart();
                cart.setOrdered(new Ordered());

                cart.getOrdered().getCartItemList().add(cartItem);
                session.setAttribute(SHOPPING_CART, cart);
            } else {
                if (shoppingCart instanceof ShoppingCart) {

                    ShoppingCart cart = (ShoppingCart) shoppingCart;
                    if (cart.getOrdered() == null){
                        cart.setOrdered(new Ordered());
                    }
                    cart.getOrdered().getCartItemList().add(cartItem);
                    // à verifier si ça nécissaire
                    session.setAttribute(SHOPPING_CART, cart);
                } else {
                    throw new IllegalStateException("Should not have this state");
                }
            }


        }
        return "redirect:/galerie";
    }

    @RequestMapping("/view")
    public String shoppingCart(Model model, HttpSession session) {

        ShoppingCart shoppingCart = (ShoppingCart) session.getAttribute(SHOPPING_CART);

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof User) {
            // we will switch to work with database and not anymore with session
            User connectedUser = (User) authentication.getPrincipal();
            ShoppingCart persistedShoppingCart = shoppingCartService.findByUser(connectedUser);
            if (persistedShoppingCart == null) {
                // to be checked
                shoppingCart.setUser(connectedUser);
                shoppingCart = shoppingCartService.save(shoppingCart);
                session.removeAttribute(SHOPPING_CART);
            } else {
                // Il a un panier ouvert dans le session et un dans la base de donnée (ancient )
                if (shoppingCart != null) {
                    shoppingCart.setUser(persistedShoppingCart.getUser());
                    shoppingCartService.remove(persistedShoppingCart);
                    shoppingCart = shoppingCartService.save(shoppingCart);
                    session.removeAttribute(SHOPPING_CART);
                } else {
                    shoppingCart = persistedShoppingCart;

                }
            }
        }

/*
        List<CartItem> cartItems = cartItemRepository.findByShoppingCart(shoppingCart);
*/
        //Product product = cartItems.stream()
            //product.getImagesList().stream().forEach(img->img.setFullURL((fileToPath(storageService.load(img.getUrl_image())))));


        model.addAttribute("shoppingCart", shoppingCart);

        return "shoppingCart";
    }


/*    @GetMapping("/confirm")
    public String confirmShoppingCart(Model model, HttpSession session) {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User connectedUser = (User) authentication.getPrincipal();
        ShoppingCart persistedShoppingCart = shoppingCartService.findByUser(connectedUser);



            Ordered cart =  new Ordered();
            cart.setEnabled(true);
            cart.getCartItemList().stream().forEach(cartItem -> cartItem.setQty(Integer.min(cartItem.getQty(),cartItem.getProduct().getInStockNumber())));
            LocalDateTime localDateTime = LocalDateTime.now();
            Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            cart.setOrderDate(date);


            orderService.save(cart);
            session.setAttribute(SHOPPING_CART,null);

            return "orderConfirm";
        }*/

    @RequestMapping("/updateCartItem")
    public String updateShoppingCart(
            @ModelAttribute("id") Long cartItemId,
            @ModelAttribute("qty") int qty
    ) {
        CartItem cartItem = cartItemService.findById(cartItemId);
        cartItem.setQty(qty);
        cartItemService.updateCartItem(cartItem);

        return "forward:/shoppingCart/cart";
    }

    @RequestMapping("/removeItem")
    public String removeItem(@RequestParam("id") Long id) {
        cartItemService.removeOne(id);

        return "forward:/shoppingCart/cart";
    }

    private void fillFulImgUrl(List<Product> products) {
        products.forEach(product -> product.getImagesList().stream().forEach(image -> image.setFullURL((fileToPath(storageService.load(image.getUrl_image()))))));
    }

}
