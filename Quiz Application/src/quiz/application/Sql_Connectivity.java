package quiz.application;
import java.sql.*;

public class Sql_Connectivity {
    
    public Statement s;
    public Connection c; // step 2 create connection
    public Sql_Connectivity(){  //step 1 create connectivity
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Quiz_application","root","shruti");
            s = c.createStatement(); //step third create statement
        
        }
        catch(Exception e) {
           e.printStackTrace();
    }
    }

public static void main(String[] args) {
        new Sql_Connectivity();
    }
}
