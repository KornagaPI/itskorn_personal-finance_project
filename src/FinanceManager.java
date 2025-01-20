import java.io.*;
import java.util.*;

public class PersonalFinanceApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String DATA_PATH = "user_finance_data.txt";
    private static final Map<String, AccountHolder> accountDatabase = new HashMap<>();
    private static AccountHolder activeAccount;

    public static void main(String[] args) {
        loadAccountData();
        while (true) {
            System.out.println("\n1. Вход в систему\n2. Создать новый аккаунт\n3. Выход");
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    login();
                    break;
                case "2":
                    createNewAccount();
                    break;
                case "3":
                    saveAccountData();
                    System.exit(0);
                default:
                    System.out.println("Некорректный ввод. Попробуйте еще раз.");
            }
        }
    }

    private static void login() {
        System.out.print("Введите имя пользователя: ");
        String username = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();

        if (accountDatabase.containsKey(username) && accountDatabase.get(username).checkPassword(password)) {
            activeAccount = accountDatabase.get(username);
            System.out.println("Добро пожаловать, " + activeAccount.getAccountName() + "!");
            manageFinances();
        } else {
            System.out.println("Неверное имя пользователя или пароль.");
        }
    }

    private static void createNewAccount() {
        System.out.print("Введите новое имя пользователя: ");
        String username = scanner.nextLine();
        if (accountDatabase.containsKey(username)) {
            System.out.println("Такой аккаунт уже существует.");
            return;
        }
        System.out.print("Введите пароль для нового аккаунта: ");
        String password = scanner.nextLine();
        AccountHolder newAccount = new AccountHolder(username, password);
        accountDatabase.put(username, newAccount);
        System.out.println("Аккаунт успешно создан.");
    }

    private static void manageFinances() {
        while (true) {
            System.out.println("\n1. Добавить доход\n2. Добавить расход\n3. Установить лимит\n4. Показать отчет\n5. Выйти");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    addIncome();
                    break;
                case "2":
                    addExpense();
                    break;
                case "3":
                    setLimit();
                    break;
                case "4":
                    showReport();
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
            }
        }
    }

    private static void addIncome() {
        System.out.print("Категория дохода: ");
        String category = scanner.nextLine();
        System.out.print("Сумма: ");
        double amount = Double.parseDouble(scanner.nextLine());
        activeAccount.recordIncome(category, amount);
        System.out.println("Доход добавлен.");
    }

    private static void addExpense() {
        System.out.print("Категория расхода: ");
        String category = scanner.nextLine();
        System.out.print("Сумма: ");
        double amount = Double.parseDouble(scanner.nextLine());
        if (activeAccount.recordExpense(category, amount)) {
            System.out.println("Расход добавлен.");
        } else {
            System.out.println("Недостаточно средств для добавления расхода.");
        }
    }

    private static void setLimit() {
        System.out.print("Категория: ");
        String category = scanner.nextLine();
        System.out.print("Установить лимит: ");
        double limit = Double.parseDouble(scanner.nextLine());
        activeAccount.assignLimit(category, limit);
        System.out.println("Лимит установлен.");
    }

    private static void showReport() {
        activeAccount.showFinanceReport();
    }

    private static void saveAccountData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_PATH))) {
            oos.writeObject(accountDatabase);
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении данных.");
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadAccountData() {
        File file = new File(DATA_PATH);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Map<String, AccountHolder> loadedAccounts = (Map<String, AccountHolder>) ois.readObject();
                accountDatabase.putAll(loadedAccounts);
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Ошибка при загрузке данных.");
            }
        }
    }
}
