package dartapp.simplynews;
////////////////////////////////////////////
//   Отличный сайт про Android и котов    //
//                                        //
//  http://developer.alexanderklimov.ru/  //
////////////////////////////////////////////

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Main extends AppCompatActivity
{
    Switch dateSort;
    TextView loadCount;
    CheckBox favoriteOnly;
    Button loadNews;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        dateSort = (Switch) findViewById(R.id.sortDateSwitch);
        loadCount = (TextView) findViewById(R.id.newsCountField);
        favoriteOnly = (CheckBox) findViewById(R.id.favoriteOnlyCheck);
        loadNews = (Button) findViewById(R.id.loadNews);

        loadNews.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Main.this, Feed.class);
                intent.putExtra("sort",dateSort.isChecked());
                intent.putExtra("count",Integer.parseInt(loadCount.getText().toString()));
                intent.putExtra("favorite",favoriteOnly.isChecked());

                startActivity(intent);
            }
        });

        //String test = "<table border=\"0\" cellpadding=\"2\" cellspacing=\"7\" style=\"vertical-align:top\"><tr><td width=\"80\" align=\"center\" valign=\"top\"><font style=\"font-size:85%;font-family:arial,sans-serif\"><a href=\"http://news.google.com/news/url?sa=t&amp;fd=R&amp;ct2=ru_ru&amp;usg=AFQjCNEHdUS73PCRDaHybVLB3NkMbRRQVw&amp;clid=c3a7d30bb8a4878e06b80cf16b898331&amp;cid=52779785077811&amp;ei=vOaEV7iOA4Px1Aa4zazgAw&amp;url=http://www.rbc.ru/photoreport/12/07/2016/5784dd969a7947b4b3849d47?from%3Dnewsfeed\"><img src=\"//t0.gstatic.com/images?q=tbn:ANd9GcQm7BQoDcDyRTdZ3xayZZuAtQUIDg_gmAPdv55IeExvHJVkw4V1FEExZrcT7nBETpk3he8Oj7E\" alt=\"\" border=\"1\" width=\"80\" height=\"80\"><br><font size=\"-2\">РБК</font></a></font></td><td valign=\"top\"><font style=\"font-size:85%;font-family:arial,sans-serif\"><br><div style=\"padding-top:0.8em\"><img alt=\"\" height=\"1\" width=\"1\"></div><div><a href=\"http://news.google.com/news/url?sa=t&amp;fd=R&amp;ct2=ru_ru&amp;usg=AFQjCNEHdUS73PCRDaHybVLB3NkMbRRQVw&amp;clid=c3a7d30bb8a4878e06b80cf16b898331&amp;cid=52779785077811&amp;ei=vOaEV7iOA4Px1Aa4zazgAw&amp;url=http://www.rbc.ru/photoreport/12/07/2016/5784dd969a7947b4b3849d47?from%3Dnewsfeed\"><b>Столкновение пассажирских поездов в Италии. Фоторепортаж</b></a><br><font size=\"-1\"><b><font color=\"#6f6f6f\">РБК</font></b></font><br><font size=\"-1\">Во вторник, 12 июля на юге Италии произошло лобовое столкновение двух пассажирских поездов. По данным Rainews 24 и Ansa, в результате погибли по меньшей мере десять человек, еще десятки получили травмы и были доставлены в больницы близлежащих городов. Инцидент ...</font><br><font size=\"-1\"><a href=\"http://news.google.com/news/url?sa=t&amp;fd=R&amp;ct2=ru_ru&amp;usg=AFQjCNEoZbeEUNq91ad8PlPFVVSG6Zqo_g&amp;clid=c3a7d30bb8a4878e06b80cf16b898331&amp;cid=52779785077811&amp;ei=vOaEV7iOA4Px1Aa4zazgAw&amp;url=http://www.rosbalt.ru/world/2016/07/12/1531264.html\">Число жертв в результате столкновения двух поездов в Италии увеличилось</a><font size=\"-1\" color=\"#6f6f6f\">Росбалт.RU</font></font><br><font size=\"-1\"><a href=\"http://news.google.com/news/url?sa=t&amp;fd=R&amp;ct2=ru_ru&amp;usg=AFQjCNFY8ILJTZzjJ6PjKxHBOhTw9Hu35w&amp;clid=c3a7d30bb8a4878e06b80cf16b898331&amp;cid=52779785077811&amp;ei=vOaEV7iOA4Px1Aa4zazgAw&amp;url=https://rg.ru/2016/07/12/v-italii-stolknulis-dva-passazhirskih-poezda.html\">В Италии столкнулись два пассажирских поезда</a><font size=\"-1\" color=\"#6f6f6f\">Российская Газета</font></font><br><font size=\"-1\"><a href=\"http://news.google.com/news/url?sa=t&amp;fd=R&amp;ct2=ru_ru&amp;usg=AFQjCNHSDtirsQ0aEOfKm06LNbu_wYz1Jg&amp;clid=c3a7d30bb8a4878e06b80cf16b898331&amp;cid=52779785077811&amp;ei=vOaEV7iOA4Px1Aa4zazgAw&amp;url=http://tass.ru/proisshestviya/3448022\">В Италии столкнулись два поезда, погибли 20 человек</a><font size=\"-1\" color=\"#6f6f6f\">ТАСС</font></font><br><font size=\"-1\"><a href=\"http://news.google.com/news/url?sa=t&amp;fd=R&amp;ct2=ru_ru&amp;usg=AFQjCNH6lLbHEtuFcMWuR5r3jxkF6U6BZQ&amp;clid=c3a7d30bb8a4878e06b80cf16b898331&amp;cid=52779785077811&amp;ei=vOaEV7iOA4Px1Aa4zazgAw&amp;url=http://www.mk.ru/incident/2016/07/12/tela-zastryali-v-iskorezhennom-zheleze-v-italii-stolknulis-passazhirskie-poezda.html\">Московский комсомолец</a> -<a href=\"http://news.google.com/news/url?sa=t&amp;fd=R&amp;ct2=ru_ru&amp;usg=AFQjCNERdigUymW7khk7ZBHETHofM5ccqQ&amp;clid=c3a7d30bb8a4878e06b80cf16b898331&amp;cid=52779785077811&amp;ei=vOaEV7iOA4Px1Aa4zazgAw&amp;url=http://kommersant.ru/doc/3036630\">Коммерсантъ</a> -<a href=\"http://news.google.com/news/url?sa=t&amp;fd=R&amp;ct2=ru_ru&amp;usg=AFQjCNHLTocrRrABg1QFfh98FGRrgEFRWw&amp;clid=c3a7d30bb8a4878e06b80cf16b898331&amp;cid=5277978";
        //Log.e("Test", ""+ContentFix(test));

    }




    public InputStream getImgStream(String ImgURL, int dbElSize) throws FileNotFoundException
    {
        InputStream input = null;
        try
        {
            URL url = new URL(ImgURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);

        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return input;
    }
}
