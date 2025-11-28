package com.library.repository;

import com.library.model.Book;
import com.library.util.IdGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class BookRepositoryImpl implements Repository<Book, String> {
    private Map<String, Book> storage = new ConcurrentHashMap<>();

    @Override
    public Book save(Book entity) {
        String id = entity.getId();
        if (id == null || id.isBlank()) {
            id = IdGenerator.generate("BOOK");
            entity.setId(id);
        }
        // store with id as key (also keep isbn inside Book)
        String key = id;
        storage.put(key, entity);
        return entity;
    }

    @Override
    public Optional<Book> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    public Optional<Book> findByIsbn(String isbn) {
        if (isbn == null) return Optional.empty();
        return storage.values().stream().filter(b -> isbn.equals(b.getIsbn())).findFirst();
    }

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void deleteById(String id) {
        storage.remove(id);
    }

    // Persistence helper
    public Map<String, Book> getStorage() {
        return storage;
    }

    public void setStorage(Map<String, Book> storage) {
        this.storage = storage;
    }
}
