package dartapp.simplynews;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by DartAP on 12.07.2016.
 */
public class ToolBox
{
    public static String DateConverter(String date)
    {
        //Tue, 12 jul 2016 03:06:11 -0700
        //YYYY-MM-DD HH:MM:SS.SSS
        char[] charDate = date.toCharArray();
        String month = "01";
        switch (String.copyValueOf(charDate,8,3))
        {
            case "Jan": month = "01";break;
            case "Feb": month = "02";break;
            case "Mar": month = "03";break;
            case "Apr": month = "04";break;
            case "May": month = "05";break;
            case "Jun": month = "06";break;
            case "Jul": month = "07";break;
            case "Aug": month = "08";break;
            case "Sep": month = "09";break;
            case "Oct": month = "10";break;
            case "Nov": month = "11";break;
            case "Dec": month = "12";break;
                default: break;
        }
        String trueDate = String.copyValueOf(charDate,12,4) + "-" + month + "-" +
                String.copyValueOf(charDate,5,2) + " " + String.copyValueOf(charDate,17,8);

        return trueDate;
    }

    public static String SourceFromContent(String content)
    {
        char[] chars = content.toCharArray();
        int start = Integer.parseInt(String.valueOf(content.indexOf("url=")+4));
        int end = Integer.parseInt(String.valueOf(content.indexOf("><",start)));
        String source = String.copyValueOf(chars,start,end - start - 1);

        return source;
    }

    public static String ImgFormContent(String content)
    {
        char[] chars = content.toCharArray();
        int start = Integer.parseInt(String.valueOf(content.indexOf("src")+7));
        int end = Integer.parseInt(String.valueOf(content.indexOf("alt",start)));
        String img = String.copyValueOf(chars,start,end - start - 2);

        return "http://" + img;
    }

    public static String ContentFix(String content)
    {
        char[] chars = content.toCharArray();
        int start = Integer.parseInt(String.valueOf(content.indexOf("</font><br><font size=\"-1\">")+27));
        int end = Integer.parseInt(String.valueOf(content.indexOf("</font>",start)));
        String mainText = String.copyValueOf(chars,start,end - start);

        return mainText;
    }

    public static String[] HrefResearch(String content)
    {
        char[] chars = content.toCharArray();
        String test[] = new String[10];
        int lastPos = 0;
        int end = 0;
        for (int i = 0; end < content.length(); i++)
        {
            try
            {
                int start = Integer.parseInt(String.valueOf(content.indexOf("\">", lastPos)));
                end = Integer.parseInt(String.valueOf(content.indexOf("</a>", start)));
                if (end - start < chars.length)
                    test[i] = String.copyValueOf(chars, start, end - start);
                //Log.i("TEST!!!!", test[i]);
                lastPos = end;
            }
            catch (StringIndexOutOfBoundsException e)
            {
                e.printStackTrace();
                break;
            }
        }
        return test;
    }

    public static boolean isOnline(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            return true;
        }
        return false;
    }
}
