package com.ecom.ecom.service;

import com.ecom.ecom.model.Product;
import com.ecom.ecom.model.Sale;
import com.ecom.ecom.model.Salesman;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ServiceNullOrEmptyTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private SaleService saleService;

    @Autowired
    private SalesmanService salesmanService;


    @Test
    void product_fetchAll_Empty() {
        final List<Product> productsResult = productService.fetchAll();
        Assertions.assertEquals(0, productsResult.size());
    }

    @Test
    void sale_fetchAll_Empty() {
        final List<Sale> salesResult = saleService.fetchAll();
        Assertions.assertEquals(0, salesResult.size());
    }

    @Test
    void salesman_fetchAll_Empty() {
        final List<Salesman> salesmanResult = salesmanService.fetchAll();
        Assertions.assertEquals(0, salesmanResult.size());
    }

    @Test
    void convertToDao_Empty() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> productService.convertToDAO(null));
    }

    @Test
    void convertToEntity_Empty() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> productService.convertToEntity(null));
    }
}
