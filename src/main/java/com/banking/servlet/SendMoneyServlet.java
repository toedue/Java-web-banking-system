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

@WebServlet("/user/sendMoney")
public class SendMoneyServlet extends HttpServlet {
    private TransactionDAO transactionDAO = new TransactionDAO();
    private CustomerDAO customerDAO = new CustomerDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"user".equals(session.getAttribute("userRole"))) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        request.getRequestDispatcher("/user/sendMoney.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"user".equals(session.getAttribute("userRole"))) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        Customer sender = (Customer) session.getAttribute("customer");
        if (sender == null) {
            sender = customerDAO.getCustomerByUserId((Integer) session.getAttribute("userId"));
            session.setAttribute("customer", sender);
        }
        
        String receiverAccountNumber = request.getParameter("receiverAccountNumber");
        String amountStr = request.getParameter("amount");
        String note = request.getParameter("note");
        
        // Validation
        if (receiverAccountNumber == null || receiverAccountNumber.trim().isEmpty() ||
            amountStr == null || amountStr.trim().isEmpty()) {
            request.setAttribute("error", "Please fill all required fields");
            request.getRequestDispatcher("/user/sendMoney.jsp").forward(request, response);
            return;
        }
        
        double amount;
        try {
            amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                request.setAttribute("error", "Amount must be greater than 0");
                request.getRequestDispatcher("/user/sendMoney.jsp").forward(request, response);
                return;
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid amount");
            request.getRequestDispatcher("/user/sendMoney.jsp").forward(request, response);
            return;
        }
        
        // Check if receiver account exists
        if (!customerDAO.accountExists(receiverAccountNumber)) {
            request.setAttribute("error", "Receiver account not found");
            request.getRequestDispatcher("/user/sendMoney.jsp").forward(request, response);
            return;
        }
        
        // Check if sender has sufficient balance
        if (sender.getBalance() < amount) {
            request.setAttribute("error", "Insufficient balance");
            request.getRequestDispatcher("/user/sendMoney.jsp").forward(request, response);
            return;
        }
        
        // Create transaction
        Transaction transaction = new Transaction();
        transaction.setTransactionType("transfer");
        transaction.setSenderAccountNumber(sender.getAccountNumber());
        transaction.setReceiverAccountNumber(receiverAccountNumber);
        transaction.setAmount(amount);
        transaction.setNote(note != null ? note : "");
        
        try {
            if (transactionDAO.addTransaction(transaction)) {
                // Update balances
                customerDAO.updateBalance(sender.getAccountNumber(), -amount);
                customerDAO.updateBalance(receiverAccountNumber, amount);
                
                // Update session customer
                Customer updatedSender = customerDAO.getCustomerByAccountNumber(sender.getAccountNumber());
                session.setAttribute("customer", updatedSender);
                
                request.setAttribute("success", "Money sent successfully!");
            } else {
                request.setAttribute("error", "Transaction failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Transaction failed: " + e.getMessage());
        }
        
        request.getRequestDispatcher("/user/sendMoney.jsp").forward(request, response);
    }
}

