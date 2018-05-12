package itesm.mx.saludintegral;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class SaludIntegralFragment extends Fragment implements View.OnClickListener{

    private OnEventAddedListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_salud_integral, container, false);
        if (mListener == null) {
            mListener = (OnEventAddedListener) getActivity();
        }

        Button btnAlimentos = (Button) v.findViewById(R.id.btn_alimentos);
        btnAlimentos.setOnClickListener(this);

        Button btnEjercicio = (Button) v.findViewById(R.id.btn_ejercicios);
        btnEjercicio.setOnClickListener(this);

        Button btnMental = (Button) v.findViewById(R.id.btn_mental);
        btnMental.setOnClickListener(this);

//FANTOMTEXT
        return v;
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.btn_alimentos:
                mListener.onEventAddFood();
                break;
            case R.id.btn_ejercicios:
                mListener.onEventAddExercise();
                break;
            case R.id.btn_mental:
                mListener.onEventAddMental();
            default:
                break;
        }
    }


    public interface OnEventAddedListener {
        void onEventAddFood();
        void onEventAddExercise();
        void onEventAddMental();
    }
}
