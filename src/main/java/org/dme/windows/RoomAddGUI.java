package org.dme.windows;


import com.formdev.flatlaf.FlatIntelliJLaf;
import org.dme.entities.Room;
import org.dme.repositories.RoomRepository;
import org.dme.utils.IntValidator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class RoomAddGUI extends JFrame {

    private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd LLLL yyyy");

    private JLabel roomNumberLB = new JLabel("Numéros de chambres");
    private JTextField roomNumberTF = new JTextField();
    private JButton validateBtn = new JButton("Valider");
    private JButton backBtn = new JButton("Retour");


    public RoomAddGUI() {
        super();
        setTitle("Création d'une chambre");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Dimension inputDim = new Dimension(150, 30);
        roomNumberTF.setPreferredSize(inputDim);


        validateBtn.addActionListener(e -> validateBtnListener(e));
        backBtn.addActionListener(e -> backBtnListener(e));


        JPanel rootPane = (JPanel) this.getContentPane();
        JPanel infoPane = new JPanel();
        JPanel buttonsPane = new JPanel();

        GridBagConstraints gbc = new GridBagConstraints();
        infoPane.setLayout(new GridBagLayout());
        infoPane.setBackground(new Color(250, 250, 250));

        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        infoPane.add(roomNumberLB, gbc);
        infoPane.add(roomNumberTF, gbc);
        ;


        buttonsPane.setLayout(new FlowLayout());
        buttonsPane.setBackground(new Color(250, 250, 250));
        buttonsPane.add(validateBtn);
        buttonsPane.add(backBtn);

        rootPane.add(infoPane);
        rootPane.add(buttonsPane);
        rootPane.setLayout(new BoxLayout(rootPane, BoxLayout.PAGE_AXIS));
        rootPane.setBackground(new Color(250, 250, 250));
        rootPane.setBorder(new EmptyBorder(10, 10, 10, 10));
    }


    public static void load() {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            System.out.println("Bug look and feel");
            e.getStackTrace();
        }

        RoomAddGUI roomAddGUI = new RoomAddGUI();
        roomAddGUI.pack();
        roomAddGUI.backBtn.requestFocusInWindow();
        roomAddGUI.setVisible(true);
        roomAddGUI.setLocationRelativeTo(null);
    }

    private void backBtnListener(ActionEvent e) {
        System.out.println("Fermeture de l'application...");
        this.dispose();
        RoomListGUI.load();
    }


    private void validateBtnListener(ActionEvent e) {

        int number = 0;


        List<String> errors = new ArrayList<>();

        if (IntValidator.isThisIntValid(roomNumberTF.getText())) {
            number = Integer.parseInt(roomNumberTF.getText());
            if (number < 0) {
                errors.add("Il faut entrer un numéro de chambre");
            } else if (number > 60) {
                errors.add("Capacitée de l'hotel atteinte");
            }
        }


        if (errors.size() == 0) {
            Room r = new Room();
            r.setNumber(number);


            RoomRepository.createRoom(r);

            this.dispose();
            RoomListGUI.load();

        } else {
            String message = "";
            for (String s : errors) {
                message += s + "\n";
            }
            JOptionPane.showMessageDialog(RoomAddGUI.this, message);
        }

    }


}
