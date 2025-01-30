package sudoku.graphic;

import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import sudoku.Grid;
import sudoku.Position;
import sudoku.Sudoku;

public class MainGraphic {

    private Grid grid;

    private JFrame frame = new JFrame("Sudoku");

    private int startX = 0, startY = 0, cellSize = 110;

    public MainGraphic(Grid grid) {
        this.grid = grid;
    }

    public static void main(String[] args) {
        Sudoku sudoku1 = new Sudoku(
                new String[] { "1", "2", "3", "4", "5", "6" });
        Sudoku sudoku2 = new Sudoku(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9" }, 1);

        ArrayList<Sudoku> sudokus = new ArrayList<>();
        sudokus.add(sudoku1);
        sudokus.add(sudoku2);
        Grid grid = new Grid(sudokus);
        grid.print();
        MainGraphic main = new MainGraphic(grid);
        main.init();
    }

    public void init() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(null);

        frame.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                calculateStartCoordinates();
                draw();

            }
        });

        frame.setVisible(true);
    }

    private void calculateStartCoordinates() {
        int frameWidth = frame.getWidth();
        int frameHeight = frame.getHeight();
        int gridWidth = this.grid.getSize().getX() * cellSize;
        int gridHeight = this.grid.getSize().getY() * cellSize;

        this.cellSize = 110 * 9 / Math.max(this.grid.getSize().getX(),
                this.grid.getSize().getY());
        this.startX = (frameWidth - gridWidth) / 2;
        this.startY = (frameHeight - gridHeight) / 2;

        System.out.println("Start coordinates: " + startX + ", " + startY);
        System.out.println("Cell size: " + cellSize);

    }

    public void draw() {
        frame.getContentPane().removeAll();
        for (int x = 0; x < grid.getSize().getX(); x++) {
            for (int y = 0; y < grid.getSize().getY(); y++) {
                sudoku.Cell cell = grid.getCell(x, y);
                if (cell == null) {
                    System.out.println("Cell is null");
                } else {
                    String value = cell.getValue();
                    drawCell(x, y, value);
                }

            }
        }
        frame.revalidate();
        frame.repaint();
    }

    public void drawCell(int x, int y, String value) {
        JPanel cell = new JPanel();
        cell.setBounds(startX + x * cellSize, startY + y * cellSize, cellSize, cellSize);
        cell.setBorder(BorderFactory.createLineBorder(java.awt.Color.BLACK));

        JLabel label = new JLabel(value, SwingConstants.CENTER);
        label.setPreferredSize(new java.awt.Dimension(cellSize, cellSize));
        cell.add(label);

        frame.add(cell);
    }

    public void play() {
        final int[] clicked = { 0, 0 };
        frame.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                clicked[0] = (e.getX() - startX) / cellSize;
                clicked[1] = (e.getY() - startY) / cellSize;

                System.out.println("Clicked: " + clicked[0] + ", " + clicked[1]);

                if (clicked[0] >= 0 && clicked[0] < grid.getSize().getX() && clicked[1] >= 0
                        && clicked[1] < grid.getSize().getY()) {
                    String newValue = javax.swing.JOptionPane.showInputDialog(frame,
                            "Enter value for cell (" + clicked[0] + ", " + clicked[1] + "):");
                    if (newValue != null && !newValue.isEmpty()
                            && grid.canInsertValue(newValue, new Position(clicked[0], clicked[1]))) {
                        grid.insertValue(newValue, new Position(clicked[0], clicked[1]));
                    }
                }
            }
        });
    }

}
