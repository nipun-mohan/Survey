package nimble.survey;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nimble.survey.adapters.ContentAdapter;
import nimble.survey.interfaces.RecyclerViewClickListener;
import nimble.survey.models.Survey;

public class ContentFragment extends Fragment implements RecyclerViewClickListener {

    public ContentFragment() {
    }

    public static Fragment newInstance(Survey survey, int position) {
        Bundle args = new Bundle();
        args.putParcelable("survey", survey);
        args.putInt("position", position);
        ContentFragment fragment = new ContentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        initToolbar(view);
        initRecyclerView(view);
        return view;
    }

    private void initToolbar(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle(getSurvey().getTitle());
    }

    private void initRecyclerView(View view) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.content_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new ContentAdapter(getSurvey(), this));
    }

    public Survey getSurvey() {
        return getArguments().getParcelable("survey");
    }

    public int getPosition() {
        return getArguments().getInt("position");
    }

    @Override
    public void onClick(String id) {
        Intent i = new Intent(getContext(), SurveyActivity.class);
        i.putExtra("surveyId", id);
        startActivity(i);
    }
}
