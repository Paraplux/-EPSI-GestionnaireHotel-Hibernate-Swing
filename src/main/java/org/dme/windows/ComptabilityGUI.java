package org.dme.windows;

import com.formdev.flatlaf.FlatIntelliJLaf;
import org.dme.entities.Client;
import org.dme.repositories.ClientRepository;
import org.dme.repositories.RegistrationRepository;
import org.dme.repositories.ReservationRepository;
import org.dme.repositories.RoomRepository;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ComptabilityGUI extends JFrame {

    private JLabel totalMoneyLB = new JLabel("Chiffre d'affaires");
    private JLabel projectedMoneyLB = new JLabel("Prévision");
    private JLabel totalMoneyValue = new JLabel();
    private JLabel projectedMoneyValue = new JLabel();
    private JPanel moneyPanel = new JPanel();


    public ComptabilityGUI() {
        super();
        setTitle("Comptabilité");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //ROOMS STATS
        Integer occupiedRooms = RegistrationRepository.getRegistrationsByDate(LocalDate.now()).size();
        Integer availableRooms = RoomRepository.readRoom().size() - occupiedRooms;

        HashMap<String, Integer> roomsMap = new HashMap<>();
        roomsMap.put("Libres : " + availableRooms, availableRooms);
        roomsMap.put("Occupées : " + occupiedRooms, occupiedRooms);
        DefaultPieDataset roomsDataSet = createDataSetForPieChart(roomsMap);
        JFreeChart roomsChart = createPieChart("Chambres", roomsDataSet);
        ChartPanel roomsPanel = new ChartPanel(roomsChart);


        //RESERVATIONS STATS
        HashMap<String, Integer> reservationsMap = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            //reservationsMap.put(String.valueOf(i), ReservationRepository.getReservationCountByMonth(i));
        }
        DefaultCategoryDataset reservationsDataSet = createDataSetForLineChart(reservationsMap, "Réservations");
        JFreeChart reservationsChart = createLineChart("Réservations", reservationsDataSet);
        ChartPanel reservationsPanel = new ChartPanel(reservationsChart);

        //REGISTRATIONS STATS
        HashMap<String, Integer> registrationsMap = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            registrationsMap.put(String.valueOf(i), RegistrationRepository.getRegistrationCountByMonth(i));
        }
        DefaultCategoryDataset registrationsDataSet = createDataSetForLineChart(registrationsMap, "Enregistrements");
        JFreeChart registrationsChart = createLineChart("Enregistrements", registrationsDataSet);
        ChartPanel registrationsPanel = new ChartPanel(registrationsChart);


        //MONEY STATS
        int totalMoney = 0;
        for (Client c : ClientRepository.getAllClient()) {
            totalMoney += c.turnover();
        }
        int projectedMoney = 0;
        for (Client c : ClientRepository.getAllClient()) {
            projectedMoney += c.projectedTurnover();
        }
        totalMoneyLB.setHorizontalAlignment(JLabel.CENTER);
        totalMoneyLB.setFont(totalMoneyValue.getFont().deriveFont(20F));
        projectedMoneyLB.setHorizontalAlignment(JLabel.CENTER);
        projectedMoneyLB.setFont(totalMoneyValue.getFont().deriveFont(20F));

        totalMoneyValue.setText("+ " + totalMoney + " €");
        totalMoneyValue.setFont(totalMoneyValue.getFont().deriveFont(30F));
        totalMoneyValue.setHorizontalAlignment(JLabel.CENTER);
        totalMoneyValue.setVerticalAlignment(JLabel.TOP);
        totalMoneyValue.setForeground(new Color(50, 150, 100));

        projectedMoneyValue.setText("+ " + projectedMoney + " €");
        projectedMoneyValue.setFont(totalMoneyValue.getFont().deriveFont(30F));
        projectedMoneyValue.setHorizontalAlignment(JLabel.CENTER);
        projectedMoneyValue.setVerticalAlignment(JLabel.TOP);
        projectedMoneyValue.setForeground(new Color(50, 150, 100));

        moneyPanel.setPreferredSize(new Dimension(0, 200));
        moneyPanel.setLayout(new GridLayout(0, 2));
        moneyPanel.setBackground(Color.WHITE);
        moneyPanel.add(totalMoneyLB);
        moneyPanel.add(projectedMoneyLB);
        moneyPanel.add(totalMoneyValue);
        moneyPanel.add(projectedMoneyValue);

        JPanel rootPane = (JPanel) this.getContentPane();
        GridLayout grid = new GridLayout(0, 2, 20, 20);
        rootPane.setBackground(Color.WHITE);
        rootPane.setLayout(grid);

        Dimension chartDimension = new Dimension(400, 262);
        roomsPanel.setPreferredSize(chartDimension);
        reservationsPanel.setPreferredSize(chartDimension);
        registrationsPanel.setPreferredSize(chartDimension);
        rootPane.add(roomsPanel);
        rootPane.add(reservationsPanel);
        rootPane.add(registrationsPanel);
        rootPane.add(moneyPanel);
    }

    public static void load() {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            System.out.println("Bug look and feel");
            e.getStackTrace();
        }

        ComptabilityGUI comptabilityGUI = new ComptabilityGUI();
        comptabilityGUI.pack();
        comptabilityGUI.setVisible(true);
        comptabilityGUI.setLocationRelativeTo(null);
    }

    private DefaultPieDataset createDataSetForPieChart(HashMap<String, Integer> data) {
        DefaultPieDataset dataset = new DefaultPieDataset();

        for (Map.Entry<String, Integer> set : data.entrySet()) {
            dataset.setValue(set.getKey(), set.getValue());
        }
        return dataset;
    }

    private JFreeChart createPieChart(String label, PieDataset dataset) {
        JFreeChart chart = ChartFactory.createPieChart(label, dataset, true, false, false);
        return chart;
    }

    private DefaultCategoryDataset createDataSetForLineChart(HashMap<String, Integer> data, String label) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map.Entry<String, Integer> set : data.entrySet()) {
            dataset.addValue(set.getValue(), label, set.getKey());
        }
        return dataset;
    }

    private JFreeChart createLineChart(String title, CategoryDataset dataset) {
        JFreeChart lineChart = ChartFactory.createLineChart(title, "Mois", "Nombre", dataset, PlotOrientation.VERTICAL, true, true, false);
        return lineChart;
    }

}
