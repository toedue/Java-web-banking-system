package com.banking.servlet;

import com.banking.dao.CustomerDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/admin/deleteCustomer")
public class DeleteCustomerServlet extends HttpServlet {
    private CustomerDAO customerDAO = new CustomerDAO();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"admin".equals(session.getAttribute("userRole"))) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        String accountNumber = request.getParameter("accountNumber");
        if (accountNumber != null && customerDAO.deleteCustomer(accountNumber)) {
            request.setAttribute("success", "Customer deleted successfully!");
        } else {
            request.setAttribute("error", "Failed to delete customer");
        }
        response.sendRedirect(request.getContextPath() + "/admin/viewCustomers.jsp");
    }
}

