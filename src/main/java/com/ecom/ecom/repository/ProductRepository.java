package com.ecom.ecom.repository;

import com.ecom.ecom.model.dao.ProductDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CommonRepository<ProductDao, Long> {

}
