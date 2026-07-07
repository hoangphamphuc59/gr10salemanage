import IO.IOHelper;
// import models.Customer;
// import models.Product;
// import models.Transaction;
// import services.ReportManager;
// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.Scanner;
import ui.ConsoleUi;
import services.*;
import services.Validation.*;
public class Main {

    public static void main(String[] args) {
        IOHelper ioHelper = new IOHelper();
        ProductManager productManager = new ProductManager();
        CustomerManager customerManager = new CustomerManager();
        InventoryManager inventoryManager = new InventoryManager();
        TransactionManager transactionManager = new TransactionManager(inventoryManager);
        CustomerValidation customerValidation = new CustomerValidation();
        ProductValidation productValidation = new ProductValidation(productManager);
        ConsoleUi ui = new ConsoleUi(ioHelper, productManager, 
            customerManager, transactionManager, customerValidation, productValidation);

        ui.start();
        ui.mainMenu();

    }

}
