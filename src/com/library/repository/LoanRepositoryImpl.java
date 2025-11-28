package com.library.repository;

import com.library.model.Loan;
import com.library.util.IdGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class LoanRepositoryImpl implements Repository<Loan, String> {
    private Map<String, Loan> storage = new ConcurrentHashMap<>();

    @Override
    public Loan save(Loan entity) {
        String id = entity.getId();
        if (id == null || id.isBlank()) {
            id = IdGenerator.generate("LOAN");
            entity.setId(id);
        }
        storage.put(id, entity);
        return entity;
    }

    @Override
    public Optional<Loan> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Loan> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void deleteById(String id) {
        storage.remove(id);
    }

    public Map<String, Loan> getStorage() {
        return storage;
    }

    public void setStorage(Map<String, Loan> storage) {
        this.storage = storage;
    }
}
