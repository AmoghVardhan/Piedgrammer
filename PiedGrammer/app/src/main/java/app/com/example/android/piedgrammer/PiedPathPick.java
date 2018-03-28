package app.com.example.android.piedgrammer;

import android.Manifest;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static android.R.string.no;
import static java.security.AccessController.getContext;

public class PiedPathPick extends MainActivity implements AdapterView.OnItemClickListener {
    final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    DatabaseHelper myDb;
    String iso = null,aperture= null,exposure=null,focal=null,flash = null,white_balance = null,color_effect=null;
    Cursor res;
    int noOfItems  = new File("/storage/emulated/0/Android/data/app.com.example.android.piedgrammer/files/Available piedPicks/").listFiles().length;

    public static ArrayList<String> titles = new ArrayList<String>();

    public static final ArrayList<String> descriptions = new ArrayList<String>();


    public static final ArrayList<Uri> images = new ArrayList<Uri>();

    ListView listView;
    List<RowItem> rowItems;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pied_path_pick);
        Toast.makeText(this,"i have entered",Toast.LENGTH_SHORT).show();




        myDb = new DatabaseHelper(getApplicationContext());
        res = myDb.getAllData();
        if(res.getCount() == 0){
            showMessage("Error","Nothing Found");
            return;
        }
        int z = 0;
        while(res.moveToNext()){
            //titles[z] = res.getString(0);
            titles.add(res.getString(0));
            descriptions.add(String.valueOf(z));
            images.add(Uri.parse(res.getString(1)));
            z++;
        }

        rowItems = new ArrayList<RowItem>();

        for (int i = 0; i < titles.size(); i++) {
            RowItem item = new RowItem(images.get(i), titles.get(i), descriptions.get(i));
            rowItems.add(item);
        }


        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        Toast.makeText(this,String.valueOf(permissionCheck),Toast.LENGTH_LONG).show();


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        else{
            listView = (ListView) findViewById(R.id.list);
            CustomListViewAdapter adapter = new CustomListViewAdapter(this,
                    R.layout.list_view_layout, rowItems);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);

        }


    }

    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    listView = (ListView) findViewById(R.id.list);
                    CustomListViewAdapter adapter = new CustomListViewAdapter(this,
                            R.layout.list_view_layout, rowItems);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(this);
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Toast.makeText(this, "Sorry permission not granted",Toast.LENGTH_LONG).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {



        res = myDb.getRowData(String.valueOf(position+1));


        if (res.moveToFirst())
        {
            do
            {
                 iso = res.getString(res.getColumnIndex("ISO"));
                 aperture = res.getString(res.getColumnIndex("APERTURE"));
                 exposure = res.getString(res.getColumnIndex("EXPOSURE_TIME"));
                 focal= res.getString(res.getColumnIndex("FOCAL_LENGTH"));
                 flash = res.getString(res.getColumnIndex("FLASH"));
                 white_balance = res.getString(res.getColumnIndex("WHITE_BALANCE"));
                 color_effect = res.getString(res.getColumnIndex("COLOR_EFFECT"));
                 Intent intent = new Intent();
                 intent.putExtra("iso",iso);
                 intent.putExtra("aperture",aperture);
                 intent.putExtra("exposure",exposure);
                 intent.putExtra("focal",focal);
                 intent.putExtra("flash",flash);
                 intent.putExtra("white_balance",white_balance);
                 intent.putExtra("color_effect",color_effect);
                 setResult(2,intent);
            }while (res.moveToNext());
        }







        Toast toast = Toast.makeText(getApplicationContext(),
                        "iso :" + iso +
                        "aperture : " + aperture +
                        "exposure : " + exposure +
                        "focal : " + focal +
                        "flash :" + flash +
                        "white balance :"+white_balance +
                        "color effect :"+color_effect,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
        this.finish();


    }
    @Override
    protected void onDestroy(){
        titles.clear();
        descriptions.clear();
        images.clear();
        rowItems.clear();
        super.onDestroy();
    }
}
