package gerber.uchicago.edu.Places;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gerber.uchicago.edu.R;

/**
 * Created by Edwin on 15/02/2015.
 */
public class Tab3 extends Fragment {
    //this is the gridview
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_3,container,false);

        Log.d("tab2","Should this be gridview?");
        return v;
    }
}