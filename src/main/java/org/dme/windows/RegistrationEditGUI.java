package org.dme.windows;


import com.formdev.flatlaf.FlatIntelliJLaf;
import org.dme.entities.Registration;
import org.dme.entities.Reservation;
import org.dme.entities.Room;
import org.dme.repositories.RegistrationRepository;
import org.dme.utils.TempData;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.format.DateTimeFormatter;

public class RegistrationEditGUI extends JFrame {

    private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private Room selectedRoom;
    private Reservation selectedReservation;
    private Registration currentRegistration;
    private Reservation currentReservation;
    private Room currentRoom;

    private JLabel reservationLB = new JLabel("Reservation");
    private JLabel roomLB = new JLabel("Room");

    private JButton validateBtn = new JButton("Valider");
    private JButton deleteBtn = new JButton("Supprimer");
    private JButton backBtn = new JButton("Retour");
    private JButton SelectBtnReservation = new JButton("Changer de reservation");
    ;
    private JButton SelectBtnRoom = new JButton("Changer de chambre");

    public RegistrationEditGUI(Registration registration) {
        super();
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        if (registration != null) {
            setTitle("Modification de l'enregistrement N°" + registration.getId());
            reservationLB.setText(registration.getReservation().getNumber() + " " + registration.getReservation().getDate() + " " + registration.getReservation().getClient().getFirstname() + " " + registration.getReservation().getClient().getLastname());
            roomLB.setText(String.valueOf(registration.getRoom().getNumber()));
        }

        SelectBtnReservation.addActionListener(e -> SelectBtnListenerReservationAndKeepRoom(e));
        SelectBtnRoom.addActionListener(e -> SelectBtnListenerRoomAnsKeepReservation(e));
        validateBtn.addActionListener(e -> validateBtnListener(e));
        deleteBtn.addActionListener(e -> deleteBtnListener(e));
        backBtn.addActionListener(e -> backBtnListener(e));

        JPanel rootPane = (JPanel) this.getContentPane();
        JPanel infoPane = new JPanel();
        JPanel buttonsPane = new JPanel();

        GridBagConstraints gbc = new GridBagConstraints();
        infoPane.setLayout(new GridBagLayout());
        //infoPane.setBackground(new Color(250, 250, 250));

        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        infoPane.add(reservationLB, gbc);
        infoPane.add(SelectBtnReservation, gbc);
        infoPane.add(roomLB, gbc);
        infoPane.add(SelectBtnRoom, gbc);

        buttonsPane.setLayout(new FlowLayout());

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

        Registration registration = null;
        RegistrationEditGUI registrationEditGUI = new RegistrationEditGUI(registration);
        registrationEditGUI.pack();
        registrationEditGUI.setVisible(true);
        registrationEditGUI.setLocationRelativeTo(null);
    }

    public static void load(Registration registration) {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.getStackTrace();
        }

        RegistrationEditGUI registrationEditGUI = new RegistrationEditGUI(registration);
        registrationEditGUI.pack();
        registrationEditGUI.setVisible(true);
        registrationEditGUI.setLocationRelativeTo(null);
    }

    private void backBtnListener(ActionEvent e) {
        System.out.println("Fermeture de l'application...");
        this.dispose();
    }

    private void deleteBtnListener(ActionEvent e) {
        this.dispose();
        RegistrationRepository.deleteRegistretion(currentRegistration.getId());
        RegistrationListGUI.load();
    }

    private void SelectBtnListenerReservationAndKeepRoom(ActionEvent e) {
        System.out.println("Fermeture de l'application...");
        this.dispose();

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

        if (currentReservation != selectedReservation) {
            currentRegistration.setReservation(selectedReservation);
        } else {
            currentRegistration.setReservation(currentReservation);
        }

        if (currentRoom != selectedRoom) {
            currentRegistration.setRoom(selectedRoom);
        } else {
            currentRegistration.setRoom(currentRoom);
        }


        RegistrationRepository.updateRegistrationById(currentRegistration);

        this.dispose();
        JOptionPane.showMessageDialog(RegistrationEditGUI.this, "L'enregistrement à bien était modifié");
        ReservationListGUI.load();
    }
}
