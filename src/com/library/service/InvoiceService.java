package com.library.service;

import com.library.model.Invoice;
import com.library.model.Loan;
import com.library.repository.InvoiceRepositoryImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class InvoiceService {
    private final InvoiceRepositoryImpl invoiceRepo;

    public InvoiceService(InvoiceRepositoryImpl invoiceRepo) {
        this.invoiceRepo = invoiceRepo;
    }

    public Invoice createInvoiceForLoan(Loan loan, BigDecimal amount) {
        Invoice inv = new Invoice(null, loan.getId(), amount, LocalDate.now());
        System.out.printf("Fatura kesildi: Loan %s için %s TL%n", loan.getId(), amount);
        return invoiceRepo.save(inv);
    }

    public void refundInvoiceForLoan(String loanId) {
        findInvoiceByLoanId(loanId).ifPresent(inv -> {
            if (!inv.isPaid()) { // Basit bir mantık, eğer ödenmediyse iade et
                System.out.printf("Fatura iade edildi: Loan %s için %s TL%n", loanId, inv.getAmount());
                // Gerçek bir sistemde bu fatura iptal edilebilir veya iade faturası oluşturulabilir.
                // Şimdilik sadece ödenmiş olarak işaretleyelim.
                inv.setPaid(true);
                invoiceRepo.save(inv);
            }
        });
    }

    public void payInvoice(String invoiceId) {
        invoiceRepo.findById(invoiceId).ifPresent(i -> {
            i.setPaid(true);
            invoiceRepo.save(i);
        });
    }

    public java.util.Optional<Invoice> findInvoiceByLoanId(String loanId) {
        return invoiceRepo.findAll().stream().filter(i -> i.getLoanId().equals(loanId)).findFirst();
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepo.findAll();
    }
}
