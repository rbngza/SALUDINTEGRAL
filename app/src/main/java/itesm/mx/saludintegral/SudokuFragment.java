package itesm.mx.saludintegral;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Class that makes the Sudoku open in a fragment
 * @author Juan Pablo García
 * @version 1
 */
public class SudokuFragment extends Fragment {

    //
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sudoku_layout, container, false);
        GameEngine.getInstance().createGrid(getActivity());
        return v;
    }
}
