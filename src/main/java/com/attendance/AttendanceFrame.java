package com.attendance;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class AttendanceFrame extends JFrame {

    JTextField studentIdField;
    JTextField totalClassesField;
    JTextField classesAttendedField;

    JLabel percentageLabel;
    JLabel statusLabel;

    JButton calculateButton;
    JButton saveButton;

    public AttendanceFrame() {

        setTitle("Attendance");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        panel.add(new JLabel("Student ID:"));
        studentIdField = new JTextField();
        panel.add(studentIdField);

        panel.add(new JLabel("Total Classes:"));
        totalClassesField = new JTextField();
        panel.add(totalClassesField);

        panel.add(new JLabel("Classes Attended:"));
        classesAttendedField = new JTextField();
        panel.add(classesAttendedField);

        calculateButton = new JButton("Calculate");
        panel.add(calculateButton);

        saveButton = new JButton("Save Attendance");
        panel.add(saveButton);

        percentageLabel = new JLabel("Attendance: 0.00%");
        panel.add(percentageLabel);

        statusLabel = new JLabel("Status: -");
        panel.add(statusLabel);

        add(panel);

        calculateButton.addActionListener(e -> calculateAttendance());

        saveButton.addActionListener(e -> saveAttendance());

        setVisible(true);
    }

    private void calculateAttendance() {

        try {
            int total = Integer.parseInt(totalClassesField.getText().trim());
            int attended = Integer.parseInt(classesAttendedField.getText().trim());

            if (total <= 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Total classes must be greater than 0!"
                );
                return;
            }

            if (attended < 0 || attended > total) {
                JOptionPane.showMessageDialog(
                        this,
                        "Invalid classes attended!"
                );
                return;
            }

            double percentage = (attended * 100.0) / total;

            percentageLabel.setText(
                    String.format("Attendance: %.2f%%", percentage)
            );

            if (percentage < 75) {
                statusLabel.setText("Status: SHORTAGE ALERT");
            } else {
                statusLabel.setText("Status: SAFE");
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter valid numbers!"
            );
        }
    }

    private void saveAttendance() {

        try {

            String studentId = studentIdField.getText().trim();

            int total = Integer.parseInt(
                    totalClassesField.getText().trim()
            );

            int attended = Integer.parseInt(
                    classesAttendedField.getText().trim()
            );

            if (studentId.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please enter Student ID!"
                );
                return;
            }

            if (total <= 0 || attended < 0 || attended > total) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please enter valid attendance values!"
                );
                return;
            }

            double percentage = (attended * 100.0) / total;

            String sql =
                    "INSERT INTO attendance " +
                            "(student_id, total_classes, classes_attended, attendance_percentage) " +
                            "SELECT id, ?, ?, ? FROM students WHERE roll_no = ?";

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement pst = con.prepareStatement(sql)) {

                pst.setInt(1, total);
                pst.setInt(2, attended);
                pst.setDouble(3, percentage);
                pst.setString(4, studentId);

                int rows = pst.executeUpdate();

                if (rows > 0) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Attendance Saved Successfully!"
                    );

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "Student ID not found!"
                    );
                }
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter valid numbers!"
            );

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database Error: " + ex.getMessage()
            );
        }
    }

    public static void main(String[] args) {
        new AttendanceFrame();
    }
}