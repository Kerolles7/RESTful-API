package com.example.fady.finaal;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Fady on 1/11/2018.
 */

public class ImageApabter extends BaseAdapter {

    Context context;
    ArrayList<ProductInfo.FeedImage> urls = new ArrayList<>();

    public ImageApabter(Context context, ArrayList Images) {
        this.context = context;
        this.urls = Images;
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public Object getItem(int position) {
        return null ;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {

        final ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(250, 250));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);

        imageView.setSelected(true);

        } else {
            imageView = (ImageView) convertView;
        }


        Picasso.with(context).load(urls.get(position).getUrl())
                .placeholder(R.drawable.placeholder)
                .fit()
                .centerCrop()
                .into(imageView);


        return imageView;
    }


}

