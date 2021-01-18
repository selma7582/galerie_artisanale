package com.example.galerie_artisanale.controller;

import com.example.galerie_artisanale.entity.*;
import com.example.galerie_artisanale.service.*;
import com.example.galerie_artisanale.util.Container;
import com.example.galerie_artisanale.util.PriceRange;
import com.example.galerie_artisanale.util.ProductDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

import static com.example.galerie_artisanale.controller.HomeController.fileToPath;

@Controller
@RequestMapping("/product")
public class ProductController {

    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private DimensionService dimensionService;

    @Autowired
    private ShapeService shapeService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private ProviderService providerService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;
    


    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addProduct(Model model) {
        Product product = new Product();
        List<Shape> shapeList = shapeService.findAll();
        List<Dimension> dimensionList = dimensionService.findAll();
        List<Category> categoryList = categoryService.findAll();
        List<Provider> providerList = providerService.findAll();
        model.addAttribute("providerList",providerList);
        model.addAttribute("categoryList",categoryList);
        model.addAttribute("dimensionList",dimensionList);
        model.addAttribute("shapeList",shapeList);
        model.addAttribute("product", product);

        return "/admin/addProduct";
    }

    @GetMapping(value = "/delete/{product}")
    public String deleteProduct(@PathVariable("product") Long id, Model model) {


        productService.removeOne(id);

        model.addAttribute("productList", productService.findAll());

        model.addAttribute("deleteSuccess", true);

        return "redirect:/product/productList";

    }

    @GetMapping(value = "/deleteCategory/{category}")
    public String deleteCategory(@PathVariable("category") Long id, Model model) {

        Category category = categoryService.findById(id);
        if (category.getProductList().size() == 0) {

            categoryService.removeOne(id);

            model.addAttribute("deleteSuccess", true);

        } else {
            model.addAttribute("notSuccess", true);
        }
        model.addAttribute("categoryList", categoryService.findAll());

        return "redirect:/product/categoryList";

        //return "admin/categoryList";
    }


    @GetMapping(value = "/deleteShape/{shape}")
    public String deleteShape(@PathVariable("shape") Long id, Model model) {

        Shape shape = shapeService.findById(id);
        List<Shape> shapeList = shapeService.findAll();

        if (shape.getProductList().size() == 0) {

            shapeService.removeOne(id);

            model.addAttribute("shapeList", shapeList);
            model.addAttribute("deleteSuccess", true);

        } else {
            model.addAttribute("shapeList", shapeList);
            model.addAttribute("notSuccess", true);
        }
        return "redirect:/product/shapeList";

    }

    @GetMapping(value = "/editShape")
    public String editShapeGet(Model model, @RequestParam("id") Long id) {
        Shape shape = shapeService.findById(id);
        /*if (shape != null) {*/
        model.addAttribute("shape", shape);
        return "admin/updateShape";
      /*  } else {
            throw new IllegalArgumentException();
        }*/
    }

    @PostMapping("/editShape")
    public String updateShape(@ModelAttribute Shape shape, BindingResult result) {
        String name = shape.getShapeName();
        String nameMaj = name.replaceFirst(".",(name.charAt(0)+"").toUpperCase());
        shape.setShapeName(nameMaj);
        shapeService.save(shape);
        return "redirect:../product/shapeList";
    }




    @PostMapping(value = "/add")
    public String addProductPost(@ModelAttribute("product") Product product, Model model,
                                 @RequestParam("file") MultipartFile[] files) throws Exception {

        model.addAttribute("productName", product.getProductName());
        if (productService.findByProductName(product.getProductName()) != null) {
            model.addAttribute("productExists", true);
            model.addAttribute("product",product);
            List<Category> categoryList = categoryService.findAll();
            model.addAttribute("categoryList", categoryList);
            List<Shape> shapeList = shapeService.findAll();
            model.addAttribute("shapeList",shapeList);
            List<Provider> providerList = providerService.findAll();
            model.addAttribute("providerList",providerList);
            List<Dimension> dimensions = dimensionService.findAll();
            model.addAttribute("dimensions",dimensions);
            return "admin/addProduct";
        }

        String name = product.getProductName();
        String nameMaj = name.replaceFirst(".",(name.charAt(0)+"").toUpperCase());
        product.setProductName(nameMaj);
        product = this.productService.save(product);
        for (MultipartFile file : files) {
            Image image = new Image();
            image.setProduct(product);

            image = imageService.save(image);
            image.setUrl_image("products/product_" + product.getId() + "/" + image.getId() + ".jpeg");
            // image.setUrl_image(String.format("product_%s/%s.jpeg",product.getId(),image.getId()));
            image = imageService.save(image);
            storageService.store(file, image.getUrl_image());

        }


        model.addAttribute("addSuccess", true);

        // return "admin/addProduct";
        return "redirect:../product/productInfo?id=" + product.getId();

        /*return "redirect:productList";*/
    }

