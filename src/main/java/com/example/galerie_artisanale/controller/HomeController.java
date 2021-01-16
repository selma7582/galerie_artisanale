package com.example.galerie_artisanale.controller;

import com.example.galerie_artisanale.entity.*;
import com.example.galerie_artisanale.security.MailConstructor;
import com.example.galerie_artisanale.security.PasswordResetToken;
import com.example.galerie_artisanale.security.SecurityUtility;
import com.example.galerie_artisanale.service.*;
import com.example.galerie_artisanale.service.impl.UserSecurityService;
import com.example.galerie_artisanale.util.PriceRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;
import java.nio.file.Path;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class HomeController {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailConstructor mailConstructor;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private SecurityUtility securityUtility;


    @Autowired
    private OrderService orderService;

    @Autowired
    private CityService cityService;

    @Autowired
    private DimensionService dimensionService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ShapeService shapeService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private ShoppingCartHeaderFiller shoppingCartHeaderFiller ;

    @Autowired
    private AddressService addressService;

    @RequestMapping("/")
    public String index(Model model, HttpSession session) {
        model.addAttribute("categories", categoryService.findAllCategoryNames());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (User.class.isInstance(authentication.getPrincipal())) {

            User connectedUser = (User) authentication.getPrincipal();
            if (connectedUser.getRole().getRole().equals("ROLE_ADMIN")) {
                /*return "admin/orderedList";*/
                return "redirect:/orderedList";
            }
        }
        shoppingCartHeaderFiller.fill(model,session);
        return "index";
    }

    @RequestMapping("/login")
    public String login(Model model) {

        model.addAttribute("categories", categoryService.findAllCategoryNames());

        model.addAttribute("classActiveLogin", true);

        return "myAccount";
    }

    @RequestMapping("/galerie/{category}")
    public String galerieByCategory(@PathVariable String category, Model model, Principal principal, HttpSession session,
                                    @RequestParam(required = false) Integer page) {
        int pageSize = 4;
        if (page == null){
            page = 1 ;
        }
        Page<Product> pages = productService.findByCategory(category,page,pageSize);
        return galerieForAll(model, principal, session, pages,page);
    }
    @RequestMapping("/searchByDimension/{dimension}")
    public String searchByDimension(@PathVariable String dimension, Model model, Principal principal, HttpSession session,
                                    @RequestParam(required = false) Integer page){
        int pageSize = 4;
        if (page == null){
            page = 1 ;
        }
        Page<Product> pages = productService.findByDimension(dimension,page,pageSize);
        return galerieForAll(model, principal, session, pages,page);
    }

    @RequestMapping("/searchByShape/{shape}")
    public String searchByshape(@PathVariable String shape,
                                Model model, Principal principal, HttpSession session,
                                @RequestParam(required = false) Integer page){

        int pageSize = 4;
        if (page == null){
            page = 1 ;
        }
        Page<Product> pages = productService.findByShape(shape,page,pageSize);
        return galerieForAll(model, principal, session, pages, page);
    }



    @RequestMapping("/galerie")
    public String galerie(Model model, Principal principal, HttpSession session,
                          @RequestParam(required = false) Integer page) {

        int pageSize = 4;
        if (page == null){
            page = 1 ;
        }
        Page<Product> pages = productService.findPagination(page,pageSize);
        return galerieForAll(model, principal, session, pages,page);
    }


    private String galerieForAll(Model model, Principal principal, HttpSession session, Page<Product> pages,int page) {
        model.addAttribute("categories", categoryService.findAllCategoryNames());
        model.addAttribute("dimensions", dimensionService.findAllDimensionDescription());
        model.addAttribute("shapes", shapeService.findAllShapeName());
        model.addAttribute("currentPage",page);
        model.addAttribute("totalPages",pages.getTotalPages());
        Ordered shoppingCart;
        if (principal != null) {
            String username = principal.getName();
            User user = userService.findByUsername(username);
            model.addAttribute("user", user);
            shoppingCart = orderService.findShoppingCart(user);
        } else {
            shoppingCart = (Ordered) session.getAttribute(ShoppingCartController.SHOPPING_CART_SESSION);
        }



        List<Product> productList = pages.getContent();
        productList.stream()
                .filter(product -> !product.getImagesList().isEmpty())
                .map(product -> product.getImagesList().get(0))
                .forEach(img -> img.setFullURL((fileToPath(storageService.load(img.getUrl_image())))));

        model.addAttribute("productList", productList);
        model.addAttribute("activeAll", true);
        model.addAttribute("shoppingCart", shoppingCart);

        if (productList.isEmpty()) {
            model.addAttribute("emptyList", true);

        }
        return "galerie";
    }

    @RequestMapping("/productDetail")
    public String productDetail(
            @PathParam("id") Long id, Model model, Principal principal, HttpSession session) {
        model.addAttribute("categories", categoryService.findAllCategoryNames());

        if (principal != null) {
            String username = principal.getName();
            User user = userService.findByUsername(username);
            model.addAttribute("user", user);

        }
        Product product = productService.findOne(id);
        product.getImagesList()
                .stream().forEach(img -> img.setFullURL((fileToPath(storageService.load(img.getUrl_image())))));

        model.addAttribute("product", product);
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setBuyinPrice(product.getPrice());
        cartItem.setQty(1);
        List<Integer> qtyList = new ArrayList<>();
        // Ajouter de i jusquau la quaitié s'il est < 10 ou bien 10
        for (int i = 1; i <= Math.min(10, product.getInStockNumber()); i++) {
            qtyList.add(i);
        }
        model.addAttribute("qtyList", qtyList);
        model.addAttribute("cartItem", cartItem);
        shoppingCartHeaderFiller.fill(model,session);
        return "productDetail";

    }



    @RequestMapping("/forgetPassword")
    public String forgetPassword(
            HttpServletRequest request,
            @ModelAttribute("email") String email,
            Model model) {
        model.addAttribute("categories", categoryService.findAllCategoryNames());

        model.addAttribute("classActiveForgetPassword", true);
        User user = userService.findByEmail(email);

        if (user == null) {
            model.addAttribute("emailNotExists", true);
            return "myAccount";
        }

        userService.save(user);
        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user, token);
        String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        SimpleMailMessage newEmail = mailConstructor.constructResetTokenPasswordEmail(appUrl, request.getLocale(), token, user);
        mailSender.send(newEmail);
        model.addAttribute("forgetPasswordEmailSent", "true");
        return "myAccount";

    }

    //resetForgetPassword
    @RequestMapping(value="/resetPassword", method=RequestMethod.POST)
    public String resetForgetPassword(
            @ModelAttribute("user") User user,
            @ModelAttribute("newPassword") String newPassword,
            Model model
    ) throws Exception {
        model.addAttribute("categories", categoryService.findAllCategoryNames());
        User currentUser = userService.findById(user.getId_user());
        if (currentUser == null) {
            throw new Exception("User not found");
        }

//		update password

        if (newPassword != null && !newPassword.isEmpty() && !newPassword.equals("")) {
            //BCryptPasswordEncoder passwordEncoder = SecurityUtility.passwordEncoder();
            String dbPassword = currentUser.getPassword();
          /*  if (passwordEncoder.matches(user.getPassword(), dbPassword)) {
                currentUser.setPassword(passwordEncoder.encode(newPassword));
            } else {
                model.addAttribute("incorrectPassword", true);
                model.addAttribute("classActiveEdit", true);
                return "resetPassword";
            }*/
        }
        currentUser.setPassword(passwordEncoder.encode(newPassword));
        userService.save(currentUser);

        model.addAttribute("updateSuccess", true);
        model.addAttribute("user", currentUser);
        model.addAttribute("classActiveEdit", true);
        model.addAttribute("listOfShippingAddresses", true);
        UserDetails userDetails = userSecurityService.loadUserByUsername(currentUser.getUsername());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
                userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // return "redirect:../product/shapeList";
        return "redirect:/";
    }


    @RequestMapping("/resetPassword")
    public String resetForgetPassword(@RequestParam("token") String token, Model model) {
        model.addAttribute("categories", categoryService.findAllCategoryNames());

        PasswordResetToken passToken = userService.getPasswordResetToken(token);
        if (passToken == null) {
            String message = "Invalid Token.";
            model.addAttribute("message", message);
            return "redirect:/badrequest";
        }
        User user = passToken.getUser();
        String username = user.getUsername();

        UserDetails userDetails = userSecurityService.loadUserByUsername(username);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
                userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        model.addAttribute("user", user);
        model.addAttribute("classActiveNewUser, true");
        model.addAttribute("classActiveEdit", true);
        return "resetPassword";

    }

    @RequestMapping("/myProfile")
    public String myProfile(Model model, Principal principal) {
        model.addAttribute("categories", categoryService.findAllCategoryNames());

        User user = userService.findByUsername(principal.getName());

        model.addAttribute("user", user);

        return "myProfile";
    }

    @RequestMapping("/profile")
    public String profile(Model model, Principal principal) {
        model.addAttribute("categories", categoryService.findAllCategoryNames());

        User user = userService.findByUsername(principal.getName());

        model.addAttribute("user", user);

        return "profile";
    }





    @RequestMapping(value = "/newUser", method = RequestMethod.POST)
    public String newUserPost(
            HttpServletRequest request,
            @ModelAttribute("email") String userEmail,  Model model ) throws Exception {

        model.addAttribute("categories", categoryService.findAllCategoryNames());

        model.addAttribute("classActiveNewAccount", true);


        if (userService.findByEmail(userEmail) != null) {
            model.addAttribute("emailExists", true);
            return "myAccount";
        }
        User user = new User();
        user.setEnabled(1);
        user.setUsername(userEmail);
        user.setEmail(userEmail);



        User persistedUser = userService.createUser(user, "ROLE_USER");


        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(persistedUser, token);

        String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

        String appUrl2 = MvcUriComponentsBuilder.fromMethodName(HomeController.class,
                "newUser", token,model).build().toString();



        SimpleMailMessage email = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user);
        System.out.println("coucou");
        System.out.println(email);
        mailSender.send(email);//.send(email);//.send(email);

        model.addAttribute("emailSent", "true");
        return "myAccount";
    }

    @RequestMapping("/newUser")
    public String newUser(@RequestParam("token") String token, Model model) {
        model.addAttribute("categories", categoryService.findAllCategoryNames());

        PasswordResetToken passToken = userService.getPasswordResetToken(token);

        if (passToken == null) {
            String message = "Invalid Token.";
            model.addAttribute("message", message);
            return "redirect:/badrequest";

        }

        User user = passToken.getUser();
        String username = user.getUsername();

        UserDetails userDetails = userSecurityService.loadUserByUsername(username);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getUsername(),
                userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        model.addAttribute("user", user);
        model.addAttribute("classActiveNewUser, true");
        model.addAttribute("classActiveEdit", true);
        return "myProfile";

    }


    @RequestMapping(value="/updateInfo", method=RequestMethod.POST)
    public String updateInfo(
            @ModelAttribute("user") User user,
            Model model
    ) throws Exception {

        model.addAttribute("categories", categoryService.findAllCategoryNames());



        User currentUser = userService.findById(user.getId_user());


        if(currentUser == null) {
            throw new Exception ("User not found");
        }

        if (userService.findByEmail(user.getEmail())!=null) {
            if(userService.findByEmail(user.getEmail()).getId_user() != currentUser.getId_user()) {
                model.addAttribute("emailExists", true);
                return "myProfile";
            }
        }



        currentUser.setFirstName(user.getFirstName());
        currentUser.setLastName(user.getLastName());
        currentUser.setUsername(user.getUsername());
        currentUser.setEmail(user.getEmail());
        currentUser.setTel(user.getTel());



        userService.save(currentUser);

        model.addAttribute("updateSuccess", true);
        model.addAttribute("user",currentUser );
        model.addAttribute("classActiveEdit", true);

        model.addAttribute("listOfShippingAddresses", true);

        UserDetails userDetails = userSecurityService.loadUserByUsername(currentUser.getUsername());

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
                userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);


        return "profile";
    }


    @RequestMapping(value="/updateUserInfo", method=RequestMethod.POST)
    public String updateUserInfo(
            @ModelAttribute("user") User user,
            @ModelAttribute("newPassword") String newPassword,
            Model model
    ) throws Exception {

        model.addAttribute("categories", categoryService.findAllCategoryNames());


        User currentUser = userService.findById(user.getId_user());

        User currentUser1 = userService.findByUsername(user.getUsername());


        if(currentUser == null) {
            throw new Exception ("User not found");
        }

        if (userService.findByEmail(user.getEmail())!=null) {
            if(userService.findByEmail(user.getEmail()).getId_user() != currentUser.getId_user()) {
                model.addAttribute("emailExists", true);
                return "myProfile";
            }
        }

        if (userService.findByUsername(user.getUsername())!=null) {
            if(userService.findByUsername(user.getUsername()).getId_user() != currentUser.getId_user()) {
                model.addAttribute("usernameExists", true);
                return "myProfile";
            }
        }


        if(currentUser.getEmail().equals(user.getEmail())) {
            currentUser.setFirstName(user.getFirstName());
            currentUser.setLastName(user.getLastName());
            currentUser.setUsername(user.getUsername());
            currentUser.setEmail(user.getEmail());
            currentUser.setTel(user.getTel());
            currentUser.setPassword(passwordEncoder.encode(newPassword));

            userService.save(currentUser);

            model.addAttribute("updateSuccess", true);
            model.addAttribute("user",currentUser );
            model.addAttribute("classActiveEdit", true);

            model.addAttribute("listOfShippingAddresses", true);

            UserDetails userDetails = userSecurityService.loadUserByUsername(currentUser.getUsername());

            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
                    userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return "redirect:/";
        }
        else{

            model.addAttribute("incorrectToken",true);
            model.addAttribute("classActiveEdit", true);

            return "myProfile";

        }

    }

    @GetMapping("/imgs/{filename:images.+}")
    @ResponseBody
    public ResponseEntity<Resource> loadImageFromServer(@PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename.replaceAll(">", "/"));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(MediaType.IMAGE_JPEG_VALUE))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFilename()).body(file);
    }

    // localhost:8080/imgs/prouct_4>7.jpeg
    public static String fileToPath(Path path) {
        return MvcUriComponentsBuilder.fromMethodName(HomeController.class,
                "loadImageFromServer", path.toString().replaceAll("\\\\", ">")).build().toString();
    }


    @GetMapping(value = "/products")
    public String filterProducts(PriceRange priceRange, Model model, Principal principal, HttpSession session,
                                 @RequestParam(required = false)Integer page) {

        List<Product> products = productService.filterProducts(priceRange.getMin(), priceRange.getMax());
        model.addAttribute("emptyList", products.isEmpty());
        products.stream()
                .filter(product -> !product.getImagesList().isEmpty())
                .map(product -> product.getImagesList().get(0))
                .forEach(img -> img.setFullURL((fileToPath(storageService.load(img.getUrl_image())))));
        model.addAttribute("productList", products);

        return "products";
    }





    @GetMapping("/myOrderedList")
    public String view(Model model) {
        model.addAttribute("categories", categoryService.findAllCategoryNames());

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof User) {


            List<Ordered> myOrderedList = orderService.findByUser((User) authentication.getPrincipal());//.findByUserAndStatus((User) authentication.getPrincipal(), OrderedStatus.VALID);

            model.addAttribute("myOrderedList", myOrderedList);
        } else {
            throw new IllegalArgumentException("This service requires identification");
        }
        return "myOrderedList";
    }


    @RequestMapping("/myOrderedDetail")
    public String orderDetail(
            @RequestParam("id") Long orderId,
            Principal principal, Model model
    ){
        model.addAttribute("categories", categoryService.findAllCategoryNames());

        User user = userService.findByUsername(principal.getName());
        Ordered ordered = orderService.findOne(orderId);
        List<Ordered> myOrderedList = orderService.findByUser(user);//.findByUserAndStatus((User) authentication.getPrincipal(), OrderedStatus.VALID);

        model.addAttribute("myOrderedList", myOrderedList);

        if(ordered.getUser().getId_user()!=user.getId_user()) {
            return "badRequestPage";
        } else {
            List<CartItem> cartItemList = cartItemService.findByOrdered(ordered); //.findByOrder(order);
            model.addAttribute("cartItemList", cartItemList);
            model.addAttribute("user", user);
            model.addAttribute("ordered", ordered);

            model.addAttribute("listOfCreditCards", true);
            model.addAttribute("displayOrderDetail", true);

            return "/myOrderedList";
        }
    }

    @GetMapping(value = "/annulerCommande/{ordered}")
    public String annulerCommande(@PathVariable("ordered") Long id,Principal principal, Model model) {
        model.addAttribute("categories", categoryService.findAllCategoryNames());

        Ordered ordered = orderService.findById(id);
        List<Ordered> orderedList = orderService.findAll();

        ordered.setStatus(OrderedStatus.ANNULER);
        ordered.getCartItemList().stream().forEach(product->product.getProduct().setInStockNumber(product.getQty()+product.getProduct().getInStockNumber()));
        orderService.save(ordered);


        User user = userService.findByUsername(principal.getName());
        List<Ordered> myOrderedList = orderService.findByUser(user);
        model.addAttribute("ordered", ordered);
        model.addAttribute("myOrderedList", myOrderedList);

        model.addAttribute("displayOrderDetail", true);

        return "/myOrderedList";
    }


    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable(value="pageNo")int pageNo,Model model,Principal principal,HttpSession session){
        int pageSize = 8;
        Page<Product> page = productService.findPagination(pageNo,pageSize);
        List<Product> productList = page.getContent();

        model.addAttribute("currentPage",pageNo);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("productList",productList);
        return "galerie";
    }


}
