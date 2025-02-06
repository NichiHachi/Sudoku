package sudoku.sudoku;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import sudoku.Position;
import sudoku.rule.BlockRule;

public class SudokuClassic extends Sudoku {

    public static List<Integer> primeFactors(int number) {
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

        Collections.shuffle(factors);

        while (factors.size() > 2) {
            int a = factors.remove(0);
            int b = factors.remove(0);
            factors.add(a * b);
        }

        if (factors.size() == 1) {
            factors.add(1);
        }

        return factors;
    }

    public SudokuClassic(int size, Position offset) {
        super(size, generateSymbols(size), offset);
        List<Integer> blockSize = primeFactors(size);

        for (int y = 0; y < size / blockSize.get(0); y++) {
            for (int x = 0; x < size / blockSize.get(1); x++) {
                super.add(
                    new BlockRule(
                        new Position(
                            x * blockSize.get(1),
                            y * blockSize.get(0)
                        ),
                        new Position(
                            (x + 1) * blockSize.get(1) - 1,
                            (y + 1) * blockSize.get(0) - 1
                        )
                    )
                );
            }
        }

        super.addRowRules();
        super.addColumnRules();
    }

    public SudokuClassic(int size, int offset) {
        this(size, new Position(offset));
    }

    public SudokuClassic(int size) {
        this(size, 0);
    }

    private static Set<String> generateSymbols(int size) {
        Set<String> symbols = new HashSet<>();
        for (int i = 1; i <= size; i++) {
            symbols.add(Integer.toString(i));
        }
        return symbols;
    }
}