    @PostMapping(value = "/addimage/{id}")
    public String addProductImagePost(@PathVariable Long id, Model model,
                                      @RequestParam("file") MultipartFile[] files) {
        Product product = this.productService.findOne(id);
        for (MultipartFile file : files) {
            Image image = new Image();
            image.setProduct(product);

            image = imageService.save(image);
            image.setUrl_image("products/product_" + product.getId() + "/" + image.getId() + ".jpeg");
            // image.setUrl_image(String.format("product_%s/%s.jpeg",product.getId(),image.getId()));
            image = imageService.save(image);
            storageService.store(file, image.getUrl_image());

        }
        model.addAttribute("addSuccess", true);
        return "redirect:../productInfo?id=" + id;
    }


    @RequestMapping("/productList")
    public String productList(Model model) {
        List<Product> productList = productService.findAll();
        Container container = new Container();
        container.setProductList(productList);
        model.addAttribute("productList", productList);
        model.addAttribute("container", container);

        productList.stream()
                .flatMap(products -> products.getImagesList().stream())
                .forEach(img -> img.setFullURL((fileToPath(storageService.load(img.getUrl_image())))));
        return "admin/productList";
    }

    @RequestMapping("/categoryList")
    public String categoryList(Model model) {

        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryList", categoryList);
        return "admin/categoryList";
    }

    @RequestMapping("/shapeList")
    public String shapeList(Model model) {
        List<Shape> shapeList = shapeService.findAll();
        model.addAttribute("shapeList", shapeList);
        return "admin/shapeList";
    }

    @RequestMapping("/dimensionList")
    public String dimensionList(Model model) {
        List<Dimension> dimensionList = dimensionService.findAll();
        model.addAttribute("dimensionList", dimensionList);
        return "admin/dimensionList";
    }

    @RequestMapping("/addShape")
    public String addShape(Model model) {
        List<Shape> shapeList = shapeService.findAll();
        model.addAttribute("shapeList", shapeList);
        return ("admin/addShape");
    }


    @RequestMapping(value = "/addShape", method = RequestMethod.POST)
    public String addShapePost(HttpServletRequest request,
                               @ModelAttribute("shape") Shape shape,
                               Model model) throws Exception {
        model.addAttribute("shapeName", shape.getShapeName());

        if (shapeService.findByShapeName(shape.getShapeName()) != null) {
            model.addAttribute("shapeExists", true);
            return "/admin/addShape";
        }

        String name = shape.getShapeName();
        String nameMaj = name.replaceFirst(".",(name.charAt(0)+"").toUpperCase());
        shape.setShapeName(nameMaj);

        this.shapeService.save(shape);
        return ("redirect:shapeList");
    }


   /* @RequestMapping(value = "/addShapePop", method = RequestMethod.GET)
    public String addShapePopPost(Model model) {
        Product product = new Product();
        List<Shape> shapeList = shapeService.findAll();
        List<Dimension> dimensionList = dimensionService.findAll();
        List<Category> categoryList = categoryService.findAll();
        List<Provider> providerList = providerService.findAll();
        model.addAttribute("providerList",providerList);
        model.addAttribute("categoryList",categoryList);
        model.addAttribute("dimensionList",dimensionList);
        model.addAttribute("shapeList",shapeList);
        model.addAttribute("product", product);

        return "/admin/addProduct";
    }*/

    @RequestMapping(value = "/addShapePop", method = RequestMethod.POST)
    public String addShapePopPost(HttpServletRequest request,
                               @ModelAttribute("shape") Shape shape,
                               Model model) throws Exception {
        model.addAttribute("shapeName", shape.getShapeName());
        //model.addAttribute("product", product);

        if (shapeService.findByShapeName(shape.getShapeName()) != null) {
            model.addAttribute("shapeExistss", true);
            return "redirect:/product/addShapePop";
        }

        String name = shape.getShapeName();
        String nameMaj = name.replaceFirst(".",(name.charAt(0)+"").toUpperCase());
        shape.setShapeName(nameMaj);

        this.shapeService.save(shape);

        return "redirect:/product/add";
    }


