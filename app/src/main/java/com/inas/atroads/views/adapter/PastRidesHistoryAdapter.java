package com.inas.atroads.views.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.inas.atroads.R;
import com.inas.atroads.views.model.RidesHistoryResponseModel;

public class PastRidesHistoryAdapter extends RecyclerView.Adapter<PastRidesHistoryAdapter.MyViewHolder>
{
    Context context;
    RidesHistoryResponseModel mResponse;
    private String TAG = "PastRidesHistoryAdapter";


    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView vehicleImg;
        public TextView dateandtime,Autonumber,pickup,droplocation,rupees;
        public ConstraintLayout historyItemCLayout;

        public MyViewHolder(View view) {
            super(view);
            dateandtime = (TextView)view.findViewById(R.id.dateandtime);
            Autonumber = (TextView)view.findViewById(R.id.number);
            pickup = (TextView)view.findViewById(R.id.pickup);
            droplocation = (TextView)view.findViewById(R.id.droplocation);
            rupees = (TextView)view.findViewById(R.id.rupees);
            vehicleImg = (ImageView) view.findViewById(R.id.vehicleImg);
            vehicleImg.setImageResource(R.drawable.auto_grey);
            historyItemCLayout = (ConstraintLayout)view.findViewById(R.id.historyItemCLayout);
        }
    }


    public PastRidesHistoryAdapter(Context context, RidesHistoryResponseModel mResponse) {
        this.context = context;
        this.mResponse = mResponse;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rides_history_row, parent, false);

        return new PastRidesHistoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.dateandtime.setText(": "+mResponse.getResult().get(position).getRideTimeStamp());
        Log.i(TAG, "RideTimeStamp: "+mResponse.getResult().get(position).getRideTimeStamp());
        holder.Autonumber.setText(": "+mResponse.getResult().get(position).getAutoNumber());
        holder.pickup.setText(""+mResponse.getResult().get(position).getUserSourceAddress());
        holder.droplocation.setText(""+mResponse.getResult().get(position).getUserDestAddress());
        holder.rupees.setText("Rs."+mResponse.getResult().get(position).getPayableAmount());
    }

    @Override
    public int getItemCount() {
        return mResponse.getResult().size();
    }

}
