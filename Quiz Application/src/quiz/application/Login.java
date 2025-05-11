package quiz.application;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

public class Login extends JFrame implements ActionListener{
 
    JTextField tfname;
    JPasswordField tfpass;
    Button loginBtn, registerBtn;
    
    Login() {
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);
        
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/login.jpeg"));
        JLabel image = new JLabel(i1);
        image.setBounds(10, 30, 600, 500);
        add(image);
        
        JLabel heading = new JLabel("Quiz Application");
        heading.setBounds(725, 60, 400, 55);
        heading.setFont(new Font("Viner Hand ITC", Font.BOLD, 40));
        heading.setForeground(new Color(30, 144, 254));
        add(heading);
        
        JLabel name = new JLabel("UserName");
        name.setBounds(730,180,300,30);
        add(name);
        tfname = new JTextField();
        tfname.setBounds(730, 210, 300, 25);
//        tfname.setFont(new Font("Times New Roman", Font.BOLD, 20));
        add(tfname);
        
        JLabel pass = new JLabel("Password:");
        pass.setBounds(730,250,300,30);
        add(pass);
        tfpass = new JPasswordField();
        tfpass.setBounds(730,275,300,25);
        tfpass.setEchoChar('*'); 
        add(tfpass);
        
        loginBtn = new Button("Login"); 
        loginBtn.addActionListener(this); 
        add(loginBtn);
        loginBtn.setBounds(770,335,110,22);
        add(loginBtn);
        
        registerBtn = new Button("Register"); 
        registerBtn.addActionListener(this); 
        add(registerBtn);
        registerBtn.setBounds(905,335,110,22);
        add(registerBtn);
        
        setSize(1200, 600);
        setLocation(170, 180);
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent ae) {
        String username = tfname.getText();
        String password = new String(tfpass.getPassword());
        
        if (ae.getSource() == loginBtn) {
            try {
                Sql_Connectivity c = new Sql_Connectivity();
                String query = "Select * from users where username=?";
                PreparedStatement pstmt = c.c.prepareStatement(query);
                pstmt.setString(1, username);
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                   String storedHash = rs.getString("password");    
                    String role = rs.getString("role");

                    if (PasswordUtil.checkPassword(password, storedHash)) {
                        JOptionPane.showMessageDialog(this, "Login successful, " + username);
                        dispose(); 
                    
                        if ("admin".equalsIgnoreCase(role)) {
                        new AdminDashboard();
                    }else {
                            new Rules(username);  // Assuming you want to navigate to rules
                }
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid username/password", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                        } else {
                    JOptionPane.showMessageDialog(this, "User not found", "Error", JOptionPane.ERROR_MESSAGE);
                }
                
                tfname.setText("");  // Clear username
                tfpass.setText("");  // Clear password
                }
                
            catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error while Loging in.");
            } 
            
            
            
        } else if(ae.getSource() == registerBtn){
            JOptionPane.showMessageDialog(this, "Redirecting to registration page");
            new Register(username);
            dispose();
        }
            setVisible(false);
        }

    
    public static void main(String[] args) {
        new Login();
    }
}


