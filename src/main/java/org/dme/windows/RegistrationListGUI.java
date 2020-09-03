package org.dme.windows;

import com.formdev.flatlaf.FlatIntelliJLaf;
import org.dme.entities.Registration;
import org.dme.pagination.PaginatedTableDecorator;
import org.dme.repositories.RegistrationRepository;
import org.dme.utils.ObjectTableModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RegistrationListGUI extends JFrame {

    private static JTable table;
    private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private JPanel tablePane;
    private org.dme.interfaces.PaginationDataProvider<Registration> dataProvider;
    private PaginatedTableDecorator<Registration> paginatedDecorator;
    //BUTTONS
    private JButton addBtn = new JButton("Ajouter");
    private JButton backBtn = new JButton("Retour");

    public RegistrationListGUI() {
        super();

        setTitle("Gestion des enregistrements");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        table = new JTable();
        table.setModel(createObjectDataModel());
        table.setAutoCreateRowSorter(true);
        dataProvider = createDataProvider();
        paginatedDecorator = PaginatedTableDecorator.decorate(table, dataProvider, new int[]{15, 30, 50, 75, 100}, 15);


        //LISTENER
        table.getSelectionModel().addListSelectionListener(e -> selectionListener(e));

        addBtn.addActionListener(e -> addBtnListener(e));

        backBtn.addActionListener(e -> backBtnListener(e));


        //PANES
        JPanel rootPane = (JPanel) this.getContentPane();
        JPanel menuPane = new JPanel();
        tablePane = new JPanel();


        tablePane.add(paginatedDecorator.getContentPanel());
        tablePane.setLayout(new GridLayout(1, 1));

        menuPane.setLayout(new FlowLayout());
        menuPane.add(addBtn);
        menuPane.add(backBtn);

        rootPane.setLayout(new BoxLayout(rootPane, BoxLayout.PAGE_AXIS));
        rootPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        rootPane.add(tablePane);
        rootPane.add(menuPane);
    }

    public static void load() {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            System.out.println("Bug look and feel");
            e.getStackTrace();
        }

        RegistrationListGUI registrationListGUI = new RegistrationListGUI();
        registrationListGUI.setVisible(true);
        registrationListGUI.setSize(new Dimension(800, 410));
        registrationListGUI.setLocationRelativeTo(null);
    }

    private static TableModel createObjectDataModel() {
        return new ObjectTableModel<Registration>() {
            @Override
            public Object getValueAt(Registration reg, int columnIndex) {
                switch (columnIndex) {
                    case 0:
                        return reg.getId();
                    case 1:
                        return reg.getReservation().getNumber();
                    case 2:
                        return reg.getRoom().getNumber();
                    case 3:
                        return reg.getReservation().getClient().getLastname();
                    case 4:
                        return reg.getReservation().getClient().getFirstname();
                    case 5:
                        return reg.getReservation().getDate();
                    case 6:
                        return reg.getReservation().getTotal_price();
                }
                return null;
            }

            @Override
            public int getColumnCount() {
                return 7;
            }

            @Override
            public String getColumnName(int column) {
                switch (column) {
                    case 0:
                        return "Id";
                    case 1:
                        return "Numéro de réservation";
                    case 2:
                        return "Numéro de Chambre";
                    case 3:
                        return "Nom";
                    case 4:
                        return "Prénom";
                    case 5:
                        return "Date de réservation";
                    case 6:
                        return "Prix total";
                }
                return null;
            }
        };
    }

    private static org.dme.interfaces.PaginationDataProvider<Registration> createDataProvider() {

        final List<Registration> registrations = RegistrationRepository.readRegistration();


        return new org.dme.interfaces.PaginationDataProvider<Registration>() {
            @Override
            public int getTotalRowCount() {
                return registrations.size();
            }

            @Override
            public List<Registration> getRows(int startIndex, int endIndex) {
                return registrations.subList(startIndex, endIndex);
            }
        };

    }

    private void selectionListener(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
            this.dispose();
            RegistrationEditGUI.load(RegistrationRepository.readRegistrationById(Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString())));
        }
    }

    private void addBtnListener(ActionEvent e) {
        this.dispose();
        RegistrationAddGUI.load();
    }

    private void backBtnListener(ActionEvent e) {
        System.out.println("Fermeture de l'application...");
        this.dispose();
    }
}
