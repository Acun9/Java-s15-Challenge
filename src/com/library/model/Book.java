package com.library.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Book extends Item implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final List<String> authors;
    private final String isbn;
    private int copies;
    private final Set<String> genres = new HashSet<>();

    public Book(String id, String title, List<String> authors, String publisher, int year, String isbn, int copies) {
        super(id, title, publisher, year);
        this.authors = authors;
        this.isbn = isbn;
        this.copies = copies;
    }

    public List<String> getAuthors() {
        return authors;
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
        return genres;
    }

    // Başlığı güncellemek için setter (kitap güncelleme senaryosu)
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        // Domain açısından ISBN aynıysa kitabı aynı kabul ediyoruz
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
        // Format:
        // Kitap Adı: <title> (<year>) | Yazar:<authors> | Tür:<genres> | Yayınevi:<publisher> | Kopya sayısı:<copies> | ISBN:<isbn>
        return String.format("Kitap Adı: %s (%d) | Yazar:%s | Tür:%s | Yayınevi:%s | Kopya sayısı:%d | ISBN:%s",
                title, year, yazarlar, tur, publisher, copies, isbn);
    }
}
