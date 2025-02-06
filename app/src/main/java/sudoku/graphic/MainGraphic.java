package sudoku.graphic;

import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import solvers.Solver;
import solvers.wfc.WaveFunctionCollapse;
import sudoku.GenerateSudoku;
import sudoku.Grid;
import sudoku.Position;
import sudoku.rule.BlockRule;
import sudoku.rule.Rule;
import sudoku.sudoku.Sudoku;
import sudoku.sudoku.SudokuClassic;

public class MainGraphic {

    private Grid grid;
    private ArrayList<java.awt.Color> colors;

    private final JFrame frame = new JFrame("Sudoku");

    private int startX = 0, startY = 0, cellSize = 110;
    private int clickedX = -1;
    private int clickedY = -1;

    public MainGraphic(Grid grid) {
        this.grid = grid;
        initializeColors();
    }

    public static void main(String[] args) {
        Grid grid = new Grid.Builder()
            .addSudoku(new SudokuClassic(6, new Position(5, 0)))
            .addSudoku(new SudokuClassic(6, new Position(-5, 0)))
            .addSudoku(new SudokuClassic(6, new Position(0, -5)))
            .addSudoku(new SudokuClassic(6, new Position(0, 5)))
            .build();
        grid.print();
        GenerateSudoku sudokuGenerator = new GenerateSudoku(grid, 0.3);

        sudokuGenerator.generateSudoku();

        grid = sudokuGenerator.getGrid();

        MainGraphic main = new MainGraphic(grid);
        main.init();
    }

    public void init() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(null);

        frame.addComponentListener(
            new java.awt.event.ComponentAdapter() {
                @Override
                public void componentResized(
                    java.awt.event.ComponentEvent evt
                ) {
                    calculateStartCoordinates();
                    draw();
                }
            }
        );

        frame.setVisible(true);
    }

    private void calculateStartCoordinates() {
        int frameWidth = frame.getWidth();
        int frameHeight = frame.getHeight();
        int gridWidth = this.grid.getSize().getX() * cellSize;
        int gridHeight = this.grid.getSize().getY() * cellSize;

        this.cellSize =
            (110 * 9) /
            Math.max(this.grid.getSize().getX(), this.grid.getSize().getY());
        this.startX = (frameWidth - gridWidth) / 2;
        this.startY = (frameHeight - gridHeight) / 2;
    }

    private void initializeColors() {
        this.colors = new ArrayList<>();
        for (Rule rule : this.grid.getRules()) {
            if (rule instanceof BlockRule) {
                java.awt.Color randomColor = new java.awt.Color(
                    (int) (Math.random() * 0x1000000)
                );
                colors.add(randomColor);
            } else {
                colors.add(java.awt.Color.WHITE);
            }
        }
    }

    public void draw() {
        this.grid.print();
        frame.getContentPane().removeAll();
        for (int x = 0; x < grid.getSize().getX(); x++) {
            for (int y = 0; y < grid.getSize().getY(); y++) {
                if (grid.getCell(new Position(x, y)) != null) {
                    String value = this.grid.getSymbol(new Position(x, y));
                    drawCell(x, y, value);
                }
            }
        }

        // Add solve button
        javax.swing.JButton solveButton = new javax.swing.JButton("Résoudre");
        solveButton.setBounds(frame.getWidth() - 150, 50, 100, 50);
        solveButton.addActionListener(
            new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    Solver solver = new WaveFunctionCollapse(grid);
                    solver.solve();
                    draw();
                }
            }
        );
        frame.add(solveButton);

        javax.swing.JButton generateButton = new javax.swing.JButton("Générer");
        generateButton.setBounds(frame.getWidth() - 150, 110, 100, 50);
        generateButton.addActionListener(
            new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    String gridSizeStr = JOptionPane.showInputDialog(
                        frame,
                        "Entrez la taille de la grille:"
                    );
                    String multipleSudokuStr = JOptionPane.showInputDialog(
                        frame,
                        "Y a-t-il plusieurs Sudoku? (oui/non):"
                    );

                    int gridSize = Integer.parseInt(gridSizeStr);
                    System.out.println(multipleSudokuStr);
                    boolean multipleSudoku = multipleSudokuStr.equalsIgnoreCase(
                        "oui"
                    );
                    System.out.println("Multiple Sudoku: " + multipleSudoku);
                    String[] values = new String[gridSize];
                    for (int i = 0; i < gridSize; i++) {
                        values[i] = String.valueOf(i + 1);
                    }

                    draw();
                }
            }
        );
        frame.add(generateButton);

        frame.revalidate();
        frame.repaint();
    }

    public void drawCell(int x, int y, String value) {
        JPanel cell = new JPanel();
        cell.setBounds(
            startX + x * cellSize,
            startY + y * cellSize,
            cellSize,
            cellSize
        );
        cell.setBorder(BorderFactory.createLineBorder(java.awt.Color.BLACK));
        sudoku.Cell c = grid.getCell(new Position(x, y));
        int nb = c.getNumberOfPrintableRules(grid.getRules());
        if (nb > 1) {
            int red = 0, green = 0, blue = 0;
            int count = 0;
            for (Integer idRule : c.getIdRules()) {
                if (grid.getRules().get(idRule) instanceof BlockRule) {
                    java.awt.Color color = colors.get(idRule);
                    red += color.getRed();
                    green += color.getGreen();
                    blue += color.getBlue();
                    count++;
                }
            }
            if (count > 0) {
                red /= count;
                green /= count;
                blue /= count;
                java.awt.Color averageColor = new java.awt.Color(
                    red,
                    green,
                    blue
                );
                cell.setOpaque(true);
                cell.setBackground(averageColor);
            }
        } else {
            for (Integer idRule : c.getIdRules()) {
                if (grid.getRules().get(idRule) instanceof BlockRule) {
                    cell.setOpaque(true);
                    java.awt.Color color = colors.get(idRule);

                    cell.setBackground(color);
                }
            }
        }

        JLabel label = new JLabel(value, SwingConstants.CENTER);
        label.setPreferredSize(new java.awt.Dimension(cellSize, cellSize));
        label.setForeground(java.awt.Color.WHITE);
        label.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 32));
        cell.add(label);

        cell.add(label);

        cell.addMouseListener(
            new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    clickedX = x;
                    clickedY = y;
                    System.out.println(
                        "Cellule cliquée : (" + x + ", " + y + ")"
                    );
                    cell.requestFocusInWindow();
                }
            }
        );

        cell.addKeyListener(
            new java.awt.event.KeyAdapter() {
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
                        if (
                            !value.isEmpty() && clickedX != -1 && clickedY != -1
                        ) {
                            System.out.println(
                                "Cellule cliquée : (" +
                                clickedX +
                                ", " +
                                clickedY +
                                ")"
                            );
                            MainGraphic.this.grid.insertSymbol(
                                    value,
                                    new Position(clickedX, clickedY)
                                );
                            label.setText(value);
                            System.out.println(
                                "Après insertion : " +
                                MainGraphic.this.grid.getCell(
                                        new Position(clickedX, clickedY)
                                    ).getSymbol()
                            );
                            MainGraphic.this.draw();
                            input.setLength(0);
                        }
                    }
                }
            }
        );

        cell.setFocusable(true);
        frame.add(cell);
    }
}
