package com.inas.atroads.views.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.inas.atroads.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    ArrayList<String> al;
    ArrayList<String> al_pic;
    HashMap<String, String> map = new HashMap<>();


    public MyListAdapter(Activity context, ArrayList<String> al, ArrayList<String> al_pic) {
        super(context, R.layout.user_list_item);
        // TODO Auto-generated constructor stub
        this.context=context;
        this.al= al;
        this.al_pic=al_pic;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.user_list_item, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.NameTv);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.profile_image);
        titleText.setText(al.get(position));
        LoadImageFromUrl(context,al_pic.get(position),imageView);


        for(Map.Entry map1  :  map.entrySet() )
        {
            Log.i("MyListAdapter", "getView: "+map1.getKey()+" "+map1.getValue());
            titleText.setText(map1.getKey()+"");
            if(map1.getValue().equals(""))
            {
                //LoadImageFromUrl(context, String.valueOf(map1.getValue()),imageView);
                imageView.setImageResource(R.drawable.profile);
            }
            else {
                LoadImageFromUrl(context, String.valueOf(map1.getValue()),imageView);

            }

        }
        return rowView;

    };


    /**
     *
     * @param context
     * @param imageUrl
     * @param imageView
     */
    private void LoadImageFromUrl(Context context, String imageUrl, ImageView imageView)
    {
        Picasso.with(context).load(imageUrl).error(R.drawable.profile).into(imageView);
    }

}