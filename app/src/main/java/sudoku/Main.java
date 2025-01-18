package sudoku;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args){
        Sudoku sudoku1 = new Sudoku(27, -1);
        Sudoku sudoku2 = new Sudoku(27, 0);
        Sudoku sudoku3 = new Sudoku(27, 1);

        ArrayList<String> ruleMap = new ArrayList<>();
        for(int i=1; i<10; i++){
            ruleMap.add(String.valueOf(i));
        }

        Rule rule = new Rule(ruleMap);


        for(int y=0; y<3; y++){
            for(int x=0; x<3; x++){
                ArrayList<Position> rulePositions = new ArrayList<>();
                for(int y_local=0; y_local<3; y_local++){
                    for(int x_local=0; x_local<3; x_local++){
                        rulePositions.add(new Position(x*3+x_local,y*3+y_local));
                    }
                }
                sudoku1.add(rule, rulePositions);
                sudoku2.add(rule, rulePositions);
                sudoku3.add(rule, rulePositions);
            }
        }

        ArrayList<Sudoku> sudokus = new ArrayList<>();
        sudokus.add(sudoku1);
        sudokus.add(sudoku2);
        sudokus.add(sudoku3);
        Grid grid = new Grid(sudokus);
        grid.print();
    }
}
