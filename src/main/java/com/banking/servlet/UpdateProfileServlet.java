package com.banking.servlet;

import com.banking.dao.CustomerDAO;
import com.banking.dao.UserDAO;
import com.banking.model.Customer;
import com.banking.model.User;
import com.banking.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/user/updateProfile")
public class UpdateProfileServlet extends HttpServlet {
    private CustomerDAO customerDAO = new CustomerDAO();
    private UserDAO userDAO = new UserDAO();
    
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
        
        request.setAttribute("customer", customer);
        request.getRequestDispatcher("/user/updateProfile.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"user".equals(session.getAttribute("userRole"))) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            customer = customerDAO.getCustomerByUserId((Integer) session.getAttribute("userId"));
        }
        
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String password = request.getParameter("password");
        
        // Update customer details
        customer.setName(name != null ? name : customer.getName());
        customer.setPhone(phone != null ? phone : customer.getPhone());
        customer.setAddress(address != null ? address : customer.getAddress());
        
        if (customerDAO.updateCustomer(customer)) {
            // Update password if provided
            if (password != null && !password.trim().isEmpty()) {
                User user = userDAO.login(customer.getEmail(), "");
                if (user != null) {
                    // Update password in users table
                    String sql = "UPDATE users SET password = ? WHERE id = ?";
                    try (java.sql.Connection conn = com.banking.util.DatabaseConnection.getConnection();
                         java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setString(1, PasswordUtil.hashPassword(password));
                        stmt.setInt(2, user.getId());
                        stmt.executeUpdate();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            
            // Refresh customer from database
            Customer updatedCustomer = customerDAO.getCustomerByAccountNumber(customer.getAccountNumber());
            session.setAttribute("customer", updatedCustomer);
            
            request.setAttribute("success", "Profile updated successfully!");
            request.setAttribute("customer", updatedCustomer);
        } else {
            request.setAttribute("error", "Failed to update profile");
            request.setAttribute("customer", customer);
        }
        
        request.getRequestDispatcher("/user/updateProfile.jsp").forward(request, response);
    }
}

