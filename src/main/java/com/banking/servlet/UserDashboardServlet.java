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

@WebServlet("/user/dashboard")
public class UserDashboardServlet extends HttpServlet {
    private CustomerDAO customerDAO = new CustomerDAO();
    private TransactionDAO transactionDAO = new TransactionDAO();
    
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
            request.setAttribute("customer", customer);
            request.setAttribute("recentTransactions", 
                transactionDAO.getTransactionsByAccountNumber(customer.getAccountNumber())
                    .stream().limit(5).collect(java.util.stream.Collectors.toList()));
        }
        
        request.getRequestDispatcher("/user/dashboard.jsp").forward(request, response);
    }
}

