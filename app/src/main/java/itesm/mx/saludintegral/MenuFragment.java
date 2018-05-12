package itesm.mx.saludintegral;

import android.app.Fragment;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

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

public class MenuFragment extends Fragment implements View.OnClickListener{
    private OnFragmentInteractionListener mListener;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.menu_layout, container, false);

        ImageButton btnHistory = (ImageButton) view.findViewById(R.id.btn_history);
        btnHistory.setOnClickListener(this);

        ImageButton btnAgenda = (ImageButton) view.findViewById(R.id.btn_agenda);
        btnAgenda.setOnClickListener(this);

        ImageButton btnSudoku = (ImageButton) view.findViewById(R.id.btn_sudoku);
        btnSudoku.setOnClickListener(this);

        ImageButton btnPlan = (ImageButton) view.findViewById(R.id.btn_plan);
        btnPlan.setOnClickListener(this);

        ImageButton btnPill = (ImageButton) view.findViewById(R.id.btn_pill);
        btnPill.setOnClickListener(this);

        if (mListener == null) {
            mListener = (OnFragmentInteractionListener) getActivity();
        }

        return view;
    }


    /**
     * Method for handling clicks to buttons in menu fragment.
     */
    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.btn_agenda:
                mListener.onAgendaButtonClicked();
                break;
            case R.id.btn_history:
                mListener.onHistoryButtonClicked();
                break;
            case R.id.btn_sudoku:
                mListener.onSudokuButtonClicked();
                break;
            case R.id.btn_plan:
                mListener.onPlanButtonClicked();
                break;
            case R.id.btn_pill:
                mListener.onPillButtonClicked();
                break;
            default:
                break;
        }
    }

    /**
     * Method for OnFragmentInteractionListener.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MenuFragment.OnFragmentInteractionListener) {
            mListener = (MenuFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Method for creating the fucntions to every button, this is used by the mListener.
     */
    public interface OnFragmentInteractionListener {
        void onAgendaButtonClicked();
        void onSudokuButtonClicked();
        void onHistoryButtonClicked();
        void onPlanButtonClicked();
        void onPillButtonClicked();
    }
}
