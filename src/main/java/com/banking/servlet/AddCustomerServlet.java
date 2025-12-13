package com.banking.servlet;

import com.banking.dao.CustomerDAO;
import com.banking.dao.UserDAO;
import com.banking.model.Customer;
import com.banking.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/admin/addCustomer")
public class AddCustomerServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();
    private CustomerDAO customerDAO = new CustomerDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"admin".equals(session.getAttribute("userRole"))) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        request.getRequestDispatcher("/admin/addCustomer.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"admin".equals(session.getAttribute("userRole"))) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String balanceStr = request.getParameter("balance");
        
        // Validation
        if (name == null || name.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Please fill all required fields");
            request.getRequestDispatcher("/admin/addCustomer.jsp").forward(request, response);
            return;
        }
        
        // Check if email already exists
        if (userDAO.emailExists(email)) {
            request.setAttribute("error", "Email already registered");
            request.getRequestDispatcher("/admin/addCustomer.jsp").forward(request, response);
            return;
        }
        
        double balance = 0.0;
        try {
            balance = Double.parseDouble(balanceStr != null ? balanceStr : "0");
        } catch (NumberFormatException e) {
            balance = 0.0;
        }
        
        // Create user
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRole("user");
        
        if (userDAO.registerUser(user)) {
            User newUser = userDAO.login(email, password);
            if (newUser != null) {
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(email);
                customer.setPhone(phone != null ? phone : "");
                customer.setAddress(address != null ? address : "");
                customer.setBalance(balance);
                
                if (customerDAO.addCustomer(customer, newUser.getId())) {
                    request.setAttribute("success", "Customer added successfully!");
                    request.getRequestDispatcher("/admin/addCustomer.jsp").forward(request, response);
                } else {
                    request.setAttribute("error", "Failed to add customer");
                    request.getRequestDispatcher("/admin/addCustomer.jsp").forward(request, response);
                }
            } else {
                request.setAttribute("error", "Failed to add customer");
                request.getRequestDispatcher("/admin/addCustomer.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("error", "Failed to add customer");
            request.getRequestDispatcher("/admin/addCustomer.jsp").forward(request, response);
        }
    }
}

