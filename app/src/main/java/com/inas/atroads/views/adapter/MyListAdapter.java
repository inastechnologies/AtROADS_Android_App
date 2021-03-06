package com.inas.atroads.views.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.inas.atroads.R;
import com.inas.atroads.views.Activities.ChatActivity;
import com.inas.atroads.views.Activities.UserDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{
 //   private MyListData[] listdata;
    Activity context;
    ArrayList<String> al;
    ArrayList<String> al_pic;
    ArrayList<Integer> al_userId;
    int userRideId;

    // RecyclerView recyclerView;
    public MyListAdapter(Activity context, ArrayList<String> al, ArrayList<String> al_pic, ArrayList<Integer> al_userId) {
        this.context = context;
        this.al= al;
        this.al_pic=al_pic;
        this.al_userId = al_userId;
        this.userRideId= userRideId;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.user_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //final  myListData = listdata[position];
        holder.titleText.setText(al.get(position));
        LoadImageFromUrl(context,al_pic.get(position),holder.profile_image);
        //holder.profile_image.setImageResource(al_pic.get(position));
        holder.liner_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   Toast.makeText(view.getContext(),"click on item: "+myListData.getDescription(),Toast.LENGTH_LONG).show();
                Intent i = new Intent(context, ChatActivity.class);
                UserDetails.chatWith = al.get(position);
                i.putExtra("OtheruserId",al_userId.get(position));
                i.putExtra("OtheruserName",al.get(position));
                i.putExtra("FROMACTIVITY","UsersActivity");
                context.startActivity(i);
            }
        });
    }


    @Override
    public int getItemCount() {
        return al.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView profile_image;
        public TextView titleText;
        public LinearLayout liner_top;
        public ViewHolder(View itemView) {
            super(itemView);
            this.profile_image = (ImageView) itemView.findViewById(R.id.profile_image);
            this.titleText = (TextView) itemView.findViewById(R.id.NameTv);
            this.liner_top = (LinearLayout) itemView.findViewById(R.id.liner_top);
           // relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }

    private void LoadImageFromUrl(Context context, String imageUrl, ImageView imageView)
    {
        Picasso.with(context).load(imageUrl).error(R.drawable.profile).into(imageView);
    }
}

/*
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


    */
/**
     *
     * @param context
     * @param imageUrl
     * @param imageView
     *//*



}*/
