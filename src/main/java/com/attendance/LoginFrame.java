package com.attendance;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginFrame extends JFrame {

    JTextField usernameField;
    JPasswordField passwordField;
    JButton loginButton;

    public LoginFrame() {

        setTitle("Student Attendance Alert System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel titleLabel = new JLabel("Student Attendance System");
        titleLabel.setBounds(90, 30, 250, 30);
        panel.add(titleLabel);

        JLabel usernameLabel = new JLabel("username:");
        usernameLabel.setBounds(50, 90, 100, 25);
        panel.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(150, 90, 180, 25);
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 130, 100, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 130, 180, 25);
        panel.add(passwordField);

        loginButton = new JButton("Register");
        loginButton.setBounds(150, 180, 100, 30);
        panel.add(loginButton);

        loginButton.addActionListener(e -> {

            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

            try (
                    Connection con = DBConnection.getConnection();
                    PreparedStatement pst = con.prepareStatement(sql)
            ) {

                pst.setString(1, username);
                pst.setString(2, password);

                ResultSet rs = pst.executeQuery();

                if (rs.next()) {

                    JOptionPane.showMessageDialog(this, "Login Successful!");

                    dispose();
                    new DashboardFrame();

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "Invalid Username or Password"
                    );
                }

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Database Error: " + ex.getMessage()
                );
            }
        });

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        new LoginFrame();
    }
}