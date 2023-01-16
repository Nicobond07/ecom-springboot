package com.ecom.ecom.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpMethod;

import java.util.List;

@Getter
@Setter
public class EndPointSecurity {

    private String path;
    private HttpMethod methode;
    private List<String> roles;
}
