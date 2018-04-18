package itesm.mx.saludintegral;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class MenuFragment extends Fragment implements View.OnClickListener{
    private OnFragmentInteractionListener mListener;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.menu_layout, container, false);

        Button btnAlimentacion = (Button) view.findViewById(R.id.btn_alimentacion);
        btnAlimentacion.setOnClickListener(this);
        Button btnCalendario = (Button) view.findViewById(R.id.btn_calendario);
        btnCalendario.setOnClickListener(this);
        Button btnSudoku = (Button) view.findViewById(R.id.btn_entretenimiento);
        btnSudoku.setOnClickListener(this);
        Button btnActFi = (Button) view.findViewById(R.id.btn_actfi);
        btnActFi.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {

            case R.id.btn_calendario:
                mListener.onCalendarButtonClicked();
                break;

            case R.id.btn_alimentacion:
                // do your code
                break;

            case R.id.btn_entretenimiento:
                GameEngine.getInstance().createGrid(getActivity());
                break;

            case R.id.btn_actfi:
                // do your code
                break;

            default:
                break;
        }
    }

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

    public interface OnFragmentInteractionListener {
        void onCalendarButtonClicked();
    }

    private void printSudoku(int Sudoku[][]) {
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                System.out.print(Sudoku[x][y] + "|");
            }
            System.out.println();
        }
    }
}
