package com.library.repository;

import com.library.model.User;
import com.library.util.IdGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepositoryImpl implements Repository<User, String> {
    private Map<String, User> storage = new ConcurrentHashMap<>();

    @Override
    public User save(User entity) {
        // E-posta benzersiz olsun: aynı e-posta başka bir kullanıcıda varsa hata fırlat
        String email = entity.getEmail();
        if (email != null && !email.isBlank()) {
            for (User existing : storage.values()) {
                if (existing.getEmail().equalsIgnoreCase(email) && (entity.getId() == null || !existing.getId().equals(entity.getId()))) {
                    throw new IllegalStateException("Bu e-posta ile kay1tl1 bir cyeye zaten var: " + email);
                }
            }
        }

        String id = entity.getId();
        if (id == null || id.isBlank()) {
            id = IdGenerator.generate("USER");
            entity.setId(id);
        }
        storage.put(id, entity);
        return entity;
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void deleteById(String id) {
        storage.remove(id);
    }

    public Optional<User> findByEmail(String email) {
        if (email == null) {
            return Optional.empty();
        }
        return storage.values().stream()
                .filter(u -> email.equalsIgnoreCase(u.getEmail()))
                .findFirst();
    }

    public Map<String, User> getStorage() {
        return storage;
    }

    public void setStorage(Map<String, User> storage) {
        this.storage = storage;
    }
}
