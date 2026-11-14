package com.attendance;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StudentFrame extends JFrame {

    private JTextField idField;
    private JTextField nameField;
    private JTextField courseField;

    private JButton addButton;
    private JButton clearButton;

    private JTable table;
    private DefaultTableModel model;

    public StudentFrame() {

        setTitle("Manage Students");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        formPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        // Student ID
        formPanel.add(new JLabel("Student ID:"));

        idField = new JTextField();
        formPanel.add(idField);

        // Student Name
        formPanel.add(new JLabel("Student Name:"));

        nameField = new JTextField();
        formPanel.add(nameField);

        // Course
        formPanel.add(new JLabel("Course:"));

        courseField = new JTextField();
        formPanel.add(courseField);

        // Buttons
        addButton = new JButton("Add Student");
        clearButton = new JButton("Clear");

        formPanel.add(addButton);
        formPanel.add(clearButton);

        // Table
        model = new DefaultTableModel(
                new String[]{"Student ID", "Name", "Course"},
                0
        );

        table = new JTable(model);

        add(formPanel, BorderLayout.NORTH);

        add(
                new JScrollPane(table),
                BorderLayout.CENTER
        );

        // Button actions
        addButton.addActionListener(e -> addStudent());

        clearButton.addActionListener(e -> clearFields());

        // Load existing students
        loadStudents();

        setVisible(true);
    }

    // Add student to MySQL
    private void addStudent() {

        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String course = courseField.getText().trim();

        if (id.isEmpty() ||
                name.isEmpty() ||
                course.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill all fields!"
            );

            return;
        }

        try {

            int studentId = Integer.parseInt(id);

            String sql =
                    "INSERT INTO students " +
                            "(id, roll_no, name, course, semester) " +
                            "VALUES (?, ?, ?, ?, ?)";

            try (
                    Connection con = DBConnection.getConnection();
                    PreparedStatement pst =
                            con.prepareStatement(sql)
            ) {

                pst.setInt(1, studentId);
                pst.setString(2, id);
                pst.setString(3, name);
                pst.setString(4, course);
                pst.setInt(5, 1);

                pst.executeUpdate();
            }

            // Add to table
            model.addRow(
                    new Object[]{
                            id,
                            name,
                            course
                    }
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Student Added Successfully!"
            );

            clearFields();

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Student ID must be a number!"
            );

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database Error: " +
                            ex.getMessage()
            );
        }
    }

    // Load students from MySQL
    private void loadStudents() {

        String sql =
                "SELECT id, name, course FROM students";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement pst =
                        con.prepareStatement(sql);
                ResultSet rs =
                        pst.executeQuery()
        ) {

            while (rs.next()) {

                model.addRow(
                        new Object[]{
                                rs.getInt("id"),
                                rs.getString("name"),
                                rs.getString("course")
                        }
                );
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load students: " +
                            ex.getMessage()
            );
        }
    }

    // Clear input fields
    private void clearFields() {

        idField.setText("");
        nameField.setText("");
        courseField.setText("");
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentFrame());
    }
}