package com.example.galerie_artisanale.controller;

import com.example.galerie_artisanale.entity.*;
import com.example.galerie_artisanale.repository.ProductRepository;
import com.example.galerie_artisanale.service.*;
import com.example.galerie_artisanale.util.Container;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    private ShapeService shapeService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private StorageService storageService;



    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addProduct(Model model) {
        Product product = new Product();
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
        if(category.getProductList().size() == 0){

            categoryService.removeOne(id);

            model.addAttribute("deleteSuccess", true);

        }else {
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

        if(shape.getProductList().size() == 0){

            shapeService.removeOne(id);

            model.addAttribute("shapeList", shapeList);
            model.addAttribute("deleteSuccess", true);

        }else{
            model.addAttribute("shapeList", shapeList);
            model.addAttribute("notSuccess", true);
        }
        return "redirect:/product/shapeList";

        //return "admin/shapeList";

    }


    /*@RequestMapping(value = "/remove2", method = RequestMethod.POST)
    public String remove(@ModelAttribute Product product) {
        System.out.println("Removing :" + product.getId());
        productService.removeOne(product.getId());

        return "redirect:/product/productList";

    }

    @RequestMapping(value = "/removelist", method = RequestMethod.POST)
    public String remove(@ModelAttribute Container container) {
        System.out.println("Removing  List:");

        container.getProductList().stream().filter(Product::isSelected)
                .forEach(p -> {
                    System.out.println("Removing " + p.getId());
                    productService.removeOne(p.getId());
                });

        return "redirect:/product/productList";

    }*/

    @PostMapping(value = "/add")
    public String addProductPost(@ModelAttribute("product") Product product, Model model,
                                 @RequestParam("file") MultipartFile[] files) throws Exception{

        model.addAttribute("productName",product.getProductName());
        if(productService.findByProductName(product.getProductName()) != null){
            model.addAttribute("productExists",true);
            return "admin/addProduct";
        }
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

        /*List<Product> productList = productService.findAll();
        productList.stream()
                .flatMap(products -> products.getImagesList().stream())
                .forEach(img -> img.setFullURL((fileToPath(storageService.load(img.getUrl_image())))));
*/
        model.addAttribute("addSuccess", true);

        return "admin/addProduct";

        /*return "redirect:productList";*/
    }


    @RequestMapping("/productList")
    public String productList(Model model) {
        List<Product> productList = productService.findAll();
        Container container = new Container();
        container.setProductList(productList);
        model.addAttribute("productList", productList);
        model.addAttribute("container", container);
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

        this.shapeService.save(shape);
        return ("redirect:shapeList");
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

        if (categoryService.findByCategoryName(category.getCategoryName()) != null) {
            model.addAttribute("categoryExists", true);
            return "/admin/addCategory";
        }

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
    public String updateProduct(@RequestParam("id") Long id, Model model) {
        Product product = productService.findOne(id);
        model.addAttribute("product", product);
        product.getShape();
        product.getCategory();
        product.getImagesList()
                .stream().forEach(img -> img.setFullURL((fileToPath(storageService.load(img.getUrl_image())))));
        return "admin/updateProduct";
    }


    @RequestMapping(value = "/updateProduct", method = RequestMethod.POST)
    public String updateProductPost(@ModelAttribute("product") Product product,@RequestParam("file") MultipartFile[] files,Model model) {

        //Product productP = productService.save(product);

        product = this.productService.save(product);
        //imageService.save(product.getImagesList().stream().reduce());

        for (MultipartFile file : files) {
            Image image = new Image();
            image.setProduct(product);

            image = imageService.save(image);
            image.setUrl_image("products/product_" + product.getId() + "/" + image.getId() + ".jpeg");
            // image.setUrl_image(String.format("product_%s/%s.jpeg",product.getId(),image.getId()));
            image = imageService.save(image);
            storageService.store(file, image.getUrl_image());

        }

        model.addAttribute("updateSuccess", true);

        return "admin/updateProduct";

        /*return "redirect:/product/productInfo?id="+product.getId();*/
    }

}
