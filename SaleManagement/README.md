## 📁 Project Structure
```text
📁 SalesManagementSystem
├── 📁 src
│   └──├── 📄 Main.java
│       ├── 📁 models
│       │   ├── 📄 Product.java
│       │   ├── 📄 Customer.java          (abstract - base class)
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
├── 📁 data                               (BR12: lưu/load dữ liệu)
│   ├── 📄 products.txt
│   ├── 📄 customers.txt
│   └── 📄 transactions.txt
├── 📁 reports                            (Reporting requirements)
│   └── 📄 report_YYYYMMDD.txt
└── 📄 README.md