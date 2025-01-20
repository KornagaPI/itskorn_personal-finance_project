import java.io.*;
import java.util.*;

public class User implements Serializable {
    private final String username;
    private final String password;
    private final Map<String, Double> incomeMap = new HashMap<>();
    private final Map<String, Double> expenseMap = new HashMap<>();
    private final Map<String, Double> budgetMap = new HashMap<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public boolean authenticate(String password) {
        return this.password.equals(password);
    }

    public void addIncome(String category, double amount) {
        incomeMap.put(category, incomeMap.getOrDefault(category, 0.0) + amount);
    }

    public boolean addExpense(String category, double amount) {
        double totalIncome = incomeMap.values().stream().mapToDouble(Double::doubleValue).sum();
        double totalExpenses = expenseMap.values().stream().mapToDouble(Double::doubleValue).sum();
        if (totalIncome - totalExpenses >= amount) {
            expenseMap.put(category, expenseMap.getOrDefault(category, 0.0) + amount);
            return true;
        }
        return false;
    }

    public void setBudget(String category, double budget) {
        budgetMap.put(category, budget);
    }

    public void displayFinancialOverview() {
        System.out.println("\nФинансовая информация:");
        double totalIncome = incomeMap.values().stream().mapToDouble(Double::doubleValue).sum();
        double totalExpenses = expenseMap.values().stream().mapToDouble(Double::doubleValue).sum();
        System.out.println("Общий доход: " + totalIncome);
        System.out.println("Общие расходы: " + totalExpenses);

        System.out.println("\nБюджет по категориям:");
        for (String category : budgetMap.keySet()) {
            double budget = budgetMap.get(category);
            double spent = expenseMap.getOrDefault(category, 0.0);
            System.out.printf("Категория: %s, Бюджет: %.2f, Потрачено: %.2f, Остаток: %.2f\n",
                    category, budget, spent, budget - spent);
        }
    }
}
