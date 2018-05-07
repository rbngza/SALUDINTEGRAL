package itesm.mx.saludintegral;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


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
