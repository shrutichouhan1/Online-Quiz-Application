package quiz.application;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class Score extends JFrame implements ActionListener {

    Score(String username, int score) {
        setBounds(400, 150, 750, 550);
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);
        
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/score.png"));
        Image i2 = i1.getImage().getScaledInstance(300, 250, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        image.setBounds(0, 200, 300, 250);
        add(image);
        
        JLabel heading = new JLabel("Thankyou " + username + " for playing Simple Minds");
        heading.setBounds(45, 30, 700, 30);
        heading.setFont(new Font("Tahoma", Font.PLAIN, 26));
        add(heading);
        
        JLabel lblscore = new JLabel("Your score is " + score);
        lblscore.setBounds(350, 200, 300, 30);
        lblscore.setFont(new Font("Tahoma", Font.PLAIN, 26));
        add(lblscore);
        
        JButton submit = new JButton("Play Again");
        submit.setBounds(380, 270, 120, 30);
        submit.setBackground(new Color(30, 144, 255));
        submit.setForeground(Color.WHITE);
        submit.addActionListener(this);
        add(submit);
        
        insertScoreIntoDatabase(username, score);
        setVisible(true);
    }
    
    private void insertScoreIntoDatabase(String username, int score){
        try{
            Sql_Connectivity c = new Sql_Connectivity();
            String query = "Select id from users where username=?";
            PreparedStatement pstmt = c.c.prepareStatement(query);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            int userId =-1;
            if (rs.next()) {
                userId = rs.getInt("id");
            }
            if(userId != -1){
            int quizId = 1;
                String insertquery = "INSERT INTO results (user_id, quiz_id, score) VALUES(?, ?, ?)";
                PreparedStatement ps = c.c.prepareStatement(insertquery);
                ps.setInt(1, userId);
                ps.setInt(2, quizId);
                ps.setInt(3, score);
                ps.executeUpdate();
                System.out.println("Score Inserted");
            }else {
                    System.out.println("user not found");
            }
            c.c.close();
        }catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error while storing score.");
        } 
    }
    public void actionPerformed(ActionEvent ae) {
        setVisible(false);
        new Login();
    }

    public static void main(String[] args) {
        new Score("User", 10);
    }
}

