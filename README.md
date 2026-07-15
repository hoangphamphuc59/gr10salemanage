# 🛒 Sale Management System

A **console-based (CLI)** sales management application built with **pure Java** for the **PRO192** course project (Group **GR10**).

The system supports managing products, customers, sales transactions, and revenue reports, with data persisted directly to text files (no database setup required).

---

## 📚 Table of Contents

- [Features](#-features)
- [Project Structure](#-project-structure)
- [Requirements](#-requirements)
- [Installation & Running](#-installation--running)
- [Usage Guide](#-usage-guide)
- [Data Storage](#-data-storage)
- [Contributing](#-contributing)

---

## ✨ Features

### 1. Product Management
- Add a new product
- Update product information
- Remove a product
- View all products
- Search products by name or category

### 2. Customer Management
- Add a new customer (supports **Regular** and **VIP** customer types)
- Update customer information
- Remove a customer
- View all customers

### 3. Sales Management
- Create a new sales transaction, automatically checking and deducting stock
- Update or cancel a transaction
- Confirm a transaction
- View transaction history

### 4. Reporting
- Generate a daily sales report
- Generate a monthly sales report
- List best-selling products
- List customers with the highest purchase value

### 5. Intuitive CLI Interface
- A clean command-line interface with ANSI color codes: success messages are shown in **green**, while errors and validation warnings are shown in **red**, making them easy to spot.

---

## 📁 Project Structure

```text
📁 SaleManagement
├── 📁 data                               (Stores serialized data)
│   ├── 📄 customer.txt
│   ├── 📄 product.txt
│   └── 📄 transaction.txt
├── 📁 src
│   ├── 📄 Main.java                      (Application entry point)
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
│   │       ├── 📄 StringUtils.java
│   │       └── 📄 Validator.java
│   └── 📁 ui
│       ├── 📄 ConsoleColor.java          (Utility for colored CLI output)
│       └── 📄 ConsoleUi.java             (Main user interface / menus)
└── 📄 README.md
```

---

## 💻 Requirements

- **JDK 8** (required — the project targets JDK 8 compatibility)
- **NetBeans 13** (recommended IDE — this project was originally built and is best supported with NetBeans)
- Alternatively, other common Java IDEs can also be used:
  - **IntelliJ IDEA** (the project already includes `.idea` config)
  - **VS Code** with the Java Extension Pack
  - Or any other Java IDE of your choice
- A terminal that supports ANSI color codes for the best display experience (macOS/Linux terminal, or modern Windows Terminal / CMD on Windows)

---

## 🚀 Installation & Running

### Option 1: Using NetBeans (recommended)
1. Clone the repository:
   ```bash
   git clone https://github.com/hoangphamphuc59/gr10salemanage.git
   ```
2. Open NetBeans (version 13 or later), choose **File → Open Project**, and select the `SaleManagement` folder.
3. Right-click the project and choose **Run**, or locate `Main.java` and run it directly.

### Option 2: Using IntelliJ IDEA (alternative)
1. Clone the repository:
   ```bash
   git clone https://github.com/hoangphamphuc59/gr10salemanage.git
   ```
2. Open IntelliJ IDEA, choose **Open**, and select the `gr10salemanage` folder.
3. Wait for the IDE to finish indexing, then locate `SaleManagement/src/Main.java`.
4. Click **Run** (▶) to launch the application.

### Option 3: Compile & run from the command line
```bash
# 1. Clone the repository
git clone https://github.com/hoangphamphuc59/gr10salemanage.git
cd gr10salemanage/SaleManagement

# 2. Compile the source code
javac -d out -encoding UTF-8 $(find src -name "*.java")

# 3. Run the program
java -cp out Main
```

> 💡 Note: run the program from the `SaleManagement` directory so it can correctly read/write the `data/` folder.

---

## 📖 Usage Guide

Once launched, the program displays the main menu:

```
========== SALE MANAGEMENT SYSTEM ==========
1. Product Management
2. Customer Management
3. Sales Management
4. Reporting
0. Exit
```

Enter the corresponding number to access each module:

| Option | Description |
|--------|-------------|
| 1 | Product Management: add / update / remove / view / search |
| 2 | Customer Management: add / update / remove / view |
| 3 | Sales Management: create / update / cancel / confirm transactions, view history |
| 4 | Reporting: daily revenue, monthly revenue, best-selling products, top customers |
| 0 | Exit the program (data is automatically saved) |

---

## 💾 Data Storage

All data is stored as plain text files inside `SaleManagement/data/`:

| File | Content |
|------|---------|
| `product.txt` | List of products |
| `customer.txt` | List of customers |
| `transaction.txt` | Sales transaction history |

Data is loaded when the program starts and automatically saved whenever changes occur or when the program exits.

---

## 🤝 Contributing

This is an academic project for the **PRO192** course — Group **GR10**. Feedback, bug reports (issues), and pull requests are all welcome.

1. Fork the repository
2. Create a new branch (`git checkout -b feature/your-feature-name`)
3. Commit your changes (`git commit -m "Add ..."`)
4. Push the branch (`git push origin feature/your-feature-name`)
5. Open a Pull Request

---

## 📝 License

This project was built for educational purposes and does not currently use a specific open-source license.
