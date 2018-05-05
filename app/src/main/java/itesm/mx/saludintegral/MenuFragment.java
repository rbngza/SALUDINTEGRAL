package itesm.mx.saludintegral;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MenuFragment extends Fragment implements View.OnClickListener{
    private OnFragmentInteractionListener mListener;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.menu_layout, container, false);

        Button btnHistory = (Button) view.findViewById(R.id.btn_history);
        btnHistory.setOnClickListener(this);

        Button btnAgenda = (Button) view.findViewById(R.id.btn_agenda);
        btnAgenda.setOnClickListener(this);

        Button btnSudoku = (Button) view.findViewById(R.id.btn_sudoku);
        btnSudoku.setOnClickListener(this);

        Button btnPlan = (Button) view.findViewById(R.id.btn_plan);
        btnPlan.setOnClickListener(this);
        if (mListener == null) {
            mListener = (OnFragmentInteractionListener) getActivity();
        }
        return view;
    }

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
        void onAgendaButtonClicked();
        void onSudokuButtonClicked();
        void onHistoryButtonClicked();
        void onPlanButtonClicked();
    }
}
