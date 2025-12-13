package com.banking.servlet;

import com.banking.dao.CustomerDAO;
import com.banking.model.Customer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/admin/editCustomer")
public class EditCustomerServlet extends HttpServlet {
    private CustomerDAO customerDAO = new CustomerDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"admin".equals(session.getAttribute("userRole"))) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        String accountNumber = request.getParameter("accountNumber");
        if (accountNumber != null) {
            Customer customer = customerDAO.getCustomerByAccountNumber(accountNumber);
            request.setAttribute("customer", customer);
        }
        request.getRequestDispatcher("/admin/editCustomer.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"admin".equals(session.getAttribute("userRole"))) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        String accountNumber = request.getParameter("accountNumber");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        
        if (accountNumber == null || name == null || email == null) {
            request.setAttribute("error", "Invalid data");
            request.getRequestDispatcher("/admin/editCustomer.jsp").forward(request, response);
            return;
        }
        
        Customer customer = new Customer();
        customer.setAccountNumber(accountNumber);
        customer.setName(name);
        customer.setEmail(email);
        customer.setPhone(phone != null ? phone : "");
        customer.setAddress(address != null ? address : "");
        
        if (customerDAO.updateCustomer(customer)) {
            request.setAttribute("success", "Customer updated successfully!");
            request.setAttribute("customer", customerDAO.getCustomerByAccountNumber(accountNumber));
        } else {
            request.setAttribute("error", "Failed to update customer");
            request.setAttribute("customer", customer);
        }
        request.getRequestDispatcher("/admin/editCustomer.jsp").forward(request, response);
    }
}

