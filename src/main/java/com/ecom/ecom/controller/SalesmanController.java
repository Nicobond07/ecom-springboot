package com.ecom.ecom.controller;

import com.ecom.ecom.model.Salesman;
import com.ecom.ecom.service.SalesmanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "SalesMan", description = "Gestion des vendeurs")
@RestController
@RequestMapping("/salesman")
public class SalesmanController {

    private final SalesmanService salesmanService;

    public SalesmanController(final SalesmanService salesmanService) {
        this.salesmanService = salesmanService;
    }

    @Operation(summary = "Récupérer tous les vendeurs")
    @GetMapping("/list")
    public List<Salesman> getSalesmen() {
        return salesmanService.fetchAll();
    }

    @Operation(summary = "Récupérer un vendeur par son id")
    @GetMapping("/{id}")
    public Salesman getSalesman(@PathVariable("id") final Long id) {
        return salesmanService.getById(id);
    }

    @Operation(summary = "Récupérer le vendeur qui a le plus de ventes")
    @GetMapping("/top")
    public Salesman getTopSalesman() {
        return salesmanService.getTopSalesman();
    }

    @Operation(summary = "Créer un nouveau vendeur")
    @PostMapping("/")
    public Salesman createSalesman(@Valid @RequestBody final Salesman salesman) {
        return salesmanService.save(salesman);
    }

    @Operation(summary = "Modifier un vendeur existant par son id")
    @PutMapping("/{id}")
    public Salesman updateSalesman(@PathVariable("id") final Long id, @Valid @RequestBody final Salesman salesman) {
        return salesmanService.updateById(id, salesman);
    }

    @Operation(summary = "Supprimer un vendeur")
    @DeleteMapping(value = "/{id}")
    public void deleteSalesman(@PathVariable("id") final Long id) {
        salesmanService.deleteById(id);
    }
}
