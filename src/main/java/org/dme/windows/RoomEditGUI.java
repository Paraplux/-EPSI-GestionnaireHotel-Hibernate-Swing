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

public class RoomEditGUI extends JFrame {

    private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private Room currentRoom;
    private JTextField numberTF = new JTextField();
    private JCheckBox archiveCheckBox = new JCheckBox("Archivé ?");
    private JButton validateBtn = new JButton("Valider");
    private JButton deleteBtn = new JButton("Supprimer");
    private JButton backBtn = new JButton("Retour");

    public RoomEditGUI(Room r) {
        super();
        currentRoom = r;
        setTitle("Modification du numéro de chambre " + currentRoom.getNumber());
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        archiveCheckBox.setSelected(r.getActive() == 0);

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

        numberTF.setText(Integer.toString(currentRoom.getNumber()));


        infoPane.add(numberTF, gbc);
        infoPane.add(archiveCheckBox, gbc);

        buttonsPane.setLayout(new FlowLayout());
        buttonsPane.add(validateBtn);
        buttonsPane.add(deleteBtn);
        buttonsPane.add(backBtn);

        rootPane.add(infoPane);
        rootPane.add(buttonsPane);
        rootPane.setLayout(new BoxLayout(rootPane, BoxLayout.PAGE_AXIS));
        rootPane.setBorder(new EmptyBorder(10, 10, 10, 10));
    }

    public static void load(Room r) {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.getStackTrace();
        }

        RoomEditGUI roomEditGUI = new RoomEditGUI(r);
        roomEditGUI.setVisible(true);
        roomEditGUI.pack();
        roomEditGUI.setLocationRelativeTo(null);
    }

    private void backBtnListener(ActionEvent e) {
        System.out.println("Fermeture de l'application...");
        this.dispose();
    }

    private void deleteBtnListener(ActionEvent e) {

        if (currentRoom.getOccupation() == 0) {
            this.dispose();
            RoomRepository.deleteRoomIfNotUsed(currentRoom.getNumber());
            RoomListGUI.load();

        } else {
            JOptionPane.showMessageDialog(RoomEditGUI.this, "Vous ne pouvez pas supprimer cette chambre, elle est occupée!");
        }

    }

    private void validateBtnListener(ActionEvent e) {
        int number = 0;

        List<String> errors = new ArrayList<>();

        if (IntValidator.isThisIntValid(numberTF.getText())) {
            number = Integer.parseInt(numberTF.getText());
            if (number < 0) {
                errors.add("Il faut entrer un numéro de chambre");
            } else if (number > 60) {
                errors.add("Capacitée de l'hotel atteinte");
            }
        }

        if (archiveCheckBox.isSelected() && currentRoom.getOccupation() != 0) {
            errors.add("Vous ne pouvez pas archivé cette chambre, elle est occupée !");
        }


        if (errors.size() == 0) {
            currentRoom.setNumber(number);

            int newActiveValue = archiveCheckBox.isSelected() ? 0 : 1;

            System.out.println(newActiveValue);

            currentRoom.setActive(newActiveValue);
            RoomRepository.updateRoom(currentRoom);

            this.dispose();
            RoomListGUI.load();
        } else {
            String message = "";
            for (String s : errors) {
                message += s + "\n";
            }
            JOptionPane.showMessageDialog(RoomEditGUI.this, message);
        }

    }
}
