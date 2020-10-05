package com.inas.atroads.views.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.inas.atroads.R;
import com.inas.atroads.views.Activities.ChatActivity;
import com.inas.atroads.views.Activities.StartUpActivity;
import com.inas.atroads.views.Activities.UserDetails;
import com.inas.atroads.views.UI.MobileNumberRegisterScreen;
import com.inas.atroads.views.UI.SpalshScreen;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class StartUpVPAdapter extends PagerAdapter {
    Context context;
    int images[];
    String text[];
    LayoutInflater layoutInflater;


    public StartUpVPAdapter(Context context, int[] images, String[] text) {
        this.context = context;
        this.images = images;
        this.text= text;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.activity_startup_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        imageView.setImageResource(images[position]);

        TextView tv_slogan = itemView.findViewById(R.id.tv_slogan);
        tv_slogan.setText(text[position]);

        Button btn_get_started= itemView.findViewById(R.id.btn_get_started);
        if(position==1){
            btn_get_started.setVisibility(View.VISIBLE);
        }else{
            btn_get_started.setVisibility(View.GONE);
        }
        btn_get_started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, MobileNumberRegisterScreen.class);
                context.startActivity(i);
                ((Activity)context).finish();
            }
        });

        container.addView(itemView);

        //listening to image click

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}