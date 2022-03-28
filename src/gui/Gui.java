package gui;

import com.toedter.calendar.JDateChooserCellEditor;
import exception.*;
import flightressources.*;
import io.FileManager;
import threads.ControlTowerRunnable;
import threads.FlightRunnable;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class Gui extends JFrame {

    private JPanel panel;
    private int width;
    private int height;
    private JTable flightPlanTable;
    private Object[][] flightData;
    private List<Flight> flightList;
    private JScrollPane scrollPane;
    private JButton add = new JButton();
    JTable flightTable;

    private Map<String, FlightInformation> flightInformation = new HashMap<>();

    private JTextArea distanceCovered;
    private JTextArea timeTaken;
    private JTextArea consumedFuel;
    private JTextArea co2emitted;

    private String selectedFlightCode = "";

    private Gui() throws IOException, ResourceNotFoundException {
        super("Flight Tracker");

        try {
            flightList = new ArrayList<>(FileManager.getDefaultFlights());
        } catch (IOException ioe) {
			/*if(ioe.getMessage().contains("Flights.txt")){
	            JOptionPane.showMessageDialog(null, ioe.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
	            flightList = new ArrayList<>();
			} else {*/
            JOptionPane.showMessageDialog(null, ioe.getMessage() + "\nCannot start application.", "Error", JOptionPane.ERROR_MESSAGE);
            throw ioe;
            //}
        } catch (InvalidFlightException | InvalidPlaneException | InvalidAirportException
                | InvalidFlightPlanException | InvalidAirlineException e1) {
            JOptionPane.showMessageDialog(null, e1.getMessage());
        }

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        width = dimension.width;
        height = dimension.height;
        setSize(new Dimension(width, height));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel generalPanel = new JPanel();

        /*
         * Existing flights on the table
         * */
        try {
            flightData = getFlights();
        } catch (IOException ioe) {
            //Do nothing, error message is already displayed above (line 42)
            flightData = new String[1][1];
        } catch (InvalidFlightException | InvalidPlaneException | InvalidAirportException
                | InvalidFlightPlanException | InvalidAirlineException e1) {
            JOptionPane.showMessageDialog(null, e1.getMessage());
        }

        try {
            List<Airport> airports = FileManager.loadAirports();
            for (Airport airport : airports) {
                ((ControlTowerRunnable) airport.getControlTower()).registerObserver(this);
            }
        } catch (InvalidAirportException e1) {
            JOptionPane.showMessageDialog(null, e1.getMessage());
        }

        DefaultTableModel defaultFlightTableModel = new DefaultTableModel();
        defaultFlightTableModel.setDataVector(flightData, getFlightColumnHeader());

        flightTable = new JTable(defaultFlightTableModel);
        flightTable.setRowHeight(25);

        scrollPane = new JScrollPane(flightTable);

        scrollPane.setPreferredSize(new Dimension((width * 69) / 100, (height * 45) / 100));
        flightTable.setFillsViewportHeight(true);

        panel = new JPanel();
        panel.add(scrollPane);
        panel.setPreferredSize(new Dimension((width * 70) / 100, height / 2));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(),
                "Flights", TitledBorder.CENTER, TitledBorder.TOP,
                new Font("tahoma", Font.PLAIN, 20)));
        generalPanel.add(panel);

        fillFlightPlanTable(flightData[0][0], generalPanel);

        /*
         * Setting the labels for distance covered, Co2 emission and time taken
         * */
        JLabel distanceCoveredLabel = new JLabel("Distance (km) :");
        JLabel timeTakenLabel = new JLabel("Time (hr):");
        JLabel consumedFuelLabel = new JLabel("Fuel Consumption (litre):");
        JLabel co2ConsumptionLabel = new JLabel("CO2 (kg):");

        distanceCovered = new JTextArea(1, 5);
        distanceCovered.setFont(new Font("tahoma", Font.PLAIN, 18));
        distanceCovered.setEditable(false);

        timeTaken = new JTextArea(1, 5);
        timeTaken.setFont(new Font("tahoma", Font.PLAIN, 18));
        timeTaken.setEditable(false);

        consumedFuel = new JTextArea(1, 5);
        consumedFuel.setFont(new Font("tahoma", Font.PLAIN, 18));
        consumedFuel.setEditable(false);

        co2emitted = new JTextArea(1, 5);
        co2emitted.setFont(new Font("tahoma", Font.PLAIN, 18));
        co2emitted.setEditable(false);


        JPanel labeAndTextPanel = new JPanel(new GridLayout(8, 2));
        labeAndTextPanel.setPreferredSize(new Dimension((width * 11) / 100, (height * 45) / 100));
        labeAndTextPanel.add(distanceCoveredLabel);
        labeAndTextPanel.add(distanceCovered);
        labeAndTextPanel.add(timeTakenLabel);
        labeAndTextPanel.add(timeTaken);
        labeAndTextPanel.add(consumedFuelLabel);
        labeAndTextPanel.add(consumedFuel);
        labeAndTextPanel.add(co2ConsumptionLabel);
        labeAndTextPanel.add(co2emitted);
        panel = new JPanel();
        panel.add(labeAndTextPanel);
        panel.setPreferredSize(new Dimension((width * 12) / 100, height / 2));

        generalPanel.add(panel);

        JPanel leftBasePanel = new JPanel();
        leftBasePanel.setPreferredSize(new Dimension(new Dimension((width * 68) / 100, height / 3)));

        generalPanel.add(leftBasePanel);

        JPanel rightBasePanel = new JPanel();
