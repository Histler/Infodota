package com.badr.infodota.api.trackdota.live;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ABadretdinov
 * 13.04.2015
 * 15:19
 */
public class Team implements Serializable {
    @Expose
    private List<Player> players;
    @Expose
    private int score;
    @Expose
    private Stats stats;

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }
}
