package com.example.galerie_artisanale.controller;

import com.example.galerie_artisanale.entity.*;
import com.example.galerie_artisanale.repository.CartItemRepository;
import com.example.galerie_artisanale.repository.ProductRepository;
import com.example.galerie_artisanale.security.MailConstructor;
import com.example.galerie_artisanale.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static com.example.galerie_artisanale.controller.HomeController.fileToPath;

@Controller
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    public static final String SHOPPING_CART_SESSION = "SHOPPING_CART";
    public static final String CART_ITEM_LIST = "cartItemList";
    public static final String SHOPPING_CART_MODEL = "shoppingCart";


    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private CategoryService categoryService;


    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private StorageService storageService;

    @Autowired
    private OrderService orderService;


    @PostMapping("/cart")
    public String shoppingCart(CartItem cartItem, HttpSession session, Model model) {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof User) {
            User connectedUser = (User) authentication.getPrincipal();
            Ordered shoppingCart = orderService.findShoppingCart(connectedUser);
            if (shoppingCart == null) {
                shoppingCart = new Ordered();
            }
            if (shoppingCart.getCartItemList() == null) {
                shoppingCart.setCartItemList(new ArrayList<>());
            }
            cartItem.setOrdered(shoppingCart);
            Ordered cart = (Ordered) session.getAttribute(SHOPPING_CART_SESSION);
            if (cart != null && cart.getId() == null && cart.getUser() != null) {
                cart = orderService.save(cart);
            }
            CartItem cartItem1 = currentCartItem(cartItem.getProduct().getId(), (Ordered) shoppingCart);
            if (cartItem1 != null) {
                cartItem1.setQty(cartItem1.getQty() + cartItem.getQty());
                if (cartItem1.getId() != null) {
                    cartItemRepository.save(cartItem1);
                }
            } else {

                shoppingCart.getCartItemList().add(cartItem);
                shoppingCart.setUser(connectedUser);
                orderService.save(shoppingCart);
                session.removeAttribute(SHOPPING_CART_SESSION);
            }
        } else {

            Object shoppingCart = session.getAttribute(SHOPPING_CART_SESSION);
            if (shoppingCart == null) {

                Ordered cart = new Ordered();

                cartItem.setOrdered(cart);
                cart.getCartItemList().add(cartItem);
                session.setAttribute(SHOPPING_CART_SESSION, cart);


            } else {
                if (shoppingCart instanceof Ordered) {
                    CartItem cartItem1 = currentCartItem(cartItem.getProduct().getId(), (Ordered) shoppingCart);
                    if (cartItem1 != null) {
                        cartItem1.setQty(cartItem1.getQty() + cartItem.getQty());
                        if (cartItem1.getId() != null) {
                            cartItemRepository.save(cartItem1);
                        }
                    } else {

                        Ordered cart = (Ordered) shoppingCart;
                        cartItem.setOrdered(cart);
                        cart.getCartItemList().add(cartItem);
                        // à verifier si ça nécissaire
                        session.setAttribute(SHOPPING_CART_SESSION, cart);
                    }
                } else {
                    throw new IllegalStateException("Should not have this state");
                }
            }


        }
        model.addAttribute("addSuccess", true);
        //return "shoppingCart";
        return "forward:/shoppingCart/view";

    }

    @RequestMapping("/view")
    public String shoppingCart(Model model, HttpSession session) {
        model.addAttribute("categories", categoryService.findAllCategoryNames());

        Ordered shoppingCart = (Ordered) session.getAttribute(SHOPPING_CART_SESSION);


        /*List<Integer> qtyList = new ArrayList<>();
        List<CartItem> cartItems = shoppingCart.getCartItemList();
        // Ajouter de i jusquau la quaitié s'il est < 10 ou bien 10
        for (int i = 1 ; i<= Math.min(10,cartItems.stream().map().filter(p)product.getInStockNumber());i++){
            qtyList.add(i);
        }
        model.addAttribute("qtyList", qtyList);*/


        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof User) {
            // we will switch to work with database and not anymore with session
            User connectedUser = (User) authentication.getPrincipal();
            Ordered persistedShoppingCart = orderService.findShoppingCart(connectedUser);
            if (persistedShoppingCart == null) {
                // to be checked
                if (shoppingCart != null) {
                    shoppingCart.setUser(connectedUser);
                    shoppingCart = orderService.save(shoppingCart);
                    session.removeAttribute(SHOPPING_CART_SESSION);
                }
            } else {
                // Il a un panier ouvert dans le session et un dans la base de donnée (ancient )
                if (shoppingCart != null) {
                    shoppingCart.setUser(persistedShoppingCart.getUser());
                    orderService.remove(persistedShoppingCart);
                    shoppingCart = orderService.save(shoppingCart);
                    session.removeAttribute(SHOPPING_CART_SESSION);
                } else {
                    shoppingCart = persistedShoppingCart;
                }
            }
        }
        if (shoppingCart != null) {
            fillFullURLOfFirstImage(shoppingCart);
        }

        model.addAttribute(SHOPPING_CART_MODEL, shoppingCart);
        return "shoppingCart";
    }

    private void fillFullURLOfFirstImage(Ordered shoppingCart) {
        List<CartItem> cartItems = shoppingCart.getCartItemList();

        cartItems.stream()
                .map(CartItem::getProduct)
                .filter(product -> !product.getImagesList().isEmpty())
                .map(product -> product.getImagesList().get(0))
                .forEach(img -> img.setFullURL((fileToPath(storageService.load(img.getUrl_image())))));
    }

    @RequestMapping("/updateCartItem")
    public String updateShoppingCart(@ModelAttribute("qty") int qty, @ModelAttribute("itemID") long itemID) {
        CartItem persistedCartItem = cartItemService.findById(itemID);
        persistedCartItem.setQty(qty);
        cartItemService.save(persistedCartItem);
        return "redirect:/shoppingCart/view";
        // return "forward:/shoppingCart/cart";

    }

    @RequestMapping("/deleteItem/{itemId}")
    public String removeItem(@PathVariable("itemId") long itemId, Model model, HttpSession session) {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof User) {

            CartItem cartItem = cartItemService.findById(itemId);
            cartItemService.remove(cartItem);
            return "redirect:/shoppingCart/view";
        } else {
            Object shoppingCart = session.getAttribute(SHOPPING_CART_SESSION);
            if(shoppingCart == null) {

                shoppingCart = new Ordered();
            }

            if (shoppingCart instanceof Ordered) {
                List<CartItem> cartItemList = cartItemService.findByOrdered((Ordered) shoppingCart);
                cartItemService.removeOne(itemId);
                model.addAttribute("shoppingCart", cartItemList);

            }
            else {
                throw new IllegalStateException("Should not have this state");
            }

        }
        return "redirect:/shoppingCart/view";
    }


    private void fillFulImgUrl(List<Product> products) {
        products.forEach(product -> product.getImagesList().stream().forEach(image -> image.setFullURL((fileToPath(storageService.load(image.getUrl_image()))))));
    }

    private CartItem currentCartItem(Long id, Ordered ordered) {
        if (ordered.getCartItemList() == null) {
            return null;
        }
        return ordered.getCartItemList()
                .stream()
                .filter(lcc -> lcc.getProduct().getId().equals(id))
                .findFirst().orElse(null);
    }


}
