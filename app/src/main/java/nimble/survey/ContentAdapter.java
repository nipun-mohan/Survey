package nimble.survey;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import nimble.survey.interfaces.RecyclerViewClickListener;
import nimble.survey.models.Survey;

public class ContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Survey survey;

    RecyclerViewClickListener mlistener;

    public ContentAdapter(Survey survey, RecyclerViewClickListener listener) {
        this.survey = survey;
        this.mlistener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContentView(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_content, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ContentView content = (ContentView) holder;
        Uri uri = Uri.parse(survey.getCoverImage() + "l");
        Utils.setupImage(content.image, uri,false);
        content.txtTitle.setText(survey.getTitle());
        content.txtDescription.setText(survey.getDescription());


        content.btnTakeSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.onClick(survey.getSurveyId());
            }
        });
        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.onClick(survey.getSurveyId());
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ContentView extends RecyclerView.ViewHolder {

        private TextView txtTitle;
        private TextView txtDescription;
        private Button btnTakeSurvey;
        private SimpleDraweeView image;

        public ContentView(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.content_title);
            txtDescription = itemView.findViewById(R.id.content_description);
            image = itemView.findViewById(R.id.img);
            btnTakeSurvey = itemView.findViewById(R.id.btnTakeSurvey);
        }
    }
}
