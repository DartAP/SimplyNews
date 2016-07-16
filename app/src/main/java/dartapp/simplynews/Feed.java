package dartapp.simplynews;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static dartapp.simplynews.ToolBox.ContentFix;
import static dartapp.simplynews.ToolBox.DateConverter;
import static dartapp.simplynews.ToolBox.ImgFormContent;
import static dartapp.simplynews.ToolBox.SourceFromContent;
import static dartapp.simplynews.ToolBox.isOnline;

public class Feed extends AppCompatActivity
{
    public DBHelper dbHelper;
    SQLiteDatabase newsDB;
    Cursor allnews;

    public List<NewsCard> news = new ArrayList<NewsCard>();
    ListView myNews;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed);

        dbHelper = new DBHelper(this);
        newsDB = dbHelper.getWritableDatabase();

        myNews = (ListView) (findViewById(R.id.newsItems));
        RequestQueue queue = Volley.newRequestQueue(Feed.this);


        //myNews.setAdapter(adapter);

        Intent intent = getIntent();
        String otherArgs = "&scoring=h&hl=ru";
        final int loadCount = intent.getIntExtra("count",10);
        final boolean fav = intent.getBooleanExtra("favorite",false);
        int newsCount = -1; // - 1 = 100, иначе количество новостей     работает только с scoring=h
        String query = "http%3A%2F%2Fnews.google.ru%2Fnews%3F";
        String outputType = "rss";

        JsonObjectRequest newsReq = new JsonObjectRequest(Request.Method.GET,
                "http://ajax.googleapis.com/ajax/services/feed/load" +
                        "?v=1.0"+ otherArgs +"&num="+ newsCount +"&q="+ query +"output="+outputType,
                null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            JSONObject responseData = response.getJSONObject("responseData");
                            JSONObject feed = responseData.getJSONObject("feed");
                            JSONArray entries = feed.getJSONArray("entries");
                            for (int i = 0; i < entries.length(); i++)
                            {
                                final ContentValues contentValues = new ContentValues();
                                JSONObject c = entries.getJSONObject(i);

                                String title = c.getString("title");
                                String publishedDate = DateConverter(c.getString("publishedDate"));
                                String content = c.getString("content");
                                String contentSnippet = ContentFix(content);
                                String link = SourceFromContent(content);
                                String categories = c.getString("categories");
                                String Img = ImgFormContent(content);

                                contentValues.put(DBHelper.KEY_TITLE, title);
                                contentValues.put(DBHelper.KEY_DATE, publishedDate);
                                contentValues.put(DBHelper.KEY_CONTENTSMALL, contentSnippet);
                                contentValues.put(DBHelper.KEY_CONTENT, content);
                                contentValues.put(DBHelper.KEY_LINK, link);
                                contentValues.put(DBHelper.KEY_CATEGORY, categories);
                                contentValues.put(DBHelper.KEY_IMG, Img);
                                contentValues.put(DBHelper.KEY_FAVORITE, 0);

                                Cursor ifExist = newsDB.query("newsTable WHERE Title='" +
                                        title+"'",null,null,null,null,null,null);
                                if (!ifExist.moveToFirst())
                                    newsDB.insert(DBHelper.TABLE_NAME, null, contentValues);
                            }
                            news = dbHelper.getListFormDB(Feed.this,newsDB,getIntent()
                                    .getBooleanExtra("sort",true),loadCount,fav);

                            ArrayAdapter<NewsCard> adapter = new customAdapter();
                            myNews.setAdapter(adapter);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        error.printStackTrace();
                    }
                });

        if (isOnline(this))
        {
            Log.i("FeedLoadType", "Online");
            queue.add(newsReq);
        }
        else
        {
            Log.i("FeedLoadType", "Offline");
            news = dbHelper.getListFormDB(Feed.this,
                    newsDB,getIntent().getBooleanExtra("sort",true),loadCount,fav);
            ArrayAdapter<NewsCard> adapter = new customAdapter();
            myNews.setAdapter(adapter);
        }



        myNews.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                NewsCard cn = news.get(position);

                Intent intent = new Intent(Feed.this, NewsView.class);
                intent.putExtra("desc", cn.getContentSmall());
                intent.putExtra("date", cn.getPublishedDate());
                intent.putExtra("img", cn.getImgUrl());
                intent.putExtra("link", cn.getLink());
                intent.putExtra("category", cn.getCategories());
                intent.putExtra("like", cn.getFavorite()==0 ? false:true);
                intent.putExtra("id", cn.getID());
                intent.putExtra("localId", position);

                Log.e("HERE!",cn.getID()+":"+cn.getFavorite());

                startActivityForResult(intent,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null)
            return;

        int id = data.getIntExtra("id",0);
        int f = 0;
        if (data.getBooleanExtra("likeR",false))
             f = 1;
        newsDB.execSQL("UPDATE newsTable SET favorite="+f+" WHERE _id="+id+";");
        news.get(data.getIntExtra("localId",0)).setFavorite(Byte.parseByte(""+f));
    }

    private class customAdapter extends ArrayAdapter<NewsCard>
    {
        public customAdapter()
        {
            super(Feed.this, R.layout.item, news);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (convertView == null)
            {
                convertView = getLayoutInflater().inflate(R.layout.item, parent, false);
            }
            NewsCard currentItem = news.get(position);
            TextView title = (TextView) convertView.findViewById(R.id.heading);
            TextView desc = (TextView) convertView.findViewById(R.id.desc);
            TextView date = (TextView) convertView.findViewById(R.id.viewDate);
            ImageView image = (ImageView) convertView.findViewById(R.id.leftIco);

            title.setText(currentItem.getTitle());
            desc.setText(currentItem.getContentSmall());
            date.setText(currentItem.getPublishedDate());
            Picasso.with(Feed.this).load(currentItem.getImgUrl()).resize(200, 200).centerCrop().into(image);

            return convertView;
        }
    }
}
