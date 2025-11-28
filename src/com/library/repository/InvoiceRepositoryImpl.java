package com.library.repository;

import com.library.model.Invoice;
import com.library.util.IdGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InvoiceRepositoryImpl implements Repository<Invoice, String> {
    private Map<String, Invoice> storage = new ConcurrentHashMap<>();

    @Override
    public Invoice save(Invoice entity) {
        String id = entity.getId();
        if (id == null || id.isBlank()) {
            id = IdGenerator.generate("INVOICE");
            entity.setId(id);
        }
        storage.put(id, entity);
        return entity;
    }

    @Override
    public Optional<Invoice> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Invoice> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void deleteById(String id) {
        storage.remove(id);
    }

    public Map<String, Invoice> getStorage() {
        return storage;
    }

    public void setStorage(Map<String, Invoice> storage) {
        this.storage = storage;
    }
}
