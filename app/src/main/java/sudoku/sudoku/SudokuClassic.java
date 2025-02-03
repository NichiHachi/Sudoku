package sudoku.sudoku;

import sudoku.Position;
import sudoku.rule.BlockRule;

import java.util.HashSet;
import java.util.Set;

public class SudokuClassic extends Sudoku {
    public SudokuClassic(int size, Position offset){
        super(size, generateSymbols(size), offset);
    }

    public SudokuClassic(int size, int offset){
        this(size, new Position(offset));

        int blockSize = (int) Math.sqrt(size);
        if(size % blockSize > 0){
            System.err.println("[Sudoku] The size of each case " + blockSize + " can't build a Sudoku of size " + size);
            return;
        }

        for(int y = 0; y < size/blockSize; y++){
            for(int x = 0; x < size/blockSize; x++){
                super.add(new BlockRule(new Position(x*blockSize, y*blockSize), new Position((x+1)*blockSize-1, (y+1)*blockSize-1)));
            }
        }

        super.addRowRules();
        super.addColumnRules();
    }

    public SudokuClassic(int size) {
        this(size, 0);
    }

    private static Set<String> generateSymbols(int size){
        Set<String> symbols = new HashSet<>();
        for(int i = 1; i <= size; i++){
            symbols.add(Integer.toString(i));
        }
        return symbols;
    }
}
