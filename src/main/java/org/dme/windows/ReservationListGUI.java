package org.dme.windows;


import org.dme.entities.Reservation;
import org.dme.interfaces.PaginationDataProvider;
import org.dme.pagination.PaginatedTableDecorator;
import org.dme.repositories.ReservationRepository;
import org.dme.utils.ObjectTableModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReservationListGUI extends JFrame {

    private static JTable table;
    private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    List<Reservation> reservations = ReservationRepository.getAllReservation();
    private JPanel tablePane;
    private PaginationDataProvider<Reservation> allDataProvider;
    private PaginationDataProvider<Reservation> nowDataProvider;
    private PaginatedTableDecorator<Reservation> paginatedDecorator;
    //BUTTONS
    private JButton addBtn = new JButton("Ajouter");
    private JButton backBtn = new JButton("Retour");
    private JButton allBtn = new JButton("All");
    private JButton nowBtn = new JButton("Today");

    public ReservationListGUI() {
        super();

        setTitle("Gestion des reservations");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        table = new JTable();
        table.setModel(createObjectDataModel());
        table.setAutoCreateRowSorter(true);
        allDataProvider = createDataProviderAll();
        nowDataProvider = createDataProviderNow();
        paginatedDecorator = PaginatedTableDecorator.decorate(table, allDataProvider, new int[]{15, 30, 50, 75, 100}, 15);

        //LISTENER
        table.getSelectionModel().addListSelectionListener(e -> selectionListener(e));

        addBtn.addActionListener(e -> addBtnListener(e));
        backBtn.addActionListener(e -> backBtnListener(e));
        allBtn.addActionListener(e -> reload(allDataProvider));
        nowBtn.addActionListener(e -> reload(nowDataProvider));

        //PANES
        JPanel rootPane = (JPanel) this.getContentPane();
        JPanel menuPane = new JPanel();
        tablePane = new JPanel();
        //menuPane.setBackground(new Color(250, 250, 250));
        //tablePane.setBackground(new Color(250, 250, 250));


        tablePane.add(paginatedDecorator.getContentPanel());
        tablePane.setLayout(new GridLayout(1, 1));

        menuPane.setLayout(new FlowLayout());
        menuPane.add(addBtn);
        menuPane.add(backBtn);
        menuPane.add(allBtn);
        menuPane.add(nowBtn);

        rootPane.setLayout(new BoxLayout(rootPane, BoxLayout.PAGE_AXIS));
        //rootPane.setBackground(new Color(250, 250, 250));
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

        ReservationListGUI ReservatioListGUI = new ReservationListGUI();
        ReservatioListGUI.setVisible(true);
        ReservatioListGUI.setSize(new Dimension(800, 410));
        ReservatioListGUI.setLocationRelativeTo(null);
    }

    private static TableModel createObjectDataModel() {
        return new ObjectTableModel<Reservation>() {
            @Override
            public Object getValueAt(Reservation r, int columnIndex) {
                switch (columnIndex) {
                    case 0:
                        return r.getNumber();
                    case 1:
                        return r.getClient().getFirstname() + " " + r.getClient().getLastname();
                    case 2:
                        return r.getOccupation();
                    case 3:
                        return r.getDate().format(dateFormat);
                    case 4:
                        return r.getTotal_price();
                    case 5:
                        return r.getCb_number();
                    case 6:
                        return r.getUpdated();

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
                        return "Client";
                    case 2:
                        return "Nombre de pers";
                    case 3:
                        return "Date de la reservation";
                    case 4:
                        return "Prix Toal";
                    case 5:
                        return "Carte Bancaire";
                    case 6:
                        return "Effectuée le";
                }
                return null;
            }
        };
    }

    private static PaginationDataProvider<Reservation> createDataProviderNow() {

        final List<Reservation> reservations = ReservationRepository.getReservationByDate(LocalDate.now());

        return new PaginationDataProvider<Reservation>() {
            @Override
            public int getTotalRowCount() {
                return reservations.size();
            }

            @Override
            public List<Reservation> getRows(int startIndex, int endIndex) {
                return reservations.subList(startIndex, endIndex);
            }
        };
    }

    private static PaginationDataProvider<Reservation> createDataProviderAll() {

        final List<Reservation> reservations = ReservationRepository.getAllReservation();

        return new PaginationDataProvider<Reservation>() {
            public int getTotalRowCount() {
                return reservations.size();
            }

            @Override
            public List<Reservation> getRows(int startIndex, int endIndex) {
                return reservations.subList(startIndex, endIndex);
            }
        };
    }

    private void selectionListener(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
            this.dispose();
            Reservation r = ReservationRepository.getReservationById(Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString()));
            ReservationEditGUI.load(r, r.getClient());
        }
    }

    private void reload(PaginationDataProvider<Reservation> dataProvider) {
        paginatedDecorator = PaginatedTableDecorator.decorate(table, dataProvider, new int[]{15, 30, 50, 75, 100}, 15);
        tablePane.removeAll();
        tablePane.add(paginatedDecorator.getContentPanel());
    }


    private void addBtnListener(ActionEvent e) {
        this.dispose();
        ReservationAddGUI.load();
    }

    private void backBtnListener(ActionEvent e) {
        System.out.println("Fermeture de l'application...");
        this.dispose();
    }


}
