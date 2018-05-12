package itesm.mx.saludintegral;

import itesm.mx.saludintegral.R;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

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
 * Class that contains the grid for the buttons in the Sudoku
 * @author Juan Pablo García
 * @version 1
 * reference: https://www.youtube.com/watch?v=6Ld_4-gTl_g
 */
public class ButtonsGridView extends GridView{


    public ButtonsGridView( Context context , AttributeSet attrs ){
        super(context , attrs);

        ButtonsGridViewAdapter gridViewAdapter = new ButtonsGridViewAdapter(context);

        setAdapter(gridViewAdapter);
    }

    class ButtonsGridViewAdapter extends BaseAdapter {

        private Context context;

        public ButtonsGridViewAdapter(Context context) {
            this.context = context;
        }


        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        //Displays the buttons
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;

            if( v == null ){
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                v = inflater.inflate(R.layout.button, parent , false);

                NumberButton btn;
                btn = (NumberButton)v;
                btn.setTextSize(10);
                btn.setId(position);

                if( position != 9 ){
                    btn.setText(String.valueOf(position + 1));
                    btn.setNumber(position + 1);
                }else{
                    btn.setText("DEL");
                    btn.setNumber(0);
                }
                return btn;
            }

            return v;
        }

    }
}
