package gerber.uchicago.edu.db;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import gerber.uchicago.edu.R;

/**
 * Created by jennifer1 on 6/1/15.
 */
public class PeopleSimpleCursorAdapter extends SimpleCursorAdapter {


    private ImageLoader mImageLoader;
    private RequestQueue mRequestQueue;

    public PeopleSimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);

        mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());

        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(10);
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }
        });
    }

    //to use a viewholder, you must override the following two methods and define a ViewHolder class
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return super.newView(context, cursor, parent);
    }
    //function with problem
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);

        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder == null) {

            holder = new ViewHolder();

            holder.nName = cursor.getColumnIndexOrThrow(RestosDbAdapter.COL_NAME);
            //holder.nNiv = cursor.getColumnIndexOrThrow(RestosDbAdapter.COL_IMG_URL);

            holder.textViewName = (TextView) view.findViewById(R.id.person_list_name);
            //holder.networkImageView = (NetworkImageView) view.findViewById(R.id.person_list_niv);

            view.setTag(holder);
        }
        holder.textViewName.setText(cursor.getString(holder.nName));
        //holder.networkImageView.setImageUrl(cursor.getString(holder.nNiv), mImageLoader);


    }

    static class ViewHolder {

        int nName;
        //int nCity;
        //int nFav;
        //int nNiv;

        TextView textViewName;
        //TextView textViewCity;
        //View viewImportant;
        //NetworkImageView networkImageView;
    }
}
