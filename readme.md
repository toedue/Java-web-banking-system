A web-based banking system where:
Users can create accounts, log in, view balances, send money, and view transaction history
Admins can manage customers (add, edit, delete) and view all transactions
Data is stored in a MySQL database

demo/
├── database/                    # Database setup files
│   ├── schema.sql              # Creates tables (users, customers, transactions)
│   └── setup.sql               # Same as schema.sql (alternative setup)
│
├── src/main/
│   ├── java/com/banking/       # All Java code lives here
│   │   ├── model/              # Data containers (like boxes for information)
│   │   │   ├── User.java       # Represents a user account
│   │   │   ├── Customer.java   # Represents a bank customer
│   │   │   └── Transaction.java # Represents a money transaction
│   │   │
│   │   ├── dao/                # Database Access Objects (talk to database)
│   │   │   ├── UserDAO.java    # Handles user database operations
│   │   │   ├── CustomerDAO.java # Handles customer database operations
│   │   │   └── TransactionDAO.java # Handles transaction database operations
│   │   │
│   │   ├── servlet/            # Request handlers (like waiters in a restaurant)
│   │   │   ├── LoginServlet.java
│   │   │   ├── RegisterServlet.java
│   │   │   ├── SendMoneyServlet.java
│   │   │   └── ... (many more)
│   │   │
│   │   └── util/               # Helper tools
│   │       ├── DatabaseConnection.java # Connects to MySQL
│   │       └── PasswordUtil.java       # Hashes passwords
│   │
│   └── webapp/                 # Web pages (what users see)
│       ├── index.jsp           # Home page (redirects to login)
│       ├── login.jsp           # Login page
│       ├── register.jsp        # Registration page
│       ├── user/               # Pages for regular users
│       │   ├── dashboard.jsp
│       │   ├── sendMoney.jsp
│       │   └── ...
│       ├── admin/              # Pages for admins
│       │   ├── dashboard.jsp
│       │   ├── addCustomer.jsp
│       │   └── ...
│       └── WEB-INF/
│           └── web.xml         # Configuration file
│
└── pom.xml                     # Maven configuration (lists all libraries needed)