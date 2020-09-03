package org.dme.windows;


import com.formdev.flatlaf.FlatIntelliJLaf;
import org.dme.entities.Client;
import org.dme.interfaces.PaginationDataProvider;
import org.dme.pagination.PaginatedTableDecorator;
import org.dme.repositories.ClientRepository;
import org.dme.utils.ObjectTableModel;
import org.dme.utils.TempData;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ClientSelectGUI extends JFrame {

    private static JTable table;
    private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    List<Client> clients = ClientRepository.getAllClient();
    private JPanel tablePane;
    private PaginationDataProvider<Client> dataProvider;
    private PaginatedTableDecorator<Client> paginatedDecorator;
    //BUTTONS
    private JButton addBtn = new JButton("Ajouter");
    private JButton backBtn = new JButton("Retour");

    public ClientSelectGUI() {

        super();

        setTitle("Selection d'un clients");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        table = new JTable();
        table.setModel(createObjectDataModel());
        table.setAutoCreateRowSorter(true);
        dataProvider = createDataProvider();
        paginatedDecorator = PaginatedTableDecorator.decorate(table, dataProvider, new int[]{15, 30, 50, 75, 100}, 15);

        //LISTENER
        table.getSelectionModel().addListSelectionListener(e -> selectionListener(e));


        //PANES
        JPanel rootPane = (JPanel) this.getContentPane();
        tablePane = new JPanel();
        //menuPane.setBackground(new Color(250, 250, 250));
        //tablePane.setBackground(new Color(250, 250, 250));


        tablePane.add(paginatedDecorator.getContentPanel());
        tablePane.setLayout(new GridLayout(1, 1));

        rootPane.setLayout(new BoxLayout(rootPane, BoxLayout.PAGE_AXIS));
        //rootPane.setBackground(new Color(250, 250, 250));
        rootPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        rootPane.add(tablePane);

    }

    public static void load() {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            System.out.println("Bug look and feel");
            e.getStackTrace();
        }

        ClientSelectGUI clientListGUI = new ClientSelectGUI();
        clientListGUI.setVisible(true);
        clientListGUI.setSize(new Dimension(800, 410));
        clientListGUI.setLocationRelativeTo(null);
    }

    private static TableModel createObjectDataModel() {
        return new ObjectTableModel<Client>() {
            @Override
            public Object getValueAt(Client c, int columnIndex) {
                switch (columnIndex) {
                    case 0:
                        return c.getId();
                    case 1:
                        return c.getFirstname();
                    case 2:
                        return c.getLastname();
                    case 3:
                        return c.getAnniversaire().format(dateFormat);
                    case 4:
                        return c.getMail();
                    case 5:
                        return c.getReservations() == null ? "Aucune" : "En cours";
                }
                return null;
            }

            @Override
            public int getColumnCount() {
                return 4;
            }

            @Override
            public String getColumnName(int column) {
                switch (column) {
                    case 0:
                        return "Id";
                    case 1:
                        return "Prénom";
                    case 2:
                        return "Nom";
                    case 3:
                        return "Date de naissance";
                    case 4:
                        return "Adresse mail";
                    case 5:
                        return "Réservation";
                }
                return null;
            }
        };
    }

    private static PaginationDataProvider<Client> createDataProvider() {

        final List<Client> clients = ClientRepository.getAllClient();

        return new PaginationDataProvider<Client>() {
            @Override
            public int getTotalRowCount() {
                return clients.size();
            }

            @Override
            public List<Client> getRows(int startIndex, int endIndex) {
                return clients.subList(startIndex, endIndex);
            }
        };
    }

    private void selectionListener(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
            this.dispose();
            if (TempData.getLastPageData().equals("ReservationEditGUI")) {
                ReservationEditGUI.load(TempData.getTempsReservationObjecteData(), ClientRepository.getClientById(Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString())));
            } else {
                ReservationAddGUI.load(ClientRepository.getClientById(Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString())));
            }


        }
    }


}
