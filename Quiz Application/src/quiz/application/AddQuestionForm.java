package quiz.application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AddQuestionForm extends JFrame implements ActionListener {
    
    JTextField questionTextField, option1Field, option2Field, option3Field, option4Field, correctAnswerField;
    JButton submitBtn;
    int quizId; 

    AddQuestionForm(int quizId) {
        this.quizId = quizId;
        setTitle("Add Question");
        setLayout(new GridLayout(7, 2, 10, 10));  // Set layout for better organization

        // Initialize form components
        JLabel questionLabel = new JLabel("Question:");
        questionTextField = new JTextField();
        
        JLabel option1Label = new JLabel("Option 1:");
        option1Field = new JTextField();
        
        JLabel option2Label = new JLabel("Option 2:");
        option2Field = new JTextField();
        
        JLabel option3Label = new JLabel("Option 3:");
        option3Field = new JTextField();
        
        JLabel option4Label = new JLabel("Option 4:");
        option4Field = new JTextField();
        
        JLabel correctAnswerLabel = new JLabel("Correct Answer:");
        correctAnswerField = new JTextField();

        submitBtn = new JButton("Submit");
        submitBtn.addActionListener(this);
        
        // Add components to the form
        add(questionLabel);
        add(questionTextField);
        add(option1Label);
        add(option1Field);
        add(option2Label);
        add(option2Field);
        add(option3Label);
        add(option3Field);
        add(option4Label);
        add(option4Field);
        add(correctAnswerLabel);
        add(correctAnswerField);
        add(submitBtn);

        setSize(400, 300);
        setLocation(500, 200);
        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent ae) {
        String questionText = questionTextField.getText();
        String option1 = option1Field.getText();
        String option2 = option2Field.getText();
        String option3 = option3Field.getText();
        String option4 = option4Field.getText();
        String correctAnswer = correctAnswerField.getText();

        // Validate inputs
        if (questionText.isEmpty() || option1.isEmpty() || option2.isEmpty() || 
                option3.isEmpty() || option4.isEmpty() || correctAnswer.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        try {
            // SQL connection
            Sql_Connectivity connection = new Sql_Connectivity();
            String query = "INSERT INTO questions (quiz_id, question_text, option_1, option_2, "
                    + "option_3, option_4, correct_answer) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = connection.c.prepareStatement(query);
            
            pstmt.setInt(1, quizId);
            pstmt.setString(2, questionText);
            pstmt.setString(3, option1);
            pstmt.setString(4, option2);
            pstmt.setString(5, option3);
            pstmt.setString(6, option4);
            pstmt.setString(7, correctAnswer);
            
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Question added successfully.");
            setVisible(false);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error while adding question.");
        }
    }

    public static void main(String[] args) {
        new AddQuestionForm(1);  // Assume quiz_id is 1 for demonstration purposes
    }
}