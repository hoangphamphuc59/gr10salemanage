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
│       ├── 📁 services
|       |   ├── 📁 Validator
|       |       |──📄 Validator.java
|       |       |──📄 StringUtils.java
|       |       |──📄 CustomerValidation.java
│       │   ├── 📄 ProductManager.java
│       │   ├── 📄 CustomerManager.java
│       │   ├── 📄 TransactionManager.java
│       │   └── 📄 ReportManager.java
│       ├── 📁 ui
│       │   └── 📄 ConsoleUI.java
│       └── 📁 IO
│           ├── 📄 FileIOHelper.java
├── 📁 data                               
│   ├── 📄 products.txt
│   ├── 📄 customers.txt
│   └── 📄 transactions.txt
└── 📄 README.md