    @ModelAttribute("shape")
    public Shape newShape() {
        return new Shape();
    }

    @ModelAttribute("shapes")
    Collection<Shape> findAllShape() {
        Collection<Shape> shapes = (Collection<Shape>) shapeService.findAll();
        return shapes;
    }

    @RequestMapping("/addCategory")
    public String addCategory(Model model) {
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryList", categoryList);
        return ("admin/addCategory");
    }

    @RequestMapping(value = "/addCategory", method = RequestMethod.POST)
    public String addCategoryPost(
            HttpServletRequest request,
            @ModelAttribute("category") Category category,
            Model model) throws Exception {
        model.addAttribute("categoryName", category.getCategoryName());
        //model.addAttribute("category",category.);

        if (categoryService.findByCategoryName(category.getCategoryName()) != null) {
            model.addAttribute("categoryExists", true);
            return "/admin/addCategory";
        }
        String name = category.getCategoryName();
        String nameMaj = name.replaceFirst(".",(name.charAt(0)+"").toUpperCase());
        category.setCategoryName(nameMaj);

        this.categoryService.save(category);
        // return ("admin/addCategory");
        return ("redirect:categoryList");

    }

    @ModelAttribute("category")
    public Category newCategory() {
        return new Category();
    }

    @ModelAttribute("categories")
    Collection<Category> findAllCategory() {
        Collection<Category> categories = (Collection<Category>) categoryService.findAll();
        return categories;
    }

    @GetMapping(value = "/editCategory")
    public String editCategory(Model model, @RequestParam("id") Long id) {
        Category category = categoryService.findById(id);
        /*if (shape != null) {*/
        model.addAttribute("category", category);
        return "admin/updateCategory";

    }


    @PostMapping("/editCategory")
    public String editCategory(@ModelAttribute Category category, BindingResult result) {

        String name = category.getCategoryName();
        String nameMaj = name.replaceFirst(".",(name.charAt(0)+"").toUpperCase());
        category.setCategoryName(nameMaj);
        categoryService.save(category);
        return "redirect:../product/categoryList";
    }

    @RequestMapping("/addDimension")
    public String addDimension(Model model) {
        List<Dimension> dimensionList = dimensionService.findAll();
        model.addAttribute("dimensionList", dimensionList);
        return ("admin/addDimension");
    }

    @RequestMapping(value = "/addDimension", method = RequestMethod.POST)
    public String addDimensionPost(
            HttpServletRequest request,
            @ModelAttribute("dimensionList") Dimension dimension,
            Model model) throws Exception {
        model.addAttribute("dimensionDescription", dimension.getDescription());

        if (dimensionService.findByDescription(dimension.getDescription()) != null) {
            model.addAttribute("dimensionExists", true);
            return "/admin/addDimension";
        }

        this.dimensionService.save(dimension);
        // return ("admin/addCategory");
        return ("redirect:dimensionList");

    }


    @RequestMapping(value = "/addDimensionPop", method = RequestMethod.POST)
    public String addDimensionPopPost(
            HttpServletRequest request,
            @ModelAttribute("dimensionList") Dimension dimension,
            Model model) throws Exception {
        model.addAttribute("dimensionDescription", dimension.getDescription());

        if (dimensionService.findByDescription(dimension.getDescription()) != null) {
            model.addAttribute("dimensionExists", true);
            return "redirect:/product/add";
        }

        this.dimensionService.save(dimension);
        return "redirect:/product/add";

    }

    @ModelAttribute("dimension")
    public Dimension newDimension() {
        return new Dimension();
    }

    @ModelAttribute("dimensions")
    Collection<Dimension> findAllDimension() {
        Collection<Dimension> dimensions = (Collection<Dimension>) dimensionService.findAll();
        return dimensions;
    }

    @GetMapping(value = "/editDimension")
    public String editDimensionGet(@RequestParam("id") Long id,Model model) {
        Dimension dimension = dimensionService.findById(id);

        /*if (dimension != null) {*/
            model.addAttribute("dimension", dimension);

         List<Shape> shapes = shapeService.findAll();
        model.addAttribute("shapes",shapes);

            return "admin/updateDimension";
        /*} else {
            throw new IllegalArgumentException();
        }*/
    }

    @PostMapping("/editDimension")
    public String updateDimension(@ModelAttribute Dimension dimension, BindingResult result) {
        dimension.setShape(dimension.getShape());

         dimensionService.save(dimension);
        return "redirect:../product/dimensionList";
    }

