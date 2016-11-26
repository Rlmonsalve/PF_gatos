package com.rlmonsalve.pf_gatos;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.Image;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.rlmonsalve.pf_gatos.view.StickerView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.os.Environment.DIRECTORY_PICTURES;


public class StudioActivity extends AppCompatActivity {
    RelativeLayout content_studio;
    ImageView studioImg;
    Bitmap editedPic;
    View mAddSticker;
    View visorSticker;
    View pirateSticker;
    View gafasSticker;
    View starSticker;
    View libroSticker;
    StickerView mCurrentView;
    Canvas canvas;
    ArrayList<View> mViews;
    File filepath = Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES);
    String editedPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studio);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        studioImg= (ImageView) findViewById(R.id.studioImg);
        content_studio= (RelativeLayout) findViewById(R.id.content_studio);
        mAddSticker = findViewById(R.id.btnSticker);
        visorSticker = findViewById(R.id.btnStickVisor);
        pirateSticker =findViewById(R.id.btnStickPirate);
        gafasSticker = findViewById(R.id.btnStickGafas);
        starSticker= findViewById(R.id.btnStickStar);
        libroSticker= findViewById(R.id.btnStickLibro);
        mViews = new ArrayList<>();

        studioImg.setImageBitmap(null);

        Intent data = getIntent();
        String path=data.getStringExtra("src");
        studioImg.setImageBitmap(BitmapFactory.decodeFile(path));
        studioImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentView != null) {
                    mCurrentView.setInEdit(false);
                }
            }
        });


        //Aquí van los onClickListener de cada sticker

        mAddSticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStickerView(R.drawable.winterhat);
            }
        });

        visorSticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStickerView(R.drawable.visor);
            }
        });

        pirateSticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStickerView(R.drawable.pirate_hat);
            }
        });

        gafasSticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStickerView(R.drawable.gafas_nariz);
            }
        });

        starSticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStickerView(R.drawable.estrella);
            }
        });

        libroSticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStickerView(R.drawable.coleccion);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateBitmap();

                Intent UploadIntent = new Intent(getApplicationContext(), UploadActivity.class);
                UploadIntent.putExtra("foto",editedPath);
                Snackbar.make(view, "Se ha guardado la imagen", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                startActivity(UploadIntent);
            }
        });
    }
    private void addStickerView(int resID) {
        final StickerView stickerView = new StickerView(this);
        stickerView.setImageResource(resID);
        stickerView.setOperationListener(new StickerView.OperationListener() {
            @Override
            public void onDeleteClick() {
                mViews.remove(stickerView);
                content_studio.removeView(stickerView);
            }

            @Override
            public void onEdit(StickerView stickerView) {

                mCurrentView.setInEdit(false);
                mCurrentView = stickerView;
                mCurrentView.setInEdit(true);
            }

            @Override
            public void onTop(StickerView stickerView) {
                int position = mViews.indexOf(stickerView);
                if (position == mViews.size() - 1) {
                    return;
                }
                StickerView stickerTemp = (StickerView) mViews.remove(position);
                mViews.add(mViews.size(), stickerTemp);
            }
        });
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        content_studio.addView(stickerView, lp);
        mViews.add(stickerView);
        setCurrentEdit(stickerView);
    }

    private void setCurrentEdit(StickerView stickerView) {
        if (mCurrentView != null) {
            mCurrentView.setInEdit(false);
        }

        mCurrentView = stickerView;
        stickerView.setInEdit(true);
    }

    private void generateBitmap() {
        try{
            if (mCurrentView.isInEdit)
                mCurrentView.setInEdit(false);}
        catch(Exception e){

        }
        Bitmap bitmap = Bitmap.createBitmap(studioImg.getWidth(),
                studioImg.getHeight()
                , Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        content_studio.draw(canvas);

        //--------------
        File dir = new File(filepath.getAbsolutePath()
                + "/GatosUninorte/");
        dir.mkdirs();

        String number = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String targetFilename= "img_"+number+".jpg";
        File file = new File(dir, targetFilename );

        OutputStream output = null;
        try {
            output = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
            output.flush();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        MediaScannerConnection.scanFile(this,
                new String[] { file.toString() }, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });

        editedPath=file.getPath();


        //-------------//



    }



}
