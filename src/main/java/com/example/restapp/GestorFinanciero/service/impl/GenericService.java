package com.example.restapp.GestorFinanciero.service.impl;


import com.example.restapp.GestorFinanciero.repo.IGenericRepo;
import com.example.restapp.GestorFinanciero.exception.ModelNotFoundException;
import com.example.restapp.GestorFinanciero.service.IGenericService;
import java.util.List;

public abstract class GenericService<T, ID> implements IGenericService<T, ID> {

    protected abstract IGenericRepo<T, ID> getRepo();
    public static final String ID_NOT_FOUND_MESSAGE = "ID NOT FOUND: ";

    @Override
    public T save(T t) {
        return getRepo().save(t);
    }

    @Override
    public T update(T t, ID id) {

        if (!getRepo().existsById(id)) {
            throw new ModelNotFoundException(ID_NOT_FOUND_MESSAGE + id);
        }

        return getRepo().save(t);
    }

    @Override
    public List<T> findAll() {
        return getRepo().findAll();
    }

    @Override
    public T findById(ID id) {
        return getRepo().findById(id)
                .orElseThrow(() -> new ModelNotFoundException(ID_NOT_FOUND_MESSAGE + id));
    }

    @Override
    public void delete(ID id) {

        if (!getRepo().existsById(id)) {
            throw new ModelNotFoundException(ID_NOT_FOUND_MESSAGE + id);
        }

        getRepo().deleteById(id);
    }
}
