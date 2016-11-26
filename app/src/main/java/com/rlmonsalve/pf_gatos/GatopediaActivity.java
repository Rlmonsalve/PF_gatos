package com.rlmonsalve.pf_gatos;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.TextView;

import com.rlmonsalve.pf_gatos.data.Gato;

import java.util.ArrayList;

public class GatopediaActivity extends AppCompatActivity{

    private ArrayList<Gato> gatos;
    private RecyclerView rv;
    private Typeface typeface;
    private TextView title, text;
    private Button Close;
    TinyDB tinydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gatopedia);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        tinydb = new TinyDB(this);
        gatos=tinydb.getListObject("Gatos_List",Gato.class);

        rv = (RecyclerView)findViewById(R.id.rv);

        typeface = Typeface.createFromAsset(getAssets(), "fonts/Muli.ttf");

        LinearLayoutManager lim = new LinearLayoutManager(this);
        rv.setLayoutManager(lim);
        rv.setHasFixedSize(true);

        verifyFirstTime();

        initializeAdapter();

    }

    @Override
    protected void onResume() {
        super.onResume();
        verifyFirstTime();
        initializeAdapter();
    }

    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(gatos,typeface,this);
        rv.setAdapter(adapter);
    }

    private void verifyFirstTime(){
        if (tinydb.getBoolean("firstrun_gatopedia")) {
            // Do first run stuff here then set 'firstrun' as false
            // using the following line to edit/commit prefs
            //showTutorialPopup();
            //initializeData();
            tinydb.putBoolean("firstrun_gatopedia", false);
        }else{
            gatos=tinydb.getListObject("Gatos_List",Gato.class);
        }
    }

    private void showTutorialPopup(){
        final PopupWindowTutorial popupWindow = new PopupWindowTutorial(this,3);
        popupWindow.show(findViewById(R.id.activity_gatopedia), 0, -100);
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
        Intent intent = new Intent(GatopediaActivity.this,
                MapsActivity.class);
        startActivity(intent);
        finish();
    }
}
