package nimble.survey;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Calendar;
import java.util.List;

import nimble.survey.adapters.ContentFragmentAdapter;
import nimble.survey.interfaces.SurveyFetchInterface;
import nimble.survey.models.DataRepo;
import nimble.survey.models.Survey;
import nimble.survey.presenter.SurveyPresenter;

public class MainActivity extends AppCompatActivity implements SurveyFetchInterface.View, NavigationView.OnNavigationItemSelectedListener {
    ProgressDialog dialog;
    private SurveyFetchInterface.Presenter mPresenter;
    private SurveyPresenter surveyPresenter;
    private LinearLayout indicatorLayout;
    private DrawerLayout drawer;
    private SimpleDraweeView draweeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        indicatorLayout = findViewById(R.id.indicatorLayout);
        draweeView = findViewById(R.id.img);


        dialog = new ProgressDialog(this);
        surveyPresenter = new SurveyPresenter(this); //pass the view reference to the presenter
        setPresenter(surveyPresenter);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        loadSurveys();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Refresh menu options
     * Refreshing the survey list
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_refresh) {
            loadSurveys();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    Boolean[] indication; // For bubble indicator

    /**
     * Rendering the survey card and indicator bubbles
     */
    public void initViewPager() {
        if (indicatorLayout.getChildCount() > 0)
            indicatorLayout.removeAllViews();
        final VerticalViewPager viewPager = findViewById(R.id.vertical_viewpager);
        DataRepo imgRepo = DataRepo.newInstance();
        List<Survey> surveyList = imgRepo.getSurveys();
        indication = new Boolean[surveyList.size()];
        ContentFragmentAdapter.Holder holder = new ContentFragmentAdapter.Holder(getSupportFragmentManager());

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        for (int i = 0; i < surveyList.size(); i++) {
            holder.add(ContentFragment.newInstance(surveyList.get(i), i + 1));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) ((width * 3.5) / 100), (int) ((height * 2) / 100));
            layoutParams.setMargins(10, 10, 10, 10);
            final View view = new View(this);
            if (i == 0) {
                indication[i] = true;

            } else {
                indication[i] = false;

            }

            view.setPadding(5, 5, 5, 5);
            view.setLayoutParams(layoutParams);
            indicatorLayout.addView(view);
        }
        setIndication();
        viewPager.setAdapter(holder.set());
        viewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                Log.e("Scrolled", String.valueOf(i));
            }

            @Override
            public void onPageSelected(int i) {
                Log.e("Selected", String.valueOf(i));
                for (int j = 0; j < indication.length; j++) {
                    if (indication[j] == true)
                        indication[j] = false;
                }
                indication[i] = true;
                setIndication();
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                Log.e("Scroll Change", String.valueOf(i));
            }
        });

    }

    /**
     * Selecting the indicator bubbles according to the selected survey card
     */
    private void setIndication() {
        if (indicatorLayout.getChildCount() > 0) {
            for (int i = 0; i < indicatorLayout.getChildCount(); i++) {

                if (indication[i] == true)
                    indicatorLayout.getChildAt(i).setBackground(getResources().getDrawable(R.drawable.active_dot));
                else
                    indicatorLayout.getChildAt(i).setBackground(getResources().getDrawable(R.drawable.inactive_dot));
            }
            indicatorLayout.invalidate();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Fetch all surveys
     * Update access token if it is expired
     */
    private void loadSurveys() {
        int currentTime = Calendar.getInstance().get(Calendar.MINUTE);
        int fetchTime = Integer.parseInt(Pref.getValue(Pref.TYPE.TOKEN_FETCH_TIME.toString(), "0"));
        int expiry = Integer.parseInt(Pref.getValue(Pref.TYPE.EXPIRY.toString(), "0"));
        if (currentTime - fetchTime > expiry) {
            Log.i("Access token Expired", "True");
            APICall apiCall = new APICall();
            apiCall.start(mPresenter);
        } else {
            mPresenter.start();
        }
    }

    /**
     * Loading progress dialog
     */
    public void startSpin() {
        if (dialog != null) {
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage(getResources().getString(R.string.loading));
            dialog.setIndeterminate(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

    }

    /**
     * Dismissing progress Dialog
     */
    public void stopSpin() {
        if (dialog != null)
            dialog.dismiss();

    }

    /**
     * Presenter Object -  for fetching the data from server
     *
     * @param presenter
     */
    @Override
    public void setPresenter(SurveyFetchInterface.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
