package in.thesoupstoriesnews.thesoup.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import in.thesoupstoriesnews.thesoup.Fragments.DiscoverFragment;
import in.thesoupstoriesnews.thesoup.Fragments.MyFeedFragment;

public class FeedFragmentPagerAdapter extends FragmentStatePagerAdapter{

    private Context mcontext;
    SharedPreferences pref;

    public FeedFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mcontext = context;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return new DiscoverFragment();
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return new MyFeedFragment().newinstance(position);
            default:
                return null;
        }}


    @Override
    public int getItemPosition(Object object) {
        // POSITION_NONE makes it possible to reload the PagerAdapter
        return FeedFragmentPagerAdapter.POSITION_NONE;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Discover";

        } else if (position == 1){
            return "NOTIFICATIONS";
        } else {
            return "Filter";
        }
    }




    }


