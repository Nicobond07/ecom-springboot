package com.ecom.ecom.controller;

import com.ecom.ecom.configuration.EcomSecurity;
import com.ecom.ecom.configuration.security.BasicConfiguration;
import com.ecom.ecom.exceptions.DataNotFoundException;
import com.ecom.ecom.model.CustomError;
import com.ecom.ecom.model.Product;
import com.ecom.ecom.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({BasicConfiguration.class, EcomSecurity.class})
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    private static final String URI = "/product";

    @Autowired
    private WebApplicationContext webApp;


    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApp)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetProducts() throws Exception {
        final List<Product> listExpected = Collections.emptyList();
        when(productService.fetchAll()).thenReturn(listExpected);

        final String jsonExpected = objectMapper.writeValueAsString(listExpected);

        mockMvc.perform(get(URI + "/list"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonExpected)).andReturn();

        verify(productService, Mockito.times(1)).fetchAll();
    }

//    @Test
//    @WithMockUser(roles = "USER")
//    void testGetProducts403() throws Exception {
//        final List<Product> listExpected = Collections.emptyList();
//        when(productService.fetchAll()).thenReturn(listExpected);
//
//        mockMvc.perform(get(URI + "/list"))
//                .andExpect(status().isForbidden())
//                .andReturn();
//    }

    @Nested
    @DisplayName("Tests for getProductById")
    class TestGetProductById {
        @Test
        @WithMockUser(roles = "USER")
        void testGetProductByIdSuccess() throws Exception {
            final Product expected = new Product();
            when(productService.getById(Mockito.anyLong())).thenReturn(expected);

            final String jsonExpected = objectMapper.writeValueAsString(expected);

            mockMvc.perform(get(URI + "/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(jsonExpected)).andReturn();
        }

        @Test
        @WithMockUser(roles = "USER")
        void testGetProductByIdError() throws Exception {
            when(productService.getById(Mockito.anyLong())).thenThrow(new RuntimeException("Error fetching product"));

            mockMvc.perform(get(URI + "/1"))
                    .andExpect(status().isInternalServerError())
                    .andReturn();
        }
    }

    @Nested
    @DisplayName("Tests for createProduct")
    class TestCreateProduct {
        @Test
        @WithMockUser(roles = "USER")
        void testCreateProductSuccess() throws Exception {
            final Product product = new Product();
            product.setName("Luge lego");
            product.setDescription("descr");
            product.setInitialPrice(120.36F);
            when(productService.save(Mockito.any(Product.class))).thenReturn(product);

            final String jsonRequest = objectMapper.writeValueAsString(product);
            final String jsonExpected = objectMapper.writeValueAsString(product);

            mockMvc.perform(post(URI + "/")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(jsonRequest)
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().json(jsonExpected))
                    .andReturn();

            verify(productService, Mockito.times(1)).save(Mockito.any(Product.class));
        }

        @Test
        @WithMockUser(roles = "USER")
        void testCreateProductError() throws Exception {

            mockMvc.perform(post(URI + "/"))
                    .andExpect(status().isInternalServerError())
                    .andReturn();
        }
    }

    @Nested
    @DisplayName("Tests for updateProduct")
    class TestUpdateProduct {
        static Stream<Arguments> updateParameters() {
            return Stream.of(
                    Arguments.of(false, status().isOk()),
                    Arguments.of(true, status().isNotFound())

            );
        }

        @ParameterizedTest
        @MethodSource("updateParameters")
        @WithMockUser(roles = "USER")
        void testUpdate(final boolean empty, final ResultMatcher expectedStatus) throws Exception {
            final Product product = new Product();
            product.setName("Luge lego");
            product.setDescription("descr");
            product.setInitialPrice(Float.valueOf("0"));


            final String jsonRequest = objectMapper.writeValueAsString(product);
            final String jsonExpected = objectMapper.writeValueAsString(product);


            ResultMatcher json = content().json(jsonExpected);
            if (empty) {
                final String messageExpected = "id 1 inexistant";
                when(productService.updateById(Mockito.anyLong(), Mockito.any(Product.class))).thenThrow(new DataNotFoundException(messageExpected));
                json = content().json(objectMapper.writeValueAsString(new CustomError(HttpStatus.NOT_FOUND.value(), messageExpected, "/product/1")));
            } else {
                when(productService.updateById(Mockito.anyLong(), Mockito.any(Product.class))).thenReturn(product);
            }


            mockMvc.perform(put(URI + "/1").contentType(MediaType.APPLICATION_JSON).with(csrf())
                            .accept(MediaType.APPLICATION_JSON).content(jsonRequest))
                    .andExpect(expectedStatus)
                    .andExpect(json)
                    .andReturn();
        }
    }

    @Nested
    @DisplayName("Tests for deleteProduct")
    class TestDeleteProduct {
        @Test
        @WithMockUser(roles = "ADMIN")
        void testDeleteProduct() throws Exception {
            when(productService.getById(1L)).thenReturn(new Product());

            mockMvc.perform(delete(URI + "/1"))
                    .andExpect(status().isOk()).andReturn();

            verify(productService, Mockito.times(1)).deleteById(1L);
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void testDeleteProductError() throws Exception {
            doThrow(new RuntimeException("Error deleting product"))
                    .when(productService).deleteById(1L);

            mockMvc.perform(delete(URI + "/1"))
                    .andExpect(status().isInternalServerError())
                    .andReturn();
        }
    }

    private static Stream<Arguments> badRequestParameters() {
        return Stream.of(
                Arguments.of(post(URI + "/")),
                Arguments.of(put(URI + "/1"))
        );
    }

    @ParameterizedTest
    @MethodSource("badRequestParameters")
    @WithMockUser(roles = "USER")
    void testBadRequest(final MockHttpServletRequestBuilder requestType) throws Exception {
        final Map<String, String> listExpectedError = new HashMap<>();
        listExpectedError.put("name", "Nom nécessaire");
        listExpectedError.put("description", "Description nécessaire");
        listExpectedError.put("initialPrice", "Prix initial nécessaire");
        final String jsonRequest = objectMapper.writeValueAsString(new Product());

        mockMvc.perform(requestType
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> {
                    final MethodArgumentNotValidException resolvedException = (MethodArgumentNotValidException) result.getResolvedException();
                    assertNotNull(resolvedException);
                    assertEquals(listExpectedError.size(), resolvedException.getFieldErrors().size(), "Nombre de validateurs");
                    final List<FieldError> fieldErrors = resolvedException.getFieldErrors().stream()
                            .filter(fieldError -> listExpectedError.containsKey(fieldError.getField()) && listExpectedError.get(fieldError.getField()).equalsIgnoreCase(fieldError.getDefaultMessage()))
                            .toList();
                    assertEquals(listExpectedError.size(), fieldErrors.size(), "Pas les bons validateurs");
                });
    }
}
