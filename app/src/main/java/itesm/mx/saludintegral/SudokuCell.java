package itesm.mx.saludintegral;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

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
 * Class that manages the looks of the generated grid for the sudoku
 * @author Juan Pablo Gacrcía
 * @version 1
 * reference: https://www.youtube.com/watch?v=6Ld_4-gTl_g
 */
public class SudokuCell extends BaseSudokuCell {

    private Paint mPaint;

    public SudokuCell( Context context ){
        super(context);

        mPaint = new Paint();

    }

    //Draws the grid with the specified design
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawNumber(canvas);
        drawLines(canvas);
    }

    //Draws the numbers inside the grid with the specified design
    private void drawNumber(Canvas canvas){
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(60);
        mPaint.setStyle(Style.FILL);

        Rect bounds = new Rect();
        mPaint.getTextBounds(String.valueOf(getValue()), 0, String.valueOf(getValue()).length(), bounds);

        if( getValue() != 0 ){
            canvas.drawText(String.valueOf(getValue()), (getWidth() - bounds.width())/2, (getHeight() + bounds.height())/2	, mPaint);
        }
    }

    //Draws the lines surrounding the grid with the specified design
    private void drawLines(Canvas canvas) {
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(3);
        mPaint.setStyle(Style.STROKE);

        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
    }
}
