package com.badr.infodota.activity;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.ActionMenuPresenter;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.badr.infodota.R;
import com.badr.infodota.api.Constants;
import com.badr.infodota.fragment.SearchableFragment;
import com.badr.infodota.fragment.cosmetic.CosmeticList;
import com.badr.infodota.fragment.counter.CounterPickFilter;
import com.badr.infodota.fragment.hero.HeroesList;
import com.badr.infodota.fragment.item.ItemsList;
import com.badr.infodota.fragment.leagues.LeaguesGamesList;
import com.badr.infodota.fragment.news.NewsList;
import com.badr.infodota.fragment.player.groups.PlayerGroupsHolder;
import com.badr.infodota.fragment.quiz.QuizTypeSelect;
import com.badr.infodota.fragment.trackdota.LiveGamesList;
import com.badr.infodota.fragment.twitch.TwitchHolder;
import com.badr.infodota.util.UpdateUtils;

/**
 * User: ABadretdinov
 * Date: 15.01.14
 * Time: 14:27
 */
public class ListHolderActivity extends BaseActivity implements SearchView.OnQueryTextListener, AdapterView.OnItemSelectedListener {
    int lastSelected = -1;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fragment_holder);

        mActionMenuView.setPresenter(new ActionMenuPresenter(this));

        ActionBar bar = getSupportActionBar();
        bar.setDisplayShowTitleEnabled(false);
        bar.setDisplayShowHomeEnabled(false);

        Spinner navSpinner = (Spinner) mToolbar.findViewById(R.id.nav_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, getResources().getStringArray(R.array.main_menu));
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        navSpinner.setAdapter(adapter);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int selected = prefs.getInt("mainMenuLastSelected", 0);
        navSpinner.setOnItemSelectedListener(this);
        navSpinner.setSelection(Math.min(selected, adapter.getCount() - 1));

        UpdateUtils.checkNewVersion(this,false);
        //не нужен AppRater.app_launched(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        getMenuInflater().inflate(R.menu.search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(getString(android.R.string.search_go));
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    //public boolean isSearchViewVisible=false;
    public boolean onQueryTextChange(String textNew) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.details);
        if (fragment instanceof SearchableFragment) {
            ((SearchableFragment) fragment).onTextSearching(textNew);
        }
        return true;
    }

    public boolean onQueryTextSubmit(String text) {

        return true;
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getString(R.string.back_toast), Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, Constants.MILLIS_FOR_EXIT);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (!getResources().getBoolean(R.bool.is_tablet)) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                ((ViewGroup) mActionMenuView.getParent()).removeView(mActionMenuView);
                ((ViewGroup) findViewById(R.id.top_container)).addView(mActionMenuView);
                ((LinearLayout.LayoutParams) mActionMenuView.getLayoutParams()).weight = 1;
                ((LinearLayout.LayoutParams) mToolbar.getLayoutParams()).weight = 1;
            } else {
                ((ViewGroup) mActionMenuView.getParent()).removeView(mActionMenuView);
                ((ViewGroup) findViewById(R.id.main_view)).addView(mActionMenuView);
                ((LinearLayout.LayoutParams) mActionMenuView.getLayoutParams()).weight = 0;
                ((LinearLayout.LayoutParams) mToolbar.getLayoutParams()).weight = 0;
            }
        }
        mActionMenuView.onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
    }

    public void replaceFragment(Fragment details) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.details, details);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (lastSelected != position) {
            Fragment details;
            switch (position) {
                default:
                case 0:
                    details = new HeroesList();
                    break;
                case 1:
                    details = new ItemsList();
                    break;
                case 2:
                    details = new PlayerGroupsHolder();
                    break;
                case 3:
                    details = new CounterPickFilter();
                    break;
                case 4:
                    details = new CosmeticList();
                    break;
                case 5:
                    details = new QuizTypeSelect();
                    break;
                case 6:
                    details = new TwitchHolder();
                    break;
                case 7:
                    details = new NewsList();
                    break;
                case 8:
                    details = LeaguesGamesList.newInstance(null);
                    break;
                case 9:
                    details= new LiveGamesList();
                    break;
                    /*
                case 9:
					details=new LeaguesGamesList.newInstance("&c2=7057&c1=2390");
                    break;*/
            }
            replaceFragment(details);
            lastSelected = position;
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            prefs.edit().putInt("mainMenuLastSelected", lastSelected).commit();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
