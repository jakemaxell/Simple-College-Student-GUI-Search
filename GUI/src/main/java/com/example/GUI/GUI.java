package com.example.GUI;

import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class GUI implements ActionListener {

    JFrame frame;
    JPanel panel;
    JButton searchButton;
    JButton clearButton;
    JButton changeMajorButton;
    JLabel nameLabel = new JLabel("Name:");
    JLabel majorLabel = new JLabel("Major:");
    JLabel gpaLabel = new JLabel("GPA:");
    JLabel statusLabel = new JLabel("Status: ");
    JTextField studentID = new JTextField();
    JComboBox<String> comboBox;
    public GUI(){

        frame = new JFrame();

        panel = new JPanel();

        searchButton = new JButton("Search");
        searchButton.addActionListener(this::searchStudent);

        clearButton = new JButton("Clear");
        clearButton.addActionListener(this::clearConsole);

        changeMajorButton = new JButton("Change Major");
        changeMajorButton.addActionListener(this::changeStudentMajor);

        String[] choices = {"", "Accounting", "Art", "Biology", "Business Management", "Chemical Engineering", "Communications", "Computer Science", "Education", "Electrical Engineering", "English", "Graphic Design", "History", "Mathematics", "Psychology", "Theatre", "Women's Studies"};
        comboBox = new JComboBox<>(choices);
        comboBox.setVisible(true);

        panel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
        panel.setLayout(new GridLayout(0, 1));
        panel.add(nameLabel);
        panel.add(majorLabel);
        panel.add(gpaLabel);
        panel.add(statusLabel);
        panel.add(studentID);
        panel.add(comboBox);
        panel.add(searchButton);
        panel.add(changeMajorButton);
        panel.add(clearButton);

        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("GUI");
        frame.pack();
        frame.setVisible(true);

    }

    public static void main(String[] args) {

        new GUI();

    }

    public void clearConsole(ActionEvent e){
        nameLabel.setText("Name:");
        majorLabel.setText("Major:");
        gpaLabel.setText("GPA:");
        statusLabel.setText("Status:");
        studentID.setText("");
    }

    public void changeStudentMajor(ActionEvent e){

        try {
            String endpoint = "http://localhost:8080/api/change-major/" + comboBox.getSelectedItem() + "/" + studentID.getText();
            URL url = new URL(endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            System.out.println(connection.getResponseCode());
            System.out.println(endpoint);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }

    public void searchStudent(ActionEvent e) {

        try {
            String endpoint = "http://localhost:8080/api/retrieve-student-by-id/" + studentID.getText();
            URL url = new URL(endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();

            while((inputLine = bufferedReader.readLine()) != null){
                content.append(inputLine);
            }
            bufferedReader.close();

            JSONObject jsonObject = new JSONObject(content.toString());

            nameLabel.setText("Name: " + jsonObject.get("firstName") + " " + jsonObject.get("lastName"));
            majorLabel.setText("Major: " + jsonObject.get("major"));
            gpaLabel.setText("GPA: " + jsonObject.get("gpa"));
            statusLabel.setText("Status: " + jsonObject.get("status"));

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
