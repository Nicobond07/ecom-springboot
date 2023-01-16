package com.ecom.ecom.controller;


import com.ecom.ecom.model.Sale;
import com.ecom.ecom.service.SaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Sale", description = "Gestion des ventes")
@RestController
@RequestMapping("/sale")
public class SaleController {
    private final SaleService saleService;

    public SaleController(final SaleService saleService) {
        this.saleService = saleService;
    }

    @Operation(summary = "Récupérer tous les produits")
    @GetMapping("/list")
    public List<Sale> getSales() {
        return saleService.fetchAll();
    }

    @Operation(summary = "Récupérer une vente par son id")
    @GetMapping("/{id}")
    public Sale getSale(@PathVariable("id") final Long id) {
        return saleService.getById(id);
    }

    @Operation(summary = "Récupérer toutes le ventes d'un salesman")
    @GetMapping("/salesman-sales/{id}")
    public List<Sale> getSalesBySalesmanId(@PathVariable("id") final Long id) {
        return saleService.getSalesBySalesmanId(id);
    }

    @Operation(summary = "Créer une nouvelle vente")
    @PostMapping("/")
    public Sale createSale(@Valid @RequestBody final Sale sale) {
        return saleService.save(sale);
    }

    @Operation(summary = "Supprimer une vente")
    @DeleteMapping(value = "/{id}")
    public void deleteSale(@PathVariable("id") final Long id) {
        saleService.deleteById(id);
    }
}
