package com.badr.infodota.fragment.trackdota.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.badr.infodota.R;
import com.badr.infodota.activity.HeroInfoActivity;
import com.badr.infodota.api.heroes.Hero;
import com.badr.infodota.api.trackdota.GameManager;
import com.badr.infodota.api.trackdota.TrackdotaUtils;
import com.badr.infodota.api.trackdota.core.BanPick;
import com.badr.infodota.api.trackdota.core.CoreResult;
import com.badr.infodota.api.trackdota.game.League;
import com.badr.infodota.api.trackdota.game.Team;
import com.badr.infodota.api.trackdota.live.LiveGame;
import com.badr.infodota.util.GrayImageLoadingListener;
import com.badr.infodota.util.Refresher;
import com.badr.infodota.util.Updatable;
import com.badr.infodota.util.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ABadretdinov
 * 14.04.2015
 * 16:44
 */
public class CommonInfo extends Fragment implements Updatable<Pair<CoreResult,LiveGame>> {
    private Refresher refresher;
    private CoreResult coreResult;
    private LiveGame liveGame;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    final private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            if(refresher!=null) {
                mScrollContainer.setRefreshing(true);
                refresher.onRefresh();
            }
        }
    };
    public static CommonInfo newInstance(Refresher refresher,CoreResult coreResult, LiveGame liveGame){
        CommonInfo fragment=new CommonInfo();
        fragment.refresher=refresher;
        fragment.coreResult=coreResult;
        fragment.liveGame=liveGame;
        return fragment;
    }
    private SwipeRefreshLayout mScrollContainer;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.trackdota_game_common,container,false);

        mScrollContainer = (SwipeRefreshLayout) view.findViewById(R.id.listContainer);
        mScrollContainer.setOnRefreshListener(mOnRefreshListener);
        mScrollContainer.setColorSchemeResources(R.color.primary);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.empty_item)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        imageLoader = ImageLoader.getInstance();
        initView();
    }

    @Override
    public void onUpdate(Pair<CoreResult,LiveGame> entity) {
        mScrollContainer.setRefreshing(false);
        this.coreResult=entity.first;
        this.liveGame=entity.second;
        initView();
    }

    private void initView() {
        View root=getView();
        Activity activity=getActivity();
        GameManager gameManager=GameManager.getInstance();
        if(coreResult!=null&&root!=null&&activity!=null){
            League league=coreResult.getLeague();
            if(league!=null) {
                if (league.isHasImage()) {
                    imageLoader.displayImage(TrackdotaUtils.getLeagueImageUrl(league.getId()), (ImageView) root.findViewById(R.id.league_logo), options);
                }
                ((TextView) root.findViewById(R.id.league_name)).setText(league.getName());
                final String leagueUrl=league.getUrl();
                if(!TextUtils.isEmpty(leagueUrl)) {
                    root.findViewById(R.id.league_holder).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent inBrowser = new Intent(Intent.ACTION_VIEW);
                            inBrowser.setData(Uri.parse(leagueUrl));
                            startActivity(inBrowser);
                        }
                    });
                }
            }
            TextView gameRdWins=(TextView)root.findViewById(R.id.game_rd_wins);
            gameRdWins.setText(coreResult.getRadiantWins()+" - "+coreResult.getDireWins());
            StringBuilder gameState=new StringBuilder(getString(R.string.game));
            gameState.append(" ");
            gameState.append(coreResult.getDireWins()+coreResult.getRadiantWins()+1);
            gameState.append(" / ");
            gameState.append(getString(R.string.bo));
            gameState.append(1+coreResult.getSeriesType()*2);
            if(coreResult.getSeriesType()==0){
                gameRdWins.setText("-");

            }
            /*
            switch (coreResult.getSeriesType()){
                case 0:
                    gameState.append(1);
                    break;
                case 1:
                    gameState.append(3);
                    break;
                default:
                    gameState.append("{").append(coreResult.getSeriesType()).append("}");
            }*/
            ((TextView)root.findViewById(R.id.game_state)).setText(gameState.toString());
            ((TextView)root.findViewById(R.id.viewers)).setText(MessageFormat.format(getString(R.string.viewers_),coreResult.getSpectators()));
            SimpleDateFormat dateTimeFormat=new SimpleDateFormat("HH:mm  dd.MM.yyyy");
            ((TextView) root.findViewById(R.id.game_start_time)).setText(dateTimeFormat.format(new Date(coreResult.getStartTime()*1000L)));

            Team radiant=coreResult.getRadiant();
            if(radiant!=null){
                ((TextView)root.findViewById(R.id.radiant_tag)).setText(TrackdotaUtils.getTeamTag(radiant,TrackdotaUtils.RADIANT));
                ((TextView)root.findViewById(R.id.radiant_name)).setText(TrackdotaUtils.getTeamName(radiant, TrackdotaUtils.RADIANT));
                if(radiant.isHasLogo()){
                    imageLoader.displayImage(TrackdotaUtils.getTeamImageUrl(radiant), (ImageView) root.findViewById(R.id.radiant_logo), options);
                }
                ((TextView)root.findViewById(R.id.radiant_picks_header)).setText(TrackdotaUtils.getTeamTag(radiant,TrackdotaUtils.RADIANT)+" picks");
                ((TextView)root.findViewById(R.id.radiant_bans_header)).setText(TrackdotaUtils.getTeamTag(radiant,TrackdotaUtils.RADIANT)+" bans");
            }
            Team dire=coreResult.getDire();
            if(dire!=null){
                ((TextView)root.findViewById(R.id.dire_tag)).setText(TrackdotaUtils.getTeamTag(dire,TrackdotaUtils.DIRE));
                ((TextView)root.findViewById(R.id.dire_name)).setText(TrackdotaUtils.getTeamName(dire, TrackdotaUtils.DIRE));
                if(dire.isHasLogo()){
                    imageLoader.displayImage(TrackdotaUtils.getTeamImageUrl(dire), (ImageView) root.findViewById(R.id.dire_logo), options);
                }
                ((TextView)root.findViewById(R.id.dire_picks_header)).setText(TrackdotaUtils.getTeamTag(dire,TrackdotaUtils.DIRE)+" picks");
                ((TextView)root.findViewById(R.id.dire_bans_header)).setText(TrackdotaUtils.getTeamTag(dire,TrackdotaUtils.DIRE)+" bans");
            }
            long minutes=coreResult.getDuration()/60;
            long seconds=coreResult.getDuration()-minutes*60;
            ((TextView)root.findViewById(R.id.game_duration)).setText(minutes+":"+(seconds<10?"0":"")+seconds);
            //((TextView)root.findViewById(R.id.nwa_team_tag)).setText();
            TextView gameStatus = (TextView) root.findViewById(R.id.game_status);
            gameStatus.setText(TrackdotaUtils.getGameStatus(activity,coreResult.getStatus()));

            //todo net worth advantage team tag, gold advantage - from LiveGame
            if(liveGame!=null) {
                ((TextView) root.findViewById(R.id.roshan_status)).setText(liveGame.getRoshanRespawnTimer() > 0 ?MessageFormat.format(getString(R.string.respawn_in),liveGame.getRoshanRespawnTimer()): getString(R.string.alive));

                ((TextView) root.findViewById(R.id.radiant_score)).setText(String.valueOf(liveGame.getRadiant().getScore()));
                ((TextView) root.findViewById(R.id.dire_score)).setText(String.valueOf(liveGame.getDire().getScore()));

                if (liveGame.isPaused()) {
                    gameStatus.setText(getString(R.string.paused));
                }
            }
            LayoutInflater inflater= (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            /*radiant picks&bans*/
            LinearLayout radiantPicks= (LinearLayout) root.findViewById(R.id.radiant_picks);
            radiantPicks.removeAllViews();
            if(coreResult.getRadiantPicks()!=null){
                for(int i=0,size=coreResult.getRadiantPicks().size();i<size;i++){
                    BanPick pick=coreResult.getRadiantPicks().get(i);
                    View row=inflater.inflate(R.layout.trackdota_hero_pickban,radiantPicks,false);
                    ((TextView)row.findViewById(R.id.number)).setText(String.valueOf(i+1));
                    Hero hero=gameManager.getHero(pick.getHeroId());
                    if(hero!=null) {
                        imageLoader.displayImage(
                                Utils.getHeroFullImage(hero.getDotaId()),
                                (ImageView) row.findViewById(R.id.image),
                                options);
                        row.setOnClickListener(new HeroInfoActivity.OnDotaHeroClickListener(pick.getHeroId()));
                    }
                    radiantPicks.addView(row);
                    //((LinearLayout.LayoutParams)row.getLayoutParams()).weight=1;
                }
            }

            LinearLayout radiantBans= (LinearLayout) root.findViewById(R.id.radiant_bans);
            radiantBans.removeAllViews();
            if(coreResult.getRadiantBans()!=null){
                for(int i=0,size=coreResult.getRadiantBans().size();i<size;i++){
                    BanPick ban=coreResult.getRadiantBans().get(i);
                    View row=inflater.inflate(R.layout.trackdota_hero_pickban,radiantPicks,false);
                    ((TextView)row.findViewById(R.id.number)).setText(String.valueOf(i+1));
                    Hero hero=gameManager.getHero(ban.getHeroId());
                    if(hero!=null) {
                        imageLoader.displayImage(
                                Utils.getHeroFullImage(hero.getDotaId()),
                                (ImageView) row.findViewById(R.id.image),
                                options,
                                new GrayImageLoadingListener()
                                );
                        row.setOnClickListener(new HeroInfoActivity.OnDotaHeroClickListener(ban.getHeroId()));
                    }
                    radiantBans.addView(row);
                   // ((LinearLayout.LayoutParams)row.getLayoutParams()).weight=1;
                }
            }

            /*dire picks&bans*/
            LinearLayout direPicks= (LinearLayout) root.findViewById(R.id.dire_picks);
            direPicks.removeAllViews();
            if(coreResult.getDirePicks()!=null){
                for(int i=0,size=coreResult.getDirePicks().size();i<size;i++){
                    BanPick pick=coreResult.getDirePicks().get(i);
                    View row=inflater.inflate(R.layout.trackdota_hero_pickban,radiantPicks,false);
                    ((TextView)row.findViewById(R.id.number)).setText(String.valueOf(i+1));
                    Hero hero=gameManager.getHero(pick.getHeroId());
                    if(hero!=null) {
                        imageLoader.displayImage(
                                Utils.getHeroFullImage(hero.getDotaId()),
                                (ImageView) row.findViewById(R.id.image),
                                options);
                        row.setOnClickListener(new HeroInfoActivity.OnDotaHeroClickListener(pick.getHeroId()));
                    }
                    direPicks.addView(row);
                    //((LinearLayout.LayoutParams)row.getLayoutParams()).weight=1;
                }
            }

            LinearLayout direBans= (LinearLayout) root.findViewById(R.id.dire_bans);
            direBans.removeAllViews();
            if(coreResult.getDireBans()!=null){
                for(int i=0,size=coreResult.getDireBans().size();i<size;i++){
                    BanPick ban=coreResult.getDireBans().get(i);
                    View row=inflater.inflate(R.layout.trackdota_hero_pickban,radiantPicks,false);
                    ((TextView)row.findViewById(R.id.number)).setText(String.valueOf(i+1));
                    Hero hero=gameManager.getHero(ban.getHeroId());
                    if(hero!=null) {
                        imageLoader.displayImage(
                                Utils.getHeroFullImage(hero.getDotaId()),
                                (ImageView) row.findViewById(R.id.image),
                                options,
                                new GrayImageLoadingListener());
                        row.setOnClickListener(new HeroInfoActivity.OnDotaHeroClickListener(ban.getHeroId()));
                    }
                    direBans.addView(row);
                    //((LinearLayout.LayoutParams)row.getLayoutParams()).weight=1;
                }
            }
        }
    }
}