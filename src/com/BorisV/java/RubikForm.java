package com.BorisV.java;


import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class RubikForm extends JFrame implements WindowListener{

    private JTable rubikDataTable;
    private JTextField nameTextField;
    private JTextField timeTextField;
    private JButton addButton;
    private JButton deleteButton;
    private JButton quitButton;
    private JPanel rootPanel;

    RubikForm(final RubikCubeDataModel rubikCubeDataModel) {
        setContentPane(rootPanel);
        pack();
        setTitle("RubikCube Database Application");
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        rubikDataTable.setGridColor(Color.black);
        rubikDataTable.setModel(rubikCubeDataModel);

        addButton.setIcon(new ImageIcon("src/Add-Green-icon.png"));
        deleteButton.setIcon(new ImageIcon("src/Delete-icon.png"));
        quitButton.setIcon(new ImageIcon("src/quit-icon.png"));

        addButton.addActionListener(e -> {

            String nameData = nameTextField.getText();
            if (nameData == null || nameData.trim().equals("")) {
                JOptionPane.showMessageDialog(rootPane, "Please enter a name!");
                return;
            }



            double timeData;

            try {
                timeData = Double.parseDouble(timeTextField.getText());
                if (timeData < 0.0) {
                    throw new NumberFormatException("Enter a possitive number");
                }
            } catch (NumberFormatException ne) {
                JOptionPane.showMessageDialog(rootPane,"Enter a number greater than 0");
                timeTextField.setText(null);
                return;

            }

            boolean insertRow = rubikCubeDataModel.insertRow(nameData, timeData);

            if (!insertRow) {
                JOptionPane.showMessageDialog(rootPane, "Error, none added");
            }

            nameTextField.setText(null);
            timeTextField.setText(null);

        });

        deleteButton.addActionListener(e -> {
            int currentRow = rubikDataTable.getSelectedRow();

            if (currentRow == -1) {      // -1 means no row is selected. Display error message.
                JOptionPane.showMessageDialog(rootPane, "Please choose a movie to delete");
            }
            boolean deleted = rubikCubeDataModel.deleteRow(currentRow);
            if (deleted) {
                RubikCubeDatabase.loadAllRubikRecords();
            } else {
                JOptionPane.showMessageDialog(rootPane, "Error deleting movie");
            }
        });

        quitButton.addActionListener(e -> {
            RubikCubeDatabase.shutdown();
            System.exit(0);   //Should probably be a call back to Main class so all the System.exit(0) calls are in one place.
        });

    }



    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        RubikCubeDatabase.shutdown();
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

}
