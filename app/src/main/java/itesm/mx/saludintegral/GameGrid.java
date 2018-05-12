package itesm.mx.saludintegral;


import itesm.mx.saludintegral.SudokuChecker;

import android.content.Context;
import android.widget.Toast;

/* SALUDINTEGRAL - aplicación con el objetivo de asistir a personas independientes de 60 años en adelante
        Copyright (C) 2018 - ITESM

        This program is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.

        This program is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        GNU General Public License for more details.

        You should have received a copy of the GNU General Public License
        along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

/**
 * Class that holds the whole Sudoku with the views specified
 * @author Juan Pablo García
 * @version 1
 * reference: https://www.youtube.com/watch?v=6Ld_4-gTl_g
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
