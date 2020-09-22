package com.inas.atroads.views.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.inas.atroads.R;
import com.inas.atroads.views.model.NotificationResponseModel;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationVH> {
   // public List<NotificationModel> NotificationModelArrayList;
    Context context;
    NotificationResponseModel mResponse;
    public List<NotificationResponseModel> notificationResponseModelList = new ArrayList<>();

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public NotificationAdapter(Context context, NotificationResponseModel mResponse) {
        this.context = context;
        this.mResponse = mResponse;
    }

    @NonNull
    @Override
    public NotificationVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_list,parent,false);
        return new NotificationVH(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull NotificationVH holder, int position) {
        final String messageBody = mResponse.getResult().get(position).getMessageBody();
        holder.notificationTv.setText(messageBody);
        holder.notificationTime.setText(""+mResponse.getResult().get(position).getcreatedDate());
    }


    @Override
    public int getItemCount() {
        return mResponse.getResult().size();
    }


    public class NotificationVH extends RecyclerView.ViewHolder {
        public TextView notificationTv,notificationTime;

        public NotificationVH(@NonNull View itemView) {
            super(itemView);
            notificationTv=itemView.findViewById(R.id.notificationTv);
            notificationTime= itemView.findViewById(R.id.notificationTime);
        }
    }
}
