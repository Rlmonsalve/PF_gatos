package com.rlmonsalve.pf_gatos;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Robert on 19/11/2016.
 */
public class PopupWindowTutorial extends android.widget.PopupWindow
{
    Context ctx;
    TextView lblText;
    View popupView;

    public PopupWindowTutorial(Context context, int id)
    {
        super(context);

        ctx = context;
        switch (id){
            case 1:
                popupView = LayoutInflater.from(context).inflate(R.layout.activity_tutorial_map, null);
                break;
            case 2:
                popupView = LayoutInflater.from(context).inflate(R.layout.activity_tutorial_gato, null);
                break;
            case 3:
                popupView = LayoutInflater.from(context).inflate(R.layout.activity_tutorial_gatopedia, null);
                break;
            case 4:
                popupView = LayoutInflater.from(context).inflate(R.layout.activity_tutorial_camara, null);
                break;
        }

        setContentView(popupView);
        lblText = (TextView)popupView.findViewById(R.id.textClick);

        //setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        //setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        setHeight(500);
        setWidth(700);

        // Closes the popup window when touch outside of it - when looses focus
        setOutsideTouchable(true);
        setFocusable(true);

        // Removes default black background
        setBackgroundDrawable(new BitmapDrawable());

        lblText.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {


                dismiss();
            }});

        // Closes the popup window when touch it
/*     this.setTouchInterceptor(new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                dismiss();
            }
            return true;
        }
    }); */
    } // End constructor

    // Attaches the view to its parent anchor-view at position x and y
    public void show(View anchor, int x, int y)
    {
        showAtLocation(anchor, Gravity.CENTER, x, y);
    }
}