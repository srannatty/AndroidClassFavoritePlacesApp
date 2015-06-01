package gerber.uchicago.edu;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import gerber.uchicago.edu.People.Tab1List;
import gerber.uchicago.edu.People.Tab2Grid;
import gerber.uchicago.edu.People.Tab3Edit;
import gerber.uchicago.edu.People.Tab4New;
import gerber.uchicago.edu.Places.Tab1;
import gerber.uchicago.edu.Places.Tab2;
import gerber.uchicago.edu.Places.Tab3;
import gerber.uchicago.edu.Places.Tab4;

/**
 * Created by Edwin on 15/02/2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the mCharSequences of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created

    boolean bPeople;
    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

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
    //TODO THE TAB ORDERS ARE a little weird!
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 2:
                if (bPeople) {
                    Tab3Edit tab3Edit = new Tab3Edit();
                    return tab3Edit;
                } else {
                    Tab1 tab1 = new Tab1();
                    return tab1;
                }

            case 0:
                if (bPeople) {
                    Tab1List tab1List = new Tab1List();
                    return tab1List;
                } else {
                    Tab2 tab2 = new Tab2();
                    return tab2;
                }

            case 1:
                if (bPeople) {
                    Tab2Grid tab2Grid = new Tab2Grid();
                    return tab2Grid;
                } else {
                    Tab3 tab3 = new Tab3();
                    return tab3;
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
                    Tab1 tabdefault2 = new Tab1();
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