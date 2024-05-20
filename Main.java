import java.util.*;

// Class to represent an Account
class Account {
    private String accountNumber;
    private double balance;
    private List<BankTransaction> transactionHistory;

    public Account(String accountNumber, double initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
        this.transactionHistory = new ArrayList<>();
    }

    public void deposit(double amount) {
        balance += amount;
        transactionHistory.add(new BankTransaction("Deposit", amount, balance));
    }

    public boolean withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            transactionHistory.add(new BankTransaction("Withdrawal", amount, balance));
            return true;
        }
        return false;
    }

    public double getBalance() {
        return balance;
    }

    public List<BankTransaction> getTransactionHistory() {
        return transactionHistory;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}

// Class to represent an Account Holder
class AccountHolder {
    private String userId;
    private String userPin;
    private Account account;

    public AccountHolder(String userId, String userPin, Account account) {
        this.userId = userId;
        this.userPin = userPin;
        this.account = account;
    }

    public boolean verifyPin(String inputPin) {
        return userPin.equals(inputPin);
    }

    public Account getAccount() {
        return account;
    }

    public String getUserId() {
        return userId;
    }
}

// Class to represent a Bank Transaction
class BankTransaction {
    private static int transactionCounter = 0;
    private int transactionId;
    private Date date;
    private String type;
    private double amount;
    private double balanceAfter;

    public BankTransaction(String type, double amount, double balanceAfter) {
        this.transactionId = ++transactionCounter;
        this.date = new Date();
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
    }

    public String getDetails() {
        return String.format("Transaction ID: %d, Date: %s, Type: %s, Amount: %.2f, Balance After: %.2f",
                transactionId, date.toString(), type, amount, balanceAfter);
    }
}

// Class to represent a Bank
class Bank {
    private Map<String, AccountHolder> accountHolders;

    public Bank() {
        this.accountHolders = new HashMap<>();
    }

    public void addAccountHolder(AccountHolder accountHolder) {
        accountHolders.put(accountHolder.getAccount().getAccountNumber(), accountHolder);
    }

    public AccountHolder authenticateUser(String userId, String userPin) {
        for (AccountHolder accountHolder : accountHolders.values()) {
            if (accountHolder.verifyPin(userPin) && accountHolder.getUserId().equals(userId)) {
                return accountHolder;
            }
        }
        return null;
    }

    public Account getAccount(String accountNumber) {
        AccountHolder accountHolder = accountHolders.get(accountNumber);
        if (accountHolder != null) {
            return accountHolder.getAccount();
        }
        return null;
    }
}

// Class to represent an ATM
class ATM {
    private Bank bank;
    private Scanner scanner;

    public ATM(Bank bank) {
        this.bank = bank;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Welcome to the ATM!");
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter PIN: ");
        String userPin = scanner.nextLine();

        AccountHolder accountHolder = bank.authenticateUser(userId, userPin);

        if (accountHolder != null) {
            showMenu(accountHolder.getAccount());
        } else {
            System.out.println("Invalid User ID or PIN.");
        }
    }

    private void showMenu(Account account) {
        while (true) {
            System.out.println("\nATM Menu:");
            System.out.println("1. Show Transaction History");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Quit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    showTransactionHistory(account);
                    break;
                case 2:
                    withdraw(account);
                    break;
                case 3:
                    deposit(account);
                    break;
                case 4:
                    quit();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void showTransactionHistory(Account account) {
        System.out.println("\nTransaction History:");
        for (BankTransaction transaction : account.getTransactionHistory()) {
            System.out.println(transaction.getDetails());
        }
    }

    private void withdraw(Account account) {
        System.out.print("Enter amount to withdraw: ");
        double amount = scanner.nextDouble();
        if (account.withdraw(amount)) {
            System.out.println("Withdrawal successful.");
        } else {
            System.out.println("Insufficient balance.");
        }
    }

    private void deposit(Account account) {
        System.out.print("Enter amount to deposit: ");
        double amount = scanner.nextDouble();
        account.deposit(amount);
        System.out.println("Deposit successful.");
    }

    private void quit() {
        System.out.println("Thank you for using the ATM. Goodbye!");
    }
}

// Main class to initiate the ATM application
public class Main {
    public static void main(String[] args) {
        // Create accounts and account holders
        Account account1 = new Account("123456", 1000);
        AccountHolder accountHolder1 = new AccountHolder("user1", "1234", account1);

        // Add accounts to the bank
        Bank bank = new Bank();
        bank.addAccountHolder(accountHolder1);

        // Initialize and start the ATM
        ATM atm = new ATM(bank);
        atm.start();
    }
}