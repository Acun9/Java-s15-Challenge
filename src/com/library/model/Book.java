package com.library.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public class Book extends Item implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final List<String> authors;      // kitap yazarları
    private final String isbn;               // ISBN numarası
    private int copies;                      // kütüphanedeki kopya sayısı
    private final Set<String> genres = new HashSet<>(); // kitabın türleri

    public Book(String id, String title, List<String> authors, String publisher, int year, String isbn, int copies) {
        super(id, title, publisher, year);
        this.authors = new ArrayList<>(authors);
        this.isbn = isbn;
        this.copies = copies;
    }

    public List<String> getAuthors() {
        // dışarıya sadece okunabilir liste veriyorum
        return Collections.unmodifiableList(authors);
    }

    public void addAuthor(String author) {
        if (author != null && !author.isBlank()) {
            authors.add(author.trim());
        }
    }

    public void removeAuthor(String author) {
        authors.removeIf(a -> Objects.equals(a, author));
    }

    public String getIsbn() {
        return isbn;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    public Set<String> getGenres() {
        // türleri de yine sadece okunabilir şekilde dönüyorum
        return Collections.unmodifiableSet(genres);
    }

    public void addGenre(String genre) {
        if (genre != null && !genre.isBlank()) {
            genres.add(genre.trim());
        }
    }

    public void removeGenre(String genre) {
        genres.removeIf(g -> Objects.equals(g, genre));
    }

    // başlık güncelleme (güncelleme ekranında kullanıyorum)
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        // ISBN aynıysa kitabı aynı kabul ediyorum
        return Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }

    @Override
    public String toString() {
        String tur = String.join(",", genres);
        String yazarlar = String.join(",", authors);
        return String.format("Kitap Adı: %s (%d) | Yazar:%s | Tür:%s | Yayınevi:%s | Kopya sayısı:%d | ISBN:%s",
                title, year, yazarlar, tur, publisher, copies, isbn);
    }
}