//        rightBasePanel.setBackground(Color.red);
        rightBasePanel.setPreferredSize(new Dimension((width * 30) / 100, height / 3));

        generalPanel.add(rightBasePanel);

        /*
         * Add new flight table
         * */
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.setDataVector(new Object[][]{
                {"Choose ...", "", "Choose ...", "Choose ...", "Choose ...", "", ""}
        }, getAddFlightColumnHeader());
        JTable addFlightTable = new JTable(tableModel);
        addFlightTable.setRowHeight(25);
        addFlightTable.getTableHeader().setReorderingAllowed(false);

        try {
            setDropdownItemsOnTable(addFlightTable);
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(null, ioe.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
            throw ioe;
        } catch (InvalidAirlineException | InvalidPlaneException | InvalidAirportException e1) {
            JOptionPane.showMessageDialog(null, e1.getMessage());
        }

        scrollPane = new JScrollPane(addFlightTable);

        scrollPane.setPreferredSize(new Dimension((width * 66) / 100, (height * 10) / 100));
        flightTable.setFillsViewportHeight(true);

        panel = new JPanel();
        panel.add(scrollPane);
        panel.setPreferredSize(new Dimension((width * 68) / 100, height / 6));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(),
                "Add Flights", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("tahoma", Font.PLAIN, 20)));
        leftBasePanel.add(panel);

        panel = new JPanel();
        JLabel label = new JLabel("GUI Interval (sec): ");
        label.setFont(new Font("tahoma", Font.BOLD, 14));
        panel.add(label);
        panel.setPreferredSize(new Dimension((width * 18) / 100, height / 20));
        rightBasePanel.add(panel);

        panel = new JPanel();
        Integer[] codes = getTimeIntervals();
        JComboBox<Integer> timeIntervals = new JComboBox<>(codes);
        timeIntervals.setPreferredSize(new Dimension((width * 9) / 100, height / 20));
        panel.add(timeIntervals);
        panel.setPreferredSize(new Dimension((width * 10) / 100, height / 20));
        rightBasePanel.add(panel);

        panel = new JPanel();
        JLabel label1 = new JLabel("Flight Interval (sec): ");
        label1.setFont(new Font("tahoma", Font.BOLD, 14));
        panel.add(label1);
        panel.setPreferredSize(new Dimension((width * 18) / 100, height / 20));
        rightBasePanel.add(panel);

        panel = new JPanel();
        Integer[] flightTimes = getTimeIntervals();
        JComboBox<Integer> timeIntervalComboBox = new JComboBox<>(flightTimes);
        timeIntervalComboBox.setPreferredSize(new Dimension((width * 9) / 100, height / 20));
        panel.add(timeIntervalComboBox);
        panel.setPreferredSize(new Dimension((width * 10) / 100, height / 20));
        rightBasePanel.add(panel);

        timeIntervalComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
            }
        });

        /*
         * Adding flight Plan to a flight*/
        DefaultTableModel addFlightPlanTableModel = new DefaultTableModel();
        addFlightPlanTableModel.setDataVector(new Object[][]{
                {"Choose...", "Choose...", "Choose...", "Choose...", "Choose...", "Choose...", "Choose..."}
        }, new Object[]{"", "", "", "", "", "", ""});

        JTable addFlightPlanTable = new JTable(addFlightPlanTableModel);
        addFlightPlanTable.setRowHeight(25);

        try {
            setDropDownItemOnAddFlightPlanTable(addFlightPlanTable);
        } catch (IOException | InvalidAirportException ioe) {
            JOptionPane.showMessageDialog(null, ioe.getMessage());
        }

        scrollPane = new JScrollPane(addFlightPlanTable);
        scrollPane.setPreferredSize(new Dimension((width * 66) / 100, (height * 10) / 100));
        flightTable.setFillsViewportHeight(true);
        panel = new JPanel();
        panel.add(scrollPane);
        panel.setPreferredSize(new Dimension((width * 68) / 100, height / 8));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(),
                "Flight Plan", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("tahoma", Font.PLAIN, 20)));
        leftBasePanel.add(panel);

        panel = new JPanel();
        DefaultTableModel currentFlightTableModel = new DefaultTableModel();
        currentFlightTableModel.setDataVector(new String[][]{}, new String[]{"Current Flights", "Destination Flights"});
        JTable currentlyInFlightTable = new JTable(currentFlightTableModel);
        JTableHeader tableHeader = currentlyInFlightTable.getTableHeader();
        tableHeader.setFont(new Font("tahoma", Font.BOLD, 15));
        currentlyInFlightTable.setRowHeight(25);
        scrollPane = new JScrollPane(currentlyInFlightTable);
        scrollPane.setPreferredSize(new Dimension((width * 30) / 100, height / 4));
        panel.add(scrollPane);
        panel.setPreferredSize(new Dimension((width * 30) / 100, height / 4));
        rightBasePanel.add(panel);

        /*
         * Adding buttons to the panel
         * */
        panel = new JPanel();
        add = createButton("Add", width, height);
        panel.add(add);
        panel.setPreferredSize(new Dimension((width * 20) / 100, height / 6));
        generalPanel.add(panel);

        panel = new JPanel();
        JButton cancel = createButton("Cancel", width, height);
        panel.add(cancel);
        panel.setPreferredSize(new Dimension((width * 56) / 100, height / 6));
        generalPanel.add(panel);

        panel = new JPanel();
        JButton exit = createButton("Exit", width, height);
        panel.add(exit);
        panel.setPreferredSize(new Dimension((width * 21) / 100, height / 6));
        generalPanel.add(panel);

        /*Add ActionListener to exit*/
        exit.addActionListener(e -> {
            try {
                FileManager.writeFlightDataToReport(flightList);
                FileManager.writeFlightDataToFlightFile(flightList);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            System.exit(0);
        });

        /*Add action listener to cancel*/
        cancel.addActionListener(e -> {
            try {
                resetTables(addFlightTable, addFlightPlanTable);
            } catch (IOException | InvalidAirlineException | InvalidPlaneException | InvalidAirportException ex) {
                ex.printStackTrace();
            }
        });

        add.addActionListener(e -> {
            String[][] details = new String[2][7];
            String column = addFlightTable.getValueAt(0, 1).toString();
            if (column.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a unique flight code.");
                return;
            }
            if (addFlightTable.getValueAt(0, 5).toString().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please make sure flight date is selected.");
                return;
            }
            if (addFlightTable.getValueAt(0, 6).toString().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please make sure flight time is selected");
                return;
            }
            details[0] = getTableValues(addFlightTable);
            if (details[0] == null) {
                return;
            }
            details[1] = getTableValues(addFlightPlanTable);
            if (details[1] == null)
                return;

            String date = details[0][5];

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
            LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);
            details[0][5] = localDateTime.toLocalDate().toString();

            String[] newData = Arrays.copyOfRange(details[0], 1, details[0].length);
            String[] copy = Arrays.copyOf(newData, newData.length + 3);
            try {
                copy[0] = getAirlineByName(details[0][0]).getCode() + copy[0];
            } catch (InvalidAirlineException | IOException | ResourceNotFoundException e1) {
                JOptionPane.showMessageDialog(null, e1.getMessage());
            }

            try {
                Airport airportByCode = getAirportByCode(details[0][4]);
                GPSCoordinate coordinates = airportByCode.getControlTower().getCoordinates();
                copy[newData.length] = coordinates.getLatitude();
                copy[newData.length + 1] = coordinates.getLongitude();
                copy[newData.length + 2] = "On-Flight";
            } catch (IOException | ResourceNotFoundException | InvalidAirportException ex) {
                ex.printStackTrace();
            }

            boolean match = Arrays.stream((String[][]) flightData).noneMatch(da -> da[0].equalsIgnoreCase(copy[1]));
            String[][] copyOf;
            if (match) {
                try {
                    createNewFlight(flightList, details);
                    resetTables(addFlightTable, addFlightPlanTable);
                } catch (ResourceNotFoundException | IOException | InvalidFlightException | InvalidAirportException | InvalidFlightPlanException | InvalidPlaneException | InvalidAirlineException rnfe) {
                    JOptionPane.showMessageDialog(null, rnfe.getMessage());
                    return;
                }
                copyOf = Arrays.copyOf((String[][]) flightData, flightData.length + 1);
                copyOf[flightData.length] = copy;
                flightData = copyOf;
                DefaultTableModel flightTableModel = (DefaultTableModel) flightTable.getModel();
                flightTableModel.addRow(copy);

            } else {
                JOptionPane.showMessageDialog(null, "Flight Code already exist.");
            }

        });

        add(generalPanel);

        ListSelectionModel selectionModel = flightTable.getSelectionModel();


        selectionModel.addListSelectionListener(e -> {
            if (!flightTable.getSelectionModel().getValueIsAdjusting()) {
                String flightCode = (String) flightTable.getValueAt(flightTable.getSelectedRow(), 0);
                selectedFlightCode = flightCode;
                Object[][] flightPlan = new String[0][];
                try {
                    flightPlan = getFlightPlan(flightCode);
                } catch (ResourceNotFoundException ex) {
                    System.out.println(ex.getMessage());
                }
                DefaultTableModel flightPlanDefaultTableModel = new DefaultTableModel(flightPlan, new String[]{"", ""}) {
                    public Class getColumnClass(int column) {
                        return getValueAt(0, column).getClass();
                    }
                };
                flightPlanTable.setModel(flightPlanDefaultTableModel);

                distanceCovered.setText(getCurrentDistanceCovered(flightCode));
                timeTaken.setText(getCurrentTimeTaken(flightCode));
                co2emitted.setText(getCurrentCO2Emitted(flightCode));
                consumedFuel.setText(getCurrentConsumedFuel(flightCode));

                FlightInformation flightInformation = this.flightInformation.get(selectedFlightCode);
                updateFlightPlanWithIcon(flightInformation);
            }
        });

        ListSelectionModel flightPlanTableSelectionModel = flightPlanTable.getSelectionModel();
        flightPlanTableSelectionModel.addListSelectionListener(e -> {
            if (!flightPlanTableSelectionModel.isSelectionEmpty()) {
                String airportCode = (String) flightPlanTable.getValueAt(flightPlanTable.getSelectedRow(), 0);
                try {
                    Airport airportByCode = getAirportByCode(airportCode);
                    List<Flight> flightsHavingThisAirportAsDestination = flightList.stream()
                            .filter(flight -> flight.getDestinationAirport().equals(airportByCode))
                            .collect(Collectors.toList());
                    ControlTower controlTower = airportByCode.getControlTower();
                    List<FlightInformation> flightsCommunicatingWithThisAirport = new ArrayList<>();
                    for (Map.Entry<String, FlightInformation> entry : flightInformation.entrySet()) {
                        FlightInformation flightInformation = entry.getValue();
                        if (!flightInformation.isLanded() && flightInformation.getNearestControlTower().equals(controlTower)) {
                            flightsCommunicatingWithThisAirport.add(flightInformation);
                        }
                    }
                    String[][] currentFlightData = getCurrentFlightData(flightsCommunicatingWithThisAirport, flightsHavingThisAirportAsDestination);
                    currentlyInFlightTable.setModel(new DefaultTableModel(currentFlightData, new String[]{"Current Flights", "Destination Flight"}));
                } catch (IOException | InvalidAirportException | ResourceNotFoundException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });

        setWindowListener();

    }

    private String[][] getCurrentFlightData(List<FlightInformation> flightInformationList, List<Flight> flightList) {
        String[][] data = new String[flightInformationList.size() + flightList.size()][2];
        if (flightInformationList.isEmpty()) {
            data = new String[1][2];
            data[0][0] = "No Flight";
        } else {
            for (int i = 0; i < flightInformationList.size(); i++) {
                data[i][0] = flightInformationList.get(i).getFlightIdentifier();
            }
        }
        if (flightList.isEmpty()) {
            data[0][1] = "No Flight";
        } else {
            for (int i = 0; i < flightList.size(); i++) {
                data[i][1] = flightList.get(i).getIdentifier();
            }
        }
        return data;
    }

    private Integer[] getTimeIntervals() {
        Integer[] times = new Integer[30];
        for (int i = 3; i < 30; i++) {
            times[i - 3] = i;
        }
        return times;
    }

    private void setWindowListener() {
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    FileManager.writeFlightDataToReport(flightList);
                    FileManager.writeFlightDataToFlightFile(flightList);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
                System.exit(0);
            }
        });
    }

    private void resetTables(JTable addFlightTable, JTable addFlightPlanTable) throws IOException, InvalidAirlineException, InvalidPlaneException, InvalidAirportException {
        addFlightTable.setModel(new DefaultTableModel(new Object[][]{
                {"Choose ...", "", "Choose ...", "Choose ...", "Choose ...", "", ""}
        }, getAddFlightColumnHeader()));
        setDropdownItemsOnTable(addFlightTable);

        addFlightTable.getTableHeader().setReorderingAllowed(false);

        addFlightPlanTable.setModel(new DefaultTableModel(new Object[][]{
                {"Choose...", "Choose...", "Choose...", "Choose...", "Choose...", "Choose...", "Choose..."}
        }, new Object[]{"", "", "", "", "", "", ""}));
        setDropDownItemOnAddFlightPlanTable(addFlightPlanTable);
    }

    private void createNewFlight(List<Flight> flightList, String[][] details) throws IOException, ResourceNotFoundException, InvalidFlightException, InvalidFlightPlanException, InvalidAirportException, InvalidPlaneException, InvalidAirlineException {
        Flight flight = new FlightRunnable();
        try {
            flight.setIdentifier(getAirlineByName(details[0][0]).getCode() + details[0][1]);
        } catch (ResourceNotFoundException rnfe) {
            throw new ResourceNotFoundException("No airline selected");
        }
        try {
            flight.setDepartureAirport(getAirportByCode(details[0][3]));
        } catch (ResourceNotFoundException rnfe) {
            throw new ResourceNotFoundException("No departure airport selected");
        }
        try {
            flight.setDestinationAirport(getAirportByCode(details[0][4]));
        } catch (ResourceNotFoundException rnfe) {
            throw new ResourceNotFoundException("No destination airport selected");
        }
        try {
            flight.setPlane(getPlaneByCode(details[0][2]));
        } catch (ResourceNotFoundException rnfe) {
            throw new ResourceNotFoundException("No plane selected");
        }
        try {
            flight.setAirline(getAirlineByName(details[0][0]));
        } catch (ResourceNotFoundException rnfe) {
            throw new ResourceNotFoundException("No airline selected");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime localDateTime = LocalDateTime
                .parse(details[0][5] + " " + details[0][6], formatter)
                .atZone(ZoneId.of("CET")).toLocalDateTime();
        flight.setDepartureDateTime(localDateTime);
        List<Airport> airports = Arrays.stream(details[1])
                .filter(code -> !code.contains("Choose"))
                .map(code -> {
                    try {
                        return getAirportByCode(code);
                    } catch (IOException | ResourceNotFoundException | InvalidAirportException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).filter(Objects::nonNull).collect(Collectors.toList());
        flight.setFlightPlan(new FlightPlan(new LinkedList<>(airports)));

        Thread flightThread = new Thread((FlightRunnable) flight);
        flightThread.start();
        flightList.add(flight);
    }

    private Airline getAirlineByName(String airlineName) throws IOException, ResourceNotFoundException, InvalidAirlineException {
        Optional<Airline> optionalAirline = FileManager.loadAirlines()
                .stream()
                .filter(airline -> airline.getName().equalsIgnoreCase(airlineName))
                .findFirst();
        if (!optionalAirline.isPresent()) {
            throw new ResourceNotFoundException("Airline not found");
        }
        return optionalAirline.get();
    }

    private Aeroplane getPlaneByCode(String code) throws IOException, ResourceNotFoundException, InvalidPlaneException {
        Optional<Aeroplane> optionalAeroplane = FileManager.loadAeroplanes()
                .stream()
                .filter(aeroplane -> aeroplane.getModel().equalsIgnoreCase(code))
                .findFirst();
        if (!optionalAeroplane.isPresent()) {
            throw new ResourceNotFoundException("Plane not found");
        }
        return optionalAeroplane.get();
    }

    private Airport getAirportByCode(String code) throws IOException, ResourceNotFoundException, InvalidAirportException {
        Optional<Airport> optionalAirport = FileManager.loadAirports()
                .stream()
                .filter(airport -> airport.getCode().equalsIgnoreCase(code))
                .findFirst();
        if (!optionalAirport.isPresent()) {
            throw new ResourceNotFoundException("Airport not found");
        }
        return optionalAirport.get();
    }

    private String[] getTableValues(JTable table) {
        String[] columnValues = new String[7];
        try {
            int columnCount = table.getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                columnValues[i] = table.getValueAt(0, i).toString();
            }
            return columnValues;
        } catch (ArrayIndexOutOfBoundsException | NullPointerException aioobe) {
            JOptionPane.showMessageDialog(null, "An error occurred. Please enter flight details.");
        }

        return null;
    }

    private JButton createButton(String label, int width, int height) {
        JButton button = new JButton(label);
        button.setBackground(new Color(66, 147, 245));
        button.setFont(new Font("tahoma", Font.BOLD, 18));
        button.setPreferredSize(new Dimension(width / 10, height / 26));
        button.setForeground(Color.WHITE);
        return button;
    }

    private void setDropDownItemOnAddFlightPlanTable(JTable addFlightTable) throws IOException, InvalidAirportException {
        for (int i = 0; i < 7; i++) {
            String[] codes = getAirportCodes();
            JComboBox<String> airportCodeDropDown = new JComboBox<>(codes);
            TableColumn airportCodeColumn = addFlightTable.getColumnModel().getColumn(i);
            airportCodeColumn.setCellEditor(new DefaultCellEditor(airportCodeDropDown));
        }
    }

    private void setDropdownItemsOnTable(JTable addFlightTable) throws IOException, InvalidAirlineException, InvalidPlaneException, InvalidAirportException {
        /*Add Airlines Dropdown*/
        String[] airlines = getAirlineNames();
        JComboBox<String> airlinesDropDown = new JComboBox<>(airlines);
        TableColumn airlineColumn = addFlightTable.getColumnModel().getColumn(0);
        airlineColumn.setCellEditor(new DefaultCellEditor(airlinesDropDown));

        /*Add Planes dropdown*/
        String[] planes = getPlaneCodes();
        JComboBox<String> planeModelDropDown = new JComboBox<>(planes);
        TableColumn planesColumn = addFlightTable.getColumnModel().getColumn(2);
        planesColumn.setCellEditor(new DefaultCellEditor(planeModelDropDown));

        /*Add Departure airport dropdown*/
        String[] departureAirports = getAirportCodes();
        JComboBox<String> departureAirportDropDown = new JComboBox<>(departureAirports);
        TableColumn departureAirportColumn = addFlightTable.getColumnModel().getColumn(3);
        departureAirportColumn.setCellEditor(new DefaultCellEditor(departureAirportDropDown));

        /*Add destination airport dropdown*/
        String[] destinationAirport = getAirportCodes();
        JComboBox<String> destinationAirportDropdown = new JComboBox<>(destinationAirport);
        TableColumn destinationAirportColumn = addFlightTable.getColumnModel().getColumn(4);
        destinationAirportColumn.setCellEditor(new DefaultCellEditor(destinationAirportDropdown));

        /*Add Departure date*/
        JDateChooserCellEditor chooserCellEditor = new JDateChooserCellEditor();
        TableColumn departureDateColumn = addFlightTable.getColumnModel().getColumn(5);
        departureDateColumn.setCellEditor(chooserCellEditor);

        /*Add departure time*/
        String[] times = getFlightTimes();
        JComboBox<String> timeDropdown = new JComboBox<>(times);
        TableColumn departureTimeColumn = addFlightTable.getColumnModel().getColumn(6);
        departureTimeColumn.setCellEditor(new DefaultCellEditor(timeDropdown));
    }

    private String[] getFlightTimes() {
        List<String> times = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            int j = 0;
            while (j < 60) {
                String format = String.format("%02d:%02d", i, j);
                times.add(format);
                j += 10;
            }
        }
        String[] timing = new String[times.size()];
        return times.toArray(timing);
    }

    private String[] getAirportCodes() throws IOException, InvalidAirportException {
        List<String> airportCodeList = FileManager.loadAirports()
                .stream()
                .map(Airport::getCode)
                .collect(Collectors.toList());
        String[] airportCodes = new String[airportCodeList.size()];
        airportCodeList.toArray(airportCodes);
        return airportCodes;
    }

    private String[] getPlaneCodes() throws InvalidPlaneException {
        String[] planeCodes = null;
        try {
            List<String> planeModels = FileManager.loadAeroplanes()
                    .stream()
                    .map(Aeroplane::getModel)
                    .collect(Collectors.toList());
            planeCodes = new String[planeModels.size()];
            planeModels.toArray(planeCodes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return planeCodes;
    }

    private String[] getAirlineNames() throws InvalidAirlineException, IOException {
        String[] airlines;
        List<String> airlineNames = FileManager.loadAirlines()
                .stream()
                .map(Airline::getName)
                .collect(Collectors.toList());
        airlines = new String[airlineNames.size()];
        airlineNames.toArray(airlines);
        return airlines;
    }

    /*
     * Gets the flight plan for a particular flight
     * */
    private Object[][] getFlightPlan(String flightCode) throws ResourceNotFoundException {
        Flight flight = getFlightWithCode(flightCode);
        List<Airport> airports = flight.getFlightPlan().getAirports();
        flightData = new Object[airports.size()][];
        Airport[] ports = new Airport[airports.size()];
        airports.toArray(ports);
        for (int i = 0; i < ports.length; i++) {
            Object[] airportData = new Object[]{
                    ports[i].getCode(),
                    ""
            };
            flightData[i] = airportData;
        }
        return flightData;
    }

    /*
     * Get a flight a given unique flight code
     * @throws ResourceNotFoundException if flight is not found.
     * @throws IOException if there's an error reading default data from file.
     * */
    private Flight getFlightWithCode(String code) throws ResourceNotFoundException {
        Optional<Flight> optionalFlight = flightList
                .stream()
                .filter(flight1 -> flight1.getIdentifier().equalsIgnoreCase(code))
                .findFirst();
        if (!optionalFlight.isPresent())
            throw new ResourceNotFoundException("Flight not found.");
        return optionalFlight.get();
    }

    /*
     * Calculates and returns the distance covered for a particular flight.
     *  @throws ResourceNotFoundException if flight is not found.
     * */
    private String getDistanceCovered(String flightCode) {
        String num = null;
        try {
            Flight flight = getFlightWithCode(flightCode);
            DecimalFormat df = new DecimalFormat("###.##");
            num = df.format(flight.distanceCovered());
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return num;
    }

    private String getCurrentDistanceCovered(String flightCode) {
        String num = "0.0";
        if (flightInformation.get(flightCode) != null) {
            num = String.format("%.4f", this.flightInformation.get(flightCode).getCurrentDistance());
        }

        return num;
    }

    /*
     * Calculates and returns the time taken for a particular flight.
     *  @throws ResourceNotFoundException if flight is not found.
     * */
    private String getTimeTaken(String flightCode) {
        String num = null;
        try {
            Flight flight = getFlightWithCode(flightCode);
            DecimalFormat df = new DecimalFormat("###.##");
            num = df.format(flight.timeTaken());
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return num;
    }

    private String getCurrentTimeTaken(String flightCode) {
        String num = "0.0";
        if (flightInformation.get(flightCode) != null) {
            num = String.format("%.4f", this.flightInformation.get(flightCode).getCurrentTime());
        }

        return num;
    }

    /*
     * Calculates and returns the CO2 emitted in kilogram for a particular flight.
     *  @throws ResourceNotFoundException if flight is not found.
     * */
    private String getCO2Emitted(String flightCode) {
        String num = null;
        try {
            Flight flight = getFlightWithCode(flightCode);
            DecimalFormat df = new DecimalFormat("###.##");
            num = df.format(flight.CO2Emission());
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return num;
    }

    private String getCurrentCO2Emitted(String flightCode) {
        String num = "0.0";
        if (flightInformation.get(flightCode) != null) {
            num = String.format("%.4f", this.flightInformation.get(flightCode).getCurrentCO2());
        }

        return num;
    }

    /*
     * Calculates and returns the fuel consume in the course of the flight.
     *  @throws ResourceNotFoundException if flight is not found.
     * */
    private String getConsumedFuel(String flightCode) throws ResourceNotFoundException {
        Flight flight = getFlightWithCode(flightCode);
        DecimalFormat df = new DecimalFormat("###.##");
        return df.format(flight.fuelConsumption());
    }

    private String getCurrentConsumedFuel(String flightCode) {
        String num = "0.0";
        if (flightInformation.get(flightCode) != null) {
            num = String.format("%.4f", this.flightInformation.get(flightCode).getCurrentFuel());
        }

        return num;
    }

    private Object[][] getFlights() throws IOException, InvalidFlightException, InvalidPlaneException, InvalidAirportException, InvalidFlightPlanException, InvalidAirlineException {
        List<Flight> flights = FileManager.getDefaultFlights();
        Flight[] flights1 = new Flight[flights.size()];
        flightData = new String[flights.size()][];
        flights.toArray(flights1);
        for (int i = 0; i < flights1.length; i++) {
            LocalDateTime departureDateTime = flights1[i].getDepartureDateTime();
            String latitude = flights1[i].getDepartureAirport()
                    .getControlTower()
                    .getCoordinates()
                    .getLatitude();
            String longitude = flights1[i].getDepartureAirport()
                    .getControlTower()
                    .getCoordinates()
                    .getLongitude();
            String[] line = new String[]{
                    flights1[i].getIdentifier(),
                    flights1[i].getPlane().getModel(),
                    flights1[i].getDepartureAirport().getCode(),
                    flights1[i].getDestinationAirport().getCode(),
                    departureDateTime.toLocalDate().toString(),
                    departureDateTime.toLocalTime().toString(),
                    latitude,
                    longitude,
                    "On-Flight"
            };

            flightData[i] = line;
        }
        return flightData;
    }

    private String[][] getNewFlightData() throws InvalidPlaneException, InvalidAirportException {
        try {
            List<Aeroplane> aeroplanes = FileManager.loadAeroplanes();
            List<Airport> airports = FileManager.loadAirports();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new String[][]{};
    }

    String[] getFlightColumnHeader() {
        return new String[]{
                "Flight",
                "Plane",
                "Departure",
                "Destination",
                "Date",
                "Time",
                "Latitude",
                "Longitude",
                "Status"
        };
    }

    private void fillFlightPlanTable(Object flightCode, JPanel jPanel) throws ResourceNotFoundException {
        DefaultTableModel flightPlanTableModel =
                new DefaultTableModel(getFlightPlan((String) flightCode), new String[]{"", ""});
        flightPlanTable = new JTable(flightPlanTableModel) {
            public Class getColumnClass(int column) {
                return (column == 1) ? ImageIcon.class : Object.class;
            }
        };
        flightPlanTable.setRowHeight(25);
        flightPlanTable.repaint();
        scrollPane = new JScrollPane(flightPlanTable);
        scrollPane.setPreferredSize(new Dimension((width * 14) / 100, (height * 45) / 100));
        flightPlanTable.setFillsViewportHeight(true);
        panel = new JPanel(new GridBagLayout());
        panel.add(scrollPane);
        panel.setPreferredSize(new Dimension((width * 15) / 100, height / 2));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(),
                "Flight Plan", TitledBorder.CENTER, TitledBorder.TOP,
                new Font("times new roman", Font.PLAIN, 20)));
        jPanel.add(panel);
    }

    String[] getAddFlightColumnHeader() {
        return new String[]{
                "Airline",
                "Number",
                "Plane",
                "Departure",
                "Destination",
                "Date",
                "Time"
        };
    }

    public void updateFlightPlanWithIcon(FlightInformation flightInfo) {
        Optional<Flight> optionalFlight = flightList.stream()
                .filter(flight -> flight.getIdentifier().equalsIgnoreCase(selectedFlightCode))
                .findFirst();
        if (optionalFlight.isPresent()) {
            Flight flight = optionalFlight.get();
            LinkedList<Airport> airportLinkedList = flight.getFlightPlan()
                    .getAirports();
            Optional<Airport> optionalAirport = airportLinkedList.stream()
                    .filter(ap -> ap.getControlTower().equals(flightInfo.getNearestControlTower()))
                    .findFirst();
            if (optionalAirport.isPresent()) {
                Airport airport = optionalAirport.get();
                int index = airportLinkedList.indexOf(airport);
                for (int row = 0; row < index; row++) {
                    ImageIcon imageIcon = new ImageIcon(new ImageIcon("src/images/check.png")
                            .getImage().getScaledInstance(15, 15, Image.SCALE_DEFAULT));
                    flightPlanTable.setValueAt(imageIcon, row, 1);
                }

                ImageIcon imageIcon = new ImageIcon(new ImageIcon("src/images/flight.png")
                        .getImage().getScaledInstance(15, 15, Image.SCALE_DEFAULT));
                flightPlanTable.setValueAt(imageIcon, index, 1);
            }
//                correspondingControlTowers.indexOf(flightInfo.getNearestControlTower());
//                correspondingControlTowers.stream()
//                        .filter(controlTower -> controlTower.equals(flightInfo.getNearestControlTower()))
//                        .findFirst();
        }
    }

    //Update the gui according to the information you got by the control tower runnable
    public void update(ArrayList<FlightInformation> flightInformation) throws InvalidAirportException {
        List<String> flightIdentifiers = new ArrayList<>();
        for (int row = 0; row < flightTable.getRowCount(); row++) {
            flightIdentifiers.add(flightTable.getValueAt(row, 0).toString());
        }
        for (FlightInformation flightInfo : flightInformation) {
            this.flightInformation.put(flightInfo.getFlightIdentifier(), flightInfo);
            GPSCoordinate coordinates = flightInfo.getCurrentGPSCoordinate();
            Optional<String> first = flightIdentifiers.stream()
                    .filter(flightIdentifier -> flightIdentifier.equalsIgnoreCase(flightInfo.getFlightIdentifier()))
                    .findFirst();
            if (first.isPresent()) {
                String s = first.get();
                int index = flightIdentifiers.indexOf(s);
                flightTable.setValueAt(coordinates.getLatitude(), index, flightTable.getColumn("Latitude").getModelIndex());
                flightTable.setValueAt(coordinates.getLongitude(), index, flightTable.getColumn("Longitude").getModelIndex());
                if (flightInfo.isLanded()) {
                    ImageIcon imageIcon = new ImageIcon(new ImageIcon("src/images/check.png")
                            .getImage().getScaledInstance(15, 15, Image.SCALE_DEFAULT));
                    for (int row = 0; row < flightPlanTable.getRowCount(); row++) {
                        flightPlanTable.setValueAt(imageIcon, row, 1);
                    }
                    flightTable.setValueAt("Landed", index, flightTable.getColumn("Status").getModelIndex());
                }
            }
            if (selectedFlightCode.equals(flightInfo.getFlightIdentifier())) {
                updateFlightPlanWithIcon(flightInfo);
                distanceCovered.setText(getCurrentDistanceCovered(selectedFlightCode));
                timeTaken.setText(getCurrentTimeTaken(selectedFlightCode));
                consumedFuel.setText(getCurrentConsumedFuel(selectedFlightCode));
                co2emitted.setText(getCurrentCO2Emitted(selectedFlightCode));
            }
        }
    }

    public static void main(String[] args) {
        try {
            new Gui().setVisible(true);
        } catch (IOException | ResourceNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
