package com.banking.servlet;

import com.banking.dao.CustomerDAO;
import com.banking.dao.TransactionDAO;
import com.banking.model.Customer;
import com.banking.model.Transaction;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/admin/manageTransaction")
public class ManageTransactionServlet extends HttpServlet {
    private TransactionDAO transactionDAO = new TransactionDAO();
    private CustomerDAO customerDAO = new CustomerDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"admin".equals(session.getAttribute("userRole"))) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        request.getRequestDispatcher("/admin/manageTransaction.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"admin".equals(session.getAttribute("userRole"))) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        String transactionType = request.getParameter("transactionType");
        String accountNumber = request.getParameter("accountNumber");
        String receiverAccountNumber = request.getParameter("receiverAccountNumber");
        String amountStr = request.getParameter("amount");
        String note = request.getParameter("note");
        
        // Validation
        if (transactionType == null || accountNumber == null || amountStr == null) {
            request.setAttribute("error", "Please fill all required fields");
            request.getRequestDispatcher("/admin/manageTransaction.jsp").forward(request, response);
            return;
        }
        
        double amount;
        try {
            amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                request.setAttribute("error", "Amount must be greater than 0");
                request.getRequestDispatcher("/admin/manageTransaction.jsp").forward(request, response);
                return;
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid amount");
            request.getRequestDispatcher("/admin/manageTransaction.jsp").forward(request, response);
            return;
        }
        
        // Check if account exists
        if (!customerDAO.accountExists(accountNumber)) {
            request.setAttribute("error", "Account not found");
            request.getRequestDispatcher("/admin/manageTransaction.jsp").forward(request, response);
            return;
        }
        
        Transaction transaction = new Transaction();
        transaction.setTransactionType(transactionType);
        transaction.setSenderAccountNumber(accountNumber);
        transaction.setAmount(amount);
        transaction.setNote(note != null ? note : "");
        
        try {
            // Process transaction based on type
            if ("deposit".equals(transactionType)) {
                transaction.setReceiverAccountNumber(null);
                if (transactionDAO.addTransaction(transaction)) {
                    customerDAO.updateBalance(accountNumber, amount);
                    request.setAttribute("success", "Deposit successful!");
                } else {
                    request.setAttribute("error", "Transaction failed");
                }
            } else if ("withdrawal".equals(transactionType)) {
                transaction.setReceiverAccountNumber(null);
                // Check balance
                Customer customer = customerDAO.getCustomerByAccountNumber(accountNumber);
                if (customer != null && customer.getBalance() >= amount) {
                    if (transactionDAO.addTransaction(transaction)) {
                        customerDAO.updateBalance(accountNumber, -amount);
                        request.setAttribute("success", "Withdrawal successful!");
                    } else {
                        request.setAttribute("error", "Transaction failed");
                    }
                } else {
                    request.setAttribute("error", "Insufficient balance");
                }
            } else if ("transfer".equals(transactionType)) {
                if (receiverAccountNumber == null || !customerDAO.accountExists(receiverAccountNumber)) {
                    request.setAttribute("error", "Receiver account not found");
                    request.getRequestDispatcher("/admin/manageTransaction.jsp").forward(request, response);
                    return;
                }
                transaction.setReceiverAccountNumber(receiverAccountNumber);
                // Check balance
                Customer customer = customerDAO.getCustomerByAccountNumber(accountNumber);
                if (customer != null && customer.getBalance() >= amount) {
                    if (transactionDAO.addTransaction(transaction)) {
                        customerDAO.updateBalance(accountNumber, -amount);
                        customerDAO.updateBalance(receiverAccountNumber, amount);
                        request.setAttribute("success", "Transfer successful!");
                    } else {
                        request.setAttribute("error", "Transaction failed");
                    }
                } else {
                    request.setAttribute("error", "Insufficient balance");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Transaction failed: " + e.getMessage());
        }
        
        request.getRequestDispatcher("/admin/manageTransaction.jsp").forward(request, response);
    }
}

