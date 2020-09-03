package org.dme.windows;


import com.formdev.flatlaf.FlatIntelliJLaf;
import org.dme.entities.Client;
import org.dme.entities.Reservation;
import org.dme.repositories.ReservationRepository;
import org.dme.utils.DateValidator;
import org.dme.utils.IntValidator;
import org.dme.utils.TempData;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationAddGUI extends JFrame {

    private JLabel clientLB = new JLabel("Client");
    private JLabel occupationLB = new JLabel("Occupation");
    private JTextField occupationTF = new JTextField();
    private JLabel dateLB = new JLabel("Date de la reservation");
    private JTextField dateTF = new JTextField();
    private JLabel cbLB = new JLabel("Carte Bancaire");
    private JTextField cbTF = new JTextField();


    private JButton validateBtn = new JButton("Valider");
    private JButton backBtn = new JButton("Retour");
    private JButton SelectBtn = new JButton("Selectionner le client");

    public ReservationAddGUI(Client c) {
        super();
        setTitle("Création d'une reservation");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        if (c.getLastname() == null && c.getFirstname() == null) {
            clientLB.setText("Aucun client selectionner");
        } else {
            List<String> previousData = TempData.getReservationClientSelectionData();
            occupationTF.setText(previousData.get(0));
            dateTF.setText(previousData.get(1));
            cbTF.setText(previousData.get(2));
            clientLB.setText(c.getFirstname() + " " + c.getLastname());
        }

        Dimension inputDim = new Dimension(150, 30);


        occupationTF.setPreferredSize(inputDim);
        dateTF.setPreferredSize(inputDim);
        cbTF.setPreferredSize(inputDim);


        SelectBtn.addActionListener(e -> SelectBtnListener(e));
        validateBtn.addActionListener(e -> validateBtnListener(e, c));
        backBtn.addActionListener(e -> backBtnListener(e));

        JPanel infoPane = new JPanel();
        infoPane.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        infoPane.add(occupationLB, gbc);
        infoPane.add(occupationTF, gbc);
        infoPane.add(dateLB, gbc);
        infoPane.add(dateTF, gbc);
        infoPane.add(cbLB, gbc);
        infoPane.add(cbTF, gbc);
        infoPane.add(clientLB, gbc);
        infoPane.add(SelectBtn, gbc);

        JPanel buttonsPane = new JPanel();
        buttonsPane.setLayout(new FlowLayout());
        buttonsPane.add(validateBtn);
        buttonsPane.add(backBtn);

        JPanel rootPane = (JPanel) this.getContentPane();
        rootPane.add(infoPane);
        rootPane.add(buttonsPane);
        rootPane.setLayout(new BoxLayout(rootPane, BoxLayout.PAGE_AXIS));
        rootPane.setBorder(new EmptyBorder(10, 10, 10, 10));
    }

    public static void load() {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.getStackTrace();
        }

        Client c = new Client();
        ReservationAddGUI clientAddGUI = new ReservationAddGUI(c);
        clientAddGUI.pack();
        clientAddGUI.setVisible(true);
        clientAddGUI.setLocationRelativeTo(null);
    }

    public static void load(Client c) {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.getStackTrace();
        }

        ReservationAddGUI clientAddGUI = new ReservationAddGUI(c);
        clientAddGUI.pack();
        clientAddGUI.setVisible(true);
        clientAddGUI.setLocationRelativeTo(null);
    }

    private void backBtnListener(ActionEvent e) {
        System.out.println("Fermeture de l'application...");
        this.dispose();
        ReservationListGUI.load();
    }

    private void SelectBtnListener(ActionEvent e) {
        System.out.println("Fermeture de l'application...");
        this.dispose();
        List<String> data = new ArrayList<>();
        data.add(occupationTF.getText());
        data.add(dateTF.getText());
        data.add(cbTF.getText());
        TempData.setLastPageData("ReservationADDGUI");
        TempData.setReservationClientSelectionData(data);
        ClientSelectGUI.load();
    }

    private void validateBtnListener(ActionEvent e, Client c) {
        String date = dateTF.getText();

        List<String> errors = new ArrayList<>();
        if (c.getLastname() == null && c.getFirstname() == null) {
            errors.add("Il faut selectionner un client");
        }
        if (occupationTF.getText().equals("")) {
            errors.add("Il faut préciser le nb d'occupant");
        } else if (!IntValidator.isThisIntValid(occupationTF.getText())) {
            errors.add("L'occupation doit etre un chiffre");
        }
        if (cbTF.getText().equals("")) {
            errors.add("Il faut entrer une carte bancaire");
        } else if (!IntValidator.isThisIntValid(cbTF.getText())) {
            errors.add("La carte banche doit etre un chiffre");
        }
        if (date.equals("")) {
            errors.add("Il faut entrer la date de la reservation");
        } else if (!DateValidator.isThisDateValid(date, "dd-MM-yyyy")) {
            errors.add("La date de la reservation n'est pas une date valide (jj-mm-aaaa)");
        }

        if (errors.size() == 0) {
            int occupation = Integer.parseInt(occupationTF.getText());
            int cb_number = Integer.parseInt(cbTF.getText());

            String dateParts[] = date.split("-");
            int year = Integer.parseInt(dateParts[2]);
            int month = Integer.parseInt(dateParts[1]);
            int day = Integer.parseInt(dateParts[0]);

            Reservation r = new Reservation(c, occupation, LocalDate.of(year, month, day), cb_number);
            ReservationRepository.addReservation(r);

            this.dispose();
            JOptionPane.showMessageDialog(ReservationAddGUI.this, "La reservation a bien été ajoutée");
            ReservationListGUI.load();
        } else {
            String message = "";
            for (String s : errors) {
                message += s + "\n";
            }
            JOptionPane.showMessageDialog(ReservationAddGUI.this, message);
        }
    }
}
