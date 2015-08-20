package com.badr.infodota.task;

import com.badr.infodota.BeanContainer;
import com.badr.infodota.api.dotabuff.Unit;
import com.badr.infodota.service.player.PlayerService;
import com.badr.infodota.util.retrofit.TaskRequest;

/**
 * Created by ABadretdinov
 * 20.08.2015
 * 16:56
 */
public class FriendsLoadRequest extends TaskRequest<Unit.List> {
    private long mAccountId;

    public FriendsLoadRequest(long accountId) {
        super(Unit.List.class);
        mAccountId = accountId;
    }

    @Override
    public Unit.List loadData() throws Exception {
        PlayerService playerService = BeanContainer.getInstance().getPlayerService();
        return new Unit.List(playerService.loadFriends(mAccountId));
    }
}