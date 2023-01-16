package com.ecom.ecom.service;

import com.ecom.ecom.model.Product;
import com.ecom.ecom.model.dao.ProductDao;
import com.ecom.ecom.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ProductService extends CommonServiceAbstract<Product, ProductDao, ProductRepository> {

    public ProductService(final ModelMapper modelMapper, final ProductRepository productRepository) {
        super(modelMapper, productRepository);
    }

}
