package com.simplebank;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class SendMoneyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login");
            return;
        }

        request.getRequestDispatcher("/sendMoney.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login");
            return;
        }

        String receiver = request.getParameter("receiver");
        String amountStr = request.getParameter("amount");

        int balance = (int) session.getAttribute("balance");
        int amount = Integer.parseInt(amountStr);

        if (amount <= 0 || amount > balance) {
            request.setAttribute("message", "Invalid amount or not enough balance.");
        } else {
            balance -= amount;
            session.setAttribute("balance", balance);
            request.setAttribute(
                    "message",
                    "Sent " + amount + " ETP to " + receiver + ". New balance: " + balance + " ETP."
            );
        }

        request.getRequestDispatcher("/sendMoney.jsp").forward(request, response);
    }
}


