package com.library.service;

import com.library.model.Book;
import com.library.repository.BookRepositoryImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CatalogService {
    private final BookRepositoryImpl bookRepo;

    public CatalogService(BookRepositoryImpl bookRepo) {
        this.bookRepo = bookRepo;
    }

    public Book addBook(Book book) {
        // ISBN unique olmalı kontrolü
        if (bookRepo.findByIsbn(book.getIsbn()).isPresent()) {
            throw new IllegalStateException("Bu ISBN zaten mevcut: " + book.getIsbn());
        }
        return bookRepo.save(book);
    }

    public Optional<Book> findByIsbn(String isbn) {
        return bookRepo.findByIsbn(isbn);
    }

    public Optional<Book> findById(String id) {
        return bookRepo.findById(id);
    }

    public List<Book> searchByTitle(String title) {
        if (title == null || title.isBlank()) return List.of();
        String q = title.toLowerCase();
        return bookRepo.findAll().stream()
                .filter(b -> b.getTitle() != null && b.getTitle().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }

    public List<Book> searchByAuthor(String author) {
        if (author == null || author.isBlank()) return List.of();
        String q = author.toLowerCase();
        return bookRepo.findAll().stream()
                .filter(b -> b.getAuthors().stream().anyMatch(a -> a.toLowerCase().contains(q)))
                .collect(Collectors.toList());
    }

    public List<Book> searchByGenre(String genre) {
        if (genre == null || genre.isBlank()) return List.of();
        String q = genre.toLowerCase();
        return bookRepo.findAll().stream()
                .filter(b -> b.getGenres().stream().anyMatch(g -> g.toLowerCase().contains(q)))
                .collect(Collectors.toList());
    }

    // README: id, isim veya yazar bilgisine göre bir kitap seçilebilir -> hepsini kapsayan arama
    public List<Book> search(String query) {
        if (query == null || query.isBlank()) return List.of();
        String q = query.toLowerCase();
        List<Book> result = new ArrayList<>();
        // Başlığa göre
        result.addAll(searchByTitle(query));
        // Yazara göre
        result.addAll(searchByAuthor(query));
        // ID
        findById(query).ifPresent(result::add);
        // ISBN
        findByIsbn(query).ifPresent(result::add);
        // Tekrarlayanları kaldır
        return result.stream().distinct().collect(Collectors.toList());
    }

    public Book updateBook(Book book) {
        if (book.getId() == null || bookRepo.findById(book.getId()).isEmpty()) {
            throw new IllegalStateException("Güncellenecek kitap bulunamadı.");
        }
        return bookRepo.save(book);
    }

    public void deleteBook(String bookId) {
        if (bookRepo.findById(bookId).isEmpty()) {
            throw new IllegalStateException("Silinecek kitap bulunamadı.");
        }
        bookRepo.deleteById(bookId);
    }

    public List<Book> getAll() {
        return bookRepo.findAll();
    }
}
