package com.ecom.ecom.service;

import com.ecom.ecom.exceptions.DataNotFoundException;
import com.ecom.ecom.model.CommonEntity;
import com.ecom.ecom.repository.CommonRepository;
import org.modelmapper.ModelMapper;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class CommonServiceAbstract<E extends CommonEntity, D, R extends CommonRepository<D, Long>> {
    private final ModelMapper mapper;
    private final Type typeDao;
    private final Type typeEntity;
    protected final R repository;

    protected CommonServiceAbstract(final ModelMapper modelMapper, final R repository) {
        this.mapper = modelMapper;
        this.typeEntity = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.typeDao = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        this.repository = repository;
    }

    public D convertToDAO(final E source) {
        return mapper.map(source, typeDao);
    }

    public E convertToEntity(final D source) {
        return mapper.map(source, typeEntity);
    }

    // Common actions
    public List<E> fetchAll() {
        return repository.findAll().stream().map(this::convertToEntity).toList();
    }

    public List<D> saveAll(final List<D> entities) {
        return (List<D>) repository.saveAll(entities);
    }

    public E getById(final Long id) {
        final Optional<D> byId = repository.findById(id);
        final Optional<E> e = byId.map(this::convertToEntity);
        if (e.isPresent()) {
            return e.get();
        }
        throw new DataNotFoundException(String.format("id %s inexistant", id));
    }

    public void deleteById(final Long id) {
        repository.deleteById(id);
    }

    public E save(final E entity) {
        final D dao = convertToDAO(entity);
        final D createdDao = repository.save(dao);
        return convertToEntity(createdDao);
    }

    public E updateById(final Long id, final E entity) {
        if (entity.getId() == null || Objects.equals(entity.getId(), id)) {
            final E byId = getById(id);

            entity.setId(byId.getId());

            return save(entity);
        }
        throw new DataNotFoundException("incoherence id");
    }
}

