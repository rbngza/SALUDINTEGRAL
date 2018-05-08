package itesm.mx.saludintegral;

import itesm.mx.saludintegral.GameEngine;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

//Class that makes the buttons generated for numbers in the sudoku set their value into the grid in modifiable cells
public class NumberButton extends android.support.v7.widget.AppCompatButton implements View.OnClickListener {

    private int number;

    public NumberButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
    }

    //on click gets value of the button
    @Override
    public void onClick(View v) {
        GameEngine.getInstance().setNumber(number);
    }

    //inserts the value of the button into the grid
    public void setNumber(int number){
        this.number = number;
    }
}
