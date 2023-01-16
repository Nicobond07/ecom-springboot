package com.ecom.ecom.controller;

import com.ecom.ecom.model.Product;
import com.ecom.ecom.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Product", description = "Gestion des produits")
@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(final ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Récupérer tous les produits")

    // Différentes annotations : GetMapping, PostMapping, PatchMapping, PutMapping, DeleteMapping
    @GetMapping("/list")
    public List<Product> getProducts(final Authentication user) {
        return productService.fetchAll();
    }

    @Operation(summary = "Récupérer un produit par son id")
    @GetMapping("/{id}")
    public Product getProduct(@PathVariable("id") final Long id) {
        return productService.getById(id);
    }

    @Operation(summary = "Créer un nouveau produit")
    @PostMapping("/")
    public Product createProduct(@Valid @RequestBody final Product product) {
        return productService.save(product);
    }

    @Operation(summary = "Modifier un produit existant par son id")
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable("id") final Long id, @Valid @RequestBody final Product product) {
        return productService.updateById(id, product);
    }

    @Operation(summary = "Supprimer un produit")
    @DeleteMapping(value = "/{id}")
    public void deleteProduct(@PathVariable("id") final Long id) {
        productService.deleteById(id);
    }

}
