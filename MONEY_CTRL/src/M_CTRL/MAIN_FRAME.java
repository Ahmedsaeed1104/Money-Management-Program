package M_CTRL;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MAIN_FRAME extends JFrame {
    private JPanel panel1, panel2, cards;
    private JTextField totalMoneyField, exceptionsField, personalField;
    private JLabel currentDayLabel, currentMonthLabel, currentMoneyLabel;
    private JCheckBox[] dayCheckBoxes = new JCheckBox[30];
    private double currentMoney;
    private FileDataHelper fileDataHelper = new FileDataHelper();
    private JButton calcButton;
    double dailyAmount = 0;

    public MAIN_FRAME() {
        // Frame settings
        setTitle("Monthly Spending Manager");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(0xF0F0F0)); // Light gray background

        // Card Layout for Panel Switching
        cards = new JPanel(new CardLayout());

        // Design and Add Panel 1
        panel1 = new JPanel();
        panel1.setBackground(new Color(0xFFFFFF)); // White background
        panel1.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("SansSerif", Font.BOLD, 26);
        Font textFieldFont = new Font("SansSerif", Font.PLAIN, 22);
        

        JLabel totalMoneyLabel = new JLabel("TOTAL MONEY:");
        totalMoneyLabel.setFont(labelFont);
        totalMoneyLabel.setForeground(new Color(0x004080));

        totalMoneyField = new JTextField(10);
        totalMoneyField.setFont(textFieldFont);

        JLabel exceptionsLabel = new JLabel("EXCEPTIONS SPENDING:");
        exceptionsLabel.setFont(labelFont);
        exceptionsLabel.setForeground(new Color(0x004080));

        exceptionsField = new JTextField(10);
        exceptionsField.setFont(textFieldFont);

        JLabel personalLabel = new JLabel("SPECIAL SPENDINGS:");
        personalLabel.setFont(labelFont);
        personalLabel.setForeground(new Color(0x004080));

        personalField = new JTextField(10);
        personalField.setFont(textFieldFont);

        // Calc Button
        calcButton = new JButton("CALCULATE");
        calcButton.setFont(new Font("SansSerif", Font.BOLD, 28));
        calcButton.setBackground(new Color(0x004080)); // Dark blue background
        calcButton.setForeground(Color.WHITE);
        calcButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateCurrentMoney();
            }
        });

        JButton switchToPanel2 = new JButton("MONTH VIEW");
        switchToPanel2.setFont(new Font("SansSerif", Font.BOLD, 28));
        switchToPanel2.setBackground(new Color(0x004080)); // Dark blue background
        switchToPanel2.setForeground(Color.WHITE);
        switchToPanel2.addActionListener(e -> switchToPanel("Panel_2"));

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel1.add(totalMoneyLabel, gbc);
        gbc.gridx = 1;
        panel1.add(totalMoneyField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel1.add(exceptionsLabel, gbc);
        gbc.gridx = 1;
        panel1.add(exceptionsField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel1.add(personalLabel, gbc);
        gbc.gridx = 1;
        panel1.add(personalField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        panel1.add(calcButton, gbc);

        gbc.gridy = 4;
        panel1.add(switchToPanel2, gbc);

        // Redesign and Add Panel 2
        panel2 = new JPanel(new BorderLayout(20, 20));
        panel2.setBackground(new Color(0xFFF1DB)); // Dark gray background

        JPanel labelsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        labelsPanel.setBackground(panel2.getBackground());

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("d EEEE");
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MM");

        currentDayLabel = new JLabel("Day: " + currentDate.format(dayFormatter));
        currentDayLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        currentDayLabel.setForeground(Color.GREEN);

        currentMonthLabel = new JLabel("Month: " + currentDate.format(monthFormatter));
        currentMonthLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        currentMonthLabel.setForeground(Color.GREEN);

        currentMoneyLabel = new JLabel("Current Money: $0.00");
        currentMoneyLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        currentMoneyLabel.setForeground(new Color(0xEF5A6F));

        labelsPanel.add(currentDayLabel);
        labelsPanel.add(currentMonthLabel);
        labelsPanel.add(currentMoneyLabel);

        JPanel daysPanel = new JPanel(new GridLayout(6, 5, 10, 10));
        daysPanel.setBackground(panel2.getBackground());
        for (int i = 0; i < 30; i++) {
            dayCheckBoxes[i] = new JCheckBox("Day " + (i + 1));
            dayCheckBoxes[i].setBackground(new Color(0x536493));
            dayCheckBoxes[i].setForeground(new Color(0xFCF8F3));
            dayCheckBoxes[i].setFont(new Font("SansSerif", Font.PLAIN, 20));
            int dayIndex = i;
            dayCheckBoxes[i].addActionListener(e -> toggleDaySelection(dayCheckBoxes[dayIndex]));
            daysPanel.add(dayCheckBoxes[i]);
        }

        // Button to switch back to Panel 1
        JButton switchToPanel1 = new JButton("Back to Plan");
        switchToPanel1.setFont(new Font("SansSerif", Font.BOLD, 20));
        switchToPanel1.setBackground(new Color(0x536493)); // Orange background
        switchToPanel1.setForeground(Color.WHITE);
        switchToPanel1.addActionListener(e -> switchToPanel("Panel_1"));

        panel2.add(labelsPanel, BorderLayout.NORTH);
        panel2.add(daysPanel, BorderLayout.CENTER);
        panel2.add(switchToPanel1, BorderLayout.SOUTH);

        // Add Panels to Cards (for CardLayout)
        cards.add(panel1, "Panel_1");
        cards.add(panel2, "Panel_2");

        // Add the card panel to the frame
        add(cards, BorderLayout.CENTER);

        // Load previously saved data
        loadData();

        setVisible(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveData();
                System.exit(0);
            }
        });
    }

    private void switchToPanel(String panelName) {
        CardLayout cl = (CardLayout) (cards.getLayout());
        cl.show(cards, panelName);
    }

    private void toggleDaySelection(JCheckBox checkBox) {
         // Each day checked/unchecked affects the current money by $50
        if (checkBox.isSelected()) {
            currentMoney -= dailyAmount;
            
        } else {
            currentMoney += dailyAmount;
        }
        currentMoneyLabel.setText("Current Money: $" + String.format("%.2f", currentMoney));
    }

    private void saveData() {
        String totalMoney = totalMoneyField.getText();
        String exceptions = exceptionsField.getText();
        String personal = personalField.getText();
        double currentMoney = Double.parseDouble(currentMoneyLabel.getText().replace("Current Money: $", ""));
        boolean[] daySelections = new boolean[30];
        for (int i = 0; i < dayCheckBoxes.length; i++) {
            daySelections[i] = dayCheckBoxes[i].isSelected();
        }

        fileDataHelper.saveData(totalMoney, exceptions, personal, currentMoney, daySelections);
    }

    private void loadData() {
        String[] data = fileDataHelper.loadData();
        if (data[0] != null) {
            totalMoneyField.setText(data[0]);
            exceptionsField.setText(data[1]);
            personalField.setText(data[2]);

            // Restore current money
            currentMoney = Double.parseDouble(data[3]);
            currentMoneyLabel.setText("Current Money: $" + String.format("%.2f", currentMoney));

            // Restore checkbox states
            boolean[] daySelections = fileDataHelper.convertStringToDaySelections(data[4]);
            for (int i = 0; i < dayCheckBoxes.length; i++) {
                dayCheckBoxes[i].setSelected(daySelections[i]);
            }

            // Recalculate the current money based on the selected checkboxes
            calculateCurrentMoney_2(); // Recalculate current money
            dailyAmount = currentMoney / 30; // Set dailyAmount based on the loaded data
            for (int i = 0; i < dayCheckBoxes.length; i++) {
                if (dayCheckBoxes[i].isSelected()) {
                    currentMoney -= dailyAmount; // Adjust current money for each selected day
                }
            }

            // Update the label to reflect the recalculated current money
            currentMoneyLabel.setText("Current Money: $" + String.format("%.2f", currentMoney));
        }
    }


    private void calculateCurrentMoney() {
        try {
            // Calculate current money before switching
            currentMoney = Double.parseDouble(totalMoneyField.getText()) 
                           - Double.parseDouble(exceptionsField.getText())
                           - Double.parseDouble(personalField.getText());
            dailyAmount= currentMoney/30;
            currentMoneyLabel.setText("Current Money: $" + String.format("%.2f", currentMoney));
            JOptionPane.showMessageDialog(this, "Calculation Successful", "Info", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            
        }
    }
    private void calculateCurrentMoney_2() {
        try {
            // Calculate current money before switching
            currentMoney = Double.parseDouble(totalMoneyField.getText()) 
                           - Double.parseDouble(exceptionsField.getText())
                           - Double.parseDouble(personalField.getText());
            dailyAmount= currentMoney/30;
            currentMoneyLabel.setText("Current Money: $" + String.format("%.2f", currentMoney));
            //JOptionPane.showMessageDialog(this, "Calculation Successful", "Info", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            
        }
    }

    public static void main(String[] args) {
        new MAIN_FRAME();
    }
}
