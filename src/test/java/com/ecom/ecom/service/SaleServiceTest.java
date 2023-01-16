package com.ecom.ecom.service;

import com.ecom.ecom.model.dao.SaleDao;
import com.ecom.ecom.repository.SaleRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SaleServiceTest {

    @Autowired
    private SaleService saleService;

    @Autowired
    private SaleRepository saleRepository;

    private List<SaleDao> listResults;

    @BeforeAll
    void setup() {
        listResults = persistDataTest();
    }

    private List<SaleDao> persistDataTest() {
        final SaleDao dao1 = new SaleDao();
        dao1.setId(1L);
        dao1.setPromo_code("ZKJBVJKD");
        dao1.setTotalPrice(Float.valueOf("240"));

        final SaleDao dao2 = new SaleDao();
        dao2.setId(2L);
        dao1.setPromo_code("dDDSS");
        dao1.setTotalPrice(Float.valueOf("140"));

        final List<SaleDao> expectedDao = new ArrayList<>();

        expectedDao.add(dao1);
        expectedDao.add(dao2);
        return saleService.saveAll(expectedDao);
    }

//    @Test
//    void fetchAll() {
//        final List<SaleDao> expectedDao = persistDataTest();
//
//        final List<Sale> salesResult = saleService.fetchAll();
//
//        assertEquals(2, salesResult.size());
//
//        final List<Sale> salesExpected = expectedDao.stream()
//                .map(saleService::convertToEntity)
//                .toList();
//        salesExpected.forEach(e -> assertEquals(e, salesResult.get(salesExpected.indexOf(e))));
//    }
}
