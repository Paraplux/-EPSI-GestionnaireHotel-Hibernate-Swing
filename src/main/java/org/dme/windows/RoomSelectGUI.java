package org.dme.windows;


import com.formdev.flatlaf.FlatIntelliJLaf;
import org.dme.entities.Room;
import org.dme.pagination.PaginatedTableDecorator;
import org.dme.repositories.RoomRepository;
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

public class RoomSelectGUI extends JFrame {


    private static JTable table;
    private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private JPanel tablePane;
    private org.dme.interfaces.PaginationDataProvider<Room> dataProvider;
    private PaginatedTableDecorator<Room> paginatedDecorator;
    //BUTTONS
    private JButton addBtn = new JButton("Ajouter");
    private JButton backBtn = new JButton("Retour");

    public RoomSelectGUI() {

        super();

        setTitle("Selection d'une chambre");
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

        RoomSelectGUI roomListGUI = new RoomSelectGUI();
        roomListGUI.setVisible(true);
        roomListGUI.setSize(new Dimension(800, 410));
        roomListGUI.setLocationRelativeTo(null);
    }

    private static TableModel createObjectDataModel() {
        return new ObjectTableModel<Room>() {
            @Override
            public Object getValueAt(Room r, int columnIndex) {
                switch (columnIndex) {
                    case 0:
                        return r.getNumber();
                    case 1:
                        return r.getActive();

                }
                return null;
            }

            @Override
            public int getColumnCount() {
                return 2;
            }

            @Override
            public String getColumnName(int column) {
                switch (column) {
                    case 0:
                        return "Numéro de chambre";
                    case 1:
                        return "Active";
                }
                return null;
            }
        };
    }

    private static org.dme.interfaces.PaginationDataProvider<Room> createDataProvider() {

        final List<Room> room = RoomRepository.getAllAvailableRoomByDate(TempData.getTempsRegistrationReservation());

        return new org.dme.interfaces.PaginationDataProvider<Room>() {
            @Override
            public int getTotalRowCount() {
                return room.size();
            }

            @Override
            public List<Room> getRows(int startIndex, int endIndex) {
                return room.subList(startIndex, endIndex);
            }
        };
    }

    private void selectionListener(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
            this.dispose();
            RegistrationAddGUI.load(RoomRepository.getRoomByNumber(Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString())));
        }
    }
}
