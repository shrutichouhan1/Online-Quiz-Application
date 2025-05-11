package quiz.application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;


public class AdminDashboard extends JFrame implements ActionListener {
    private JButton addQuestionButton, updateQuestionButton, deleteQuestionButton, viewQuestionsButton, logoutButton;
    private JTextField questionField, option1Field, option2Field, option3Field, option4Field, correctAnswerField;
    private JTable table;
       private DefaultTableModel tableModel;

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setLayout(null);

        
        JLabel q = new JLabel("Question");
        q.setBounds(45,20,100,25);
        add(q);
        questionField = new JTextField();
        questionField.setBounds(145,25,510,22);
        add(questionField);

        JLabel o1 = new JLabel("Option 1:");
        o1.setBounds(40,70,60,20);
        add(o1);
        option1Field = new JTextField();
        option1Field.setBounds(145,70,160,20);
        add(option1Field);

        JLabel o2 = new JLabel("Option 2:");
        o2.setBounds(390,70,70,25);
        add(o2);
        option2Field = new JTextField();
        option2Field.setBounds(490,70,160,20);
        add(option2Field);

        JLabel o3 = new JLabel("Option 3:");
        o3.setBounds(40,115,70,25);
        add(o3);
        option3Field = new JTextField();
        option3Field.setBounds(145,115,160,20);
        add(option3Field);

        JLabel o4 = new JLabel("Option 4:");
        o4.setBounds(390,115,70,25);
        add(o4);
        option4Field = new JTextField();
        option4Field.setBounds(490,115,160,20);
        add(option4Field);

        JLabel ans =new JLabel("Correct Answer:");
        ans.setBounds(150,165,150,25);
        add(ans);
        correctAnswerField = new JTextField();
        correctAnswerField.setBounds(270,165,300,25);
        add(correctAnswerField);

        addQuestionButton = new JButton("Add Question");
        addQuestionButton.addActionListener(this);
        addQuestionButton.setBounds(180,215,150,25);
        add(addQuestionButton);

        updateQuestionButton = new JButton("Update Question");
        updateQuestionButton.addActionListener(this);
        updateQuestionButton.setBounds(380,215,150,25);
        add(updateQuestionButton);

        deleteQuestionButton = new JButton("Delete Question");
        deleteQuestionButton.addActionListener(this);
        deleteQuestionButton.setBounds(180,255,150,25);
        add(deleteQuestionButton);

        viewQuestionsButton = new JButton("View Questions");
        viewQuestionsButton.addActionListener(this);
        viewQuestionsButton.setBounds(380,255,150,25);
        add(viewQuestionsButton);

        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(this);
        logoutButton.setBounds(255,525,130,20);
        add(logoutButton);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Question");
        tableModel.addColumn("Option 1");
        tableModel.addColumn("Option 2");
        tableModel.addColumn("Option 3");
        tableModel.addColumn("Option 4");
        tableModel.addColumn("Correct Answer");
        
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(40, 300, 600, 200);
        add(scrollPane);

        this.setSize(700, 600);
        setLocation(400, 100);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

@Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "Add Question":
                addQuestion();
                break;
            case "Update Question":
                updateQuestion();
                break;
            case "Delete Question":
                deleteQuestion();
                break;
            case "View Questions":
                viewQuestions();
                break;
            case "Logout":
                logout();
                break;
        }
    }
    
        private void addQuestion() {
        try {
            String question = questionField.getText();
            String option1 = option1Field.getText();
            String option2 = option2Field.getText();
            String option3 = option3Field.getText();
            String option4 = option4Field.getText();
            String correctAnswer = correctAnswerField.getText();

            if (question.isEmpty() || option1.isEmpty() || option2.isEmpty() ||
                option3.isEmpty() || option4.isEmpty() || correctAnswer.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
                return;
            }

            Sql_Connectivity connection = new Sql_Connectivity();
            String query = "INSERT INTO questions (question, option_a, option_b, option_c, option_d, correct_answer) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = connection.c.prepareStatement(query);

            pstmt.setString(1, question);
            pstmt.setString(2, option1);
            pstmt.setString(3, option2);
            pstmt.setString(4, option3);
            pstmt.setString(5, option4);
            pstmt.setString(6, correctAnswer);

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Question added successfully!");
            viewQuestions();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error adding question: " + ex.getMessage());
        }
    }

    private void updateQuestion() {
        // Get the selected row from the JTable
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a question to update.");
            return;
        }

        // Retrieve question data from selected row
        int questionId = (int) table.getValueAt(selectedRow, 0);
        String question = questionField.getText();
        String option1 = option1Field.getText();
        String option2 = option2Field.getText();
        String option3 = option3Field.getText();
        String option4 = option4Field.getText();
        String correctAnswer = correctAnswerField.getText();

        // Update the selected question in the database
        try {
            Sql_Connectivity connection = new Sql_Connectivity();
            String query = "UPDATE questions SET question = ?, option_a = ?, option_b = ?, option_c = ?, option_d = ?, correct_answer = ? WHERE id = ?";
            PreparedStatement pstmt = connection.c.prepareStatement(query);

            pstmt.setString(1, question);
            pstmt.setString(2, option1);
            pstmt.setString(3, option2);
            pstmt.setString(4, option3);
            pstmt.setString(5, option4);
            pstmt.setString(6, correctAnswer);
            pstmt.setInt(7, questionId);

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Question updated successfully!");
            viewQuestions();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error updating question: " + ex.getMessage());
        }
    }

    private void deleteQuestion() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a question to delete.");
            return;
        }

        int questionId = (int) table.getValueAt(selectedRow, 0);

        // Confirm deletion
        int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this question?");
        if (confirmation == JOptionPane.YES_OPTION) {
            // Delete the question from the database
            try {
                Sql_Connectivity connection = new Sql_Connectivity();
                String query = "DELETE FROM questions WHERE id = ?";
                PreparedStatement pstmt = connection.c.prepareStatement(query);
                pstmt.setInt(1, questionId);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Question deleted successfully!");
                viewQuestions();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error deleting question: " + ex.getMessage());
            }
        }
    }

    private void viewQuestions() {
        try {
            Sql_Connectivity connection = new Sql_Connectivity();
            String query = "SELECT id, question, option_a, option_b, option_c, option_d, correct_answer FROM questions";
            PreparedStatement pstmt = connection.c.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            tableModel.setRowCount(0);  

            // Add rows to the table
            while (rs.next()) {
//                model.addRow(new Object[] {
                    tableModel.addRow(new Object[] {
                    rs.getInt("id"), 
                    rs.getString("question"),
                    rs.getString("option_a"),
                    rs.getString("option_b"),
                    rs.getString("option_c"),
                    rs.getString("option_d"),
                    rs.getString("correct_answer")
                });
            }

            // Set the table model to the JTable
            table.setModel(tableModel);

            // Refresh the frame
            revalidate();
            repaint();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching questions: " + ex.getMessage());
        }
    }
    
    private void logout() {
        JOptionPane.showMessageDialog(this, "Logging out...");
        this.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new AdminDashboard();
            }
        });
    }
}