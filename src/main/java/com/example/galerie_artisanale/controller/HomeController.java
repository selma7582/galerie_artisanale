package com.example.galerie_artisanale.controller;

import com.example.galerie_artisanale.entity.*;
import com.example.galerie_artisanale.security.MailConstructor;
import com.example.galerie_artisanale.security.PasswordResetToken;
import com.example.galerie_artisanale.security.SecurityUtility;
import com.example.galerie_artisanale.service.*;
import com.example.galerie_artisanale.service.impl.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
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
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    private BCryptPasswordEncoder passwordEncoder ;

    @Autowired
    private SecurityUtility securityUtility ;


    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CityService cityService;


    @Autowired
    private CategoryService categoryService ;

    @RequestMapping("/")
    public String index(Model model) {


        model.addAttribute("categories", categoryService.findAllCategoryNames());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (User.class.isInstance(authentication.getPrincipal())) {

            User connectecUser = (User) authentication.getPrincipal();
            if (connectecUser.getRole().getRole().equals("ROLE_ADMIN")) {
                return "admin/home";
            }
        }

        return "index";
    }


    @RequestMapping("/login")
    public String login(Model model) {

        model.addAttribute("categories", categoryService.findAllCategoryNames());

        model.addAttribute("classActiveLogin", true);

        return "myAccount";
    }


    @RequestMapping("/galerie/{category}")
    public String galerieByCategory(@PathVariable String category, Model model, Principal principal, HttpSession session) {

        List<Product> productList = productService.findByCategory(category);
        return galerieForAll(model, principal, session, productList);
    }

    @RequestMapping("/galerie")
    public String galerie(Model model, Principal principal, HttpSession session) {

        List<Product> productList = productService.findAll();
        return galerieForAll(model, principal, session, productList);
    }


    private String galerieForAll(Model model, Principal principal, HttpSession session, List<Product> productList) {
        model.addAttribute("categories", categoryService.findAllCategoryNames());
        ShoppingCart shoppingCart ;
        if (principal != null) {
            String username = principal.getName();
            User user = userService.findByUsername(username);
            model.addAttribute("user", user);
            shoppingCart = shoppingCartService.findByUser(user);
        } else {
            shoppingCart =(ShoppingCart) session.getAttribute(ShoppingCartController.SHOPPING_CART);
        }

        // construire l'url des images
        productList.stream()
                .flatMap(product -> product.getImagesList().stream())
                .forEach(img -> img.setFullURL((fileToPath(storageService.load(img.getUrl_image())))));
        // fin du construction des url des images
        model.addAttribute("productList", productList);
        model.addAttribute("activeAll", true);
        model.addAttribute("shoppingCart",shoppingCart);
        return "galerie";
    }



    @RequestMapping("/productDetail")
    public String productDetail(
            @PathParam("id") Long id, Model model, Principal principal) {
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
        cartItem.setQty(1);
        List<Integer> qtyList = new ArrayList<>();
        // Ajouter de i jusquau la quaitié s'il est < 10 ou bien 10
        for (int i = 1 ; i<= Math.min(10,product.getInStockNumber());i++){
            qtyList.add(i);
        }
        model.addAttribute("qtyList", qtyList);
        model.addAttribute("cartItem", cartItem);

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

        String password = securityUtility.randomPassword();

        String encryptedPassword = passwordEncoder.encode(password);
        user.setPassword(encryptedPassword);

        userService.save(user);

        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user, token);

        String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

        SimpleMailMessage newEmail = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user, password);

        mailSender.send(newEmail);

        model.addAttribute("forgetPasswordEmailSent", "true");
        return "myAccount";

    }

    @RequestMapping("/myProfile")
    public String myProfile(Model model, Principal principal) {
        model.addAttribute("categories", categoryService.findAllCategoryNames());

        User user = userService.findByUsername(principal.getName());
        /*Ordered ordered = (Ordered) orderService.findByUser(user);
        Address address = new Address();
        City city = new City();*/
        model.addAttribute("user", user);/*
        model.addAttribute("orderedList",user.getOrderedList());
        model.addAttribute("userShipping",ordered.getShippingAddress());
        model.addAttribute("userBilling",ordered.getBillingAddress());*/
/*
        model.addAttribute("cityList",address.getCity());
*/
        /*List<City> cityList = cityService.findAll();
        model.addAttribute("cityList",cityList);
        model.addAttribute("classActiveEdit",true);*/

        return "myProfile";
    }



    @RequestMapping(value = "/newUser", method = RequestMethod.POST)
    public String newUserPost(
            HttpServletRequest request,
            @ModelAttribute("email") String userEmail,  Model model ) throws Exception {

        model.addAttribute("categories", categoryService.findAllCategoryNames());

        model.addAttribute("classActiveNewAccount", true);
        // model.addAttribute("email", userEmail);
        //model.addAttribute("username", userName);

      /*  if (userService.findByUsername(userName) != null) {
            model.addAttribute("usernameExists", true);
            return "myAccount";
        }*/

        if (userService.findByEmail(userEmail) != null) {
            model.addAttribute("emailExists", true);
            return "myAccount";
        }
        User user = new User();
        user.setEnabled(1);
        user.setUsername(userEmail);
        user.setEmail(userEmail);

        String password = securityUtility.randomPassword();

        String encryptedPassword = passwordEncoder.encode(password);
        user.setPassword(encryptedPassword);

        User persistedUser = userService.createUser(user, "ROLE_USER");


        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(persistedUser, token);

        String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

        String appUrl2 = MvcUriComponentsBuilder.fromMethodName(HomeController.class,
                "newUser", token,model).build().toString();

        SimpleMailMessage email = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user, password);
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

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
                userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        model.addAttribute("user", user);
        model.addAttribute("classActiveNewUser, true");
        model.addAttribute("classActiveEdit", true);
        return "myProfile";

    }

    @RequestMapping(value="/updateUserInfo", method=RequestMethod.POST)
    public String updateUserInfo(
            @ModelAttribute("user") User user,
            @ModelAttribute("newPassword") String newPassword,
            Model model
    ) throws Exception {

        model.addAttribute("categories", categoryService.findAllCategoryNames());

        model.addAttribute("categories", categoryService.findAllCategoryNames());

/*
        User currentUser = userService.findById(user.getId_user());
*/

        User currentUser = userService.findByUsername(user.getUsername());


        if(currentUser == null) {
            throw new Exception ("User not found");
        }

        /*check email already exists*/
        if (userService.findByEmail(user.getEmail())!=null) {
            if(userService.findByEmail(user.getEmail()).getId_user() != currentUser.getId_user()) {
                model.addAttribute("emailExists", true);
                return "myProfile";
            }
        }

        /*check username already exists*/
        if (userService.findByUsername(user.getUsername())!=null) {
            if(userService.findByUsername(user.getUsername()).getId_user() != currentUser.getId_user()) {
                model.addAttribute("usernameExists", true);
                return "myProfile";
            }
        }

//		update password

        if (newPassword != null && !newPassword.isEmpty() && !newPassword.equals("")){


            //BCryptPasswordEncoder passwordEncoder = SecurityUtility.passwordEncoder();
            String dbPassword = currentUser.getPassword();
            if(passwordEncoder.matches(user.getPassword(), dbPassword)){
                currentUser.setPassword(passwordEncoder.encode(newPassword));
            } else {
                model.addAttribute("incorrectPassword", true);

                return "myProfile";
            }
        }
        currentUser.setFirstName(user.getFirstName());
        currentUser.setLastName(user.getLastName());
        currentUser.setUsername(user.getUsername());
        currentUser.setEmail(user.getEmail());
        currentUser.setTel(user.getTel());
/*
        currentUser.setPassword(passwordEncoder.encode(newPassword));
*/


        userService.save(currentUser);

        model.addAttribute("updateSuccess", true);
        model.addAttribute("user",currentUser );
        model.addAttribute("classActiveEdit", true);

        model.addAttribute("listOfShippingAddresses", true);

        UserDetails userDetails = userSecurityService.loadUserByUsername(currentUser.getUsername());

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
                userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
/*
        model.addAttribute("orderList", user.getOrderedList());
*/

        return "myProfile";
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
}
