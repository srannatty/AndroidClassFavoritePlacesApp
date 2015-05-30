package gerber.uchicago.edu.Places;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import gerber.uchicago.edu.R;
import gerber.uchicago.edu.db.RestosDbAdapter;
import gerber.uchicago.edu.db.RestosSimpleCursorAdapter;

/**
 * Created by Edwin on 15/02/2015.
 */
public class Tab3 extends Fragment {
    //this is the GridView

    //database and adapter
    private RestosDbAdapter mDbAdapter;
    private RestosSimpleCursorAdapter mCursorAdapter;
    //private ListAdapter mAdapter;

    private GridView mGridView;


    //Don't know if I need this
    private SharedPreferences mPreferences;
    private static final String SORT_ORDER = "sort_order";
    private static final String VERY_FIRST_LOAD = "our_very_first_load_";
    private String mSortOrder;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_place_grid,container,false);

        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_grid);

        mGridView = (GridView) v.findViewById(android.R.id.list);

        mDbAdapter = new RestosDbAdapter(getActivity());
        mDbAdapter.open();


        //get the shared preferences
        mPreferences = getActivity().getSharedPreferences(
                "gerber.uchicago.edu", getActivity().MODE_PRIVATE);

        boolean bFirstLoad = mPreferences.getBoolean(VERY_FIRST_LOAD, true);

        if (bFirstLoad) {
            mDbAdapter.insertSomeRestos();
            //set the flag in preferences so that this block will never be called again.
            mPreferences.edit().putBoolean(VERY_FIRST_LOAD, false).commit();
        }

        mSortOrder = mPreferences.getString(SORT_ORDER, null);

        mSortOrder = mPreferences.getString(SORT_ORDER, null);
        Cursor cursor = mDbAdapter.fetchAllRestos(getSortOrder());

        //from columns defined in the db
        String[] from = new String[]{
                RestosDbAdapter.COL_NAME,
                RestosDbAdapter.COL_CITY
        };

        //to the ids of views in the layout
        int[] to = new int[]{
                R.id.list_text,
                //R.id.grid_resto_name,
                R.id.list_city
                //R.id.grid_resto_city
        };

        mCursorAdapter = new RestosSimpleCursorAdapter(
                //context
                getActivity(),
                //the layout of the row - now pulling from our new layout
                //When I change to grid, the app doesn't work get null pointer exception RSCursorAdapter.bindView
                R.layout.restos_row,
                //cursor
                cursor,
                //from columns defined in the db
                from,
                //to the ids of views in the layout
                to,
                //flag - not used
                0);

        mGridView.setAdapter(mCursorAdapter);

        Log.d("tab2", "Should this be gridview?");
        return v;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnTab2InteractionListener {

        // TODO: Update argument type and name
        public void onTab2Interaction(String id);
    }


    public String getSortOrder() {
        return mSortOrder;
    }

    public void setSortOrder(String strSortOrder) {
        mSortOrder = strSortOrder;
        mPreferences.edit().putString(SORT_ORDER, strSortOrder).commit();

    }



    private int getIdFromPosition(int nPosition) {
        Cursor cursor = mDbAdapter.fetchAllRestos(getSortOrder());
        cursor.move(nPosition);
        return cursor.getInt(RestosDbAdapter.INDEX_ID);
    }



    @Override
    public void onPause() {
        super.onPause();
        mDbAdapter.close();
    }

    @Override
    public void onResume() {
        super.onResume();
        mDbAdapter.open();
        // mDbAdapter.insertSomeRestos();
        mCursorAdapter.changeCursor(mDbAdapter.fetchAllRestos(getSortOrder()));

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        }
        return true;
    }
}