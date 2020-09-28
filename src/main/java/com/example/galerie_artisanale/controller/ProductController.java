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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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



    @RequestMapping(value="/add", method = RequestMethod.GET)
    public String addProduct(Model model){
        Product product = new Product();
        model.addAttribute("product",product);
        return "admin/addProduct";
    }

    @RequestMapping(value = "/remove2", method = RequestMethod.POST)
    public String remove(@ModelAttribute Product product){
        System.out.println("Removing :"+product.getId());
         productService.removeOne(product.getId());

        return "redirect:/product/productList";

    }



    @RequestMapping(value = "/removelist", method = RequestMethod.POST)
    public String remove(@ModelAttribute Container container){
        System.out.println("Removing  List:");


        container.getProductList().stream().filter(Product::isSelected)
                .forEach(p -> {System.out.println("Removing " + p.getId());
                     productService.removeOne(p.getId());
                });

        return "redirect:/product/productList";

    }

    @PostMapping(value="/add")
    public String addProductPost(@ModelAttribute Product product,Model model,
                                 @RequestParam("file") MultipartFile[] files){

        product = this.productService.save(product);
        for(MultipartFile file : files ) {
            Image image = new Image();
            image.setProduct(product);

            image = imageService.save(image);
            image.setUrl_image("products/product_" + product.getId() + "/" + image.getId() + ".jpeg");
            // image.setUrl_image(String.format("product_%s/%s.jpeg",product.getId(),image.getId()));
            image = imageService.save(image);
            storageService.store(file, image.getUrl_image());

        }
        model.addAttribute("addSuccess", true);
        model.addAttribute("classActiveAdd", true);

        return "admin/addProduct";

/*
        return "redirect:productList";
*/

    }


    @RequestMapping("/productList")
    public String productList(Model model){
        List<Product> productList = productService.findAll();
        Container container = new Container() ;
        container.setProductList(productList);
        model.addAttribute("productList",productList);
        model.addAttribute("container",container);
        return "admin/productList";
    }

    @RequestMapping("/categoryList")
    public String categoryList(Model model){
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryList",categoryList);
        return "admin/categoryList";
    }

    @RequestMapping("/shapeList")
    public String shapeList(Model model){
        List<Shape> shapeList = shapeService.findAll();
        model.addAttribute("shapeList",shapeList);
        return "admin/shapeList";
    }


    @RequestMapping("/addShape")
    public String addShape(Model model){
        List<Shape> shapeList = shapeService.findAll();
        model.addAttribute("shapeList",shapeList);
        return ("admin/addShape");
    }



     @RequestMapping(value = "/addShape", method = RequestMethod.POST)
     public String  addShapePost(HttpServletRequest request,
             @ModelAttribute("Shape") Shape shape,
             Model model)throws Exception{
         model.addAttribute("shapeName",shape.getShapeName());

         if(shapeService.findByShapeName(shape.getShapeName()) != null){
             model.addAttribute("shapeExists",true);
             return "/admin/addShape";
         }

         this.shapeService.save(shape);
         return ("redirect:shapeList") ;
     }



    @ModelAttribute("shape")
    public Shape newShape(){
        return new Shape();
    }

    @ModelAttribute("shapes")
    Collection<Shape>  findAllShape(){
        Collection<Shape> shapes = (Collection<Shape>)shapeService.findAll();
        return shapes;
    }

    @RequestMapping("/addCategory")
    public String addCategory(Model model){
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryList",categoryList);
        return ("admin/addCategory");
    }

    @RequestMapping(value="/addCategory", method = RequestMethod.POST)
    public String addCategoryPost(
            HttpServletRequest request,
            @ModelAttribute("category")Category category,
            Model model)throws Exception{
        model.addAttribute("categoryName",category.getCategoryName());

        if(categoryService.findByCategoryName(category.getCategoryName()) != null ){
            model.addAttribute("categoryExists",true);
            return "/admin/addCategory";
        }

        this.categoryService.save(category);
       // return ("admin/addCategory");
        return ("redirect:categoryList");

    }

    @ModelAttribute("category")
    public Category newCategory(){
        return new Category();
    }

    @ModelAttribute("categories")
    Collection<Category> findAllCategory(){
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

        return "admin/updateProduct";
    }

    @RequestMapping(value="/updateProduct", method=RequestMethod.POST)
    public String updateProductPost(@ModelAttribute("product")Product product, HttpServletRequest request) {

        productService.save(product);

        List<Product> productList = productService.findAll();
      /* Container container = new Container() ;
        container.getProductList();*/
        productList.stream()
                .flatMap(products -> products.getImagesList().stream())
                .forEach(img -> img.setFullURL((fileToPath(storageService.load(img.getUrl_image())))));

        return "redirect:/product/productInfo?id="+product.getId();

    }
/*

    @RequestMapping(value="/removeC", method=RequestMethod.POST)
    public String removeC(
            @ModelAttribute("id") String id, Model model
    ) {
        categoryService.removeOne(Long.parseLong(id.substring(8)));
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryList", categoryList);

        return "redirect:/category/categoryList";
    }

*/

/*
    @ModelAttribute("image")
    public Image newImage(){
        return new  Image();
    }

    @ModelAttribute("image")
    Collection<Image> findAllImage(){
        Collection<Image> images = (Collection<Image>) imageService.findAll();
        return images;
    }

    @ModelAttribute("image")
    public String ImageList(Model model){
        List<Image> imageList = imageService.findAll();
        model.addAttribute("imageList",imageList);
        return "admin/imageList";
    }
*/
    /*
    @GetMapping(value = "/edit/{productId}")
    public String edit(Model model, @PathVariable Long productId) {
        Optional<Product> product = productService.findById(productId);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            model.addAttribute("productList", productService.findByRemovedFalse());

            return "admin/addProduct";
        } else {
            throw new IllegalArgumentException();
        }
    }

    @GetMapping(value = "/delete/{productId}")
    public String delete(Model model, @PathVariable Long productId) {
        final Optional<Product> product = productService.findById(productId);
        if (product.isPresent()) {
            product.get().setRemoved(true);
            productService.save(product.get());
            return "admin/productList";
        } else {
            throw new IllegalArgumentException();
        }
    }

    @GetMapping(value = "/restore/{productId}")
    public String restore(Model model, @PathVariable Long productId) {
        final Optional<Product> product = productService.findById(productId);
        if (product.isPresent()) {
            product.get().setRemoved(false);
            productService.save(product.get());
            return "admin/productList";
        } else {
            throw new IllegalArgumentException();
        }
    }
*/



  /* @RequestMapping(value="/removeS", method=RequestMethod.POST)
    public String removeS(
            @ModelAttribute("id") String id, Model model
    ) {
        shapeService.removeOne(Long.parseLong(id));
        List<Shape> shapeList = shapeService.findAll();
        model.addAttribute("shapeList", shapeList);

        return "redirect:product/shapeList";

   }*/

    @RequestMapping(value = "/removeS", method = RequestMethod.POST)
    public String removeS(@ModelAttribute("id")String id,  Model model){
/*
        System.out.println("Removing :"+shape.getId());
*/
        shapeService.removeOne(Long.parseLong(id.substring(1)));
        //shapeService.removeOne(shape.getId());
        List<Shape> shapeList = shapeService.findAll();
        model.addAttribute("shapeList", shapeList);

        return "redirect:/product/shapeList";

    }

}
