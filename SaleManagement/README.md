## 📁 Project Structure
```text
📁 SaleManagement
├── 📁 data                               (Stores serialized data)
│   ├── 📄 customer.txt
│   ├── 📄 product.txt
│   └── 📄 transaction.txt
├── 📁 src
│   ├── 📄 Main.java                      (Application Entry Point)
│   ├── 📁 IO
│   │   └── 📄 IOHelper.java              (File reading and writing)
│   ├── 📁 models
│   │   ├── 📄 Customer.java              (Base customer class)
│   │   ├── 📄 Product.java
│   │   ├── 📄 RegularCustomer.java
│   │   ├── 📄 Transaction.java
│   │   └── 📄 VipCustomer.java
│   ├── 📁 services
│   │   ├── 📄 CustomerManager.java
│   │   ├── 📄 InventoryManager.java      (Handles stock checking and deductions)
│   │   ├── 📄 ProductManager.java
│   │   ├── 📄 ReportManager.java
│   │   ├── 📄 TransactionManager.java
│   │   └── 📁 Validation
│   │       ├── 📄 CustomerValidation.java
│   │       ├── 📄 ProductValidation.java
│   │       ├── 📄 StringUtils.java
│   │       └── 📄 Validator.java
│   └── 📁 ui
│       ├── 📄 ConsoleColor.java          (Utility for colored CLI output)
│       └── 📄 ConsoleUi.java             (Main user interface menus)
└── 📄 README.md
```

## ✨ Features

- **CLI Coloring:** Enjoy a clear and intuitive command-line interface with ANSI color codes. Success messages are highlighted in green, while error and validation messages are displayed in red to quickly catch your attention.
