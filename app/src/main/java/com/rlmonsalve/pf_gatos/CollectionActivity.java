package com.rlmonsalve.pf_gatos;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rlmonsalve.pf_gatos.data.Objeto;

import java.util.ArrayList;

public class CollectionActivity extends AppCompatActivity {

    ImageView imgCon1, imgCon2,imgObj1,imgObj2,imgToy1,imgToy2;
    ArrayList<Objeto> objList;
    TinyDB tinydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        tinydb = new TinyDB(this);
        objList = tinydb.getListObjectItem("Objetos_List",Objeto.class);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Muli-Bold.ttf");
        TextView t1 = (TextView) findViewById(R.id.text_consumibles);
        TextView t2 = (TextView) findViewById(R.id.text_objetos);
        TextView t3 = (TextView) findViewById(R.id.text_juguetes);
        t1.setTypeface(typeface);
        t2.setTypeface(typeface);
        t3.setTypeface(typeface);

        imgCon1 = (ImageView)findViewById(R.id.imageConsum1);
        imgCon2 = (ImageView)findViewById(R.id.imageConsum2);
        imgObj1 = (ImageView)findViewById(R.id.imageObj1);
        imgObj2 = (ImageView)findViewById(R.id.imageObj2);
        imgToy1 = (ImageView)findViewById(R.id.imageToy1);
        imgToy2 = (ImageView)findViewById(R.id.imageToy2);

        imgCon1.setImageResource(objList.get(0).getIconId());
        imgCon2.setImageResource(objList.get(1).getIconId());
        imgObj1.setImageResource(objList.get(2).getIconId());
        imgObj2.setImageResource(objList.get(3).getIconId());
        imgToy1.setImageResource(objList.get(4).getIconId());
        imgToy2.setImageResource(objList.get(5).getIconId());

    }

    public void displayInfo(View view){
        int pos =  Integer.parseInt((String) view.getTag());
        Objeto obj = objList.get(pos);
        if (obj.getStock()>=0){
            Toast.makeText(CollectionActivity.this, obj.getName()+", tienes "+obj.getStock(), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(CollectionActivity.this, obj.getName(), Toast.LENGTH_SHORT).show();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Intent intent = new Intent(CollectionActivity.this,
                MapsActivity.class);
        startActivity(intent);
        finish();
    }
}
