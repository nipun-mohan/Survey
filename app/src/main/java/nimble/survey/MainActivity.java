package nimble.survey;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.List;

import nimble.survey.interfaces.RecyclerViewClickListener;
import nimble.survey.interfaces.SurveyFetchInterface;
import nimble.survey.models.DataRepo;
import nimble.survey.models.Survey;
import nimble.survey.presenter.SurveyPresenter;

public class MainActivity extends AppCompatActivity implements SurveyFetchInterface.View {
    ProgressDialog dialog;
    private SurveyFetchInterface.Presenter mPresenter;
    private SurveyPresenter surveyPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("");
        dialog = new ProgressDialog(this);
        surveyPresenter = new SurveyPresenter(this); //pass the view reference to the presenter
        setPresenter(surveyPresenter);
        mPresenter.start();
    }


    public void initViewPager() {
        VerticalViewPager viewPager = (VerticalViewPager) findViewById(R.id.vertical_viewpager);
        //viewPager.setPageTransformer(false, new ZoomOutTransformer());
        //viewPager.setPageTransformer(true, new StackTransformer());
        String title = "ContentFragment";

        DataRepo imgRepo = DataRepo.newInstance();
        List<Survey> surveyList = imgRepo.getSurveys();

        ContentFragmentAdapter.Holder holder = new ContentFragmentAdapter.Holder(getSupportFragmentManager());

        for(int i=0;i<surveyList.size();i++){
            holder.add(ContentFragment.newInstance(surveyList.get(i), i+1));
        }
        viewPager.setAdapter(holder.set());
        //If you setting other scroll mode, the scrolled fade is shown from either side of display.
        viewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
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
