package gerber.uchicago.edu;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.LinearLayout;

import gerber.uchicago.edu.R;

/**
 * Created by jennifer1 on 6/2/15.
 */
public class TutorialActivity extends ActionBarActivity {

    private LinearLayout mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial_layout);

        mLayout = (LinearLayout) findViewById(R.id.tutorial_view);
        mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
