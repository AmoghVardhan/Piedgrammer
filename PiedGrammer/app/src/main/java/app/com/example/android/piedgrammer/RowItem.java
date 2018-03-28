package app.com.example.android.piedgrammer;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by user on 25-06-2017.
 */

public class RowItem {
    private Uri imageId;
    private String title;
    private String desc;

    public RowItem(Uri imageId, String title, String desc) {
        this.imageId = imageId;
        this.title = title;
        this.desc = desc;
    }
    public Uri getImageId() {
        return imageId;
    }
    public void setImageId(Uri imageId) {
        this.imageId = imageId;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    @Override
    public String toString() {
        return title + "\n" + desc;
    }
}
