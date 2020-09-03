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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReservationEditGUI extends JFrame {

    private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private Reservation currentReservation;
    private Client currentClient;


    private JLabel clientLB = new JLabel("Client");
    private JLabel occupationLB = new JLabel("Occupation");
    private JTextField occupationTF = new JTextField();
    private JLabel dateLB = new JLabel("Date de la reservation");
    private JTextField dateTF = new JTextField();
    private JLabel cbLB = new JLabel("Carte Bancaire");
    private JTextField cbTF = new JTextField();
    private JLabel priceLB = new JLabel("Prix");
    private JTextField priceTF = new JTextField();


    private JButton validateBtn = new JButton("Valider");
    private JButton deleteBtn = new JButton("Supprimer");
    private JButton backBtn = new JButton("Retour");
    private JButton SelectBtn = new JButton("Changer de client");

    public ReservationEditGUI(Reservation r, Client c) {
        super();
        currentReservation = r;
        currentClient = c;
        setTitle("Modification de la réservation N°" + currentReservation.getNumber());
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        SelectBtn.addActionListener(e -> SelectBtnListener(e));
        validateBtn.addActionListener(e -> validateBtnListener(e));
        deleteBtn.addActionListener(e -> deleteBtnListener(e));
        backBtn.addActionListener(e -> backBtnListener(e));

        JPanel rootPane = (JPanel) this.getContentPane();
        JPanel infoPane = new JPanel();
        JPanel buttonsPane = new JPanel();

        GridBagConstraints gbc = new GridBagConstraints();
        infoPane.setLayout(new GridBagLayout());

        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = new Insets(10, 10, 10, 10);


        clientLB.setText(currentClient.getFirstname() + " " + currentClient.getLastname());
        occupationTF.setText(Integer.toString(currentReservation.getOccupation()));
        dateTF.setText(currentReservation.getDate().format(dateFormat));
        cbTF.setText(Integer.toString(currentReservation.getCb_number()));
        priceTF.setText(Double.toString(currentReservation.getTotal_price()));

        infoPane.add(occupationLB, gbc);
        infoPane.add(occupationTF, gbc);
        infoPane.add(dateLB, gbc);
        infoPane.add(dateTF, gbc);
        infoPane.add(cbLB, gbc);
        infoPane.add(cbTF, gbc);
        infoPane.add(priceLB, gbc);
        infoPane.add(priceTF, gbc);
        infoPane.add(clientLB, gbc);
        infoPane.add(SelectBtn, gbc);

        buttonsPane.setLayout(new FlowLayout());
        buttonsPane.add(validateBtn);
        buttonsPane.add(deleteBtn);
        buttonsPane.add(backBtn);

        rootPane.add(infoPane);
        rootPane.add(buttonsPane);
        rootPane.setLayout(new BoxLayout(rootPane, BoxLayout.PAGE_AXIS));
        rootPane.setBorder(new EmptyBorder(10, 10, 10, 10));
    }

    public static void load(Reservation r, Client c) {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.getStackTrace();
        }

        ReservationEditGUI ReservationEditGUI = new ReservationEditGUI(r, c);
        ReservationEditGUI.setVisible(true);
        ReservationEditGUI.pack();
        ReservationEditGUI.setLocationRelativeTo(null);
    }

    private void backBtnListener(ActionEvent e) {
        System.out.println("Fermeture de l'application...");
        this.dispose();
    }

    private void deleteBtnListener(ActionEvent e) {
        this.dispose();
        ReservationRepository.removeReservation(currentReservation.getNumber());
        ReservationListGUI.load();
    }

    private void SelectBtnListener(ActionEvent e) {
        System.out.println("Fermeture de l'application...");
        this.dispose();
        TempData.setLastPageData("ReservationEditGUI");
        TempData.setTempsReservationObjecteData(currentReservation);
        ClientSelectGUI.load();
    }

    private void validateBtnListener(ActionEvent e) {

        String date = dateTF.getText();
        List<String> errors = new ArrayList<>();


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
        if (priceTF.getText().equals("")) {
            errors.add("Il faut préciser le prix");
        } else if (!IntValidator.isDouble(priceTF.getText())) {
            errors.add("Le prix doit etre un chiffre");
        }
        if (date.equals("")) {
            errors.add("Il faut entrer la date de la reservation");
        } else if (!DateValidator.isThisDateValid(date, "dd-MM-yyyy")) {
            errors.add("La date de la reservation n'est pas une date valide (jj-mm-aaaa)");
        }

        if (errors.size() == 0) {
            int occupation = Integer.parseInt(occupationTF.getText());
            int cb_number = Integer.parseInt(cbTF.getText());
            Double total_price = Double.parseDouble(priceTF.getText());


            String dateParts[] = date.split("-");
            int year = Integer.parseInt(dateParts[2]);
            int month = Integer.parseInt(dateParts[1]);
            int day = Integer.parseInt(dateParts[0]);
            currentReservation.setDate(LocalDate.of(year, month, day));
            currentReservation.setClient(currentClient);
            currentReservation.setOccupation(occupation);
            currentReservation.setCb_number(cb_number);
            currentReservation.setTotal_price(total_price);


            ReservationRepository.updateReservation(currentReservation.getNumber(), currentReservation);
            this.dispose();
            JOptionPane.showMessageDialog(ReservationEditGUI.this, "La reservation a bien été modifié");
            ReservationListGUI.load();
        }

    }
}
