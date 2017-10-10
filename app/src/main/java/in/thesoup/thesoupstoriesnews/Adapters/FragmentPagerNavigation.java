package in.thesoup.thesoupstoriesnews.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import in.thesoup.thesoupstoriesnews.Fragments.DiscoverFragmentMain;
import in.thesoup.thesoupstoriesnews.Fragments.FollowingFragment;
import in.thesoup.thesoupstoriesnews.Fragments.HomeFragment;

/**
 * Created by Jani on 07-09-2017.
 */

public class FragmentPagerNavigation extends FragmentStatePagerAdapter {

        private Context mcontext;
        SharedPreferences pref;

        public FragmentPagerNavigation(FragmentManager fm, Context context) {
            super(fm);
            mcontext = context;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return new DiscoverFragmentMain();
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return new HomeFragment();
                case 2:
                    return new FollowingFragment();
                default:
                    return null;
            }}


        @Override
        public int getItemPosition(Object object) {
            // POSITION_NONE makes it possible to reload the PagerAdapter
            return FragmentPagerNavigation.POSITION_NONE;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "Discover";

            } else if (position == 1){
                return "Home";
            } else {
                return "Following";
            }
        }




    }


