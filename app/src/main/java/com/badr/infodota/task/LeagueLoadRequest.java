package com.badr.infodota.task;

import com.badr.infodota.BeanContainer;
import com.badr.infodota.api.trackdota.LeaguesResult;
import com.badr.infodota.service.trackdota.TrackdotaService;
import com.badr.infodota.util.retrofit.TaskRequest;

/**
 * Created by ABadretdinov
 * 20.08.2015
 * 15:37
 */
public class LeagueLoadRequest extends TaskRequest<LeaguesResult> {

    public LeagueLoadRequest() {
        super(LeaguesResult.class);
    }

    @Override
    public LeaguesResult loadData() throws Exception {
        BeanContainer container = BeanContainer.getInstance();
        TrackdotaService trackdotaService = container.getTrackdotaService();
        return trackdotaService.getLeagues();
    }
}
