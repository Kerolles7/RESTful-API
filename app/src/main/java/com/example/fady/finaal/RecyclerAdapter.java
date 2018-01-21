package com.example.fady.finaal;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Fady on 1/10/2018.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    DecimalFormat df2 = new DecimalFormat(".##");

    public String prestakey = "?ws_key=6F3JYKJ7MWWT8KRB8IH86MYP32CWUR2G";
    public String image = "http://xtechlabs.net/commerce/api/images/products/";

    public Context context;
    ArrayList<FeedCategory> ProductsList = new ArrayList<>();
    FeedCategory feedItem;

    public ClickListener clickListener;


    public RecyclerAdapter(Context context, ArrayList Products) {
        this.context = context;
        this.ProductsList = Products;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }


    public void setClickListener(ClickListener clickListener)
    {
        this.clickListener = clickListener;
    }



    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        YoYo.with(Techniques.FadeIn).playOn(holder.cardView);

        feedItem = this.ProductsList.get(position);

        holder.Name.setText(feedItem.getName());
        holder.Price.setText(feedItem.getPrice());
       // holder.Price.setText(String.format("%.2f", feedItem.getPrice()));


        Picasso.with(context).load(image + feedItem.getId_product() + "/" + feedItem.getId_image() + prestakey)
                .placeholder(R.drawable.placeholder)
                .fit().centerCrop()
                .into(holder.Thumbnail);

    }

    @Override
    public int getItemCount() {
        return ProductsList.size();
    }




    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        CardView cardView;
        TextView Name, Price;
        ImageView Thumbnail;

        public MyViewHolder(View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.title_text);
            Price =  itemView.findViewById(R.id.description_text);
            Thumbnail =  itemView.findViewById(R.id.thumb_img);
            cardView = itemView.findViewById(R.id.cardview);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if (clickListener != null)
            {
                clickListener.itemClicked(v, getPosition());
            }
        }
    }

        public interface ClickListener
        {
         void itemClicked(View view, int position);

        }
    }

