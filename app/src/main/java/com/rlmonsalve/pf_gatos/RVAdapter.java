package com.rlmonsalve.pf_gatos;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rlmonsalve.pf_gatos.data.Gato;

import java.util.List;

/**
 * Created by Robert on 20/11/2016.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.GatoViewHolder> {

    List<Gato> gatos;
    Context context;
    int pos;
    Typeface tf;

    RVAdapter(List<Gato> persons, Typeface typeface, Context context){
        this.gatos = persons;
        this.tf = typeface;
        this.context = context;
    }

    @Override
    public GatoViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_gatopedia, viewGroup, false);
        GatoViewHolder gvh = new GatoViewHolder(v);
        return gvh;
    }

    @Override
    public void onBindViewHolder(GatoViewHolder holder, final int i) {
        holder.gatoName.setText(gatos.get(i).getName());
        holder.gatoName.setTypeface(tf);
        holder.gatoImg.setImageResource(gatos.get(i).getIconId());
        holder.gatoFav.setImageResource(R.drawable.corazon);
        if (gatos.get(i).isFavorite()){
            holder.gatoFav.setVisibility(View.VISIBLE);
        }else{
            holder.gatoFav.setVisibility(View.INVISIBLE);
        }
        holder.gatoName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gatos.get(i).isUnlocked()) {
                    final Intent intent;
                    intent = new Intent(context, GatoInfoActivity.class);
                    intent.putExtra("gato_id",i);
                    intent.putExtra("gato_fav",gatos.get(i).isFavorite());
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return gatos.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class GatoViewHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener*/{

        CardView cv;
        TextView gatoName;
        ImageView gatoImg;
        ImageView gatoFav;
        private final Context context;

        public GatoViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            cv = (CardView)itemView.findViewById(R.id.cv);
            gatoName = (TextView)itemView.findViewById(R.id.gato_name);
            gatoImg = (ImageView)itemView.findViewById(R.id.gato_picture);
            gatoFav = (ImageView)itemView.findViewById(R.id.fav_icon);
            itemView.setClickable(true);
            //itemView.setOnClickListener(this);
        }

        /*@Override
        public void onClick(View view) {
            Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show();
            final Intent intent;
            intent =  new Intent(context, GatoInfoActivity.class);
            intent.putExtra("gato_id",getPosition());
            context.startActivity(intent);
        }*/
    }

}