package com.ecom.ecom.service;

import com.ecom.ecom.exceptions.DataNotFoundException;
import com.ecom.ecom.model.Salesman;
import com.ecom.ecom.model.dao.SalesmanDao;
import com.ecom.ecom.repository.SalesmanRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SalesmanService extends CommonServiceAbstract<Salesman, SalesmanDao, SalesmanRepository> {
    public SalesmanService(final ModelMapper modelMapper, final SalesmanRepository salesmanRepository) {
        super(modelMapper, salesmanRepository);
    }

    public Salesman getTopSalesman() {
        final Optional<SalesmanDao> topSalesmanDao = repository.findById(repository.findTopSalesmanId());
        final Optional<Salesman> topSalesman = topSalesmanDao.map(this::convertToEntity);
        if (topSalesman.isPresent()) {
            return topSalesman.get();
        }
        throw new DataNotFoundException("Il n'y a aucun top salesman");
    }

}
