package com.badr.infodota.task;

import android.content.Context;

import com.badr.infodota.BeanContainer;
import com.badr.infodota.api.heroes.Hero;
import com.badr.infodota.service.hero.HeroService;
import com.badr.infodota.util.retrofit.TaskRequest;

/**
 * Created by ABadretdinov
 * 20.08.2015
 * 14:44
 */
public class HeroLoadRequest extends TaskRequest<Hero.List> {

    private Context mContext;
    private String mFilter;

    public HeroLoadRequest(Context context, String filter) {
        super(Hero.List.class);
        mContext = context;
        mFilter = filter;
    }

    @Override
    public Hero.List loadData() throws Exception {
        HeroService heroService = BeanContainer.getInstance().getHeroService();
        return heroService.getFilteredHeroes(mContext, mFilter);
    }
}
