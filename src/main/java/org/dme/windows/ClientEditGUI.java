package org.dme.windows;

import com.formdev.flatlaf.FlatIntelliJLaf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dme.entities.Client;
import org.dme.repositories.ClientRepository;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ClientEditGUI extends JFrame {

    static final Logger guiLogger = LogManager.getLogger("GUILog");

    private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private Client currentClient;
    private JTextField firstnameTF = new JTextField();
    private JTextField lastnameTF = new JTextField();
    private JTextField anniversaireTF = new JTextField();
    private JTextField emailTF = new JTextField();
    private JButton validateBtn = new JButton("Valider");
    private JButton deleteBtn = new JButton("Supprimer");
    private JButton backBtn = new JButton("Retour");

    public ClientEditGUI(Client c) {
        super();
        currentClient = c;
        setTitle("Modification du client" + currentClient.getId());
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

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

        firstnameTF.setText(currentClient.getFirstname());
        lastnameTF.setText(currentClient.getLastname());
        anniversaireTF.setText(currentClient.getAnniversaire().format(dateFormat));
        emailTF.setText(currentClient.getMail());

        infoPane.add(firstnameTF, gbc);
        infoPane.add(lastnameTF, gbc);
        infoPane.add(anniversaireTF, gbc);
        infoPane.add(emailTF, gbc);

        buttonsPane.setLayout(new FlowLayout());
        buttonsPane.add(validateBtn);
        buttonsPane.add(deleteBtn);
        buttonsPane.add(backBtn);

        rootPane.add(infoPane);
        rootPane.add(buttonsPane);
        rootPane.setLayout(new BoxLayout(rootPane, BoxLayout.PAGE_AXIS));
        rootPane.setBorder(new EmptyBorder(10, 10, 10, 10));
    }

    public static void load(Client c) {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            guiLogger.error("####  - " + e.getClass() + " - " + e.getMessage());
        }

        guiLogger.debug("####  - Application GUI loading");
        ClientEditGUI clientEditGUI = new ClientEditGUI(c);
        clientEditGUI.setVisible(true);
        clientEditGUI.pack();
        clientEditGUI.setLocationRelativeTo(null);
    }

    private void backBtnListener(ActionEvent e) {
        guiLogger.debug("####  - Back button fired");
        this.dispose();
    }

    private void deleteBtnListener(ActionEvent e) {
        guiLogger.debug("####  - Supprimer button fired");
        this.dispose();
        ClientRepository.removeClient(currentClient.getId());
        ClientListGUI.load();
    }

    private void validateBtnListener(ActionEvent e) {
        guiLogger.debug("####  - Valider button fired");
        String firstname = firstnameTF.getText();
        String lastname = lastnameTF.getText();
        String anniversaire = anniversaireTF.getText();
        System.out.println(anniversaire);
        String dateParts[] = anniversaire.split("-");
        System.out.println(dateParts);
        int year = Integer.parseInt(dateParts[2]);
        int month = Integer.parseInt(dateParts[1]);
        int day = Integer.parseInt(dateParts[0]);
        String email = emailTF.getText();

        currentClient.setFirstname(firstname);
        currentClient.setLastname(lastname);
        currentClient.setAnniversaire(LocalDate.of(year, month, day));
        currentClient.setMail(email);

        try {
            ClientRepository.updateClient(currentClient.getId(), currentClient);
            guiLogger.info("####  - Le client a bien été modifié");
            JOptionPane.showMessageDialog(ClientEditGUI.this, "Le client a bien été modifié");
            this.dispose();
            ClientListGUI.load();
        } catch (Exception exception) {
            guiLogger.error("####  - " + exception.getClass() + " - " + exception.getMessage());
        }
    }
}
