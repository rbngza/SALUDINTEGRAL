package itesm.mx.saludintegral;

import java.util.ArrayList;
import java.util.Random;

/**
 * Class that generates the Grid of the Sudoku
 * @author Juan Pablo García
 * @version 1
 */
public class SudokuGenerator {
    private static SudokuGenerator instance;

    private ArrayList<ArrayList<Integer>> Available = new ArrayList<ArrayList<Integer>>();

    private Random rand = new Random();

    private SudokuGenerator(){}

    public static SudokuGenerator getInstance(){
        if( instance == null ){
            instance = new SudokuGenerator();
        }
        return instance;
    }

    // Gives back the generated Sudoku Grid

    public int[][] generateGrid(){
        int[][] Sudoku = new int[9][9];

        int currentPos = 0;


        while( currentPos < 81 ){
            if( currentPos == 0 ){
                clearGrid(Sudoku);
            }

            if( Available.get(currentPos).size() != 0 ){
                int i = rand.nextInt(Available.get(currentPos).size());
                int number = Available.get(currentPos).get(i);

                if( !checkConflict(Sudoku, currentPos , number)){
                    int xPos = currentPos % 9;
                    int yPos = currentPos / 9;

                    Sudoku[xPos][yPos] = number;

                    Available.get(currentPos).remove(i);

                    currentPos++;
                }else{
                    Available.get(currentPos).remove(i);
                }

            }else{
                for( int i = 1 ; i <= 9 ; i++ ){
                    Available.get(currentPos).add(i);
                }
                currentPos--;
            }
        }


        return Sudoku;
    }

    //Removes the amount of numbers in the cells indicated inside the while, the numbers will be removed from random positions in the Grid
    public int[][] removeElements( int[][] Sudoku ){
        int i = 0;

        while( i < 34 ){
            int x = rand.nextInt(9);
            int y = rand.nextInt(9);

            if( Sudoku[x][y] != 0 ){
                Sudoku[x][y] = 0;
                i++;
            }
        }
        return Sudoku;

    }

    //Clears the numbers from the grid
    private void clearGrid(int [][] Sudoku){
        Available.clear();

        for( int y =  0; y < 9 ; y++ ){
            for( int x = 0 ; x < 9 ; x++ ){
                Sudoku[x][y] = -1;
            }
        }

        for( int x = 0 ; x < 81 ; x++ ){
            Available.add(new ArrayList<Integer>());
            for( int i = 1 ; i <= 9 ; i++){
                Available.get(x).add(i);
            }
        }
    }

    //When generating the numbers, it checks that there are no repeated ones in the same column or row
    private boolean checkConflict( int[][] Sudoku , int currentPos , final int number){
        int xPos = currentPos % 9;
        int yPos = currentPos / 9;

        if( checkHorizontalConflict(Sudoku, xPos, yPos, number) || checkVerticalConflict(Sudoku, xPos, yPos, number) || checkRegionConflict(Sudoku, xPos, yPos, number) ){
            return true;
        }

        return false;
    }

     //Checks for conflict in the rows of the grid
    private boolean checkHorizontalConflict( final int[][] Sudoku , final int xPos , final int yPos , final int number ){
        for( int x = xPos - 1; x >= 0 ; x-- ){
            if( number == Sudoku[x][yPos]){
                return true;
            }
        }

        return false;
    }
    //checks for conflict in the columns of the grid
    private boolean checkVerticalConflict( final int[][] Sudoku , final int xPos , final int yPos , final int number ){
        for( int y = yPos - 1; y >= 0 ; y-- ){
            if( number == Sudoku[xPos][y] ){
                return true;
            }
        }

        return false;
    }


    //Checks for conflict inside a specific region, so the comparisons are by parts and not the whole grid
    private boolean checkRegionConflict( final int[][] Sudoku , final int xPos , final int yPos , final int number ){
        int xRegion = xPos / 3;
        int yRegion = yPos / 3;

        for( int x = xRegion * 3 ; x < xRegion * 3 + 3 ; x++ ){
            for( int y = yRegion * 3 ; y < yRegion * 3 + 3 ; y++ ){
                if( ( x != xPos || y != yPos ) && number == Sudoku[x][y] ){
                    return true;
                }
            }
        }

        return false;
    }
}