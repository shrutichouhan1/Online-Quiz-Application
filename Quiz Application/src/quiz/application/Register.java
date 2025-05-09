package quiz.application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;


public class Register extends JFrame implements ActionListener{
    JButton register, back;
    JTextField tfuname;
    JPasswordField tfpasss;
    JLabel note;
    String username;
    
    // Regular expression for password validation
    private static final String PASSWORD_REGEX =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=\\-{}|:;\"'<>,.?/]).{8}$";
    
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
        
        note = new JLabel("<html>Note: Password must be 8 characters long.<br>(uppercase, lowercase, digit, and special symbol)<br></html>");
        note.setFont(new Font("Tahoma", Font.PLAIN, 11));
        note.setForeground(Color.RED);
        note.setBounds(140, 265, 600, 30);
        add(note);

        // Dynamic password validation note hide
        tfpasss.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String password = new String(tfpasss.getPassword());
                if (password.matches(PASSWORD_REGEX)) {
                    note.setVisible(false);
                } else {
                    note.setVisible(true);
                }
            }
        });
        
        register = new JButton("Register");
        register.setBounds(180, 330, 100, 30);
        register.setBackground(new Color(30, 144, 254));
        register.setForeground(Color.WHITE);
        register.addActionListener(this);
        add(register);
        
        back = new JButton("Back");
        back.setBounds(300, 330, 100, 30);
        back.setBackground(new Color(30, 144, 254));
        back.setForeground(Color.WHITE);
        back.addActionListener(this);
        add(back);

         setSize(580, 450);
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
            
             if (!password.matches(PASSWORD_REGEX)) {
                JOptionPane.showMessageDialog(this, "Password must contain at least:\n 1 uppercase letter, 1 lowercase letter, 1 digit, and 1 special symbol.");
                return;
            }

            try {
                // Hash the password using BCrypt
                String hashedPassword = PasswordUtil.hashPassword(password);
                
                Sql_Connectivity c = new Sql_Connectivity();
                String query = "INSERT INTO users (username, password) VALUES (?, ?)";
                PreparedStatement pstmt = c.c.prepareStatement(query);
                pstmt.setString(1, username);
//                pstmt.setString(2, password);
                pstmt.setString(2, hashedPassword);
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

