## 📁 Project Structure
```text
📁 SalesManagementSystem
├── 📁 src
│   └──├── 📄 Main.java
│       ├── 📁 models
│       │   ├── 📄 Product.java
│       │   ├── 📄 Customer.java          
│       │   ├── 📄 RegularCustomer.java
│       │   ├── 📄 VIPCustomer.java
│       │   ├── 📄 Transaction.java
│       │   └── 📄 TransactionDetail.java
│       ├── 📁 services
│       │   ├── 📄 ProductManager.java
│       │   ├── 📄 CustomerManager.java
│       │   ├── 📄 TransactionManager.java
│       │   └── 📄 ReportManager.java
│       ├── 📁 ui
│       │   └── 📄 ConsoleUI.java
│       └── 📁 utils
│           ├── 📄 FileIOHelper.java
│           ├── 📄 ValidationHelper.java
│           └── 📄 SalesException.java
├── 📁 data                               
│   ├── 📄 products.txt
│   ├── 📄 customers.txt
│   └── 📄 transactions.txt
└── 📄 README.md
