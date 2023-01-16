package com.ecom.ecom.repository;
import com.ecom.ecom.model.dao.SalesmanDao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SalesmanRepository extends CommonRepository<SalesmanDao, Long>{
    @Query(value = "SELECT s.id FROM salesman s JOIN sale sa ON s.id = sa.id_client GROUP BY s.id ORDER BY COUNT(*) DESC LIMIT 1", nativeQuery = true)
    Long findTopSalesmanId();
}
