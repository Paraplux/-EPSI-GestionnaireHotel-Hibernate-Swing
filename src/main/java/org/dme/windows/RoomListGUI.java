package org.dme.windows;

import com.formdev.flatlaf.FlatIntelliJLaf;
import org.dme.entities.Room;
import org.dme.pagination.PaginatedTableDecorator;
import org.dme.repositories.RegistrationRepository;
import org.dme.repositories.RoomRepository;
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


public class RoomListGUI extends JFrame {

    private static JTable table;
    private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private JPanel tablePane;
    private org.dme.interfaces.PaginationDataProvider<Room> dataProvider;
    private PaginatedTableDecorator<Room> paginatedDecorator;
    //BUTTONS
    private JButton addBtn = new JButton("Ajouter");
    private JButton backBtn = new JButton("Retour");

    public RoomListGUI() {
        super();

        setTitle("Gestion des chambres");
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

        RoomListGUI roomListGUI = new RoomListGUI();
        roomListGUI.setVisible(true);
        roomListGUI.setSize(new Dimension(800, 410));
        roomListGUI.setLocationRelativeTo(null);
    }

    private static TableModel createObjectDataModel() {

        System.out.println("createObjectDataModel() fired");
        return new ObjectTableModel<Room>() {
            @Override
            public Object getValueAt(Room r, int columnIndex) {
                switch (columnIndex) {
                    case 0:
                        return r.getNumber();
                    case 1:
                        return RegistrationRepository.checkIfRoomIsOccupiedByNow(r);
                    case 2:
                        return r.getRegistrations().size() > 0 ? r.getRegistrations().size() : "Aucune";
                    case 3:
                        return r.getFinaleBasePrice();
                    case 4:
                        return r.getActive();
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
                        return "Numéro de chambre";
                    case 1:
                        return "Occupation actuelle";
                    case 2:
                        return "Enregistrement";
                    case 3:
                        return "Prix";
                    case 4:
                        return "Active";
                }
                return null;
            }
        };
    }

    private static org.dme.interfaces.PaginationDataProvider<Room> createDataProvider() {

        final List<Room> rooms = RoomRepository.getAllRoomsAndIgnoreWhere();

        System.out.println(rooms);

        return new org.dme.interfaces.PaginationDataProvider<Room>() {
            @Override
            public int getTotalRowCount() {
                return rooms.size();
            }

            @Override
            public List<Room> getRows(int startIndex, int endIndex) {
                return rooms.subList(startIndex, endIndex);
            }
        };
    }

    private void selectionListener(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
            this.dispose();
            RoomEditGUI.load(RoomRepository.getRoomByNumber(Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString())));
        }
    }

    private void addBtnListener(ActionEvent e) {
        this.dispose();
        RoomAddGUI.load();
    }

    private void backBtnListener(ActionEvent e) {
        System.out.println("Fermeture de l'application...");
        this.dispose();
    }

}
