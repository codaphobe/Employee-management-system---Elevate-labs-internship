# 🧾 Employee Management System (Java + MySQL)

## 📌 Objective
The **Employee Management System (EMS)** is a Java console-based application that allows managing employee data efficiently using a **MySQL** database.  
It demonstrates the use of **Object-Oriented Programming (OOP)** principles and **JDBC** for database connectivity.

The project performs CRUD operations along with **soft delete** functionalities.

---

## ⚙️ Implementation Details

### 🧱 Project Structure
| Component             | Description |
|-----------------------|-------------|
| **Employee.java**     | Represents the employee entity with fields like ID, name, department, and salary. |
| **EmployeeDAO.java**  | Handles all database operations — insert, update, delete (soft delete), retrieve, and fetch by ID. |
| **DBConnection.java** | Provides a reusable JDBC connection to the MySQL database. |
| **Main.java**         | The main driver class that contains the CLI and menu logic. |

---

### 🪟 Features Implemented
- **Add Employee** – Inserts a new employee record into the database.
- **View All Employees** – Displays all active employees.
- **Update Employee** – Updates details of an existing employee by ID.
- **Soft Delete Employee** – Marks an employee as terminated instead of permanently removing them (`status = terminated`).
- **Get Employee By ID** – Retrieves specific employee details using their unique ID.

---

## 🗄️ Database Schema

**Database Name:** `employee_db`  
**Table Name:** `employee`

```sql
CREATE TABLE employees (
  id INT AUTO_INCREMENT PRIMARY KEY,
  first_name VARCHAR(50),
  last_name VARCHAR(50),
  email VARCHAR(100) UNIQUE,
  phone VARCHAR(15),
  department ENUM('Engineering','HR','Finance','Sales','Marketing','Operations'),
  salary DECIMAL(10,2),
  joining_date DATE,
  status ENUM('Active','On Leave','Resigned','Terminated') DEFAULT 'Active',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

---

## 💻 Tech Stack
- **Language:** Java
- **Database:** MySQL
- **Driver:** MySQL JDBC Connector
- **IDE:** IntelliJ IDEA 

---

## 🧩 How It Works
1. The app connects to MySQL using JDBC.
2. The user is presented with a CLI menu to choose operations.
3. Each operation calls the appropriate DAO method.
4. Soft-deleted employees remain hidden from regular listings.
---

## 🚀 Example Flow
```
1. Add Employee
2. View All Employees
3. Update Employee
4. Delete Employee
5. Get Employee by ID
6. Exit
```

---

✅ **This project demonstrates clean modular Java code with database persistence using JDBC.**
