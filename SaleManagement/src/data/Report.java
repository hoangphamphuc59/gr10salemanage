package data;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Report {

    private ArrayList<Transaction> traList;

    public Report(ArrayList<Transaction> traList) {
        this.traList = traList;
    }

    // =========================================================================
    // HELPER METHOD: Filter CONFIRMED transactions by Date or Month
    // =========================================================================
    private List<Transaction> filterTransactions(String dateInput, String type) {
        List<Transaction> validTransactions = new ArrayList<>();

        try {
            if (type.equalsIgnoreCase("DAILY")) {
                LocalDate targetDate = LocalDate.parse(dateInput);
                for (Transaction t : traList) {
                    if ("CONFIRMED".equalsIgnoreCase(t.getStatus()) && t.getDate().equals(targetDate)) {
                        validTransactions.add(t);
                    }
                }
            } else if (type.equalsIgnoreCase("MONTHLY")) {
                YearMonth targetMonth = YearMonth.parse(dateInput);
                for (Transaction t : traList) {
                    YearMonth transMonth = YearMonth.from(t.getDate());
                    if ("CONFIRMED".equalsIgnoreCase(t.getStatus()) && transMonth.equals(targetMonth)) {
                        validTransactions.add(t);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid date format! Please use 'yyyy-MM-dd' for daily and 'yyyy-MM' for monthly reports.");
        }
        return validTransactions;
    }

    // =========================================================================
    // 1 & 2. GENERAL SALES REPORT (DAILY & MONTHLY)
    // =========================================================================
    public void dailySaleReport(String dateStr) {
        System.out.println("\n=== DAILY SALES REPORT: " + dateStr + " ===");
        generateGeneralReport(filterTransactions(dateStr, "DAILY"));
    }

    public void monthlySaleReport(String monthStr) {
        System.out.println("\n=== MONTHLY SALES REPORT: " + monthStr + " ===");
        generateGeneralReport(filterTransactions(monthStr, "MONTHLY"));
    }

    private void generateGeneralReport(List<Transaction> filteredList) {
        if (filteredList.isEmpty()) {
            System.out.println("No successful transactions found for this period.");
            return;
        }

        int totalProductsSold = 0;
        double totalRevenue = 0.0;

        for (Transaction t : filteredList) {
            totalRevenue += t.getTotalAmount();
            for (Integer qty : t.getItems().values()) {
                totalProductsSold += qty;
            }
        }

        System.out.printf("- Total products sold: %d unit(s)\n", totalProductsSold);
        System.out.printf("- Total revenue: $%.2f\n", totalRevenue);
    }

    // =========================================================================
    // 3. TOP 3 HIGHEST SPENDING CUSTOMERS
    // =========================================================================
    public void highestPurchaseCustomer(String dateInput, String type) {
        List<Transaction> filteredList = filterTransactions(dateInput, type);
        if (filteredList.isEmpty()) {
            return;
        }

        System.out.println("\n--- TOP 3 HIGHEST SPENDING CUSTOMERS ---");

        Map<String, Double> customerSpending = new HashMap<>();
        for (Transaction t : filteredList) {
            // Thay "Khách vãng lai" thành "Guest" hoặc "Walk-in Customer"
            String cusName = t.getCustomer() != null ? t.getCustomer().getName() : "Guest";
            customerSpending.put(cusName, customerSpending.getOrDefault(cusName, 0.0) + t.getTotalAmount());
        }

        customerSpending.entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .limit(3)
                .forEach(e -> System.out.printf("  + %s: $%.2f\n", e.getKey(), e.getValue()));
    }

    // =========================================================================
    // 4. TOP 3 BEST-SELLING PRODUCTS (BY QUANTITY & REVENUE)
    // =========================================================================
    public void bestSellingProducts(String dateInput, String type) {
        List<Transaction> filteredList = filterTransactions(dateInput, type);
        if (filteredList.isEmpty()) {
            return;
        }

        Map<String, Integer> productQtyMap = new HashMap<>();
        Map<String, Double> productRevMap = new HashMap<>();

        for (Transaction t : filteredList) {
            for (Map.Entry<Product, Integer> entry : t.getItems().entrySet()) {
                String pName = entry.getKey().getProductName();
                int qty = entry.getValue();
                double revenue = qty * entry.getKey().getPrice();

                productQtyMap.put(pName, productQtyMap.getOrDefault(pName, 0) + qty);
                productRevMap.put(pName, productRevMap.getOrDefault(pName, 0.0) + revenue);
            }
        }

        System.out.println("\n--- TOP 3 BEST-SELLING PRODUCTS (BY QUANTITY) ---");
        productQtyMap.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                .limit(3)
                .forEach(e -> System.out.printf("  + %s: %d unit(s)\n", e.getKey(), e.getValue()));

        System.out.println("\n--- TOP 3 BEST-SELLING PRODUCTS (BY REVENUE) ---");
        productRevMap.entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .limit(3)
                .forEach(e -> System.out.printf("  + %s: $%.2f\n", e.getKey(), e.getValue()));
    }
}
