package org.dme.windows;

import com.formdev.flatlaf.FlatIntelliJLaf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dme.entities.Client;
import org.dme.repositories.ClientRepository;
import org.dme.utils.DateValidator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClientAddGUI extends JFrame {

    static final Logger guiLogger = LogManager.getLogger("GUILog");

    private JLabel firstnameLB = new JLabel("Prénom");
    private JTextField firstnameTF = new JTextField();
    private JLabel lastnameLB = new JLabel("Nom");
    private JTextField lastnameTF = new JTextField();
    private JLabel anniversaireLB = new JLabel("Date de naissance");
    private JTextField anniversaireTF = new JTextField();
    private JLabel emailLB = new JLabel("Adresse mail");
    private JTextField emailTF = new JTextField();

    private JButton validateBtn = new JButton("Valider");
    private JButton backBtn = new JButton("Retour");

    public ClientAddGUI() {
        super();

        setTitle("Création d'un client");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Dimension inputDim = new Dimension(150, 30);

        firstnameTF.setPreferredSize(inputDim);
        lastnameTF.setPreferredSize(inputDim);
        anniversaireTF.setPreferredSize(inputDim);
        emailTF.setPreferredSize(inputDim);


        validateBtn.addActionListener(e -> validateBtnListener(e));
        backBtn.addActionListener(e -> backBtnListener(e));


        JPanel infoPane = new JPanel();
        infoPane.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        infoPane.add(firstnameLB, gbc);
        infoPane.add(firstnameTF, gbc);
        infoPane.add(lastnameLB, gbc);
        infoPane.add(lastnameTF, gbc);
        infoPane.add(anniversaireLB, gbc);
        infoPane.add(anniversaireTF, gbc);
        infoPane.add(emailLB, gbc);
        infoPane.add(emailTF, gbc);

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
            guiLogger.error("####  - " + e.getClass() + " - " + e.getMessage());
        }

        guiLogger.debug("####  - Application GUI loading");
        ClientAddGUI clientAddGUI = new ClientAddGUI();
        clientAddGUI.pack();
        clientAddGUI.setVisible(true);
        clientAddGUI.setLocationRelativeTo(null);
    }

    private void backBtnListener(ActionEvent e) {
        guiLogger.debug("####  - Back button fired");
        this.dispose();
        ClientListGUI.load();
    }

    private void validateBtnListener(ActionEvent e) {
        guiLogger.debug("####  - Validate button fired");
        String firstname = firstnameTF.getText();
        String lastname = lastnameTF.getText();
        String anniversaire = anniversaireTF.getText();
        String email = emailTF.getText();
        List<String> errors = new ArrayList<>();

        if (firstname.equals("")) {
            errors.add("Il faut entrer un prénom");
        }
        if (lastname.equals("")) {
            errors.add("Il faut entrer un nom");
        }
        if (anniversaire.equals("")) {
            errors.add("Il faut entrer un date de naissance");
        } else if (!DateValidator.isThisDateValid(anniversaire, "dd-MM-yyyy")) {
            errors.add("La date de naissance n'est pas une date valide (jj-mm-aaaa)");
        }
        if (email.equals("")) {
            errors.add("Il faut entrer une adresse mail");
        }

        if (errors.size() == 0) {
            Client c = new Client();
            c.setFirstname(firstname);
            c.setLastname(lastname);
            c.setMail(email);

            String dateParts[] = anniversaire.split("-");
            int year = Integer.parseInt(dateParts[2]);
            int month = Integer.parseInt(dateParts[1]);
            int day = Integer.parseInt(dateParts[0]);
            c.setAnniversaire(LocalDate.of(year, month, day));

            ClientRepository.addClient(c);
            this.dispose();

            guiLogger.info("####  - Le client a bien été ajouté");
            JOptionPane.showMessageDialog(ClientAddGUI.this, "Le client a bien été ajouté");
            ClientListGUI.load();
        } else {
            String message = "";
            for (String s : errors) {
                message += s + "\n";
            }
            guiLogger.error("####  - " + message);
            JOptionPane.showMessageDialog(ClientAddGUI.this, message);
        }
    }
}
