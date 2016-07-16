package dartapp.simplynews;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DartAP on 13.07.2016.
 */
public class DBHelper extends SQLiteOpenHelper
{
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "News.db";
    public static final String TABLE_NAME = "newsTable";

    public static final String KEY_ID = "_id";
    public static final String KEY_TITLE = "Title";
    public static final String KEY_LINK = "Link";
    public static final String KEY_DATE = "PubDate";
    public static final String KEY_CONTENTSMALL = "ContentSmall";
    public static final String KEY_CONTENT = "Content";
    public static final String KEY_CATEGORY = "Category";
    public static final String KEY_IMG = "Img";
    public static final String KEY_FAVORITE = "Favorite";

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String sqlCreate = "Create table newsTable(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Title TEXT,Link TEXT,PubDate NUMERIC,ContentSmall TEXT," +
                "Content TEXT,Category TEXT,Img TEXT,Favorite INTEGER);";
        db.execSQL(sqlCreate);
        Log.i("DB Sheets", sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP DATABASE IF EXIST " + DATABASE_NAME);
        Log.i("DB Sheets", "DB dropped");
        onCreate(db);
    }

    public List<NewsCard> getListFormDB(Context main,SQLiteDatabase db, boolean byDate, int count, boolean favorite)
    {
        List<NewsCard> temp = new ArrayList<NewsCard>();
        String sort = byDate ? " ORDER BY PubDate DESC" : "";
        String fav = favorite ? " WHERE Favorite=1" : "";
        Cursor cursor = db.query("newsTable"+fav+sort,null,null,null,null,null,null);
        if (cursor.moveToFirst())
        {
            for (int i = 0; i < count; i++)
            {
                temp.add(new NewsCard(cursor.getString(1),
                        cursor.getString(2), cursor.getString(3),
                        cursor.getString(4), cursor.getString(5),
                        cursor.getString(6), cursor.getString(7),
                        Byte.parseByte(cursor.getString(8)),
                        cursor.getInt(0)));
                if (!cursor.moveToNext())
                    break;
            }
        }
        else
        {
            Log.e("GetListFromDB", "News DB Is empty!");
            Toast.makeText(main, "Загрузите новости, подклчив интернет!", Toast.LENGTH_LONG).show();
        }
        Log.i("GetListFromDB", "List size: "+temp.size());
        return temp;
    }
}
