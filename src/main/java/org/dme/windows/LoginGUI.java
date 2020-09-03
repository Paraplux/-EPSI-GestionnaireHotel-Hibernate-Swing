package org.dme.windows;


import com.formdev.flatlaf.FlatIntelliJLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginGUI extends JFrame {

    //BUTTONS
    private JButton validationBTN = new JButton("Valider");

    private JLabel mpLB = new JLabel("Mot de passe");
    private JPasswordField mpTF = new JPasswordField();

    public LoginGUI() {
        super();
        setTitle("Page de connexion");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //LISTENER
        validationBTN.addActionListener(e -> validationBtnListener(e));

        //PANES
        JPanel rootPane = (JPanel) this.getContentPane();
        GridBagConstraints gbc = new GridBagConstraints();
        rootPane.setLayout(new GridBagLayout());

        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        rootPane.add(mpLB, gbc);
        rootPane.add(mpTF, gbc);

        rootPane.add(validationBTN, gbc);

        //rootPane.setBackground(new Color(250, 250, 250));
        rootPane.setBorder(new EmptyBorder(10, 10, 10, 10));
    }

    public static void load() {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            System.out.println("Bug look and feel");
            e.getStackTrace();
        }

        LoginGUI menuGUI = new LoginGUI();
        menuGUI.pack();
        menuGUI.setVisible(true);
        menuGUI.setLocationRelativeTo(null);
    }


    private void validationBtnListener(ActionEvent e) {
        String mp = mpTF.getText();

        if (mp.equals("Epsi")) {
            this.dispose();
            MenuGUI.load();
        } else {
            JOptionPane.showMessageDialog(LoginGUI.this, "Mot de passe invalide");
        }
    }

}
