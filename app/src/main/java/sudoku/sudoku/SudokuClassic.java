package sudoku.sudoku;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sudoku.Position;
import sudoku.rule.BlockRule;

public class SudokuClassic extends Sudoku {

    public static List<Integer> primeFactors(int number) {
        // First try to find perfect square factors
        int sqrt = (int) Math.sqrt(number);
        if (sqrt * sqrt == number) {
            List<Integer> factors = new ArrayList<>();
            factors.add(sqrt);
            factors.add(sqrt);
            return factors;
        }

        // Fall back to prime factorization if not a perfect square
        List<Integer> factors = new ArrayList<>();
        for (int i = 2; i <= Math.sqrt(number); i++) {
            while (number % i == 0) {
                factors.add(i);
                number /= i;
            }
        }
        if (number > 1) {
            factors.add(number);
        }

        while (factors.size() > 2) {
            int a = factors.remove(0);
            int b = factors.remove(0);
            factors.add(a * b);
        }

        if (factors.size() == 1) {
            factors.add(1);
        }
        if (Math.random() > 0.5) {
            int temp = factors.get(0);
            factors.set(0, factors.get(1));
            factors.set(1, temp);
        }
        return factors;
    }

    public SudokuClassic(int size, Set<String> symbols, Position offset) {
        super(size, symbols, offset);
        List<Integer> blockSize = primeFactors(size);

        for (int y = 0; y < size / blockSize.get(0); y++) {
            for (int x = 0; x < size / blockSize.get(1); x++) {
                super.add(
                        new BlockRule(
                                new Position(
                                        x * blockSize.get(1),
                                        y * blockSize.get(0)),
                                new Position(
                                        (x + 1) * blockSize.get(1) - 1,
                                        (y + 1) * blockSize.get(0) - 1)));
            }
        }

        super.addRowRules();
        super.addColumnRules();
    }

    public SudokuClassic(int size, Position offset) {
        this(size, generateSymbols(size), offset);
    }

    public SudokuClassic(int size, int offset) {
        this(size, new Position(offset));
    }

    public SudokuClassic(int size) {
        this(size, 0);
    }

    public SudokuClassic(Position ruleDim, Set<String> symbols, Position ruleNumber, Position offset) {
        super(ruleDim.getX()*ruleNumber.getX(), symbols, offset);
        if (ruleDim.getX()*ruleNumber.getX() != ruleDim.getY()*ruleNumber.getY()){
            throw new IllegalArgumentException("The number of rows and columns must be equal");
        }

        for (int y = 0; y < ruleNumber.getY(); y++) {
            for (int x = 0; x < ruleNumber.getX(); x++) {
                super.add(
                        new BlockRule(
                                new Position(
                                        x * ruleDim.getX(),
                                        y * ruleDim.getY()),
                                new Position(
                                        (x + 1) * ruleDim.getX() - 1,
                                        (y + 1) * ruleDim.getY() - 1)));
            }
        }

        super.addRowRules();
        super.addColumnRules();
    }

    public SudokuClassic(Position ruleDim, Position ruleNumber, Position offset){
        this(ruleDim, generateSymbols(ruleDim.getX()*ruleNumber.getX()), ruleNumber, offset);
    }

    private static Set<String> generateSymbols(int size) {
        Set<String> symbols = new HashSet<>();
        for (int i = 1; i <= size; i++) {
            symbols.add(Integer.toString(i));
        }
        return symbols;
    }
}
