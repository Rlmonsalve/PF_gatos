package com.rlmonsalve.pf_gatos;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rlmonsalve.pf_gatos.data.Gato;

import java.util.ArrayList;

public class GatoInfoActivity extends AppCompatActivity {

    private ImageView gato_img, fav_img;
    private TextView gato_nam, gato_info, txtInfo;
    private Button apadrinar_btn;
    private int id;
    TinyDB tinydb;
    boolean isFav;
    private ArrayList<Gato> gatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gato_info);
        Bundle bundle = getIntent().getExtras();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        tinydb = new TinyDB(this);
        gatos = tinydb.getListObject("Gatos_List", Gato.class);
        id = bundle.getInt("gato_id");

        gato_img = (ImageView)findViewById(R.id.imageView_gato);
        gato_nam = (TextView)findViewById(R.id.textView_gato);
        txtInfo = (TextView)findViewById(R.id.textInfo);
        gato_info = (TextView)findViewById(R.id.textTextPunto);
        fav_img = (ImageView)findViewById(R.id.imageItemFav);
        apadrinar_btn = (Button)findViewById(R.id.button_apadrinar);

        gato_info.setMovementMethod(new ScrollingMovementMethod());

        isFav = gatos.get(id).isFavorite();
        if (isFav){
            apadrinar_btn.setTextColor(this.getResources().getColor(R.color.info_title_text));
        }else{
            apadrinar_btn.setTextColor(this.getResources().getColor(R.color.secondary_text));
        }
        switch (id)
        {
            case 0:
                gato_img.setImageResource(R.drawable.electron_pic);
                gato_nam.setText(gatos.get(id).getName());
                txtInfo.setText(getResources().getString(R.string.gato_info_1));
                gato_info.setText(getResources().getString(R.string.electron_info));
                fav_img.setImageResource(gatos.get(id).getLikes().getIconId());
                break;
            case 1:
                gato_img.setImageResource(R.drawable.acuarela_pic);
                gato_nam.setText(gatos.get(id).getName());
                txtInfo.setText(getResources().getString(R.string.gato_info_1));
                gato_info.setText(getResources().getString(R.string.acuarela_info));
                fav_img.setImageResource(gatos.get(id).getLikes().getIconId());
                break;
            case 2:
                gato_img.setImageResource(R.drawable.malefico_pic);
                gato_nam.setText(gatos.get(id).getName());
                txtInfo.setText(getResources().getString(R.string.gato_info_1));
                gato_info.setText(getResources().getString(R.string.malefico_info));
                fav_img.setImageResource(gatos.get(id).getLikes().getIconId());
                break;
        }

        apadrinar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apadrinarEvent(view);
            }
        });
    }

    public void apadrinarEvent(View view){
        if (isFav){
            isFav=false;
            apadrinar_btn.setTextColor(this.getResources().getColor(R.color.secondary_text));
            gatos.get(id).setFavorite(isFav);
        }else{
            isFav=true;
            apadrinar_btn.setTextColor(this.getResources().getColor(R.color.info_title_text));
            gatos.get(id).setFavorite(isFav);
        }

    }

    @Override
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
        tinydb.putListObject("Gatos_List",gatos);
        finish();
    }
}
