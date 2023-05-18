/*@author Mark Tabak
 */
package coe528.project;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SelectScreen extends Application {

  Scene CustomerStartScreen, CustomerCostScreen, OwnerStartScreen, OwnerBooksScreen, OwnerCustomersScreen, LoginScreen;
  Stage screen;
  double TotalCost;
  TableView < Book > table;
  ObservableList < Book > SelectedBookss;
  ObservableList < Book > listofBooks = FXCollections.observableArrayList();
  ObservableList < Customer > listofCustomers = FXCollections.observableArrayList();

  //Writes customers to a file
  private static void writeCustomers(String filename, ObservableList < Customer > listofCustomers) throws IOException {

    FileWriter writeCustomer = new FileWriter(filename, true);
    for (Customer customer: listofCustomers) {
      writeCustomer.write(customer.getUsername() + "," + customer.getPassword() + "," + customer.getPoints() + "\n");
    }
    writeCustomer.close();
  }

  //Reads customers to a file
  private static List < Customer > readCustomers(String filename) throws IOException {

    List < Customer > customers = new ArrayList < > ();
    BufferedReader reader = Files.newBufferedReader(Paths.get(filename));
    String line;
    while ((line = reader.readLine()) != null) {
      String[] comma = line.split(",");
      String username = comma[0];
      String password = comma[1];
      double points = Double.parseDouble(comma[2]);
      customers.add(new Customer(username, password, points));
    }
    return customers;
  }

  //Writes books to a file
  private static void writeBooks(String filename, ObservableList < Book > listofBooks) throws IOException {

    FileWriter writeBook = new FileWriter(filename, true);
    for (Book book: listofBooks) {
      writeBook.write(book.getName() + "," + book.getPrice() + "\n");
    }
    writeBook.close();
  }

  //Reads list of books from file
  private static List < Book > readBooks(String filename) throws IOException {

    List < Book > books = new ArrayList < > ();
    BufferedReader reader = Files.newBufferedReader(Paths.get(filename));
    String line;
    while ((line = reader.readLine()) != null) {
      String[] comma = line.split(",");
      String name = comma[0];
      double price = Double.parseDouble(comma[1]);
      books.add(new Book(name, price));
    }
    return books;
  }

  //Loads book information from file
  public ObservableList < Book > getBook() {

    ObservableList < Book > loadBooks = FXCollections.observableArrayList();
    List < Book > loadbook = null;
    try {
      loadbook = readBooks("books.txt");
    } catch (IOException a) {
      a.printStackTrace();
    }
    for (int i = 0; i < loadbook.size(); i++) {
      loadBooks.add(new Book(loadbook.get(i).getName(), loadbook.get(i).getPrice()));
    }
    return loadBooks;
  }

  //Loads customer information from file
  public ObservableList < Customer > getCustomer() {

    ObservableList < Customer > loadCustomers = FXCollections.observableArrayList();
    List < Customer > loadCustomer = null;
    try {
      loadCustomer = readCustomers("customers.txt");
    } catch (IOException a) {
      a.printStackTrace();
    }
    for (int i = 0; i < loadCustomer.size(); i++) {
      loadCustomers.add(new Customer(loadCustomer.get(i).getUsername(), loadCustomer.get(i).getPassword(), loadCustomer.get(i).getPoints()));
    }
    return loadCustomers;
  }

  //Displays total cost as a string
  public String TotalCostString(double TotalCost) {

    return "Total Cost: " + TotalCost;
  }

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {

    //LOGIN SCREEN

    screen = primaryStage;
    TextField textField = new TextField();
    PasswordField pwdField = new PasswordField();
    Button login = new Button("LOGIN");
    login.setTranslateX(250);
    login.setTranslateY(75);

    Label label1 = new Label("Username: ");
    Label label2 = new Label("Password: ");

    //Validates Login Information
    login.setOnAction(e -> {

      if (textField.getText().equals("admin") && (pwdField.getText()).equals("admin")) {
        SetOwnerStartScreen();
        screen.setScene(OwnerStartScreen);
      }
      List < Customer > loginCustomer = null;
      try {
        loginCustomer = readCustomers("customers.txt");
      } catch (IOException a) {
        a.printStackTrace();
      }
      for (int i = 0; i < loginCustomer.size(); i++) {
        if (textField.getText().equals(loginCustomer.get(i).getUsername()) && (pwdField.getText()).equals(loginCustomer.get(i).getPassword())) {
          setCustomerStartScreen(loginCustomer.get(i));
          screen.setScene(CustomerStartScreen);
        }
      }

    });

    Text text = new Text("");
    Font font = Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 10);
    text.setFont(font);
    text.setTranslateX(15);
    text.setTranslateY(125);
    text.setFill(Color.BLACK);

    HBox box = new HBox(5);
    box.setPadding(new Insets(25, 5, 5, 50));
    box.getChildren().addAll(label1, textField, label2, pwdField);
    Group root = new Group(box, login, text);

    LoginScreen = new Scene(root, 595, 150, Color.WHITESMOKE);

    primaryStage.setTitle("Bookstore Application");
    primaryStage.setScene(LoginScreen);
    primaryStage.show();

  }

  //CUSTOMER START SCREEN  
  public void setCustomerStartScreen(Customer A) {
    TableColumn < Book, String > namecolumn = new TableColumn < > ("Name");
    namecolumn.setMinWidth(200);
    namecolumn.setCellValueFactory(new PropertyValueFactory < > ("name"));

    TableColumn < Book, Double > pricecolumn = new TableColumn < > ("Price");
    pricecolumn.setMinWidth(200);
    pricecolumn.setCellValueFactory(new PropertyValueFactory < > ("price"));

    table = new TableView < > ();
    table.setItems(getBook());
    table.getColumns().addAll(namecolumn, pricecolumn);

    table.setEditable(true);
    table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    table.getSelectionModel().setCellSelectionEnabled(true);

    //Select a book, hold ctrl to select multiple books
    Button select = new Button();
    select.setText("Add to cart");
    select.setOnAction((ActionEvent Select) -> {

      SelectedBookss = table.getSelectionModel().getSelectedItems();
      for (int i = 0; i < SelectedBookss.size(); i++) {
        System.out.println(SelectedBookss.get(i).getName());
      }
    });

    //Buy a book
    Button buy = new Button();
    buy.setText("Buy");
    buy.setOnAction((ActionEvent Buy) -> {

      TotalCost = 0;
      A.Buy(SelectedBookss);
      for (int l = 0; l < SelectedBookss.size(); l++) {
        TotalCost += SelectedBookss.get(l).getPrice();
      }
      SetCustomerCostScreen(A, TotalCost);
      screen.setScene(CustomerCostScreen);
    });

    //Buy a book with points
    Button redeem = new Button();
    redeem.setText("Redeem Points and Buy");
    redeem.setOnAction((ActionEvent Redeem) -> {
      TotalCost = 0;
      double updatedCost;
      A.Redeem(SelectedBookss);
      for (int l = 0; l < SelectedBookss.size(); l++) {
        TotalCost += SelectedBookss.get(l).getPrice();
      }
      updatedCost = A.updateTC(A.getPoints(), TotalCost);
      SetCustomerCostScreen(A, updatedCost);
      screen.setScene(CustomerCostScreen);
    });

    //Logout
    Button logout = new Button();
    logout.setText("Logout");
    logout.setOnAction((ActionEvent Logout) -> {

      screen.setScene(LoginScreen);
    });

    VBox vboxCustomerStart = new VBox(20);
    vboxCustomerStart.setPadding(new Insets(10, 0, 10, 0));
    CustomerStartScreen = new Scene(vboxCustomerStart, 700, 520);

    Text Welcome = new Text(A.PrintWelcomeMessage());
    Welcome.setFont(Font.font("Arial", FontWeight.NORMAL, 20));

    HBox hboxCustomerStart = new HBox(100);
    hboxCustomerStart.setAlignment(Pos.BOTTOM_CENTER);
    hboxCustomerStart.getChildren().addAll(buy, redeem, select, logout);

    vboxCustomerStart.getChildren().addAll(Welcome, table, hboxCustomerStart);

  }

  //CUSTOMER COST SCREEN

  public void SetCustomerCostScreen(Customer A, double TotalCost) {

    VBox vboxCustomerCost = new VBox(20);
    vboxCustomerCost.setPadding(new Insets(50, 20, 50, 20));
    CustomerCostScreen = new Scene(vboxCustomerCost, 500, 220);
    Text Total = new Text(TotalCostString(TotalCost));
    Total.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
    Text Update = new Text("Updated Points: " + A.getPoints() + "       Updated Status: " + A.getStatus());
    Update.setFont(Font.font("Arial", FontWeight.NORMAL, 20));

    //Logout
    Button logout = new Button();
    logout.setText("Logout");
    logout.setOnAction((ActionEvent Logout) -> {

      screen.setScene(LoginScreen);
    });

    HBox hboxCustomerCost = new HBox(100);
    hboxCustomerCost.setAlignment(Pos.BOTTOM_CENTER);
    hboxCustomerCost.getChildren().add(logout);
    vboxCustomerCost.getChildren().addAll(Total, Update, hboxCustomerCost);

  }

  public void SetOwnerStartScreen() {

    Label label1 = new Label("Welcome!\nYou are logged in as owner.");
    Button button1 = new Button("Books");
    Button button2 = new Button("Customers");
    Button logout = new Button("Logout");

    button1.setOnAction(e -> {

      SetOwnerBooksScreen();
      screen.setScene(OwnerBooksScreen);
    });

    button2.setOnAction(e -> {

      SetOwnerCustomerScreen();
      screen.setScene(OwnerCustomersScreen);
    });

    logout.setOnAction(e -> {
      screen.setScene(LoginScreen);
    });

    //layout 1 - owner start screen
    VBox layout1 = new VBox(40);
    layout1.setAlignment(Pos.CENTER);
    layout1.getChildren().addAll(label1, button1, button2, logout);
    OwnerStartScreen = new Scene(layout1, 500, 500);

  }

  public void SetOwnerCustomerScreen() {

    TableView < Customer > table = new TableView < Customer > ();

    TableColumn < Customer, String > usernameColumn = new TableColumn < > ("Username");
    usernameColumn.setCellValueFactory(new PropertyValueFactory < > ("Username"));

    TableColumn < Customer, String > passwordColumn = new TableColumn < > ("Password");
    passwordColumn.setCellValueFactory(new PropertyValueFactory < > ("Password"));

    TableColumn < Customer, String > pointsColumn = new TableColumn < > ("Points");
    pointsColumn.setCellValueFactory(new PropertyValueFactory < > ("Points"));

    table.getColumns().addAll(usernameColumn, passwordColumn, pointsColumn);

    table.getItems().addAll(getCustomer());

    Label username = new Label("Username: ");
    Label password = new Label("Password: ");

    TextField usernameInput = new TextField();
    usernameInput.setPromptText("Username");
    usernameInput.setMinWidth(200);

    TextField passwordInput = new TextField();
    passwordInput.setPromptText("Password");
    passwordInput.setMinWidth(200);

    // add customer button functionality
    Button Add = new Button("Add");
    Add.setOnAction(e -> {
      Customer customer = new Customer();
      customer.setUsername(usernameInput.getText());
      customer.setPassword(passwordInput.getText());
      table.getItems().add(customer);
      usernameInput.clear();
      passwordInput.clear();
    });

    // delete customer button functionality
    Button Delete = new Button("Delete");
    Delete.setOnAction(e -> {

      Customer selectedCustomer = table.getSelectionModel().getSelectedItem();
      table.getItems().remove(selectedCustomer);
    });

    // back button functionality
    Button Back = new Button("Back");
    Back.setOnAction(e -> {
      screen.setScene(OwnerStartScreen);
    });
    Button save = new Button("Save");
    save.setOnAction(e -> {

      listofCustomers.addAll(table.getItems());

      try {
        writeCustomers("customers.txt", listofCustomers);
      } catch (IOException a) {
        a.printStackTrace();
      }

      List < Customer > inputCustomers = null;
      try {
        inputCustomers = readCustomers("customers.txt");
      } catch (IOException a) {
        a.printStackTrace();

        screen.setScene(LoginScreen);
      }

    });

    // layouts for the buttons
    HBox hBoxaddCustomer = new HBox(15);
    hBoxaddCustomer.setAlignment(Pos.CENTER);
    hBoxaddCustomer.getChildren().addAll(username, usernameInput, password, passwordInput, Add);
    HBox hBoxdeleteCustomer = new HBox(100);
    hBoxdeleteCustomer.setAlignment(Pos.BOTTOM_CENTER);
    hBoxdeleteCustomer.getChildren().addAll(Delete, Back, save);

    VBox ownerCustomerScreenBox = new VBox(20);

    ownerCustomerScreenBox.getChildren().addAll(table, hBoxaddCustomer, hBoxdeleteCustomer);

    OwnerCustomersScreen = new Scene(ownerCustomerScreenBox, 700, 500);

  }

  public void SetOwnerBooksScreen() {

    TableView < Book > tableView = new TableView < Book > ();

    TableColumn < Book, String > bookName = new TableColumn < > ("Name of Book");
    bookName.setCellValueFactory(new PropertyValueFactory < > ("name"));

    TableColumn < Book, Double > bookPrice = new TableColumn < > ("Price");
    bookPrice.setCellValueFactory(new PropertyValueFactory < > ("price"));

    tableView.getColumns().add(bookName);
    tableView.getColumns().add(bookPrice);
    tableView.getItems().addAll(getBook());

    // adding labels
    Label name = new Label("Book Name: ");
    Label price = new Label("Book Price:");

    // adding the textbox
    TextField bookNameBox = new TextField();
    bookNameBox.setPromptText("book name");
    bookNameBox.setMinWidth(100);

    TextField priceBox = new TextField();
    priceBox.setPromptText("0.00");
    bookNameBox.setMinWidth(100);

    // adding buttons and functionalities
    Button Add = new Button("Add");
    Add.setOnAction(e -> {
      tableView.getItems().add(new Book(bookNameBox.getText(), Double.parseDouble(priceBox.getText())));

      bookNameBox.clear();
      priceBox.clear();
    });

    //Implementing the Delete Functionality
    Button Delete = new Button("Delete");
    Delete.setOnAction(e -> {
      Book selectedBook = tableView.getSelectionModel().getSelectedItem();
      tableView.getItems().remove(selectedBook);
    });

    //save button
    Button save = new Button("Save");
    save.setOnAction(e -> {

      listofBooks.addAll(tableView.getItems());
      try {
        writeBooks("books.txt", listofBooks);
      } catch (IOException a) {
        a.printStackTrace();
      }

      List < Book > inputBooks = null;
      try {
        inputBooks = readBooks("books.txt");
      } catch (IOException a) {
        a.printStackTrace();
      }

    });

    //Back Button functionality
    Button Back = new Button("Back");
    Back.setOnAction(e -> {
      screen.setScene(OwnerStartScreen);
    });

    // adding a layout for the buttons
    HBox hBoxaddBook = new HBox(15);
    hBoxaddBook.setAlignment(Pos.CENTER);
    hBoxaddBook.getChildren().addAll(name, bookNameBox, price, priceBox, Add);
    HBox h1boxaddBook = new HBox(100);
    h1boxaddBook.setAlignment(Pos.BOTTOM_CENTER);
    h1boxaddBook.getChildren().addAll(Delete, Back, save);

    VBox bookStoreScreenBox = new VBox(20);

    bookStoreScreenBox.getChildren().addAll(tableView, hBoxaddBook, h1boxaddBook);

    OwnerBooksScreen = new Scene(bookStoreScreenBox, 700, 500);

  }

}