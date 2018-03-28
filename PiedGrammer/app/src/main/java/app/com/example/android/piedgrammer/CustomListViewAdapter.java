package app.com.example.android.piedgrammer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.renderscript.ScriptGroup;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import static android.R.id.input;
import static app.com.example.android.piedgrammer.R.menu.main;

/**
 * Created by user on 25-06-2017.
 */

public class CustomListViewAdapter extends ArrayAdapter<RowItem> {

    Context context;

    public CustomListViewAdapter(Context context, int resourceId,
                                 List<RowItem> items) {
        super(context, resourceId, items);
        this.context = context;
    }




    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
        TextView txtDesc;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        RowItem rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_view_layout, null);
            holder = new ViewHolder();
            holder.txtDesc = (TextView) convertView.findViewById(R.id.desc);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.txtDesc.setText(rowItem.getDesc());
        holder.txtTitle.setText(rowItem.getTitle());
        //holder.imageView.setImageURI(null);
        //holder.imageView.setImageURI(rowItem.getImageId());

        Uri imageUri;
        Bitmap selectedImage;
        imageUri = rowItem.getImageId();

        try {



            final InputStream imageStream = context.getContentResolver().openInputStream(imageUri);
            selectedImage = BitmapFactory.decodeStream(imageStream);
            selectedImage.createScaledBitmap(selectedImage,50,50,false);
            //imageView.setImageBitmap(selectedImage);
            //imageView.setVisibility(View.VISIBLE);
            if (selectedImage != null) {
                //bitmap is not null and I can see an image using Android Studio

                selectedImage =Bitmap.createScaledBitmap(selectedImage, 120, 120, false);
                holder.imageView.setImageBitmap(selectedImage);
                holder.imageView.setVisibility(View.VISIBLE);
            } else {
                holder.imageView.setVisibility(View.GONE);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        return convertView;
    }
}
