package itesm.mx.saludintegral;


import itesm.mx.saludintegral.SudokuChecker;

import android.content.Context;
import android.widget.Toast;

/**
 * Class that holds the whole Sudoku with the views specified
 * @author Juan Pablo García
 * @version 1
 */

//Constructor
public class GameGrid {
    private SudokuCell[][] Sudoku = new SudokuCell[9][9];

    private Context context;

    public GameGrid( Context context ){
        this.context = context;
        for( int x = 0 ; x < 9 ; x++ ){
            for( int y = 0 ; y < 9 ; y++){
                Sudoku[x][y] = new SudokuCell(context);
            }
        }
    }

    //Gives the grid the values to hold
    public void setGrid( int[][] grid ){
        for( int x = 0 ; x < 9 ; x++ ){
            for( int y = 0 ; y < 9 ; y++){
                Sudoku[x][y].setInitValue(grid[x][y]);
                if( grid[x][y] != 0 ){
                    Sudoku[x][y].setNotModifiable();
                }
            }
        }
    }

    //getter
    public SudokuCell[][] getGrid(){
        return Sudoku;
    }

    //getter for value
    public SudokuCell getItem(int x , int y ){
        return Sudoku[x][y];
    }

    //getter for position
    public SudokuCell getItem( int position ){
        int x = position % 9;
        int y = position / 9;

        return Sudoku[x][y];
    }


    public void setItem( int x , int y , int number ){
        Sudoku[x][y].setValue(number);
    }

    //Checks if the sudoku has been solved correctly
    public void checkGame(){
        int [][] sudGrid = new int[9][9];
        for( int x = 0 ; x < 9 ; x++ ){
            for( int y = 0 ; y < 9 ; y++ ){
                sudGrid[x][y] = getItem(x,y).getValue();
            }
        }

        if( SudokuChecker.getInstance().checkSudoku(sudGrid)){
            Toast.makeText(context, "Resolviste el Sudoku!", Toast.LENGTH_LONG).show();
        }
    }
}
