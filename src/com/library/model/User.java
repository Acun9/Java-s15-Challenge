package com.library.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User extends Person {
    private final int maxLoanLimit;
    private final List<String> activeLoanIds = new ArrayList<>();

    public User(String id, String name, String email, int maxLoanLimit) {
        super(id, name, email);
        this.maxLoanLimit = maxLoanLimit;
    }

    public int getMaxLoanLimit() {
        return maxLoanLimit;
    }

    public List<String> getActiveLoanIds() {
        return activeLoanIds;
    }

    public void addLoan(String loanId) {
        activeLoanIds.add(loanId);
    }

    public void removeLoan(String loanId) {
        activeLoanIds.remove(loanId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        // Domain açısından e-posta aynıysa kullanıcıyı aynı kabul ediyoruz
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return String.format("%s | maxLoans:%d | active:%d", super.toString(), maxLoanLimit, activeLoanIds.size());
    }
}
