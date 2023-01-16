package com.ecom.ecom.controller;

import com.ecom.ecom.configuration.EcomSecurity;
import com.ecom.ecom.configuration.security.BasicConfiguration;
import com.ecom.ecom.exceptions.DataNotFoundException;
import com.ecom.ecom.model.CustomError;
import com.ecom.ecom.model.Salesman;
import com.ecom.ecom.service.SalesmanService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
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

@WebMvcTest(controllers = SalesmanController.class)
@Import({BasicConfiguration.class, EcomSecurity.class})
public class SalesmanControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SalesmanService salesmanService;

    private static final String URI = "/salesman";


    @Nested
    @DisplayName("Tests for getSalesmen")
    class TestGetSalesmen {
        @Test
        @WithMockUser(roles = "USER")
        void testGetSalesmenSuccess() throws Exception {
            final List<Salesman> listExpected = Collections.emptyList();
            when(salesmanService.fetchAll()).thenReturn(listExpected);

            final String jsonExpected = objectMapper.writeValueAsString(listExpected);

            mockMvc.perform(get(URI + "/list"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(jsonExpected))
                    .andReturn();

            verify(salesmanService, Mockito.times(1)).fetchAll();
        }
    }

    @Nested
    @DisplayName("Tests for getSalesmanById")
    class TestGetSalesmanById {
        @Test
        @WithMockUser(roles = "USER")
        void testGetSalesmanByIdSuccess() throws Exception {
            final Salesman expected = new Salesman();
            when(salesmanService.getById(Mockito.anyLong())).thenReturn(expected);

            final String jsonExpected = objectMapper.writeValueAsString(expected);

            mockMvc.perform(get(URI + "/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(jsonExpected)).andReturn();
        }

        @Test
        void testGetSalesmanByIdError() throws Exception {
            when(salesmanService.getById(Mockito.anyLong())).thenThrow(new RuntimeException("Error fetching salesman"));

            mockMvc.perform(get(URI + "/1"))
                    .andExpect(status().isInternalServerError())
                    .andReturn();
        }
    }

    @Nested
    @DisplayName("Tests for getTopSalesman")
    class TestGetTopSalesman {
        @Test
        @WithMockUser(roles = "USER")
        void testGetTopSalesmanSuccess() throws Exception {
            final Salesman expected = new Salesman();
            when(salesmanService.getTopSalesman()).thenReturn(expected);

            final String jsonExpected = objectMapper.writeValueAsString(expected);

            mockMvc.perform(get(URI + "/top"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(jsonExpected)).andReturn();
        }

        @Test
        @WithMockUser(roles = "USER")
        void testGetTopSalesmanError() throws Exception {
            when(salesmanService.getTopSalesman()).thenThrow(new RuntimeException("Error fetching top salesman"));

            mockMvc.perform(get(URI + "/top"))
                    .andExpect(status().isInternalServerError())
                    .andReturn();
        }
    }

    @Nested
    @DisplayName("Tests for createSalesman")
    class TestCreateSalesman {
        @Test
        @WithMockUser(roles = "USER")
        void testCreateSalesmanSuccess() throws Exception {
            final Salesman salesman = new Salesman();
            salesman.setPseudo("Patrick");
            salesman.setEmail("testeur@gmail.com");
            salesman.setPassword("zlvhskjb");
            salesman.setPromoCode("DKJBVJSD");
            when(salesmanService.save(Mockito.any(Salesman.class))).thenReturn(salesman);

            final String jsonRequest = objectMapper.writeValueAsString(salesman);
            final String jsonExpected = objectMapper.writeValueAsString(salesman);

            mockMvc.perform(post(URI + "/")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(jsonRequest)
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().json(jsonExpected))
                    .andReturn();

            verify(salesmanService, Mockito.times(1)).save(Mockito.any(Salesman.class));
        }

        @Test
        @WithMockUser(roles = "USER")
        void testCreateSalesmanError() throws Exception {

            mockMvc.perform(post(URI + "/"))
                    .andExpect(status().isInternalServerError())
                    .andReturn();

        }
    }

    @Nested
    @DisplayName("Tests for updateSalesman")
    class TestUpdateSalesman {
        private static Stream<Arguments> updateParameters() {
            return Stream.of(
                    Arguments.of(false, status().isOk()),
                    Arguments.of(true, status().isNotFound())
            );
        }

        @ParameterizedTest
        @MethodSource("updateParameters")
        @WithMockUser(roles = "USER")
        void testUpdate(final boolean empty, final ResultMatcher expectedStatus) throws Exception {
            final Salesman salesman = new Salesman();
            salesman.setPseudo("Tututu");
            salesman.setEmail("testeur@gmail.com");
            salesman.setPassword("zlvhskjb");
            salesman.setPromoCode("DKJBVJSD");

            final String jsonRequest = objectMapper.writeValueAsString(salesman);
            final String jsonExpected = objectMapper.writeValueAsString(salesman);


            ResultMatcher json = content().json(jsonExpected);
            when(salesmanService.updateById(Mockito.anyLong(), Mockito.any(Salesman.class))).thenReturn(salesman);
            if (empty) {
                final String messageExpected = "id 1 inexistant";
                when(salesmanService.updateById(Mockito.anyLong(), Mockito.any(Salesman.class))).thenThrow(new DataNotFoundException(messageExpected));
                json = content().json(objectMapper.writeValueAsString(new CustomError(HttpStatus.NOT_FOUND.value(), messageExpected, "/salesman/1")));
            } else {
                when(salesmanService.updateById(Mockito.anyLong(), Mockito.any(Salesman.class))).thenReturn(salesman);
            }


            mockMvc.perform(put(URI + "/1").contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON).content(jsonRequest))
                    .andExpect(expectedStatus)
                    .andExpect(json)
                    .andReturn();
        }


        @Test
        @WithMockUser(roles = "USER")
        void testUpdateSalesmanError() throws Exception {
            final Salesman salesman = new Salesman();
            final String jsonRequest = objectMapper.writeValueAsString(salesman);
            mockMvc.perform(put(URI + "/1").contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON).content(jsonRequest))
                    .andExpect(status().isBadRequest())
                    .andReturn();
        }
    }

    @Nested
    @DisplayName("Tests for deleteSalesman")
    class TestDeleteSalesman {
        @Test
        @WithMockUser(roles = "ADMIN")
        void testDeleteSalesmanSuccess() throws Exception {
            when(salesmanService.getById(1L)).thenReturn(new Salesman());

            mockMvc.perform(delete(URI + "/1"))
                    .andExpect(status().isOk()).andReturn();

            verify(salesmanService, Mockito.times(1)).deleteById(1L);
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void testDeleteSalesmanError() throws Exception {
            doThrow(new RuntimeException("Error deleting salesman"))
                    .when(salesmanService).deleteById(1L);

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
    void testBadRequest(final MockHttpServletRequestBuilder requestType) throws Exception {
        final Map<String, String> listExpectedError = new HashMap<>();
        listExpectedError.put("pseudo", "Pseudo nécessaire");
        listExpectedError.put("promoCode", "PromoCode nécessaire");
        listExpectedError.put("email", "Email nécessaire");
        listExpectedError.put("password", "Password nécessaire");
        final String jsonRequest = objectMapper.writeValueAsString(new Salesman());

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
