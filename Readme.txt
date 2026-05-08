# SaveIt - Personal Budget Manager  
**by Savana Team**

---

## 1. Project Overview

SaveIt is a personal budget management application designed to help users take control of their finances. It works like a smart digital piggy bank that tracks expenses and warns users before they overspend.

### How it works
Users start by creating an account or logging in. After that, they can:

- Add expenses with amount, category, and date  
- Set budget cycles (e.g., ₹10,000 from April 1 to April 30)  
- Track remaining balance in real time  
- View daily spending limits  
- Visualize spending using charts  
- Receive warnings when approaching or exceeding the budget  
- Generate weekly or monthly financial reports  

---

## 2. OOP Concepts Used

### Encapsulation
All class fields are private. Access is controlled using getters and setters to ensure data safety and consistency.  
Example: `User` class hides sensitive data like id, name, and username.

---

### Inheritance
Common behaviors are shared using inheritance:

- All DAO classes implement `DAO<T>` interface  
- All controllers extend an abstract `Controller` class  

This enforces structure and code reuse.

---

### Polymorphism
The system can treat different objects uniformly:

- `DAO<T>` works with multiple implementations like `ExpenseDAO` or `CategoryDAO`  
- Controllers can be switched dynamically (LoginController, MainController, etc.)

---

### Abstraction
Implementation details are hidden behind interfaces:

- `DAO<T>` hides database logic (MySQL, SQLite, etc.)  
- Controllers focus only on UI logic without worrying about navigation complexity  

---

## 3. Design Patterns

### Facade Pattern — BudgetManager
BudgetManager acts as a single entry point for the entire budgeting system.

**Problem solved:**
UI controllers don’t need to interact with multiple services directly.

**Result:**
It simplifies interaction with:
- CycleManager  
- ExpenseService  
- BudgetCalculator  
- BudgetNotifier  

---

### Singleton Pattern — DatabaseConnection & SceneController

Ensures only one instance exists for critical components.

**Why:**
- Prevent multiple database connections  
- Maintain single navigation controller  

**Implementation:**
- Private constructor  
- Static `getInstance()` method  

---

## 4. SOLID Principles

### Single Responsibility Principle
Each class has one responsibility:
- ExpenseDAO → database operations  
- BudgetCalculator → calculations  
- ReportGenerator → report creation  

---

### Open/Closed Principle
Code is extendable without modifying existing logic:
- New DAOs can be added without changing old ones  

---

### Liskov Substitution Principle
Child classes can replace parent classes without breaking functionality:
- Any Controller works wherever `Controller` is expected  

---

### Interface Segregation Principle
Interfaces are small and specific:
- `DAO<T>` only includes essential methods  

---

### Dependency Inversion Principle
High-level modules depend on abstractions, not implementations:
- Services depend on DAO interfaces, not concrete classes  

---

## 5. Project Structure

### com.saveit.model
Contains data models:
- User  
- Expense  
- Category  
- Cycle  

---

### com.saveit.dao
Handles database operations:
- DAO<T> interface  
- DatabaseConnection (Singleton)  
- UserDAO, ExpenseDAO, CategoryDAO, CycleDAO  

---

### com.saveit.service
Contains business logic:
- BudgetManager (Facade)  
- AuthenticationService  
- BudgetCalculator  
- ExpenseService  
- ReportGenerator  

---

### com.saveit.controller
Handles UI logic (JavaFX):
- LoginController  
- MainController  
- ExpenseLogController  
- AddExpenseController  
- BudgetSetupController  
- ChartController  
- SideBarController  

---

## 6. Maintainability

### Strengths
- Clear package separation  
- Clean use of design patterns  
- DAO abstraction isolates database logic  
- Facade simplifies UI complexity  

### Weaknesses
- BudgetManager is slightly large  
- Some duplicated logic exists (e.g., `sum_of_transactions`)  

---

## 7. Future Improvements

### Remove duplication
Move shared logic into utility classes like `CalculationHelper`.

---

### Refactor BudgetManager
Split into smaller facades:
- ExpenseFacade  
- CycleFacade  
- CategoryFacade  

---

### Add logging
Replace `System.out.println()` with proper logging frameworks.

---

### Add unit testing
Use JUnit for:
- BudgetCalculator  
- AuthenticationService  
- DAO classes  

---

### Use Dependency Injection
Replace direct `new` usage with constructor injection for better testing.

---

### Improve naming
Use clearer method names:
- `sum_of_transactions()` → `calculateTotalSpending()`  
- `resolveID()` → `findCategoryById()`  

---

## Final Note

SaveIt demonstrates real-world Java architecture using JavaFX, database integration, and clean design principles.

Key takeaways:
- Facade simplifies complex systems  
- DAO abstracts database logic  
- SOLID principles improve scalability and maintainability  
