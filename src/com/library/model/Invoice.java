package com.library.model;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Invoice implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private final String loanId;
    private final BigDecimal amount;
    private final LocalDate issuedDate;
    private boolean paid = false;

    public Invoice(String id, String loanId, BigDecimal amount, LocalDate issuedDate) {
        this.id = id;
        this.loanId = loanId;
        this.amount = amount;
        this.issuedDate = issuedDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoanId() {
        return loanId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDate getIssuedDate() {
        return issuedDate;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    @Override
    public String toString() {
        String durum = paid ? "Ödendi" : "Ödenmedi";
        return String.format("Tutar: %s | Tarih: %s | Durum: %s", amount, issuedDate, durum);
    }
}
