package com.badr.infodota.adapter.pager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.badr.infodota.R;
import com.badr.infodota.api.trackdota.game.GamesResult;
import com.badr.infodota.fragment.trackdota.FeaturedGamesList;
import com.badr.infodota.fragment.trackdota.LiveGamesList;
import com.badr.infodota.util.Refresher;
import com.badr.infodota.util.Updatable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by ABadretdinov
 * 14.04.2015
 * 11:29
 */
public class TrackdotaPagerAdapter extends FragmentPagerAdapter{
    private String[] titles;
    private Map<Integer,Updatable> groupMap=new HashMap<Integer,Updatable>();
    private Refresher refresher;
    public TrackdotaPagerAdapter(Context context,FragmentManager fm, Refresher refresher) {
        super(fm);
        titles=context.getResources().getStringArray(R.array.trackdota);
        this.refresher=refresher;
    }

    @Override
    public Fragment getItem(int position) {
        if(groupMap.get(position)!=null){
            return (Fragment)groupMap.get(position);
        }
        switch (position){
            case 0:
                LiveGamesList liveGames=LiveGamesList.newInstance(refresher);
                groupMap.put(0,liveGames);
                return liveGames;
            default:
            case 1:
                FeaturedGamesList featuredGames=FeaturedGamesList.newInstance(refresher);
                groupMap.put(1,featuredGames);
                return featuredGames;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        groupMap.remove(position);
        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return 2;//titles.length;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    public void update(GamesResult gamesResult){
        Set<Integer> keySet = groupMap.keySet();
        for (Integer key : keySet) {
            Object entity;
            if(gamesResult==null){
                entity=null;
            }
            else {
                switch (key) {
                    case 0:
                        entity = gamesResult.getEnhancedMatches();
                        break;
                    case 1:
                        entity = gamesResult.getFinishedGames();
                        break;
                    default:
                    case 2:
                        entity = gamesResult.getRecentGames();
                        break;
                }
            }
            groupMap.get(key).onUpdate(entity);
        }
    }
}
