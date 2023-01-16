package com.ecom.ecom.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class CommonEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -3490253771575845545L;
    
    private Long id;
}
