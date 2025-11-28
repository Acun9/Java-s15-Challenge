package com.library.service;

import com.library.model.Book;
import com.library.model.Invoice;
import com.library.model.Loan;
import com.library.model.User;
import com.library.repository.BookRepositoryImpl;
import com.library.repository.InvoiceRepositoryImpl;
import com.library.repository.LoanRepositoryImpl;
import com.library.repository.UserRepositoryImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public class LoanService {
    private final BookRepositoryImpl bookRepo;
    private final UserRepositoryImpl userRepo;
    private final LoanRepositoryImpl loanRepo;
    private final InvoiceService invoiceService;

    public LoanService(BookRepositoryImpl bookRepo, UserRepositoryImpl userRepo, LoanRepositoryImpl loanRepo, InvoiceService invoiceService) {
        this.bookRepo = bookRepo;
        this.userRepo = userRepo;
        this.loanRepo = loanRepo;
        this.invoiceService = invoiceService;
    }

    public Loan borrowBook(String userId, String isbn) throws IllegalStateException {
        User user = userRepo.findById(userId).orElseThrow(() -> new IllegalStateException("Kullanıcı bulunamadı: " + userId));
        if (user.getActiveLoanIds().size() >= user.getMaxLoanLimit()) {
            throw new IllegalStateException("Kullanıcı 5 kitap limitine ulaştı.");
        }
        Book book = bookRepo.findByIsbn(isbn).orElseThrow(() -> new IllegalStateException("Kitap bulunamadı: " + isbn));
        if (book.getCopies() <= 0) {
            throw new IllegalStateException("Kitabın kopyası kalmadı.");
        }
        // Kitap zaten bu kullanıcı tarafından ödünç alınmış mı kontrolü
        boolean alreadyBorrowed = loanRepo.findAll().stream()
                .anyMatch(l -> !l.isReturned() && l.getUserId().equals(userId) && l.getBookId().equals(book.getId()));
        if (alreadyBorrowed) {
            throw new IllegalStateException("Bu kitabı zaten ödünç aldınız.");
        }

        book.setCopies(book.getCopies() - 1);
        bookRepo.save(book);

        Loan loan = new Loan(null, user.getId(), book.getId(), LocalDate.now(), LocalDate.now().plusWeeks(2));
        loanRepo.save(loan);
        user.addLoan(loan.getId());
        userRepo.save(user);

        // Fatura kes
        invoiceService.createInvoiceForLoan(loan, new BigDecimal("10.00")); // Örnek tutar
        return loan;
    }

    public void returnBook(String loanId) throws IllegalStateException {
        Loan loan = loanRepo.findById(loanId).orElseThrow(() -> new IllegalStateException("Ödünç kaydı bulunamadı: " + loanId));
        if (loan.isReturned()) {
            throw new IllegalStateException("Bu kitap zaten iade edilmiş.");
        }
        Book book = bookRepo.findById(loan.getBookId()).orElseThrow(() -> new IllegalStateException("İade edilen kitap sistemde bulunamadı."));

        book.setCopies(book.getCopies() + 1);
        bookRepo.save(book);
        loan.setReturned(true);
        loanRepo.save(loan);
        userRepo.findById(loan.getUserId()).ifPresent(u -> {
            u.removeLoan(loan.getId());
            userRepo.save(u);
        });

        // Fatura iadesi
        invoiceService.refundInvoiceForLoan(loan.getId());
    }

    public boolean isBookOnLoan(String bookId) {
        return loanRepo.findAll().stream().anyMatch(l -> l.getBookId().equals(bookId) && !l.isReturned());
    }
}
