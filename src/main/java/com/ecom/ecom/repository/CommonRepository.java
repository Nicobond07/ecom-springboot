package com.ecom.ecom.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;


@NoRepositoryBean
public interface CommonRepository<D, L> extends CrudRepository<D, L> {
    List<D> findAll();

}
