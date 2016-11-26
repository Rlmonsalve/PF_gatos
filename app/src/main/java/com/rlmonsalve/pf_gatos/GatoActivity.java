package com.rlmonsalve.pf_gatos;

import android.animation.Animator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.rlmonsalve.pf_gatos.data.Gato;
import com.rlmonsalve.pf_gatos.data.Objeto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GatoActivity extends AppCompatActivity implements
        SurfaceHolder.Callback{

    TinyDB tinydb;
    private ArrayList<Objeto> objList;
    private ArrayList<Gato> catList;
    private ArrayList<Button> buttonList;
    private Gato Cat;
    private ImageView catModel, imgHeart,imgSad, imgHeart2, imgHeart3;
    private Button im1,im2,im3,im4,im5,im6;
    private RelativeLayout rl;
    private ProgressBar progressBar;
    private Camera mCamera;
    private int likeValue, catInt;
    private SurfaceHolder mSurfaceHolder;
    private boolean isCameraviewOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gato);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        tinydb = new TinyDB(this);
        objList = tinydb.getListObjectItem("Objetos_List",Objeto.class);
        catList = tinydb.getListObject("Gatos_List",Gato.class);

        likeValue =0;

        setupLayout();
        rl = (RelativeLayout)findViewById(R.id.activity_gato);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);

        Random r = new Random();
        catInt  = r.nextInt(3);
        Cat = catList.get(catInt);

        buttonList = new ArrayList<Button>();
        catModel=(ImageView)findViewById(R.id.icon);
        im1=(Button) findViewById(R.id.imageAct1);
        im2=(Button)findViewById(R.id.imageAct2);
        im3=(Button)findViewById(R.id.imageAct3);
        im4=(Button)findViewById(R.id.imageAct4);
        im5=(Button)findViewById(R.id.imageAct5);
        im6=(Button)findViewById(R.id.imageAct6);
        imgHeart = (ImageView)findViewById(R.id.heartImage);
        imgHeart2 = (ImageView)findViewById(R.id.heartImage2);
        imgHeart3 = (ImageView)findViewById(R.id.heartImage3);
        imgSad = (ImageView)findViewById(R.id.sadImage);

        catModel.setImageResource(Cat.getModelId());

        buttonList.add(im1);
        buttonList.add(im2);
        buttonList.add(im3);
        buttonList.add(im4);
        buttonList.add(im5);
        buttonList.add(im6);

        setupButtons();

    }

    private void verifyFirstTime(){
        if (tinydb.getBoolean("firstrun_gato")) {
            // Do first run stuff here then set 'firstrun' as false
            showTutorialPopup();
            // using the following line to edit/commit prefs
            tinydb.putBoolean("firstrun_gato", false);
        }
    }

    private void setupLayout() {
        getWindow().setFormat(PixelFormat.UNKNOWN);
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.cameraview);
        mSurfaceHolder = surfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        if (isCameraviewOn) {
            mCamera.stopPreview();
            isCameraviewOn = false;
        }

        if (mCamera != null) {
            try {
                mCamera.setPreviewDisplay(mSurfaceHolder);
                mCamera.startPreview();
                isCameraviewOn = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mCamera = Camera.open();
        mCamera.setDisplayOrientation(90);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
        isCameraviewOn = false;
    }

    public void setupButtons(){
        for (int pos = 0;pos<6;pos++){
            Objeto obj = objList.get(pos);
            buttonList.get(pos).setBackground(getResources().getDrawable(obj.getIconId()));
            if (obj.getStock()>=0){
                buttonList.get(pos).setText("x"+obj.getStock());
            }else{
                buttonList.get(pos).setText("");
            }
            buttonList.get(pos).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkLikes(view);
                }
            });
        }
    }

    private void checkLikes(View view){
        Button b = (Button) view;
        int pos = Integer.parseInt((String) view.getTag());
        Objeto obj = objList.get(pos);
        int st = obj.getStock();
        if (st!=0){
            if (st>0){
                st--;
                obj.setStock(st);
                b.setText("x"+obj.getStock());
            }
            if (obj.getName().equals(Cat.getLikes().getName())){
                likesEvent(view);
            }else if (obj.getName().equals(Cat.getDislikes().getName())){
                dislikesEvent(view);
            }else{
                neutralEvent(view);
            }
        }
    }

    private void neutralEvent(View view) {
        likeValue = likeValue+25;
        Toast.makeText(GatoActivity.this, "Le Gusta!", Toast.LENGTH_SHORT).show();
        imgHeart.setVisibility(View.VISIBLE);
        imgHeart.animate().translationY(-120).setDuration(1000).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                imgHeart.setVisibility(View.INVISIBLE);
                imgHeart.animate().translationY(0).setDuration(100).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }
                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                }).start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).start();

        if (likeValue>=200){
            likeValue=200;
            progressBar.setProgress(likeValue);
            showCompleteDialog();
        }else{
            progressBar.setProgress(likeValue);
        }
    }

    public void dislikesEvent(View view) {
        likeValue = likeValue-50;
        Toast.makeText(GatoActivity.this, "No le Gusta!", Toast.LENGTH_LONG).show();
        progressBar.setProgress(likeValue);
        imgSad.setVisibility(View.VISIBLE);
        imgSad.animate().translationY(-120).setDuration(1000).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                imgSad.setVisibility(View.INVISIBLE);
                imgSad.animate().translationY(0).setDuration(100).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }
                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                }).start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).start();

        if (likeValue<0){
            likeValue=0;
        }
        progressBar.setProgress(likeValue);
    }

    public void likesEvent(View view) {
        likeValue = likeValue+50;
        Toast.makeText(GatoActivity.this, "Le Gusta Mucho!", Toast.LENGTH_SHORT).show();
        imgHeart.setVisibility(View.VISIBLE);
        imgHeart.animate().translationY(-120).setDuration(1000).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                imgHeart.setVisibility(View.INVISIBLE);
                imgHeart.animate().translationY(0).setDuration(100).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }
                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                }).start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).start();

        imgHeart2.setVisibility(View.VISIBLE);
        imgHeart2.animate().translationY(-120).setDuration(1000).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                imgHeart2.setVisibility(View.INVISIBLE);
                imgHeart2.animate().translationY(0).setDuration(100).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }
                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                }).start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).start();

        imgHeart3.setVisibility(View.VISIBLE);
        imgHeart3.animate().translationY(-120).setDuration(1000).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                imgHeart3.setVisibility(View.INVISIBLE);
                imgHeart3.animate().translationY(0).setDuration(100).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }
                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                }).start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).start();

        if (likeValue>=200){
            likeValue=200;
            progressBar.setProgress(likeValue);
            showCompleteDialog();
        }else{
            progressBar.setProgress(likeValue);
        }
    }

    private void showTutorialPopup(){

        final PopupWindowTutorial popupWindow = new PopupWindowTutorial(this,2);
        popupWindow.show(findViewById(R.id.activity_gato), 0, -100);
    }

    private void showCompleteDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(GatoActivity.this).create();
        alertDialog.setTitle("Felicitaciones");
        alertDialog.setMessage("Lo has logrado!");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startMapsActivity();
                    }
                });
        alertDialog.show();
    }

    private void startMapsActivity(){
        tinydb.putListObjectItem("Objetos_List",objList);
        Intent intent = new Intent(GatoActivity.this,
                MapsActivity.class);
        startActivity(intent);
        finish();
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
        tinydb.putListObjectItem("Objetos_List",objList);
        Intent intent = new Intent(GatoActivity.this,
                MapsActivity.class);
        startActivity(intent);
        finish();
    }
}
