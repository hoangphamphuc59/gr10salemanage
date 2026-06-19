import IO.IOHelper;
// import models.Customer;
// import models.Product;
// import models.Transaction;
import services.CustomerManager;
import services.InventoryManager;
import services.ProductManager;
// import services.ReportManager;
import services.TransactionManager;
// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.Scanner;
import ui.ConsoleUi;

public class Main {

    public static void main(String[] args) {
        IOHelper ioHelper = new IOHelper();
        ProductManager productManager = new ProductManager();
        CustomerManager customerManager = new CustomerManager();
        InventoryManager inventoryManager = new InventoryManager();
        TransactionManager transactionManager = new TransactionManager(inventoryManager);
        ConsoleUi ui = new ConsoleUi(ioHelper, productManager, customerManager, transactionManager);

        ui.start();
        ui.mainMenu();

    }

}
