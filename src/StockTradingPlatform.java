import java.util.*;

class Stock {
    private String symbol;
    private double price;

    public Stock(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }

    public String getSymbol() { return symbol; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}

class Transaction {
    private String type; // BUY or SELL
    private Stock stock;
    private int quantity;
    private double totalAmount;

    public Transaction(String type, Stock stock, int quantity) {
        this.type = type;
        this.stock = stock;
        this.quantity = quantity;
        this.totalAmount = stock.getPrice() * quantity;
    }

    public String getType() { return type; }
    public Stock getStock() { return stock; }
    public int getQuantity() { return quantity; }
    public double getTotalAmount() { return totalAmount; }
}

class User {
    private String name;
    private double balance;
    private Map<String, Integer> portfolio;
    private List<Transaction> transactions;

    public User(String name, double balance) {
        this.name = name;
        this.balance = balance;
        this.portfolio = new HashMap<>();
        this.transactions = new ArrayList<>();
    }

    public void buyStock(Stock stock, int quantity) {
        double cost = stock.getPrice() * quantity;
        if (balance >= cost) {
            balance -= cost;
            portfolio.put(stock.getSymbol(), portfolio.getOrDefault(stock.getSymbol(), 0) + quantity);
            transactions.add(new Transaction("BUY", stock, quantity));
            System.out.println("✅ Bought " + quantity + " shares of " + stock.getSymbol());
        } else {
            System.out.println("❌ Not enough balance to buy!");
        }
    }

    public void sellStock(Stock stock, int quantity) {
        if (portfolio.getOrDefault(stock.getSymbol(), 0) >= quantity) {
            double revenue = stock.getPrice() * quantity;
            balance += revenue;
            portfolio.put(stock.getSymbol(), portfolio.get(stock.getSymbol()) - quantity);
            transactions.add(new Transaction("SELL", stock, quantity));
            System.out.println("✅ Sold " + quantity + " shares of " + stock.getSymbol());
        } else {
            System.out.println("❌ Not enough shares to sell!");
        }
    }

    public void showPortfolio(Map<String, Stock> market) {
        System.out.println("\n📊 Portfolio of " + name);
        double totalValue = balance;
        for (String symbol : portfolio.keySet()) {
            int qty = portfolio.get(symbol);
            double stockValue = market.get(symbol).getPrice() * qty;
            totalValue += stockValue;
            System.out.println(symbol + " - " + qty + " shares | Value: ₹" + stockValue);
        }
        System.out.println("💰 Balance: ₹" + balance);
        System.out.println("📈 Total Portfolio Value: ₹" + totalValue);
    }

    public void showTransactions() {
        System.out.println("\n📜 Transaction History:");
        for (Transaction t : transactions) {
            System.out.println(t.getType() + " " + t.getQuantity() + " of " + t.getStock().getSymbol() +
                    " | Amount: ₹" + t.getTotalAmount());
        }
    }
}

public class StockTradingPlatform {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Market setup
        Map<String, Stock> market = new HashMap<>();
        market.put("TCS", new Stock("TCS", 3500));
        market.put("INFY", new Stock("INFY", 1500));
        market.put("HDFC", new Stock("HDFC", 2800));

        // User setup
        User user = new User("Himanshu", 10000);

        int choice;
        do {
            System.out.println("\n=== Stock Trading Platform ===");
            System.out.println("1. View Market");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. View Transactions");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("\n📊 Market Data:");
                    for (Stock s : market.values()) {
                        System.out.println(s.getSymbol() + " - ₹" + s.getPrice());
                    }
                    break;
                case 2:
                    System.out.print("Enter stock symbol: ");
                    String buySymbol = sc.next();
                    System.out.print("Enter quantity: ");
                    int buyQty = sc.nextInt();
                    if (market.containsKey(buySymbol)) {
                        user.buyStock(market.get(buySymbol), buyQty);
                    } else {
                        System.out.println("❌ Stock not found!");
                    }
                    break;
                case 3:
                    System.out.print("Enter stock symbol: ");
                    String sellSymbol = sc.next();
                    System.out.print("Enter quantity: ");
                    int sellQty = sc.nextInt();
                    if (market.containsKey(sellSymbol)) {
                        user.sellStock(market.get(sellSymbol), sellQty);
                    } else {
                        System.out.println("❌ Stock not found!");
                    }
                    break;
                case 4:
                    user.showPortfolio(market);
                    break;
                case 5:
                    user.showTransactions();
                    break;
                case 6:
                    System.out.println("👋 Exiting... Thank you!");
                    break;
                default:
                    System.out.println("❌ Invalid choice!");
            }
        } while (choice != 6);

        sc.close();
    }
}
