package gerber.uchicago.edu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.view.ActionMode;
import android.util.DisplayMetrics;
import android.util.Log;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.List;

import gerber.uchicago.edu.Places.Tab2GridResto;
import gerber.uchicago.edu.Places.Tab3EditResto;
import gerber.uchicago.edu.Places.Tab1ListResto;
import gerber.uchicago.edu.db.RestosDbAdapter;
import gerber.uchicago.edu.sound.SoundVibeUtils;

/**
 * Created by Edwin on 15/02/2015.
 */
public class MainActivity extends ActionBarActivity implements
        Tab1ListResto.OnTab2InteractionListener,
        Tab3EditResto.OnTab1InteractionListener,
        ViewPager.OnPageChangeListener,
        android.support.v7.view.ActionMode.Callback {

    // Declaring Your View and Variables
    private static final String VERY_FIRST_LOAD_MAIN = "our_very_first_load_";
    public static final String BOOLEAN_ARRAY_KEY = "boolean_array_key";
    public boolean bTutorial;

    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence mCharSequences[] = {"list", "grid", "edit", "new"};
    int mNumboftabs = 4;
    ActionBar actionBar;

    ActionMode mActionMode;
    boolean bButtonArray[] = new boolean[3];
    boolean bAll;  //indicates the non-use of bFav for filtering
    boolean bFav;  //used for filtering true indicates show favorite places
    SharedPreferences mPreferences;

    int mRecentIdClicked;

    //private Menu mMenu;

    // private Drawable oldBackground = null;
    private int currentColor;
    private SystemBarTintManager mTintManager;
    // private LayoutInflater mInflator;

    private RestosDbAdapter mDbAdapter;

    //Drawer
    // variables for Drawer
    private String[] mDrawerMenuTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //filter stuff default;
        bAll = true;
        bFav = true;

        actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);


        // create our manager instance after the content view is set
        mTintManager = new SystemBarTintManager(this);
        // enable status bar tint
        mTintManager.setStatusBarTintEnabled(true);

        // Creating The ViewPagerAdapter and Passing Fragment Manager, mCharSequences fot the Tabs and Number Of Tabs.
        adapter = new ViewPagerAdapter(this, getSupportFragmentManager(), mCharSequences, mNumboftabs);
        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);

        pager.setAdapter(adapter);


        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available wid

        tabs.setOnPageChangeListener(this);

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {


                return getResources().getColor(R.color.tabsScrollColor);

            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
        changeColor(getResources().getColor(R.color.purple_dark), getResources().getColor(R.color.purple));

        //get the shared preferences
        mPreferences = this.getSharedPreferences(
                "gerber.uchicago.edu", this.MODE_PRIVATE);

        boolean bFirstLoad = mPreferences.getBoolean(VERY_FIRST_LOAD_MAIN, true);
        if (bFirstLoad) {
            for (int nC = 0; nC < 3; nC++) {
                if (nC == 1) {
                    bButtonArray[nC] = true;
                } else {
                    bButtonArray[nC] = false;
                }
            }
            bTutorial = true;
            //set the flag in preferences so that this block will never be called again.
            //We comment this out so that DB inserts new restaurant before the block is called
            //mPreferences.edit().putBoolean(VERY_FIRST_LOAD_MAIN, false).commit();

        } else {
            //get it from the prefs
            bTutorial = false;
            bButtonArray = PrefsMgr.getBooleanArray(this, BOOLEAN_ARRAY_KEY, bButtonArray.length );
        }
        adapter.setbPeople(bButtonArray[0]);
        inflateActionBar(actionBar, 0);


        //Doing this because oncreate is only called once
        mDbAdapter = new RestosDbAdapter(this);
        mDbAdapter.open();
        mRecentIdClicked = mDbAdapter.fetchSomeID();
        mDbAdapter.close();

        //Creating Drawer
        mDrawerMenuTitles = getResources().getStringArray(R.array.drawer_menu_array);
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mDrawerMenuTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        if (bTutorial) {
            //Open Tutorial on the first load
            Intent tItn = new Intent(MainActivity.this, TutorialActivity.class);
            tItn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(tItn);
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //Toast.makeText(MainActivity.this, "Click on Navigation bar" + id, Toast.LENGTH_SHORT).show();
            switch (position) {
                case 0:
                    //Listview Tab1
                    pager.setCurrentItem(0);
                    break;
                case 1:
                    //Gridview Tab2
                    pager.setCurrentItem(1);
                    break;
                case 2:
                    //Add New Tab4
                    pager.setCurrentItem(3);
                    break;
                default:
                    //Error case
                    Toast.makeText(MainActivity.this, "No such item", Toast.LENGTH_SHORT).show();
                    break;

            }

        }
    }



    @Override
    public void onPageSelected(int position) {

        SoundVibeUtils.playSound(this, R.raw.swish);
        switch (position) {
            case 0:
            case 1:
                changeColor(getResources().getColor(R.color.purple_dark), getResources().getColor(R.color.purple));
                break;
            case 2:
                changeColor(getResources().getColor(R.color.orange_dark), getResources().getColor(R.color.orange));

                break;
            case 3:
                changeColor(getResources().getColor(R.color.green_dark), getResources().getColor(R.color.green));

                break;
        }

    }


    public void changetab(int tabnumber) {
        //This is to trick the viewpager to reload the fragments
        pager.setCurrentItem(3);
        pager.setCurrentItem(tabnumber);
        adapter.notifyDataSetChanged();
    }

    //Used for refreshing fragment view when setting filters
    private void Refresh_Views_frags() {
        List<Fragment> fragLists = getSupportFragmentManager().getFragments();
        for (android.support.v4.app.Fragment frag : fragLists) {
            if (frag != null) {
                try {
                    ((Tab1ListResto) frag).filter_on();
                } catch (Exception e) {
                    try {
                        ((Tab2GridResto) frag).filter_on();
                    } catch (Exception e1) {
                        Log.d("all_frags", "tried for non-view frags");
                    }
                }
            }
        }
    }



    public void setRecentIdClicked(int id) {
        this.mRecentIdClicked = id;
    }

    public boolean getbFav() {
        return this.bFav;
    }

    public boolean getbAll() {
        return this.bAll;
    }

    public void gotoEditTab(int itemID) {
        mRecentIdClicked = itemID;
        pager.setAdapter(adapter);
        pager.setCurrentItem(2);

        return;
    }

    public int getRecentIdClicked() {
        return mRecentIdClicked;
    }

    private void inflateActionBar(ActionBar bar, int pos) {

        LayoutInflater inflator = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.ab_custom, null);

        bar.setDisplayHomeAsUpEnabled(false);
        bar.setDisplayShowHomeEnabled(false);
        bar.setDisplayShowCustomEnabled(true);
        bar.setDisplayShowTitleEnabled(false);
        bar.setCustomView(v);


        ImageButton v0 = (ImageButton) v.findViewById(R.id.action_0);
        v0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Person button
                Log.d("view0", "GGG");
                bButtonArray[0] = !bButtonArray[0];
                bButtonArray[1] = !bButtonArray[1];
                toggleActionBarButton(0, bButtonArray[0]);
                toggleActionBarButton(1, bButtonArray[1]);
                PrefsMgr.setBooleanArray(MainActivity.this, BOOLEAN_ARRAY_KEY, bButtonArray);
                adapter.setbPeople(bButtonArray[0]);

            }
        });
        ImageButton v1 = (ImageButton) v.findViewById(R.id.action_1);
        v1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Places Button
                Log.d("view1", "GGG");
                bButtonArray[1] = !bButtonArray[1];
                bButtonArray[0] = !bButtonArray[0];
                toggleActionBarButton(0, bButtonArray[0]);
                toggleActionBarButton(1, bButtonArray[1]);
                PrefsMgr.setBooleanArray(MainActivity.this, BOOLEAN_ARRAY_KEY, bButtonArray);
                adapter.setbPeople(bButtonArray[0]);

            }
        });
        ImageButton v2 = (ImageButton) v.findViewById(R.id.action_2);
        v2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Category Button
                Log.d("view2", "GGG");
                bButtonArray[2] = !bButtonArray[2];
                toggleActionBarButton(2, bButtonArray[2]);
                PrefsMgr.setBooleanArray(MainActivity.this, BOOLEAN_ARRAY_KEY, bButtonArray);


                if (bButtonArray[2]) {

                    //This Button is the Filter Button
                    //this is just an example sound
                    SoundVibeUtils.playSound(MainActivity.this, R.raw.power_up);

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                    LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    LinearLayout ll = new LinearLayout(MainActivity.this);
                    View vx = inflater.inflate(R.layout.filters_row, ll);

                    builder.setView(vx);

                    final Dialog dialog = builder.create();


                    Window window = dialog.getWindow();
                    window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    window.setGravity(Gravity.CENTER);

                    // mIdClicked = getIdFromPosition(masterListPosition);
                    //  mRestoClicked = mDbAdapter.fetchRestoById(mIdClicked);
                    dialog.setTitle("Set Filter");
                    dialog.show();

                    //All Case

                    LinearLayout all = (LinearLayout) dialog.findViewById(R.id.fitler_1);
                    all.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bAll = true;
                            adapter.notifyDataSetChanged();
                            Refresh_Views_frags();
                            dialog.dismiss();
                        }
                    });

                    //Favorite Case
                    LinearLayout fav = (LinearLayout) dialog.findViewById(R.id.fitler_2);
                    fav.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bAll = false;
                            bFav = true;
                            adapter.notifyDataSetChanged();
                            Refresh_Views_frags();
                            dialog.dismiss();
                        }
                    });

                    //Not Favorite Case
                    LinearLayout nfav = (LinearLayout) dialog.findViewById(R.id.fitler_3);
                    nfav.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bAll = false;
                            bFav = false;
                            adapter.notifyDataSetChanged();
                            Refresh_Views_frags();
                            dialog.dismiss();
                        }
                    });


                }
                else {
                    bAll = true;
                    adapter.notifyDataSetChanged();
                    Refresh_Views_frags();
                }


            }
        });
        ImageButton v3 = (ImageButton) v.findViewById(R.id.action_3);
        v3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Search Button
                Log.d("view3", "GGG");
                mActionMode = MainActivity.this.startSupportActionMode(MainActivity.this);


            }
        });

        //use position to switch the gone/view


        // MenuInflater inflater = getMenuInflater();
        for (int nC = 0; nC < bButtonArray.length; nC++) {
            toggleActionBarButton(nC, bButtonArray[nC]);
        }
        toggleActionBarButton(3, false);


    }


    private void toggleActionBarButton(int pos, final boolean checked) {

        int nId = getResourceId("fram_button_" + pos, "id", getPackageName());
        final LinearLayout ll = (LinearLayout) findViewById(nId);
        int nIdTopBar = getResourceId("topbar_button_" + pos, "id", getPackageName());
        final View vTopBar = ll.findViewById(nIdTopBar);
        int nButton = getResourceId("action_" + pos, "id", getPackageName());
        final ImageButton imageButton = (ImageButton) ll.findViewById(nButton);


        if (checked) {
            vTopBar.setVisibility(View.VISIBLE);
            imageButton.setBackground(getResources().getDrawable(R.drawable.pressed_mask));
        } else {
            vTopBar.setVisibility(View.INVISIBLE);
            imageButton.setBackground(getResources().getDrawable(R.drawable.unpressed_mask));
        }

        int nDp = dpToPx(4);
        imageButton.setPadding(nDp, nDp, nDp, nDp);

    }

    //http://stackoverflow.com/questions/8309354/formula-px-to-dp-dp-to-px-android
    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    //http://stackoverflow.com/questions/4427608/android-getting-resource-id-from-string
    public int getResourceId(String pVariableName, String pResourcename, String pPackageName) {
        try {
            return getResources().getIdentifier(pVariableName, pResourcename, pPackageName);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void changeColor(int newColor, int tabColor) {
        tabs.setBackgroundColor(tabColor);

        mTintManager.setTintColor(newColor);
        // change ActionBar color just if an ActionBar is available
        Drawable colorDrawable = new ColorDrawable(newColor);
        // Drawable bottomDrawable = new ColorDrawable(getResources().getColor(android.R.color.transparent));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        currentColor = newColor;

    }


    @Override
    public void onTab2Interaction(String id) {

    }

    @Override
    public void onTab1Interaction(int id) {
        mRecentIdClicked = id;
    }



    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //callbacks for contextual action mode
    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        // Inflate a menu resource providing context menu items
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.cam_menu_search, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search_done:


                mode.finish(); // Action picked, so close the CAB
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }


}