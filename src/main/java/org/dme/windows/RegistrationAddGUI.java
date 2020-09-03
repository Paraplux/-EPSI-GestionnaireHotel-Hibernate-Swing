package org.dme.windows;


import com.formdev.flatlaf.FlatIntelliJLaf;
import org.dme.entities.Reservation;
import org.dme.entities.Room;
import org.dme.repositories.RegistrationRepository;
import org.dme.utils.TempData;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;


public class RegistrationAddGUI extends JFrame {

    private Room selectedRoom;
    private Reservation selectedReservation;

    private JLabel reservationtLB = new JLabel("Reservation");
    private JLabel roomtLB = new JLabel("Chambre");


    private JButton validateBtn = new JButton("Valider");
    private JButton backBtn = new JButton("Retour");
    private JButton selectBtnRes = new JButton("Selectionner la réservation");
    private JButton selectBtnRoom = new JButton("Selectionner la chambre");

    public RegistrationAddGUI(Reservation res, Room r) {
        super();
        setTitle("Création d'un enregistrement");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        if (res != null) {
            selectedReservation = res;
            reservationtLB.setText(res.getNumber() + " " + res.getDate() + " " + res.getClient().getFirstname() + " " + res.getClient().getLastname());
        }
        if (r != null) {
            selectedRoom = r;
            roomtLB.setText(String.valueOf(r.getNumber()));
        }

        selectBtnRes.addActionListener(e -> SelectBtnListenerReservationAndKeepRoom(e));
        selectBtnRoom.addActionListener(e -> SelectBtnListenerRoomAnsKeepReservation(e));

        validateBtn.addActionListener(e -> validateBtnListener(e));
        backBtn.addActionListener(e -> backBtnListener(e));

        JPanel infoPane = new JPanel();
        infoPane.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = new Insets(5, 5, 5, 5);


        infoPane.add(reservationtLB, gbc);
        infoPane.add(selectBtnRes, gbc);
        infoPane.add(roomtLB, gbc);
        infoPane.add(selectBtnRoom, gbc);

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

    //Si l'utilisateur arrive il n'a selectionné ni chambre ni reservation donc load() ne demande pas de paramètre
    public static void load() {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.getStackTrace();
        }

        Reservation res = null;
        Room r = null;
        RegistrationAddGUI registrationAddGUI = new RegistrationAddGUI(res, r);
        registrationAddGUI.pack();
        registrationAddGUI.setVisible(true);
        registrationAddGUI.setLocationRelativeTo(null);
    }

    //Si l'utilisateur arrive et qu'il a selectionné une reservation alors load() prends la reservation en paramètre
    public static void load(Reservation res) {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.getStackTrace();
        }

        Room r = TempData.getTempsRegistrationRoom();
        RegistrationAddGUI registrationAddGUI = new RegistrationAddGUI(res, r);
        registrationAddGUI.pack();
        registrationAddGUI.setVisible(true);
        registrationAddGUI.setLocationRelativeTo(null);
    }

    //Si l'utilisateur arrive et qu'il a selectionné une chambre alors load() prends la chambre en paramètre
    public static void load(Room r) {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.getStackTrace();
        }

        Reservation res = TempData.getTempsRegistrationReservation();
        RegistrationAddGUI resgistrationAddGUI = new RegistrationAddGUI(res, r);
        resgistrationAddGUI.pack();
        resgistrationAddGUI.setVisible(true);
        resgistrationAddGUI.setLocationRelativeTo(null);
    }

    private void backBtnListener(ActionEvent e) {
        System.out.println("Fermeture de l'application...");
        this.dispose();
        RegistrationListGUI.load();
    }

    private void SelectBtnListenerReservationAndKeepRoom(ActionEvent e) {
        System.out.println("Fermeture de l'application...");
        this.dispose();

        System.out.println(selectedRoom);

        TempData.setTempsRegistrationRoom(selectedRoom);
        ReservationSelectGUI.load();
    }

    private void SelectBtnListenerRoomAnsKeepReservation(ActionEvent e) {
        System.out.println("Fermeture de l'application...");
        this.dispose();

        TempData.setTempsRegistrationReservation(selectedReservation);
        RoomSelectGUI.load();
    }

    private void validateBtnListener(ActionEvent e) {

        System.out.println(selectedReservation);
        System.out.println(selectedRoom);

        RegistrationRepository.createRegistration(selectedReservation, selectedRoom);

        this.dispose();
        JOptionPane.showMessageDialog(RegistrationAddGUI.this, "La reservation à bien été ajoutée");
        RegistrationListGUI.load();
    }
}