    @GetMapping(value = "/deleteDimension/{dimension}")
    public String deleteDimension(@PathVariable("dimension") Long id, Model model) {

        Dimension dimension = dimensionService.findById(id);
        if (dimension.getProductList().size() == 0) {

            dimensionService.removeOne(id);

            model.addAttribute("deleteSuccess", true);

        } else {
            model.addAttribute("notSuccess", true);
        }
        model.addAttribute("dimensionList", dimensionService.findAll());

        return "redirect:/product/dimensionList";

    }


    @ModelAttribute("provider")
    public Provider newProvider() {
        return new Provider();
    }

    @ModelAttribute("providers")
    Collection<Provider> findAllProvider() {
        Collection<Provider> providers = (Collection<Provider>) providerService.findAll();
        return providers;
    }

    @RequestMapping("/productInfo")
    public String productInfo(@RequestParam("id") Long id, Model model) {
        Product product = productService.findOne(id);
        model.addAttribute("product", product);

        List<Product> productList = productService.findAll();
        productList.stream()
                .flatMap(products -> products.getImagesList().stream())
                .forEach(img -> img.setFullURL((fileToPath(storageService.load(img.getUrl_image())))));

        return "admin/productInfo";

    }


    @RequestMapping("/updateProduct")
    public String updateProductGet(@RequestParam("id") Long id, Model model) {
        Product product = productService.findOne(id);
        model.addAttribute("product", product);
        List<Dimension> dimensionList = dimensionService.findAll();
        model.addAttribute("dimensionList",dimensionList);
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

    @GetMapping(value = "/deleteImage/{image}")
    public String deleteImage(@PathVariable("image") Long id, Model model) {
        Image image = imageService.findById(id);
        imageService.removeOne(id);
        // TODO : remove the image from the folder
        return "redirect:../productInfo?id=" + image.getProduct().getId();
    }

    @RequestMapping("/orderedDetail")
    public String orderInfo(@RequestParam("id") Long id, Model model) {
        Ordered ordered = orderService.findOne(id);

        model.addAttribute("ordered", ordered);
        OrderedStatus status = ordered.getStatus();
        model.addAttribute("status", status);


        List<CartItem> cartItemList = cartItemService.findAll();
        model.addAttribute("carItemList", cartItemList);
        cartItemList.stream().map(CartItem::getProduct).filter(product -> !product.getImagesList().isEmpty())
                .map(product -> product.getImagesList().get(0))
                .forEach(img -> img.setFullURL((fileToPath(storageService.load(img.getUrl_image())))));

        return "/admin/orderedDetail";

    }

    @PostMapping("/updateOrderedStat")
    public String updateOrderedStat(Ordered ordered,Model model, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/orderedDetail";
        }
        Ordered ordered1 = orderService.findById(ordered.getId());
        ordered1.setStatus(ordered.getStatus());
        if(ordered.getStatus()== OrderedStatus.ANNULER){
            ordered1.getCartItemList().stream().forEach(product->product.getProduct().setInStockNumber(product.getQty()+product.getProduct().getInStockNumber()));

        }
        orderService.save(ordered1);
        if(ordered1.getStatus() == OrderedStatus.LIVRER){
            List<CartItem> cartItemList = cartItemService.findByOrdered(ordered1) ;
            cartItemList.clear();
            cartItemList.size();
            model.addAttribute("cartItemList",cartItemList);
        }
         return "redirect:/product/orderedList";

    }

    @RequestMapping("/userList")
    public String userList(Model model){
        List<User> userList = userService.findAll();
        model.addAttribute("userList",userList);

        userList.stream().forEach(user -> user.getAddressList().stream());

        return "admin/userList";
    }

    @GetMapping("/orderedList")
    public String viewAll(Model model) {
        Collection<Ordered> orderedList = orderService.findAll();
        model.addAttribute("orderedList", orderedList);

        return "/admin/orderedList";
    }


    @GetMapping(value = "/desactivate/{id_user}")
    public String desactivate(Model model, @PathVariable long id_user) {
        User user = userService.findById(id_user);

        user.setEnabled(0);
        userService.save(user);
        return "redirect:../userList";

    }
    @GetMapping(value = "/activate/{id_user}")
    public String ativate(Model model, @PathVariable long id_user) {
        User user = userService.findById(id_user);
        user.setEnabled(1);
        userService.save(user);
        return "redirect:../userList";

    }


}
