package gerber.uchicago.edu.People;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gerber.uchicago.edu.R;

/**
 * Created by jennifer1 on 5/31/15.
 */
public class Tab1List extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_person_list, container, false);

        return v;
    }

}
