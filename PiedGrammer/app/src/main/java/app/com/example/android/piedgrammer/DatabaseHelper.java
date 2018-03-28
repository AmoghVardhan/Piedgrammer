package app.com.example.android.piedgrammer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import static android.R.attr.category;
import static android.R.color.white;

/**
 * Created by user on 25-06-2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    Context context1;
    public static final String DATABASE_NAME = "MasterDatabase";
    public static final String TABLE_NAME = "PiedPathList";
    public static final String COL_1 = "NAME";
    public static final String COL_2 = "URI";
    public static final String COL_3 = "ISO";
    public static final String COL_4 = "APERTURE";
    public static final String COL_5 = "EXPOSURE_TIME";
    public static final String COL_6 = "FOCAL_LENGTH";
    public static final String COL_7 = "FLASH";
    public static final String COL_8 = "WHITE_BALANCE";
    public static final String COL_9 = "COLOR_EFFECT";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context1 = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (NAME TEXT PRIMARY KEY ,URI TEXT,ISO TEXT,APERTURE TEXT," +
                "EXPOSURE_TIME TEXT,FOCAL_LENGTH TEXT,FLASH TEXT,WHITE_BALANCE TEXT,COLOR_EFFECT TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public boolean insertData(String name,String uri,String iso,String aperture,String exposure,
                              String focal,String flash,String white_balance,String color_effect) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,name);
        contentValues.put(COL_2,uri);
        contentValues.put(COL_3,iso);
        contentValues.put(COL_4,aperture);
        contentValues.put(COL_5,exposure);
        contentValues.put(COL_6,focal);
        contentValues.put(COL_7,flash);
        contentValues.put(COL_8,white_balance);
        contentValues.put(COL_9,color_effect);
        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }
    public Cursor getRowData(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME+ " WHERE NAME='" + name +"'";
        Cursor  cursor = db.rawQuery(query,null);

        return cursor;
    }

}


