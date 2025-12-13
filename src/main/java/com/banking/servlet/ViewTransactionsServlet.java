package com.banking.servlet;

import com.banking.dao.CustomerDAO;
import com.banking.dao.TransactionDAO;
import com.banking.model.Customer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/user/viewTransactions")
public class ViewTransactionsServlet extends HttpServlet {
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
        
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            customer = customerDAO.getCustomerByUserId((Integer) session.getAttribute("userId"));
            session.setAttribute("customer", customer);
        }
        
        if (customer != null) {
            request.setAttribute("transactions", 
                transactionDAO.getTransactionsByAccountNumber(customer.getAccountNumber()));
        }
        
        request.getRequestDispatcher("/user/viewTransactions.jsp").forward(request, response);
    }
}

