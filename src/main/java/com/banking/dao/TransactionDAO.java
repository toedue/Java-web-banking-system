package com.banking.dao;

import com.banking.model.Transaction;
import com.banking.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    
    public boolean addTransaction(Transaction transaction) {
        String sql = "INSERT INTO transactions (transaction_type, sender_account_number, receiver_account_number, amount, note) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, transaction.getTransactionType());
            stmt.setString(2, transaction.getSenderAccountNumber());
            stmt.setString(3, transaction.getReceiverAccountNumber());
            stmt.setDouble(4, transaction.getAmount());
            stmt.setString(5, transaction.getNote());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Transaction> getTransactionsByAccountNumber(String accountNumber) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE sender_account_number = ? OR receiver_account_number = ? ORDER BY created_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, accountNumber);
            stmt.setString(2, accountNumber);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setId(rs.getInt("id"));
                transaction.setTransactionType(rs.getString("transaction_type"));
                transaction.setSenderAccountNumber(rs.getString("sender_account_number"));
                transaction.setReceiverAccountNumber(rs.getString("receiver_account_number"));
                transaction.setAmount(rs.getDouble("amount"));
                transaction.setNote(rs.getString("note"));
                transaction.setCreatedAt(rs.getTimestamp("created_at"));
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }
    
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions ORDER BY created_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setId(rs.getInt("id"));
                transaction.setTransactionType(rs.getString("transaction_type"));
                transaction.setSenderAccountNumber(rs.getString("sender_account_number"));
                transaction.setReceiverAccountNumber(rs.getString("receiver_account_number"));
                transaction.setAmount(rs.getDouble("amount"));
                transaction.setNote(rs.getString("note"));
                transaction.setCreatedAt(rs.getTimestamp("created_at"));
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }
    
    public Transaction getTransactionById(int id) {
        String sql = "SELECT * FROM transactions WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setId(rs.getInt("id"));
                transaction.setTransactionType(rs.getString("transaction_type"));
                transaction.setSenderAccountNumber(rs.getString("sender_account_number"));
                transaction.setReceiverAccountNumber(rs.getString("receiver_account_number"));
                transaction.setAmount(rs.getDouble("amount"));
                transaction.setNote(rs.getString("note"));
                transaction.setCreatedAt(rs.getTimestamp("created_at"));
                return transaction;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

