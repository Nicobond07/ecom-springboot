package com.ecom.ecom.mapper;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;

public class Mapper {

    private ModelMapper modelMapper;

    public Mapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Bean
    public static <T, S> T convert(S source, Class<T> destination){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(source, destination);
    }
}
