package quiz.application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

public class Quiz extends JFrame implements ActionListener {

    ArrayList<String[]> questions = new ArrayList<>();
    ArrayList<String> answers = new ArrayList<>();
    String[] useranswers;

    JLabel qno, question, timeLabel;
    JRadioButton opt1, opt2, opt3, opt4;
    ButtonGroup groupoptions;
    JButton next, submit, previous;

    Timer quizTimer;
    int totalTime;
    int timeLeft;
    int count = 0;
    int score = 0;
    String name;

    Quiz(String name) {
        this.name = name;
        setBounds(50, 0, 1440, 850);
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/quiz.jpg"));
        JLabel image = new JLabel(i1);
        image.setBounds(0, 0, 1440, 392);
        add(image);

        qno = new JLabel();
        qno.setBounds(100, 450, 50, 30);
        qno.setFont(new Font("Tahoma", Font.PLAIN, 24));
        add(qno);

        question = new JLabel();
        question.setBounds(150, 450, 900, 30);
        question.setFont(new Font("Tahoma", Font.PLAIN, 24));
        add(question);

        opt1 = new JRadioButton();
        opt1.setBounds(170, 520, 700, 30);
        opt1.setBackground(Color.WHITE);
        opt1.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(opt1);

        opt2 = new JRadioButton();
        opt2.setBounds(170, 560, 700, 30);
        opt2.setBackground(Color.WHITE);
        opt2.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(opt2);

        opt3 = new JRadioButton();
        opt3.setBounds(170, 600, 700, 30);
        opt3.setBackground(Color.WHITE);
        opt3.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(opt3);

        opt4 = new JRadioButton();
        opt4.setBounds(170, 640, 700, 30);
        opt4.setBackground(Color.WHITE);
        opt4.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(opt4);

        groupoptions = new ButtonGroup();
        groupoptions.add(opt1);
        groupoptions.add(opt2);
        groupoptions.add(opt3);
        groupoptions.add(opt4);

        next = new JButton("Next");
        next.setBounds(1100, 530, 200, 40);
        next.setFont(new Font("Tahoma", Font.PLAIN, 22));
        next.setBackground(new Color(30, 144, 255));
        next.setForeground(Color.WHITE);
        next.addActionListener(this);
        add(next);

        previous = new JButton("Previous");
        previous.setBounds(1100, 600, 200, 40);
        previous.setFont(new Font("Tahoma", Font.PLAIN, 22));
        previous.setBackground(new Color(30, 144, 255));
        previous.setForeground(Color.WHITE);
        previous.addActionListener(this);
        add(previous);

        submit = new JButton("Submit");
        submit.setBounds(1100, 670, 200, 40);
        submit.setFont(new Font("Tahoma", Font.PLAIN, 22));
        submit.setBackground(new Color(30, 144, 255));
        submit.setForeground(Color.WHITE);
        submit.setEnabled(false);
        submit.addActionListener(this);
        add(submit);

        timeLabel = new JLabel("Time Left: ");
        timeLabel.setBounds(1100, 450, 300, 40);
        timeLabel.setFont(new Font("Tahoma", Font.PLAIN, 22));
        timeLabel.setForeground(Color.RED);
        add(timeLabel);

        fetchQuestionsFromDatabase();
        useranswers = new String[questions.size()];
        totalTime = questions.size() * 60;
        timeLeft = totalTime;

        start(count);
        startTimer();

        setVisible(true);
    }

    void fetchQuestionsFromDatabase() {
        try {
            Sql_Connectivity c = new Sql_Connectivity();
            String query = "SELECT * FROM questions";
            PreparedStatement pstmt = c.c.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                questions.add(new String[]{
                        rs.getString("question"),
                        rs.getString("option_a"),
                        rs.getString("option_b"),
                        rs.getString("option_c"),
                        rs.getString("option_d")
                });
                answers.add(rs.getString("correct_answer"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void start(int count) {
        qno.setText((count + 1) + ".");
        question.setText(questions.get(count)[0]);

        opt1.setText(questions.get(count)[1]);
        opt1.setActionCommand(questions.get(count)[1]);

        opt2.setText(questions.get(count)[2]);
        opt2.setActionCommand(questions.get(count)[2]);

        opt3.setText(questions.get(count)[3]);
        opt3.setActionCommand(questions.get(count)[3]);

        opt4.setText(questions.get(count)[4]);
        opt4.setActionCommand(questions.get(count)[4]);

        groupoptions.clearSelection();

        if (useranswers[count] != null) {
            if (useranswers[count].equals(opt1.getActionCommand())) opt1.setSelected(true);
            else if (useranswers[count].equals(opt2.getActionCommand())) opt2.setSelected(true);
            else if (useranswers[count].equals(opt3.getActionCommand())) opt3.setSelected(true);
            else if (useranswers[count].equals(opt4.getActionCommand())) opt4.setSelected(true);
        }
    }

    void startTimer() {
        quizTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                timeLeft--;
                int minutes = timeLeft / 60;
                int seconds = timeLeft % 60;
                timeLabel.setText(String.format("Time Left: %02d:%02d", minutes, seconds));

                if (timeLeft <= 0) {
                    quizTimer.stop();
                    autoSubmit();
                }
            }
        });
        quizTimer.start();
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == next) {
            saveAnswer();
            if (count == questions.size() - 2) {
                next.setEnabled(false);
                submit.setEnabled(true);
            }
            if (count < questions.size() - 1) {
                count++;
                start(count);
            }
        } else if (ae.getSource() == previous) {
            saveAnswer();
            if (count > 0) {
                count--;
                start(count);
                next.setEnabled(true);
                submit.setEnabled(false);
            }
        } else if (ae.getSource() == submit) {
            saveAnswer();
            quizTimer.stop();
            autoSubmit();
        }
    }

    void saveAnswer() {
        if (groupoptions.getSelection() != null) {
            useranswers[count] = groupoptions.getSelection().getActionCommand();
        } else {
            useranswers[count] = "";
        }
    }

    void autoSubmit() {
        StringBuilder feedback = new StringBuilder("<html><body><h2>Quiz Feedback</h2>");

        for (int i = 0; i < questions.size(); i++) {
            String userAns = useranswers[i] == null ? "" : useranswers[i];
            String correctAns = answers.get(i);

            feedback.append("<p><b>Q").append(i + 1).append(":</b> ").append(questions.get(i)[0]).append("<br>");
            feedback.append("Your Answer: ").append(userAns.isEmpty() ? "Not Answered" : userAns).append("<br>");
            feedback.append("Correct Answer: ").append(correctAns).append("<br>");

            if (userAns.equals(correctAns)) {
                feedback.append("<span style='color:green;'>Correct</span>");
                score += 10;
            } else {
                feedback.append("<span style='color:red;'>Incorrect</span>");
            }
            feedback.append("</p><hr>");
        }

        feedback.append("<h3>Your Score: ").append(score).append("</h3>");
        feedback.append("</body></html>");

        JLabel feedbackLabel = new JLabel(feedback.toString());
        JScrollPane scrollPane = new JScrollPane(feedbackLabel);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        JOptionPane.showMessageDialog(this, scrollPane, "Quiz Result", JOptionPane.INFORMATION_MESSAGE);

        setVisible(false);
        new Score(name, score);
    }

    public static void main(String[] args) {
        new Quiz("User");
    }
}