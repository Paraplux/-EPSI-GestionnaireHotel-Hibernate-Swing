package org.dme.windows;

import com.formdev.flatlaf.FlatIntelliJLaf;
import org.dme.entities.Reservation;
import org.dme.pagination.PaginatedTableDecorator;
import org.dme.repositories.ReservationRepository;
import org.dme.utils.ObjectTableModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReservationSelectGUI extends JFrame {


    private static JTable table;
    private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private JPanel tablePane;
    private org.dme.interfaces.PaginationDataProvider<Reservation> dataProvider;
    private PaginatedTableDecorator<Reservation> paginatedDecorator;
    //BUTTONS
    private JButton addBtn = new JButton("Ajouter");
    private JButton backBtn = new JButton("Retour");

    public ReservationSelectGUI() {

        super();

        setTitle("Selection d'une réservation");
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


        tablePane.add(paginatedDecorator.getContentPanel());
        tablePane.setLayout(new GridLayout(1, 1));

        rootPane.setLayout(new BoxLayout(rootPane, BoxLayout.PAGE_AXIS));
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

        ReservationSelectGUI reservationListGUI = new ReservationSelectGUI();
        reservationListGUI.setVisible(true);
        reservationListGUI.setSize(new Dimension(800, 410));
        reservationListGUI.setLocationRelativeTo(null);
    }

    private static TableModel createObjectDataModel() {
        return new ObjectTableModel<Reservation>() {
            @Override
            public Object getValueAt(Reservation r, int columnIndex) {
                switch (columnIndex) {
                    case 0:
                        return r.getNumber();
                    case 1:
                        return r.getDate();
                    case 2:
                        return r.getClient().getFirstname();
                    case 3:
                        return r.getClient().getLastname();
                    case 4:
                        return r.getClient().getAnniversaire().format(dateFormat);
                }
                return null;
            }

            @Override
            public int getColumnCount() {
                return 5;
            }

            @Override
            public String getColumnName(int column) {
                switch (column) {
                    case 0:
                        return "Id";
                    case 1:
                        return "Date de réservation";
                    case 2:
                        return "Prénom";
                    case 3:
                        return "Nom";
                    case 4:
                        return "Date de naissance";
                }
                return null;
            }
        };
    }

    private static org.dme.interfaces.PaginationDataProvider<Reservation> createDataProvider() {

        final List<Reservation> reservation = ReservationRepository.getAllReservation();

        return new org.dme.interfaces.PaginationDataProvider<Reservation>() {
            @Override
            public int getTotalRowCount() {
                return reservation.size();
            }

            @Override
            public List<Reservation> getRows(int startIndex, int endIndex) {
                return reservation.subList(startIndex, endIndex);
            }
        };
    }

    private void selectionListener(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
            this.dispose();

            RegistrationAddGUI.load(ReservationRepository.getReservationById(Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString())));
        }
    }
}
