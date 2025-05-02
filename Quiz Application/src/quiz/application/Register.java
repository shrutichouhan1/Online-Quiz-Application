package quiz.application;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Register extends JFrame implements ActionListener{
    JButton register, back;
    JTextField tfuname;
    JPasswordField tfpasss;
    String username;
    
    Register(String username) {
        this.username=username;
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);
        
        JLabel heading = new JLabel("New Registration");
        heading.setBounds(125, 60, 400, 55);
        heading.setFont(new Font("Viner Hand ITC", Font.BOLD, 40));
        heading.setForeground(new Color(30, 144, 254));
        add(heading);
        
        JLabel name = new JLabel("Enter UserName");
        name.setBounds(140,150,300,20);
        add(name);
        tfuname = new JTextField();
        tfuname.setBounds(140, 180, 300, 20);
        add(tfuname);
        
        JLabel pass = new JLabel("Enter Password:");
        pass.setBounds(140,210,300,20);
        add(pass);
        tfpasss = new JPasswordField();
        tfpasss.setBounds(140,240,300,20);
        add(tfpasss);
        
        register = new JButton("Register");
        register.setBounds(170, 350, 100, 30);
        register.setBackground(new Color(30, 144, 254));
        register.setForeground(Color.WHITE);
        register.addActionListener(this);
        add(register);
        
        back = new JButton("Back");
        back.setBounds(290, 350, 100, 30);
        back.setBackground(new Color(30, 144, 254));
        back.setForeground(Color.WHITE);
        back.addActionListener(this);
        add(back);

         setSize(600, 450);
        setLocation(500, 100);
        setVisible(true);
    }
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == register) {
            String username = tfuname.getText();
            String password = new String(tfpasss.getPassword());
//            String password1 = new String(tfpass1.getPassword());
            if (username.isEmpty() || password.isEmpty() ) {
                JOptionPane.showMessageDialog(this, "Please enter all fields.");
                return;
            } 
                try {
                Sql_Connectivity c = new Sql_Connectivity();
                String query = "INSERT INTO users (username, password) VALUES (?, ?)";
                PreparedStatement pstmt = c.c.prepareStatement(query);
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "User registered successfully!");
                setVisible(false);
                new Login();
            } catch (SQLIntegrityConstraintViolationException e) {
                JOptionPane.showMessageDialog(this, "Username already exists!");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error while registering user.");
            }
        } else if (ae.getSource() == back) {
            setVisible(false);
            new Login();
        }
    }
    public static void main(String[] args) {
        new Register("Register");
    }
}

