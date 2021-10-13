package com.videocall.datingapp.Accounts;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.videocall.datingapp.Favorite.FavoritesFragment;
import com.videocall.datingapp.Likes.LikesFragment;
import com.videocall.datingapp.Matches.MatchesFragment;
import com.videocall.datingapp.Visitors.VisitorsFragment;

public class AccountsAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public AccountsAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:

                MatchesFragment tab0 = new MatchesFragment();
                return tab0;
            case 1:

                LikesFragment tab1 = new LikesFragment();

                return tab1;
            case 2:
                VisitorsFragment tab2 = new VisitorsFragment();


                return tab2;

            case 3:
                FavoritesFragment tab3 = new FavoritesFragment();


                return tab3;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }


}