package org.dme.windows;


import com.formdev.flatlaf.FlatIntelliJLaf;
import org.dme.entities.Client;
import org.dme.interfaces.PaginationDataProvider;
import org.dme.pagination.PaginatedTableDecorator;
import org.dme.repositories.ClientRepository;
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

public class ClientListGUI extends JFrame {

    private static JTable table;
    private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    List<Client> clients = ClientRepository.getAllClient();
    private JPanel tablePane;
    private PaginationDataProvider<Client> allDataProvider;
    private PaginationDataProvider<Client> SearchDataProvider;
    private PaginatedTableDecorator<Client> paginatedDecorator;

    //Search LB & TF
    private JLabel firstnameLB = new JLabel("Prénom");
    private JTextField firstnameTF = new JTextField();
    private JLabel lastnameLB = new JLabel("Nom");
    private JTextField lastnameTF = new JTextField();
    private JLabel anniversaireLB = new JLabel("Date de naissance");
    private JTextField anniversaireTF = new JTextField();
    private JLabel emailLB = new JLabel("Adresse mail");
    private JTextField emailTF = new JTextField();

    //BUTTONS
    private JButton downloadBtn = new JButton("Télécharger la liste");
    private JButton addBtn = new JButton("Ajouter");
    private JButton backBtn = new JButton("Retour");
    private JButton allBtn = new JButton("All");
    private JButton searchBtn = new JButton("Search");

    public ClientListGUI() {
        super();

        setTitle("Gestion des clients");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        table = new JTable();
        table.setModel(createObjectDataModel());
        table.setAutoCreateRowSorter(true);
        allDataProvider = createDataProvider();
        paginatedDecorator = PaginatedTableDecorator.decorate(table, allDataProvider, new int[]{15, 30, 50, 75, 100}, 15);

        //Dimension
        Dimension inputDim = new Dimension(150, 30);
        firstnameTF.setPreferredSize(inputDim);
        lastnameTF.setPreferredSize(inputDim);
        anniversaireTF.setPreferredSize(inputDim);
        emailTF.setPreferredSize(inputDim);

        //LISTENER
        table.getSelectionModel().addListSelectionListener(e -> selectionListener(e));
        downloadBtn.addActionListener(e -> downloadBtnListener(e));
        addBtn.addActionListener(e -> addBtnListener(e));
        backBtn.addActionListener(e -> backBtnListener(e));
        allBtn.addActionListener(e -> reload(allDataProvider));
        searchBtn.addActionListener(e -> searchBTN());

        //PANES
        JPanel rootPane = (JPanel) this.getContentPane();
        JPanel searchPane = new JPanel();
        JPanel menuPane = new JPanel();
        tablePane = new JPanel();
        //menuPane.setBackground(new Color(250, 250, 250));
        //tablePane.setBackground(new Color(250, 250, 250));


        tablePane.add(paginatedDecorator.getContentPanel());
        tablePane.setLayout(new GridLayout(1, 1));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = new Insets(105, 105, 105, 105);

        searchPane.setLayout(new FlowLayout());
        searchPane.add(firstnameLB, gbc);
        searchPane.add(firstnameTF, gbc);
        searchPane.add(lastnameLB, gbc);
        searchPane.add(lastnameTF, gbc);

        searchPane.add(emailLB, gbc);
        searchPane.add(emailTF, gbc);
        searchPane.add(searchBtn);

        searchPane.add(allBtn);


        menuPane.setLayout(new FlowLayout());
        menuPane.add(downloadBtn);
        menuPane.add(addBtn);
        menuPane.add(backBtn);

        rootPane.setLayout(new BoxLayout(rootPane, BoxLayout.PAGE_AXIS));
        //rootPane.setBackground(new Color(250, 250, 250));
        rootPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        rootPane.add(tablePane);
        rootPane.add(searchPane);
        rootPane.add(menuPane);
    }

    public static void load() {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            System.out.println("Bug look and feel");
            e.getStackTrace();
        }

        ClientListGUI clientListGUI = new ClientListGUI();
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

    private static PaginationDataProvider<Client> createDataProviderSearch(String firstname, String lastname, String email) {


        final List<Client> clients = ClientRepository.searchClient(firstname, lastname, email);

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

    private void reload(PaginationDataProvider<Client> dataProvider) {
        paginatedDecorator = PaginatedTableDecorator.decorate(table, dataProvider, new int[]{15, 30, 50, 75, 100}, 15);
        tablePane.removeAll();
        tablePane.add(paginatedDecorator.getContentPanel());
    }

    private void searchBTN() {
        String firstname = firstnameTF.getText();
        String lastname = lastnameTF.getText();
        String email = emailTF.getText();

        SearchDataProvider = createDataProviderSearch(firstname, lastname, email);
        reload(SearchDataProvider);
    }

    private void selectionListener(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
            this.dispose();
            ClientEditGUI.load(ClientRepository.getClientById(Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString())));
        }
    }

    private void addBtnListener(ActionEvent e) {
        this.dispose();
        ClientAddGUI.load();
    }

    private void backBtnListener(ActionEvent e) {
        System.out.println("Fermeture de l'application...");
        this.dispose();
    }

    private void downloadBtnListener(ActionEvent e) {
        ClientRepository.printClients();
        JOptionPane.showMessageDialog(ClientListGUI.this, "Le client a bien été modifié");
        this.dispose();
    }
}
