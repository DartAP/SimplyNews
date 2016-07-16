package dartapp.simplynews;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

public class NewsView extends AppCompatActivity
{
    TextView NDesc;
    TextView NDate;
    TextView NCategory;
    TextView NLink;
    ToggleButton NLike;
    ImageView Nimg;
    int id = 0;
    int localId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_view);

        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);
        localId = intent.getIntExtra("localId",0);
        NDesc = (TextView) findViewById(R.id.viewDesc);
        NDate = (TextView) findViewById(R.id.viewDate);
        NCategory = (TextView) findViewById(R.id.viewCateg);
        NLink = (TextView) findViewById(R.id.viewLink);
        NLike = (ToggleButton) findViewById(R.id.viewLike);
        Nimg = (ImageView) findViewById(R.id.viewImg);

        NDesc.setText(intent.getStringExtra("desc"));
        NDate.setText(intent.getStringExtra("date"));
        NLink.setText(intent.getStringExtra("link"));
        NCategory.setText(intent.getStringExtra("category"));
        NLike.setChecked(intent.getBooleanExtra("like",false));
        Picasso.with(NewsView.this).load(intent.getStringExtra("img"))
                .resize(500, 500).centerCrop().into(Nimg);
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent();
        intent.putExtra("likeR",NLike.isChecked());
        intent.putExtra("id",id);
        intent.putExtra("localId",localId);
        Log.e("HERE!",id+":"+NLike.isChecked());
        setResult(1,intent);
        finish();
        super.onBackPressed();
    }
}
