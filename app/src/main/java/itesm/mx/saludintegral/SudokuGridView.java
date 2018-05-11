package itesm.mx.saludintegral;

import itesm.mx.saludintegral.GameEngine;
import itesm.mx.saludintegral.R;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;

/**
 * Class that manages the correct display of the generated grid in the device
 * @author Juan Pablo García
 * @version 1
 * reference: https://www.youtube.com/watch?v=6Ld_4-gTl_g
 */
public class SudokuGridView extends GridView{

    private final Context context;

    public SudokuGridView(final Context context , AttributeSet attrs) {
        super(context,attrs);

        this.context = context;

        SudokuGridViewAdapter gridViewAdapter = new SudokuGridViewAdapter(context);

        setAdapter(gridViewAdapter);

        setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int x = position % 9;
                int y = position / 9;

                GameEngine.getInstance().setSelectedPosition(x, y);
            }
        });
    }

    //Measures the space to display the grid
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    //Adapter that gets the position of the items in the Grid and how many items are displayed
    class SudokuGridViewAdapter extends BaseAdapter{

        private Context context;

        public SudokuGridViewAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return 81;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return GameEngine.getInstance().getGrid().getItem(position);
        }
    }
}
