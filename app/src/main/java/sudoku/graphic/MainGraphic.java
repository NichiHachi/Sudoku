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

    private final Grid grid;

    private final JFrame frame = new JFrame("Sudoku");

    private int startX = 0, startY = 0, cellSize = 110;
    private int clickedX = -1;
    private int clickedY = -1;

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

    }

    public void draw() {
        this.grid.print();
        frame.getContentPane().removeAll();
        for (int x = 0; x < grid.getSize().getX(); x++) {
            for (int y = 0; y < grid.getSize().getY(); y++) {
                sudoku.Cell cell = grid.getCell(x, y);
                if (cell != null) {
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
        label.setForeground(java.awt.Color.BLACK);
        cell.add(label);

        cell.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                clickedX = x;
                clickedY = y;
                System.out.println("Cellule cliquée : (" + x + ", " + y + ")");
                cell.requestFocusInWindow(); // Redonner le focus au composant cell

            }
        });

        cell.addKeyListener(new java.awt.event.KeyAdapter() {
            private StringBuilder input = new StringBuilder();

            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char keyChar = e.getKeyChar();
                if (Character.isDigit(keyChar)) {
                    input.append(keyChar);
                }
            }

            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    String value = input.toString();
                    if (!value.isEmpty() && clickedX != -1 && clickedY != -1) {
                        System.out.println("Cellule cliquée : (" + clickedX + ", " + clickedY + ")");
                        MainGraphic.this.grid.insertValue(value, new Position(clickedX, clickedY));
                        System.out.println(
                                "Après insertion : " + MainGraphic.this.grid.getCell(clickedX, clickedY).getValue());
                        MainGraphic.this.draw();
                        input.setLength(0);
                    }
                }
            }
        });

        cell.setFocusable(true);
        frame.add(cell);
    }
}
