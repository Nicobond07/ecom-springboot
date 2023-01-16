package com.ecom.ecom.service;


import com.ecom.ecom.exceptions.DataNotFoundException;
import com.ecom.ecom.model.Sale;
import com.ecom.ecom.model.dao.SaleDao;
import com.ecom.ecom.repository.SaleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class SaleService extends CommonServiceAbstract<Sale, SaleDao, SaleRepository> {

    public SaleService(final ModelMapper modelMapper, final SaleRepository saleRepository) {
        super(modelMapper, saleRepository);
    }

    public List<Sale> getSalesBySalesmanId(final Long id) {
        final Optional<List<SaleDao>> optionalSales = repository.findSalesByIdClient(id);

        if (optionalSales.isPresent()) {
            return optionalSales.get().stream()
                    .map(this::convertToEntity).toList();
        }
        throw new DataNotFoundException(String.format("id %s inexistant", id));
    }

}
