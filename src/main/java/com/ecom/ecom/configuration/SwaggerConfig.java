package com.ecom.ecom.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.customizers.OpenApiLocaleCustomizer;

import org.springframework.context.annotation.Configuration;


import java.util.Locale;

@Configuration

public class SwaggerConfig implements OpenApiLocaleCustomizer {

    @Override
    public void customise(OpenAPI openApi, Locale locale) {

    }


}

