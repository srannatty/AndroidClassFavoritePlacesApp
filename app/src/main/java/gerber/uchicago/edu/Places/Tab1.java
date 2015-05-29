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
public class Tab1 extends Fragment {
//THIS IS TAB3, EDIT


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.frag_scroll_layout_edit,container,false);
        //this doesnt seem to do the work that Tab 1 is actually doing right now....

        Log.d("Tab3", "Editing?");
        return v;
    }





}
