package itesm.mx.saludintegral;

import itesm.mx.saludintegral.GameEngine;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

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

//Class that makes the buttons generated for numbers in the sudoku set their value into the grid in modifiable cells
// reference: https://www.youtube.com/watch?v=6Ld_4-gTl_g
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
