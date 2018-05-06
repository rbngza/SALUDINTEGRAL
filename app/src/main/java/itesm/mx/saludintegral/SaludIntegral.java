package itesm.mx.saludintegral;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class SaludIntegral extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SaludIntegral() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SaludIntegral newInstance(String param1, String param2) {
        SaludIntegral fragment = new SaludIntegral();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_salud_integral, container, false);

        Button btnAlimentos = v.findViewById(R.id.btn_alimentos);
        btnAlimentos.setOnClickListener(this);

        Button btnEjercicio = v.findViewById(R.id.btn_ejercicios);
        btnEjercicio.setOnClickListener(this);

        Button btnFisica = v.findViewById(R.id.btn_mental);
        btnFisica.setOnClickListener(this);


        return v;
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.btn_alimentos:
                mListener.onEventListFragment(2);
                break;
            case R.id.btn_ejercicios:
                mListener.onEventListFragment(3);
                break;
            case R.id.btn_mental:
                mListener.onEventListFragment(0);
                break;
            default:
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SaludIntegral.OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public interface OnFragmentInteractionListener {
        void onEventListFragment(int tipo);
    }


}
