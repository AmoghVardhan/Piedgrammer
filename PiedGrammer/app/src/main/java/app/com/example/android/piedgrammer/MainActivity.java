package app.com.example.android.piedgrammer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import static android.R.attr.cycles;
import static android.R.attr.id;
import static android.R.color.white;
import static android.content.Intent.ACTION_PICK;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.os.Build.VERSION_CODES.N;


public class MainActivity extends AppCompatActivity {
    private final int SELECT_PHOTO = 1;
    private final int PIED_PATH = 2;
    int nameCount=0 ;
    DatabaseHelper myDb;
    public Uri imageUri;
    File directory;
    String name,uri,iso,aperture,exposure,focal,flash,white_balance,color_effect;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        nameCount = sharedPreferences.getInt("nameCount",0);
        myDb = new DatabaseHelper(this);
        directory = new File(getExternalFilesDir(null), "Available piedPicks");
        if (!directory.exists())
            directory.mkdir();
        Bundle bundle;
        Uri getUri = null;
        bundle = getIntent().getExtras();
        if (bundle != null){
            if(bundle.getString("uri")!=null){
                getUri = Uri.parse(bundle.getString("uri"));
                Toast.makeText(this,"done",Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(this,"some",Toast.LENGTH_LONG).show();
            }
        }

    }



    public void openCameraOnClick(View view){
        Intent intent1 = new Intent("net.sourceforge.opencamera.MainActivity");
        if(iso!=null){
            intent1.putExtra("id",1);
            intent1.putExtra("iso",iso);
            intent1.putExtra("aperture",aperture);
            intent1.putExtra("exposure",exposure);
            intent1.putExtra("focal",focal);
            intent1.putExtra("flash",flash);
            intent1.putExtra("white_balance",white_balance);
            intent1.putExtra("color_effect",color_effect);
            startActivity(intent1);
        }
        else{
            startActivity(intent1);
        }
    }
    public void piedPathPickLaunch(View view){

        Intent intent2 = new Intent(this,PiedPathPick.class);

        startActivityForResult(intent2,PIED_PATH);
    }
    public void galleryPick(View view){
        Intent intent3 = new Intent(ACTION_PICK);
        intent3.setType("image/*");
        startActivityForResult(intent3,SELECT_PHOTO);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case SELECT_PHOTO:
                if(data!=null) {
                    imageUri = data.getData();
                    nameCount++;

                    File file;

                    file = new File(directory, String.valueOf(nameCount) + ".jpg");
                    try {
                        copy(imageUri, file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ExifInterface exif = null;
                    try {
                        exif = new ExifInterface("/storage/emulated/0/Android/data/app.com.example.android.piedgrammer/files/Available piedPicks/" + String.valueOf(nameCount) + ".jpg");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    name = String.valueOf(nameCount);
                    uri = String.valueOf(imageUri);
                    iso = exif.getAttribute(ExifInterface.TAG_ISO_SPEED_RATINGS);
                    aperture = exif.getAttribute(ExifInterface.TAG_APERTURE_VALUE);
                    Toast.makeText(this, aperture, Toast.LENGTH_LONG).show();
                    exposure = exif.getAttribute(ExifInterface.TAG_EXPOSURE_TIME);
                    focal = exif.getAttribute(ExifInterface.TAG_FOCAL_LENGTH);
                    flash = exif.getAttribute(ExifInterface.TAG_FLASH);
                    white_balance = String.valueOf(exif.getAttribute(ExifInterface.TAG_LIGHT_SOURCE));
                    color_effect = exif.getAttribute(ExifInterface.TAG_COLOR_SPACE);

                    boolean isInserted = myDb.insertData(name,
                            uri,
                            iso,
                            aperture,
                            exposure,
                            focal,
                            flash,
                            white_balance,
                            color_effect);
                    if (isInserted == true)
                        Toast.makeText(MainActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(MainActivity.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                }

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("nameCount",nameCount);
                editor.commit();
                    break;

            case PIED_PATH:
                if(data!=null) {
                    iso = data.getStringExtra("iso");
                    aperture = data.getStringExtra("aperture");
                    exposure = data.getStringExtra("exposure");
                    focal = data.getStringExtra("focal");
                    flash = data.getStringExtra("flash");
                    white_balance = data.getStringExtra("white_balance");
                    color_effect = data.getStringExtra("color_effect");
                    Toast.makeText(MainActivity.this, iso + aperture + exposure + focal + flash +white_balance + color_effect, Toast.LENGTH_LONG).show();
                    break;
                }
                else{
                    Toast.makeText(this,"PiedPath not selected. Please Try again.",Toast.LENGTH_SHORT).show();
                    break;
                }


        }
    }
    public void copy(Uri uri, File dst) throws IOException {
        InputStream in = getContentResolver().openInputStream(uri);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

}

