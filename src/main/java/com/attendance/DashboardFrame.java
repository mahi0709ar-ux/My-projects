package com.attendance;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    public DashboardFrame() {

        setTitle("Student Attendance Dashboard");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel title = new JLabel("Student Attendance Dashboard");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JButton studentButton = new JButton("Manage Students");
        JButton attendanceButton = new JButton("Attendance");
        JButton logoutButton = new JButton("Logout");

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(80, 150, 80, 150));

        panel.add(studentButton);
        panel.add(attendanceButton);
        panel.add(logoutButton);

        add(title, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        studentButton.addActionListener(e -> {
            new StudentFrame();
        });

        attendanceButton.addActionListener(e -> {
            new AttendanceFrame();
        });

        logoutButton.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        setVisible(true);
    }
}