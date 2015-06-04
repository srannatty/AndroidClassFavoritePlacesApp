package gerber.uchicago.edu.People;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import gerber.uchicago.edu.R;

/**
 * Created by jennifer1 on 6/1/15.
 */
public class ImageResultActivity extends ListActivity {
    public static final String RESULT_BITMAP_ARRAY = "result_bitmap_array";
    public static final String RESULT_POSITION = "result_position";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ArrayList<String> UrlArrayList = (ArrayList<String>) getIntent().getSerializableExtra("image_data_bundle_key");

        final ArrayList<Bitmap> imageArrayList = ArrayListConversion(UrlArrayList);
        ArrayAdapter<Bitmap> modeAdapter = new ArrayAdapter<Bitmap>(this, R.layout.pop_image_layout, R.id.pictures, imageArrayList);

        setListAdapter(modeAdapter);

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent returnIntent = new Intent();
                returnIntent.putExtra(RESULT_POSITION, position);
                returnIntent.putExtra(RESULT_BITMAP_ARRAY, imageArrayList);
                setResult(RESULT_OK,returnIntent);
                finish();

            }
        });
    }

    public ArrayList<Bitmap> ArrayListConversion(ArrayList<String> urlArray) {

        int listSize = urlArray.size();
        String strImage;
        InputStream in;
        Bitmap bitmap;

        ArrayList<Bitmap> result = new ArrayList<Bitmap>(listSize);

        for (int i=0; i < listSize; i++) {
            strImage = urlArray.get(i);
            //whyyyyyyyyyyyyyy
            try {
                in = new java.net.URL(strImage).openStream();
                bitmap = BitmapFactory.decodeStream(in);
                result.add(i,bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return result;
    }


}
