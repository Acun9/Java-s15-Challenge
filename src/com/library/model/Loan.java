package com.library.model;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

public class Loan implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private final String userId;
    private final String bookId;
    private final LocalDate loanDate;
    private final LocalDate dueDate;
    private boolean returned = false;

    public Loan(String id, String userId, String bookId, LocalDate loanDate, LocalDate dueDate) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public String getBookId() {
        return bookId;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    @Override
    public String toString() {
        return String.format("Loan[%s] user=%s book=%s loanDate=%s due=%s returned=%s", id, userId, bookId, loanDate, dueDate, returned);
    }
}
