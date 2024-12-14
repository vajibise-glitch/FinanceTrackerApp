import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FinanceTrackerApp extends Application {

    private User currentUser;
    private List<Transaction> transactions = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        TabPane tabPane = new TabPane();

        // Tabs
        Tab userProfileTab = new Tab("User Profile", createUserProfilePane());
        Tab transactionTab = new Tab("Transactions", createTransactionPane());
        Tab reportTab = new Tab("Reports", createReportPane());

        tabPane.getTabs().addAll(userProfileTab, transactionTab, reportTab);

        Scene scene = new Scene(tabPane, 800, 600);
        primaryStage.setTitle("Personal Finance Tracker");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createUserProfilePane() {
        VBox userProfilePane = new VBox(10);

        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First Name");
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Last Name");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        TextField incomeField = new TextField();
        incomeField.setPromptText("Monthly Income");
        TextField savingsField = new TextField();
        savingsField.setPromptText("Savings Goal");

        Button saveButton = new Button("Save Changes");
        saveButton.setOnAction(e -> {
            try {
                currentUser = new User(
                        1,
                        firstNameField.getText(),
                        lastNameField.getText(),
                        emailField.getText(),
                        Double.parseDouble(incomeField.getText()),
                        Double.parseDouble(savingsField.getText())
                );
                System.out.println("User data saved!");
            } catch (NumberFormatException ex) {
                System.out.println("Invalid input: Please enter valid numbers for income and savings.");
            }
        });

        userProfilePane.getChildren().addAll(firstNameField, lastNameField, emailField, incomeField, savingsField, saveButton);
        return userProfilePane;
    }

    private VBox createTransactionPane() {
        VBox transactionPane = new VBox(10);

        TableView<Transaction> transactionTable = new TableView<>();
        TableColumn<Transaction, Integer> idColumn = new TableColumn<>("ID");
        TableColumn<Transaction, LocalDate> dateColumn = new TableColumn<>("Date");
        TableColumn<Transaction, Double> amountColumn = new TableColumn<>("Amount");
        TableColumn<Transaction, String> categoryColumn = new TableColumn<>("Category");
        TableColumn<Transaction, String> typeColumn = new TableColumn<>("Type");
        TableColumn<Transaction, String> descriptionColumn = new TableColumn<>("Description");

        transactionTable.getColumns().addAll(idColumn, dateColumn, amountColumn, categoryColumn, typeColumn, descriptionColumn);

        TextField amountField = new TextField();
        amountField.setPromptText("Amount");
        DatePicker datePicker = new DatePicker();
        TextField categoryField = new TextField();
        categoryField.setPromptText("Category");
        ChoiceBox<String> typeChoice = new ChoiceBox<>();
        typeChoice.getItems().addAll("Income", "Expense");
        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Description");

        Button addTransactionButton = new Button("Add Transaction");
        addTransactionButton.setOnAction(e -> {
            try {
                Transaction transaction = new Transaction(
                        currentUser.getUserID(),
                        currentUser.getFirstName(),
                        currentUser.getLastName(),
                        currentUser.getEmail(),
                        currentUser.getMonthlyIncome(),
                        currentUser.getTargetSavings(),
                        transactions.size() + 1,
                        datePicker.getValue(),
                        Double.parseDouble(amountField.getText()),
                        categoryField.getText(),
                        typeChoice.getValue(),
                        descriptionField.getText()
                );
                transactions.add(transaction);
                transactionTable.getItems().add(transaction);
                System.out.println("Transaction added!");
            } catch (NumberFormatException ex) {
                System.out.println("Invalid input: Please enter a valid amount.");
            } catch (NullPointerException ex) {
                System.out.println("Invalid input: Ensure all fields are filled out correctly.");
            }
        });

        transactionPane.getChildren().addAll(transactionTable, amountField, datePicker, categoryField, typeChoice, descriptionField, addTransactionButton);
        return transactionPane;
    }

    private VBox createReportPane() {
        VBox reportPane = new VBox(10);
        ToggleGroup reportTypeGroup = new ToggleGroup();
        RadioButton monthlyReportButton = new RadioButton("Monthly");
        RadioButton annualReportButton = new RadioButton("Annual");
        monthlyReportButton.setToggleGroup(reportTypeGroup);
        annualReportButton.setToggleGroup(reportTypeGroup);

        Button generateReportButton = new Button("Generate Report");
        generateReportButton.setOnAction(e -> {
            if (monthlyReportButton.isSelected()) {
                System.out.println("Generating Monthly Report...");
            } else if (annualReportButton.isSelected()) {
                System.out.println("Generating Annual Report...");
            }
        });

        reportPane.getChildren().addAll(monthlyReportButton, annualReportButton, generateReportButton);
        return reportPane;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

class User {
    private int userID;
    private String firstName;
    private String lastName;
    private String email;
    private double monthlyIncome;
    private double targetSavings;

    public User(int userID, String firstName, String lastName, String email, double monthlyIncome, double targetSavings) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.monthlyIncome = monthlyIncome;
        this.targetSavings = targetSavings;
    }

    public int getUserID() { return userID; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public double getMonthlyIncome() { return monthlyIncome; }
    public double getTargetSavings() { return targetSavings; }
}

class Transaction extends User {
    private int transactionID;
    private LocalDate date;
    private double amount;
    private String category;
    private String type;
    private String description;

    public Transaction(int userID, String firstName, String lastName, String email, double monthlyIncome, double targetSavings, int transactionID, LocalDate date, double amount, String category, String type, String description) {
        super(userID, firstName, lastName, email, monthlyIncome, targetSavings);
        this.transactionID = transactionID;
        this.date = date;
        this.amount = amount;
        this.category = category;
        this.type = type;
        this.description = description;
    }
}
