package gerber.uchicago.edu;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import gerber.uchicago.edu.People.Tab1List;
import gerber.uchicago.edu.People.Tab2Grid;
import gerber.uchicago.edu.People.Tab3Edit;
import gerber.uchicago.edu.People.Tab4New;
import gerber.uchicago.edu.Places.Tab3EditResto;
import gerber.uchicago.edu.Places.Tab1ListResto;
import gerber.uchicago.edu.Places.Tab2GridResto;
import gerber.uchicago.edu.Places.Tab4;

/**
 * Created by Edwin on 15/02/2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the mCharSequences of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    boolean bPeople;
    Context mContext;
    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
    }

    public ViewPagerAdapter(Context context, FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);
        this.mContext = context;
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
    }

    public void setbPeople(boolean b) {
        this.bPeople = b;
    }

    public boolean getbPeople() {
        return this.bPeople;
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 2:
                if (bPeople) {
                    Tab3Edit tab3Edit = new Tab3Edit();
                    return tab3Edit;
                } else {
                    //todo race condition things
                    Tab3EditResto tab3EditResto =
                            Tab3EditResto.newInstance(((MainActivity) mContext).mRecentIdClicked);
                    return tab3EditResto;
                }

            case 0:
                if (bPeople) {
                    Tab1List tab1List = new Tab1List();
                    return tab1List;
                } else {
                    Tab1ListResto tab1ListResto = new Tab1ListResto();
                    return tab1ListResto;
                }

            case 1:
                if (bPeople) {
                    Tab2Grid tab2Grid = new Tab2Grid();
                    return tab2Grid;
                } else {
                    Tab2GridResto tab2GridResto = new Tab2GridResto();
                    return tab2GridResto;
                }

            case 3:
                if (bPeople) {
                    Tab4New tab4New = new Tab4New();
                    return tab4New;
                } else {
                    Tab4 tab4 = new Tab4();
                    return tab4;
                }

            default:
                if (bPeople) {
                    Tab3Edit tabdefault1 = new Tab3Edit();
                    return tabdefault1;
                } else {
                    Tab3EditResto tabdefault2 = new Tab3EditResto();
                    return tabdefault2;
                }
        }
    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}