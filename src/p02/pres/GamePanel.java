package p02.pres;

import p02.game.Board;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GamePanel extends JPanel {
    private int[] road;
    private int[] sideLines;
    private JTable table;
    private DefaultTableModel model;
    private Board board;
    private int numRows = 7;
    private int numCols = 5;

    public GamePanel(Board board) {
        this.board = board;
        this.board.addObserver(this::refreshTable);
        setLayout(new BorderLayout());
        initializeTable();
    }

    private void initializeTable() {
        Object[][] data = new Object[numRows][numCols];
        updateData(data);

        String[] columnNames = {"Left", "Lane 1", "Lane 2", "Lane 3", "Right"};

        model = new DefaultTableModel(data, columnNames);
        table = new JTable(model);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            ImageIcon pothole = new ImageIcon("pothole.png");
            ImageIcon car = new ImageIcon("car.png");
            ImageIcon line = new ImageIcon("white.jpg");
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                setIcon(null);
                setText("");

                if (column==0 || column==numCols-1) {
                    if (value != null && value.equals('1')) {
                        Image img = line.getImage();
                        int cellWidth = table.getColumnModel().getColumn(column).getWidth();
                        int cellHeight = table.getRowHeight(row);
                        Image scaledImg = img.getScaledInstance(cellWidth, cellHeight, Image.SCALE_SMOOTH);
                        setIcon(new ImageIcon(scaledImg));
                    }
                }else if (row == numRows - 1) {
                    if (value != null && value.equals('1')) {
                        Image img = car.getImage();
                        int cellWidth = table.getColumnModel().getColumn(column).getWidth();
                        int cellHeight = table.getRowHeight(row);
                        Image scaledImg = img.getScaledInstance(cellWidth, cellHeight, Image.SCALE_SMOOTH);
                        setIcon(new ImageIcon(scaledImg));
                    }
                } else {
                    if (value != null && value.equals('1')) {
                        Image img = pothole.getImage();
                        int cellWidth = table.getColumnModel().getColumn(column).getWidth();
                        int cellHeight = table.getRowHeight(row);
                        Image scaledImg = img.getScaledInstance(cellWidth, cellHeight, Image.SCALE_SMOOTH);
                        setIcon(new ImageIcon(scaledImg));
                    }
                }
                return component;
            }
        };

        for (int i = 0; i < numCols; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        int[] columnWidths = {50, 150, 150, 150, 50};
        for (int i = 0; i < numCols; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
        }

        int startingRowHeight = 20;
        for (int i = 0; i < numRows; i++) {
            table.setRowHeight(i, startingRowHeight + i * 20);
        }

        table.setFillsViewportHeight(true);

        table.setTableHeader(null);
        table.setGridColor(Color.black);
        table.setBackground(Color.black);

        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);
        addKeyListener(board);
    }

    public void refreshTable() {
        Object[][] data = new Object[numRows][numCols];
        updateData(data);
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                model.setValueAt(data[i][j], i, j);
            }
        }
    }

    private void updateData(Object[][] data) {
        this.sideLines = board.getSideLines();
        this.road = board.getRoad();

        for (int i = 0; i < numRows; i++) {
            int value = road[numRows - 1 - i];
            String binaryString = Integer.toBinaryString(value);

            while (binaryString.length() < numCols - 2) {
                binaryString = "0" + binaryString;
            }

            for (int j = 1; j < numCols - 1; j++) {
                data[i][j] = binaryString.charAt(j - 1);
            }

            if (sideLines[6 - i] == 1) {
                data[i][0] = '1';
                data[i][4] = '1';
            } else {
                data[i][0] = "";
                data[i][4] = "";
            }
        }
    }
}
