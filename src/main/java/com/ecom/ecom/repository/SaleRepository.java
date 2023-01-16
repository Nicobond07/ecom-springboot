package com.ecom.ecom.repository;

import com.ecom.ecom.model.dao.SaleDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SaleRepository extends CommonRepository<SaleDao, Long> {
    Optional<List<SaleDao>> findSalesByIdClient(float idClient);
}
