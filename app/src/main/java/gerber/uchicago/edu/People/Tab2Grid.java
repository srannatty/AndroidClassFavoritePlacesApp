package gerber.uchicago.edu.People;

import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import gerber.uchicago.edu.R;

/**
 * Created by jennifer1 on 5/31/15.
 */
public class Tab2Grid extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{



    private GridView mGridView;
    SimpleCursorAdapter mAdapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mGridView = (GridView) getActivity().findViewById(R.id.person_grid_list);

        //Create an adapter for the listview
        mAdapter = new SimpleCursorAdapter
                (getActivity(),
                        R.layout.person_row, // A layout for each item on the list
                        null, // Cursor, null for now.
                        new String[]{ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.LAST_TIME_CONTACTED}, // What to retrieve from the cursor
                        new int[]{R.id.grid_person_name, R.id.grid_person_other}, // Where to put the retrieved information
                        0
                );

        mGridView.setAdapter(mAdapter);

        //Start the loader (or bind, if already started)
        getLoaderManager().initLoader(0, //ID
                null, // (SEARCH) ARGUMENTS
                this // An implementation of LoaderCallbacks
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_person_grid, container, false);
        return v;
    }


    /**
     * A list of which columns to return. Passing null will return all columns, which is inefficient.
     * (Used below)
     */
    static final String[] CONTACTS_SUMMARY_PROJECTION = new String[] {
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.LAST_TIME_CONTACTED,
            ContactsContract.Contacts.LOOKUP_KEY,
    };






    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {


        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        String select = "((" + ContactsContract.Contacts.DISPLAY_NAME + " NOTNULL) AND ("
                + ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1) AND ("
                + ContactsContract.Contacts.DISPLAY_NAME + " != '' ))";

        return new CursorLoader(getActivity(),
                ContactsContract.Contacts.CONTENT_URI, // URI of the contacts content provider
                CONTACTS_SUMMARY_PROJECTION, // What columns do we want.
                select, // Query
                null,
                    /* You may include ?s in selection, which will be replaced by the values from
                     selectionArgs, in the order that they appear in the selection. The values will
                     be bound as Strings */

                null // Sorting order
        );

    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        // Discard old cursor by new one without closing them
        mAdapter.swapCursor(cursor);

    }

    /**
     * This is called when the Loader needs to be reset. A loader will automatically
     * return the last result when called twice or more.
     * If we want it to re do the query (perpahps with new parameters) it is mandatory to
     * reset it.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        // Release cursor
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        // Also called when destroying the activity/fragment.
        mAdapter.swapCursor(null);
    }

}
