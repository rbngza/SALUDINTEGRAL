package itesm.mx.saludintegral;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

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
