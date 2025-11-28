package com.library.ui;

import com.library.model.Book;
import com.library.model.Invoice;
import com.library.model.Loan;
import com.library.model.User;
import com.library.repository.*;
import com.library.service.CatalogService;
import com.library.service.LoanService;
import com.library.service.InvoiceService;
import com.library.util.PersistenceManager;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class ConsoleUI {
    private final BookRepositoryImpl bookRepo = new BookRepositoryImpl();
    private final UserRepositoryImpl userRepo = new UserRepositoryImpl();
    private final LoanRepositoryImpl loanRepo = new LoanRepositoryImpl();
    private final InvoiceRepositoryImpl invoiceRepo = new InvoiceRepositoryImpl();
    private final CatalogService catalogService = new CatalogService(bookRepo);
    private final InvoiceService invoiceService = new InvoiceService(invoiceRepo);
    private final LoanService loanService = new LoanService(bookRepo, userRepo, loanRepo, invoiceService);

    private final Path dataDir = Paths.get("data");

    public void start() {
        Scanner scanner = new Scanner(System.in);

        // Uygulama başlarken mevcut verileri otomatik yükle
        handleLoad();

        System.out.println("=== Kütüphane Sistemi ===");
        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("1. Kitap İşlemleri");
            System.out.println("2. Üye İşlemleri");
            System.out.println("3. Ödünç/İade İşlemleri");
            System.out.println("4. Çıkış");
            System.out.print("Seçiminiz: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    handleBookMenu(scanner);
                    break;
                case "2":
                    handleUserMenu(scanner);
                    break;
                case "3":
                    handleLoanMenu(scanner);
                    break;
                case "4":
                    running = false;
                    break;
                default:
                    System.out.println("Geçersiz seçenek, tekrar deneyin.");
            }
        }

        // Uygulamadan çıkarken verileri otomatik kaydet
        handleSave();

        System.out.println("Uygulamadan çıkılıyor...");
        scanner.close();
    }

    // --- Alt menüler ---

    private void handleBookMenu(Scanner scanner) {
        System.out.println("\n--- Kitap İşlemleri ---");
        System.out.println("1. Kitap Ekle");
        System.out.println("2. Kitap Güncelle");
        System.out.println("3. Kitap Sil");
        System.out.println("4. Kitap Ara (ID/İsim/Yazar/ISBN)");
        System.out.println("5. Tüm Kitapları Listele");
        System.out.println("6. Belirli Yazarın Kitapları");
        System.out.println("7. Belirli Kategorideki Kitaplar");
        System.out.println("8. Geri");
        System.out.print("Seçiminiz: ");
        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1":
                handleAddBook(scanner);
                break;
            case "2":
                handleUpdateBook(scanner);
                break;
            case "3":
                handleDeleteBook(scanner);
                break;
            case "4":
                handleSearchBook(scanner);
                break;
            case "5":
                handleListBooks();
                break;
            case "6":
                handleListBooksByAuthor(scanner);
                break;
            case "7":
                handleListBooksByGenre(scanner);
                break;
            case "8":
                return;
            default:
                System.out.println("Geçersiz seçenek.");
        }
    }

    private void handleUserMenu(Scanner scanner) {
        System.out.println("\n--- Üye İşlemleri ---");
        System.out.println("1. Üye Ekle");
        System.out.println("2. Üyeleri Listele");
        System.out.println("3. Geri");
        System.out.print("Seçiminiz: ");
        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1":
                handleAddUser(scanner);
                break;
            case "2":
                handleListUsers();
                break;
            case "3":
                return;
            default:
                System.out.println("Geçersiz seçenek.");
        }
    }

    private void handleLoanMenu(Scanner scanner) {
        System.out.println("\n--- Ödünç/İade İşlemleri ---");
        System.out.println("1. Ödünç Al");
        System.out.println("2. Teslim Et");
        System.out.println("3. Ödünç Listesi");
        System.out.println("4. Fatura Listesi");
        System.out.println("5. Geri");
        System.out.print("Seçiminiz: ");
        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1":
                handleBorrow(scanner);
                break;
            case "2":
                handleReturn(scanner);
                break;
            case "3":
                handleListLoans();
                break;
            case "4":
                handleListInvoices();
                break;
            case "5":
                return;
            default:
                System.out.println("Geçersiz seçenek.");
        }
    }

    // --- Kitap işlemleri ---

    private void handleAddBook(Scanner scanner) {
        System.out.print("Kitap Adı: ");
        String title = scanner.nextLine().trim();
        System.out.print("Yazar(lar) (virgülle ayırın): ");
        String authorsLine = scanner.nextLine().trim();
        System.out.print("Yayınevi: ");
        String publisher = scanner.nextLine().trim();
        System.out.print("Yayın Yılı: ");
        int year = 0;
        try {
            year = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Yıl okunamadı, 0 olarak ayarlandı.");
        }
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine().trim();
        System.out.print("Kopya Sayısı: ");
        int copies = 1;
        try {
            copies = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Kopya sayısı okunamadı, 1 olarak ayarlandı.");
        }
        System.out.print("Kategoriler (virgülle ayırın, boş bırakılabilir): ");
        String genresLine = scanner.nextLine().trim();

        Book book = new Book(null, title, List.of(authorsLine.split("\\s*,\\s*")), publisher, year, isbn, copies);
        if (!genresLine.isBlank()) {
            for (String g : genresLine.split("\\s*,\\s*")) {
                book.getGenres().add(g);
            }
        }
        try {
            Book saved = catalogService.addBook(book);
            System.out.println("Kitap eklendi: " + saved);
        } catch (IllegalStateException e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    private void handleUpdateBook(Scanner scanner) {
        System.out.print("Güncellenecek kitabın ID'si: ");
        String id = scanner.nextLine().trim();
        var opt = catalogService.findById(id);
        if (opt.isEmpty()) {
            System.out.println("Kitap bulunamadı.");
            return;
        }
        Book book = opt.get();
        System.out.println("Mevcut: " + book);
        System.out.print("Yeni başlık (boş bırakılırsa değişmez): ");
        String newTitle = scanner.nextLine().trim();
        if (!newTitle.isBlank()) {
            book.setTitle(newTitle);
        }
        System.out.print("Yeni kopya sayısı (boş bırakılırsa değişmez): ");
        String copiesStr = scanner.nextLine().trim();
        if (!copiesStr.isBlank()) {
            try {
                int copies = Integer.parseInt(copiesStr);
                book.setCopies(copies);
            } catch (NumberFormatException e) {
                System.out.println("Kopya sayısı geçersiz, değiştirilmedi.");
            }
        }
        catalogService.updateBook(book);
        System.out.println("Güncellenmiş kitap: " + book);
    }

    private void handleDeleteBook(Scanner scanner) {
        System.out.print("Silinecek kitabın ID'si: ");
        String id = scanner.nextLine().trim();
        try {
            catalogService.deleteBook(id);
            System.out.println("Kitap silindi.");
        } catch (IllegalStateException e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    private void handleSearchBook(Scanner scanner) {
        System.out.print("Arama terimi (ID, isim, yazar veya ISBN): ");
        String query = scanner.nextLine().trim();
        List<Book> results = catalogService.search(query);
        if (results.isEmpty()) {
            System.out.println("Sonuç bulunamadı.");
        } else {
            System.out.println("--- Arama Sonuçları ---");
            for (Book b : results) {
                System.out.println(b);
            }
        }
    }

    private void handleListBooks() {
        List<Book> all = catalogService.getAll();
        if (all.isEmpty()) {
            System.out.println("Kayıtlı kitap yok.");
            return;
        }
        System.out.println("--- Kitap Listesi ---");
        for (Book b : all) {
            System.out.println(b);
        }
    }

    private void handleListBooksByAuthor(Scanner scanner) {
        System.out.print("Yazar adı: ");
        String author = scanner.nextLine().trim();
        List<Book> results = catalogService.searchByAuthor(author);
        if (results.isEmpty()) {
            System.out.println("Bu yazara ait kitap bulunamadı.");
        } else {
            System.out.println("--- " + author + " kitapları ---");
            for (Book b : results) {
                System.out.println(b);
            }
        }
    }

    private void handleListBooksByGenre(Scanner scanner) {
        System.out.print("Kategori adı: ");
        String genre = scanner.nextLine().trim();
        List<Book> results = catalogService.searchByGenre(genre);
        if (results.isEmpty()) {
            System.out.println("Bu kategoride kitap bulunamadı.");
        } else {
            System.out.println("--- " + genre + " kategorisindeki kitaplar ---");
            for (Book b : results) {
                System.out.println(b);
            }
        }
    }

    // --- Üye işlemleri ---

    private void handleAddUser(Scanner scanner) {
        System.out.print("Ad Soyad: ");
        String name = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        try {
            User u = new User(null, name, email, 5);
            User saved = userRepo.save(u);
            System.out.println("Üye eklendi: " + saved);
        } catch (IllegalStateException e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    private void handleListUsers() {
        var all = userRepo.findAll();
        if (all.isEmpty()) {
            System.out.println("Kayıtlı üye yok.");
            return;
        }
        System.out.println("--- Üye Listesi ---");
        for (User u : all) {
            System.out.println(u);
        }
    }

    // --- Ödünç/İade işlemleri ---

    private void handleBorrow(Scanner scanner) {
        System.out.print("Kullanıcı e-posta adresi: ");
        String email = scanner.nextLine().trim();
        Optional<User> userOpt = userRepo.findByEmail(email);
        if (userOpt.isEmpty()) {
            System.out.println("Bu e-posta ile kayıtlı bir üye bulunamadı.");
            return;
        }
        User user = userOpt.get();
        System.out.println("Seçilen üye: " + user);

        // Kitabı ISBN ezberletmek yerine aramayla seçtirelim
        System.out.print("Kitap arama terimi (isim / yazar / ISBN): ");
        String query = scanner.nextLine().trim();
        List<Book> candidates = catalogService.search(query);
        if (candidates.isEmpty()) {
            System.out.println("Bu arama ile eşleşen kitap bulunamadı.");
            return;
        }

        if (candidates.size() == 1) {
            Book book = candidates.get(0);
            System.out.println("Bulunan kitap: " + book);
            try {
                Loan loan = loanService.borrowBook(user.getId(), book.getIsbn());
                System.out.println("Ödünç oluşturuldu: " + loan);
            } catch (IllegalStateException e) {
                System.out.println("Hata: " + e.getMessage());
            }
            return;
        }

        System.out.println("Birden fazla kitap bulundu, lütfen seçim yapın:");
        for (int i = 0; i < candidates.size(); i++) {
            System.out.printf("%d) %s%n", i + 1, candidates.get(i));
        }
        System.out.print("Seçmek istediğiniz kitabın numarası: ");
        String idxStr = scanner.nextLine().trim();
        int idx;
        try {
            idx = Integer.parseInt(idxStr);
        } catch (NumberFormatException e) {
            System.out.println("Geçersiz seçim.");
            return;
        }
        if (idx < 1 || idx > candidates.size()) {
            System.out.println("Geçersiz seçim.");
            return;
        }
        Book selected = candidates.get(idx - 1);
        try {
            Loan loan = loanService.borrowBook(user.getId(), selected.getIsbn());
            System.out.println("Ödünç oluşturuldu: " + loan);
        } catch (IllegalStateException e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    private void handleListLoans() {
        var all = loanRepo.findAll();
        if (all.isEmpty()) {
            System.out.println("Aktif ödünç yok.");
            return;
        }
        System.out.println("--- Ödünç Listesi ---");
        for (Loan l : all) {
            System.out.println(l);
        }
    }

    private void handleReturn(Scanner scanner) {
        System.out.print("Loan ID: ");
        String loanId = scanner.nextLine().trim();
        try {
            loanService.returnBook(loanId);
            System.out.println("Teslim edildi: " + loanId);
        } catch (IllegalStateException e) {
            System.out.println("Hata: " + e.getMessage());
        }
    }

    // --- Fatura raporu ---

    private void handleListInvoices() {
        var all = invoiceService.getAllInvoices();
        if (all.isEmpty()) {
            System.out.println("Fatura yok.");
            return;
        }
        System.out.println("--- Fatura Listesi ---");
        all.forEach(System.out::println);
    }

    // --- Kalıcı saklama ---

    private void handleSave() {
        try {
            PersistenceManager.saveMap(bookRepo.getStorage(), dataDir.resolve("books.dat"));
            PersistenceManager.saveMap(userRepo.getStorage(), dataDir.resolve("users.dat"));
            PersistenceManager.saveMap(loanRepo.getStorage(), dataDir.resolve("loans.dat"));
            PersistenceManager.saveMap(invoiceRepo.getStorage(), dataDir.resolve("invoices.dat"));
            System.out.println("Veriler kaydedildi.");
        } catch (Exception e) {
            System.out.println("Kaydetme hatası: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void handleLoad() {
        try {
            var b = (Map<String, Book>) PersistenceManager.loadMap(dataDir.resolve("books.dat"));
            if (b != null) bookRepo.setStorage(b);
            var u = (Map<String, User>) PersistenceManager.loadMap(dataDir.resolve("users.dat"));
            if (u != null) userRepo.setStorage(u);
            var ln = (Map<String, Loan>) PersistenceManager.loadMap(dataDir.resolve("loans.dat"));
            if (ln != null) loanRepo.setStorage(ln);
            var inv = (Map<String, Invoice>) PersistenceManager.loadMap(dataDir.resolve("invoices.dat"));
            if (inv != null) invoiceRepo.setStorage(inv);
            System.out.println("Veriler yüklendi.");
        } catch (Exception e) {
            System.out.println("Yükleme hatası: " + e.getMessage());
        }
    }
}
