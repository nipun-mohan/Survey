package nimble.survey;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

import nimble.survey.interfaces.RecyclerViewClickListener;
import nimble.survey.interfaces.SurveyFetchInterface;
import nimble.survey.models.DataRepo;
import nimble.survey.models.Survey;
import nimble.survey.presenter.SurveyPresenter;
import nimble.survey.transforms.DefaultTransformer;

public class MainActivity extends AppCompatActivity implements SurveyFetchInterface.View {
    ProgressDialog dialog;
    private SurveyFetchInterface.Presenter mPresenter;
    private SurveyPresenter surveyPresenter;
    private LinearLayout indicatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        indicatorLayout = findViewById(R.id.indicatorLayout);

        setTitle("");
        dialog = new ProgressDialog(this);
        surveyPresenter = new SurveyPresenter(this); //pass the view reference to the presenter
        setPresenter(surveyPresenter);
        mPresenter.start();
    }


    Boolean[] indication;

    public void initViewPager() {
        final VerticalViewPager viewPager = (VerticalViewPager) findViewById(R.id.vertical_viewpager);
        //viewPager.setPageTransformer(false, new ZoomOutTransformer());
        //viewPager.setPageTransformer(true, new StackTransformer());
        String title = "ContentFragment";

        DataRepo imgRepo = DataRepo.newInstance();
        List<Survey> surveyList = imgRepo.getSurveys();
        indication = new Boolean[surveyList.size()];
        ContentFragmentAdapter.Holder holder = new ContentFragmentAdapter.Holder(getSupportFragmentManager());

        for (int i = 0; i < surveyList.size(); i++) {
            holder.add(ContentFragment.newInstance(surveyList.get(i), i + 1));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(15, 15);
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
        //If you setting other scroll mode, the scrolled fade is shown from either side of display.
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

    public void startSpin() {
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(getResources().getString(R.string.loading));
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    public void stopSpin() {
        if (dialog != null)
            dialog.dismiss();

    }

    @Override
    public void setPresenter(SurveyFetchInterface.Presenter presenter) {
        mPresenter = presenter;
    }

}
