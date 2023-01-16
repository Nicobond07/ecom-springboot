package com.ecom.ecom.controller;

import com.ecom.ecom.configuration.EcomSecurity;
import com.ecom.ecom.configuration.security.BasicConfiguration;
import com.ecom.ecom.model.Sale;
import com.ecom.ecom.service.SaleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SaleController.class)
@Import({BasicConfiguration.class, EcomSecurity.class})
public class SaleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SaleService saleService;

    private static final String URI = "/sale";

    @Test
    @WithMockUser(roles = "USER")
    void testGetSales() throws Exception {
        final List<Sale> listExpected = Collections.emptyList();
        when(saleService.fetchAll()).thenReturn(listExpected);
        final String jsonExpected = objectMapper.writeValueAsString(listExpected);
        mockMvc.perform(get(URI + "/list"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonExpected))
                .andReturn();

        verify(saleService, Mockito.times(1)).fetchAll();
    }


    @Nested
    @DisplayName("Tests for getSaleById")
    class TestGetSalesmanById {
        @Test
        @WithMockUser(roles = "USER")
        void testGetSaleByIdSuccess() throws Exception {
            final Sale expected = new Sale();
            when(saleService.getById(Mockito.anyLong())).thenReturn(expected);
            final String jsonExpected = objectMapper.writeValueAsString(expected);
            mockMvc.perform(get(URI + "/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(jsonExpected))
                    .andReturn();
        }

        @Test
        @WithMockUser(roles = "USER")
        void testGetSaleByIdError() throws Exception {
            when(saleService.getById(Mockito.anyLong())).thenThrow(new RuntimeException("Error fetching sale"));

            mockMvc.perform(get(URI + "/1"))
                    .andExpect(status().isInternalServerError())
                    .andReturn();
        }
    }

    @Nested
    @DisplayName("Tests for getSalesBySalesmanId")
    class TestGetSalesBySalesmanId {
        @Test
        @WithMockUser(roles = "USER")
        void testGetSalesBySalesmanIdSuccess() throws Exception {
            final List<Sale> expectedList = Collections.emptyList();
            when(saleService.getSalesBySalesmanId(Mockito.anyLong())).thenReturn(expectedList);
            final String jsonExpected = objectMapper.writeValueAsString(expectedList);
            mockMvc.perform(get(URI + "/salesman-sales/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(jsonExpected))
                    .andReturn();
        }

        @Test
        void testGetSalesBySalesmanIdError() throws Exception {
            when(saleService.getSalesBySalesmanId(Mockito.anyLong())).thenThrow(new RuntimeException("Error fetching sale"));

            mockMvc.perform(get(URI + "/salesman-sales/1"))
                    .andExpect(status().isInternalServerError())
                    .andReturn();
        }
    }

    @Nested
    @DisplayName("Tests for createSale")
    class TestCreateSalesman {
        @Test
        @WithMockUser(roles = "USER")
        void testCreateSale() throws Exception {
            final Sale sale = new Sale();
            sale.setPromo_code("JKBVJS");
            sale.setTotalPrice(Double.valueOf("125.0"));
            when(saleService.save(Mockito.any(Sale.class))).thenReturn(sale);

            final String jsonRequest = objectMapper.writeValueAsString(sale);
            final String jsonExpected = objectMapper.writeValueAsString(sale);

            mockMvc.perform(post(URI + "/")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(jsonRequest)
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().json(jsonExpected))
                    .andReturn();

            verify(saleService, Mockito.times(1)).save(Mockito.any(Sale.class));
        }

        @Test
        @WithMockUser(roles = "USER")
        void testCreateSaleError() throws Exception {

            mockMvc.perform(post(URI + "/"))
                    .andExpect(status().isInternalServerError())
                    .andReturn();
        }
    }

    @Nested
    @DisplayName("Tests for deleteSale")
    class TestDeleteSalesman {
        @Test
        @WithMockUser(roles = "ADMIN")
        void testDeleteSale() throws Exception {
            when(saleService.getById(1L)).thenReturn(new Sale());

            mockMvc.perform(delete(URI + "/1"))
                    .andExpect(status().isOk()).andReturn();

            verify(saleService, Mockito.times(1)).deleteById(1L);
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void testDeleteSaleError() throws Exception {
            doThrow(new RuntimeException("Error deleting sale"))
                    .when(saleService).deleteById(1L);

            mockMvc.perform(delete(URI + "/1"))
                    .andExpect(status().isInternalServerError())
                    .andReturn();
        }
    }


    private static Stream<Arguments> badRequestParameters() {
        return Stream.of(
                Arguments.of(post(URI + "/"))
        );
    }

    @ParameterizedTest
    @MethodSource("badRequestParameters")
    @WithMockUser(roles = "USER")
    void testBadRequest(final MockHttpServletRequestBuilder requestType) throws Exception {
        final Map<String, String> listExpectedError = new HashMap<>();
        listExpectedError.put("promo_code", "Promo code nécessaire");
        listExpectedError.put("totalPrice", "Prix total nécessaire");
        final String jsonRequest = objectMapper.writeValueAsString(new Sale());

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
