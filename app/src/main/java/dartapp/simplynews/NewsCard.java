package dartapp.simplynews;

/**
 * Created by DartAP on 28.06.2016.
 */
public class NewsCard
{
    private String title;
    private String link;
    private String publishedDate;
    private String contentSmall;
    private String content;
    private String categories;
    private String imgUrl;
    private byte favorite;
    private int ID;


    public String getTitle()  { return title; }

    public String getLink()
    {
        return link;
    }

    public String getPublishedDate()
    {
        return publishedDate;
    }

    public String getContentSmall()
    {
        return contentSmall;
    }

    public String getContent()
    {
        return content;
    }

    public String getCategories()
    {
        return categories;
    }

    public String getImgUrl()
    {
        return imgUrl;
    }

    public byte getFavorite() { return favorite; }

    public void setFavorite(byte favorite) { this.favorite = favorite; }

    public int getID() { return ID; }

    public NewsCard(String title, String link, String publishedDate, String contentSmall, String content, String categories, String imgUrl, byte favorite, int id)
    {
        this.title = title;
        this.link = link;
        this.publishedDate = publishedDate;
        this.contentSmall = contentSmall;
        this.content = content;
        this.categories = categories;
        this.imgUrl = imgUrl;
        this.favorite = favorite;
        this.ID = id;
    }
}
