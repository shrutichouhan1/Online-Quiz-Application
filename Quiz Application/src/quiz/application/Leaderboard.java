package quiz.application;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class Leaderboard extends JFrame {
    private JTable leaderboardTable;

    public Leaderboard() {
        setTitle("Leaderboard");
        setLayout(new BorderLayout());

        String[] columnNames = {"Rank", "Username", "Score"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        leaderboardTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(leaderboardTable);
        add(scrollPane, BorderLayout.CENTER);

        loadLeaderboardData();

        setSize(500, 300);
        setLocation(500, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void loadLeaderboardData() {
        try {
            Sql_Connectivity connection = new Sql_Connectivity();
//            String query = "SELECT u.username, r.score FROM results r ORDER BY score DESC LIMIT 10";
                String query = "SELECT u.username, r.score FROM results r " +
                           "JOIN users u ON r.user_id = u.id " +
                           "ORDER BY r.score DESC LIMIT 10";
            Statement stmt = connection.c.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            int rank = 1;
            while (rs.next()) {
                String username = rs.getString("username");
                int score = rs.getInt("score");
                Object[] row = {rank++, username, score};
                ((DefaultTableModel) leaderboardTable.getModel()).addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading leaderboard: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new Leaderboard();
    }
}



     