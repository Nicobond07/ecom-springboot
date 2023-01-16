package com.ecom.ecom.service;

import com.ecom.ecom.exceptions.DataNotFoundException;
import com.ecom.ecom.model.Product;
import com.ecom.ecom.model.dao.ProductDao;
import com.ecom.ecom.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    private List<ProductDao> listResults;

    @BeforeAll
    void setup() {
        listResults = persistDataTest();
    }

    private List<ProductDao> persistDataTest() {
        final ProductDao dao1 = new ProductDao();
        dao1.setId(1L);
        dao1.setName("Le produit");
        dao1.setDescription("C'est une description");
        dao1.setInitialPrice(Float.parseFloat("122"));

        final ProductDao dao2 = new ProductDao();
        dao2.setId(2L);
        dao2.setName("autre");
        dao2.setDescription("description");
        dao2.setInitialPrice(Float.parseFloat("12"));

        final List<ProductDao> expectedDao = new ArrayList<>();

        expectedDao.add(dao1);
        expectedDao.add(dao2);
        return productService.saveAll(expectedDao);

    }

    @Test
    void convertDao() {
        final Product entity = new Product();
        final ProductDao productDao = productService.convertToDAO(entity);
        Assertions.assertInstanceOf(ProductDao.class, productDao);
    }

    @Test
    void convertToEntity() {
        final ProductDao dao = new ProductDao();
        final Product entity = productService.convertToEntity(dao);
        Assertions.assertInstanceOf(Product.class, entity);
    }

    @Test
    void fetchAll() {
        final List<ProductDao> expectedDao = persistDataTest();

        final List<Product> productsResult = productService.fetchAll();

        assertEquals(2, productsResult.size());

        final List<Product> productsExpected = expectedDao.stream()
                .map(productService::convertToEntity)
                .toList();
        productsExpected.forEach(e -> assertEquals(e, productsResult.get(productsExpected.indexOf(e))));
    }


    @Test
    void getById() {
        final Product product = productService.getById(1L);

        final Product expectedEntity = productService.convertToEntity(listResults.get(0));

        assertEquals(expectedEntity.hashCode(), product.hashCode());
    }

    @Test
    void getById_NotFound() {
        assertThrows(DataNotFoundException.class, () -> productService.getById(5L));
    }


    @Test
    public void testSave_badRequest() {
        final ProductDao dao = new ProductDao();

        final ConstraintViolationException exception = Assertions.assertThrows(ConstraintViolationException.class, () -> productRepository.save(dao));
        Assertions.assertTrue(exception.getMessage().contains("ConstraintViolationImpl"));
    }

    @Test
    //TODO revoir le jeux de test
    public void testUpdateById() {
        final ProductDao daoInsert = new ProductDao();
        daoInsert.setName("autre3");
        daoInsert.setDescription("description");
        daoInsert.setInitialPrice(Float.parseFloat("12"));
        final ProductDao save = productRepository.save(daoInsert);

        final Long idtest = save.getId();
        final Product entityExpected = productService.getById(idtest);
        final String descriptionExpected = "change update";
        final String nameExpected = "name changed";
        entityExpected.setDescription(descriptionExpected);
        entityExpected.setName(nameExpected);
        final Product updated = productService.updateById(idtest, entityExpected);

        assertEquals(nameExpected, updated.getName());
        assertEquals(descriptionExpected, updated.getDescription());

        final ProductDao foundProduct = productRepository.findById(idtest).get();
        assertEquals(nameExpected, foundProduct.getName());
        assertEquals(descriptionExpected, foundProduct.getDescription());

        productService.deleteById(idtest);
    }

    @Test
    void deleteById() {
        final ProductDao dao = new ProductDao();

        dao.setName("autre3");
        dao.setDescription("description");
        dao.setInitialPrice(Float.parseFloat("12"));


        final ProductDao save = productRepository.save(dao);
        productService.deleteById(save.getId());

        assertThrows(DataNotFoundException.class, () -> productService.getById(3L));
    }

    @Test
    void deleteById_NotFound() {
        assertThrows(EmptyResultDataAccessException.class, () -> productService.deleteById(40L));
    }

    @Test
    void testHashCode() {
        final Product p1 = new Product();
        p1.setId(1L);
        p1.setName("identique");
        p1.setDescription("identique");
        p1.setInitialPrice(Float.valueOf("50"));
        final Product p2 = new Product();
        p2.setId(1L);
        p2.setName("identique");
        p2.setDescription("identique");
        p2.setInitialPrice(Float.valueOf("50"));
        final Product p3 = new Product();
        p3.setId(2L);
        p3.setName("different");
        p3.setDescription("different");
        p3.setInitialPrice(Float.valueOf("40"));

        assertEquals(p1.hashCode(), p2.hashCode());
        assertNotEquals(p2.hashCode(), p3.hashCode());
    }

}
