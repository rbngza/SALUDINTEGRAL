package itesm.mx.saludintegral;

import android.content.Context;
import android.view.View;

/**
 * Class that manages the database for events. It can create and upgrade/downgrade the database.
 * @author Juan Pablo García
 * @version 1
 * reference: https://www.youtube.com/watch?v=6Ld_4-gTl_g
 */

public class BaseSudokuCell extends View{


    private int value;
    private boolean modifiable = true;

    public BaseSudokuCell(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    public void setNotModifiable(){
        modifiable = false;
    }

    public void setInitValue(int value){
        this.value = value;
        invalidate();
    }

    public void setValue( int value ){
        if( modifiable ){
            this.value = value;
        }

        invalidate();
    }

    public int getValue(){
        return value;
    }
}
