package com.example.galerie_artisanale.service.impl;


import com.example.galerie_artisanale.entity.Product;
import com.example.galerie_artisanale.repository.ProductRepository;
import com.example.galerie_artisanale.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

/*
    @Override
    public List<Product> findByRemovedFalse() {
        return productRepository.findByRemovedFalse();
    }*/


    public void removeOne(Long id) {
        productRepository.deleteById(id);
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



    public List<Product> findByCategory(String category){

        List<Product> productList = productRepository.findByCategory(category);

        List<Product> activeProductList = new ArrayList<>();

        for (Product product: productList) {
            if(product.isActive()) {
                activeProductList.add(product);
            }
        }

        return activeProductList;
    }

}
