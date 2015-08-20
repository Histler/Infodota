package com.badr.infodota.task;

import android.content.Context;

import com.badr.infodota.BeanContainer;
import com.badr.infodota.api.trackdota.live.LiveGame;
import com.badr.infodota.service.trackdota.TrackdotaService;
import com.badr.infodota.util.retrofit.TaskRequest;

/**
 * Created by ABadretdinov
 * 20.08.2015
 * 15:10
 */
public class LiveGameLoadRequest extends TaskRequest<LiveGame> {
    private long mMatchId;
    private Context mContext;

    public LiveGameLoadRequest(Context context, long matchId) {
        super(LiveGame.class);
        this.mContext = context;
        this.mMatchId = matchId;
    }

    @Override
    public LiveGame loadData() throws Exception {
        BeanContainer container = BeanContainer.getInstance();
        TrackdotaService trackdotaService = container.getTrackdotaService();
        return trackdotaService.getLiveGame(mContext, mMatchId);
    }
}
