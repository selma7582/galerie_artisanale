package com.example.galerie_artisanale.service.impl;


import com.example.galerie_artisanale.entity.Product;
import com.example.galerie_artisanale.repository.ProductRepository;
import com.example.galerie_artisanale.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImp implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> findAll() {
        return (List<Product>) productRepository.findAll();
    }

    @Override
    public Product findById(Long id) {
        Optional<Product> byId = productRepository.findById(id);
        return byId.orElse(null);
    }

    public void removeOne(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Product findByProductName(String productName) {
        return productRepository.findByProductName(productName);
    }

    public Product findOne(Long id) {
        // Product one = productRepository.getOne(id);
        Optional<Product> byId = productRepository.findById(id);
//            if (byId.isPresent()){
//                return byId.get() ;
//            }else{
//                return null ;
//            }
        return byId.orElse(null);//.findOne(id);
    }

    @Override
    public List<Product> findByCategory(String category) {

        List<Product> productList = productRepository.findByCategoryCategoryName(category);

        return productList;
    }

    @Override
    public Page<Product> findByCategory(String category, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo-1,pageSize);
        return productRepository.findByCategoryCategoryName(category,pageable);
    }

    @Override
    public List<Product> findByDimension(String dimension) {
        return productRepository.findByDimensionDescription(dimension);
    }

    @Override
    public Page<Product> findByDimension(String dimension, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo-1,pageSize);

        return productRepository.findByDimensionDescription(dimension,pageable);
    }

    @Override
    public List<Product> blurrySearch(String productName) {
        List<Product> productList = productRepository.findByProductNameContaining(productName);
        return productList;
    }

    @Override
    public List<Product> findByShape(String shape) {
        return productRepository.findByShapeShapeName(shape);
    }

    @Override
    public Page<Product> findByShape(String shape, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo-1,pageSize);

        return productRepository.findByShapeShapeName(shape, pageable);
    }

    @Override
    public List<Product> filterProducts(int min, int max) {
        return productRepository.findAll()
                .stream()
                .filter(product -> product.getPrice() >= min && product.getPrice() <= max)
                .collect(Collectors.toList());
    }

    @Override
    public Page<Product> chercher(float priceMin, float priceMax, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo-1,pageSize);

        return productRepository.chercher(priceMin,priceMax,pageable);
    }


    @Override
    public Page<Product> findPagination(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo-1,pageSize);
        return this.productRepository.findAll(pageable);
    }


}
