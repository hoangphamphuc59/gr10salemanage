package services;

// import models.Customer;
import models.Product;
import models.Transaction;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

public class ReportManager {

    private ArrayList<Transaction> traList;

    public ReportManager(ArrayList<Transaction> traList) {
        this.traList = traList;
    }

    private List<Transaction> filterTransactions(String dateInput, String type) {
        List<Transaction> validTransactions = new ArrayList<>();

        try {
            if (type.equalsIgnoreCase("DAILY")) {
                LocalDate targetDate = LocalDate.parse(dateInput);
                for (Transaction t : traList) {
                    if (t.getStatus().equalsIgnoreCase("CONFIRMED") && t.getDate().equals(targetDate)) {
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

    public void highestPurchaseCustomer(String dateInput, String type) {
        List<Transaction> filteredList = filterTransactions(dateInput, type);
        if (filteredList.isEmpty()) {
            return;
        }

        System.out.println("\n--- TOP 3 HIGHEST SPENDING CUSTOMERS ---");

        Map<String, Double> customerSpending = new HashMap<>();

        for (Transaction t : filteredList) {
            String cusName = t.getCustomer().getName();
            customerSpending.put(cusName, t.getTotalAmount());
        }

        customerSpending.entrySet().stream().sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()));

        int c = 1;
        for (Map.Entry<String, Double> entry : customerSpending.entrySet()) {

            if (c <= 3) {
                System.out.printf("  + %s: $%.2f\n", entry.getKey(), entry.getValue());
                c++;
            }
        }
    }

    public void bestSellingProducts(String dateInput, String type) {
        List<Transaction> filteredList = filterTransactions(dateInput, type);
        if (filteredList.isEmpty()) {
            return;
        }

        Map<String, Integer> productQtyMap = new HashMap<>();
        Map<String, Double> productRevMap = new HashMap<>();

        for (Transaction t : filteredList) {
            for (Map.Entry<Product, Integer> entry : t.getItems().entrySet()) {
                Product product = entry.getKey();
                String pName = product.getProductName();
                int qty = entry.getValue();
                double rev = qty * product.getPrice();

                productQtyMap.put(pName, qty);
                productRevMap.put(pName, rev);
            }
        }

        System.out.println("\n--- TOP 3 BEST-SELLING PRODUCTS (BY QUANTITY) ---");
        productQtyMap.entrySet().stream().sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()));

        int c = 1;

        for (Map.Entry<String, Integer> entry : productQtyMap.entrySet()) {
            if (c <= 3) {
                System.out.printf("  + %s: %d unit(s)\n", entry.getKey(), entry.getValue());
                c++;
            }
        }

        System.out.println("\n--- TOP 3 BEST-SELLING PRODUCTS (BY REVENUE) ---");
        productRevMap.entrySet().stream().sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()));
        c = 1;

        for (Map.Entry<String, Double> entry : productRevMap.entrySet()) {
            if (c <= 3) {
                System.out.printf("  + %s: $%.2f\n", entry.getKey(), entry.getValue());
                c++;
            }
        }
    }
}
