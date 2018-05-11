package itesm.mx.saludintegral;

import itesm.mx.saludintegral.GameGrid;

import android.content.Context;

/**
 * Class that manages the database for events. It can create and upgrade/downgrade the database.
 * @author Juan Pablo García
 * @version 1
 * reference: https://www.youtube.com/watch?v=6Ld_4-gTl_g
 */

public class GameEngine {
    private static GameEngine instance;

    private GameGrid grid = null;

    private int selectedPosX = -1, selectedPosY = -1;

    private GameEngine(){}

    public static GameEngine getInstance(){
        if( instance == null ){
            instance = new GameEngine();
        }
        return instance;
    }

    //Creates the grid
    public void createGrid( Context context ){
        int[][] Sudoku = SudokuGenerator.getInstance().generateGrid();
        Sudoku = SudokuGenerator.getInstance().removeElements(Sudoku);
        grid = new GameGrid(context);
        grid.setGrid(Sudoku);
    }

    //returns the grid
    public GameGrid getGrid(){
        return grid;
    }

    public void setSelectedPosition( int x , int y ){
        selectedPosX = x;
        selectedPosY = y;
    }

    //sets numbers into grid
    public void setNumber( int number ){
        if( selectedPosX != -1 && selectedPosY != -1 ){
            grid.setItem(selectedPosX,selectedPosY,number);
        }
        grid.checkGame();
    }

}

