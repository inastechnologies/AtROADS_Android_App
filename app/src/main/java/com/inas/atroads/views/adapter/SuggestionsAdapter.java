package com.inas.atroads.views.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.inas.atroads.R;
import com.inas.atroads.views.model.SuggestionsListModel;
import java.util.List;

public class SuggestionsAdapter extends RecyclerView.Adapter<SuggestionsAdapter.MyViewHolder> {
    Context context;
//    RidesHistoryResponseModel mResponse;
    private String TAG = "SuggestionsAdapter";
    List<SuggestionsListModel> suggestionsListModelList;
    MyAdapterListener adapterListener;

    public interface MyAdapterListener
    {
        void AcceptOnClick(View view, int position, int pairableUserID, MyViewHolder holder);
        void getHolder( MyViewHolder holder);

    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvGender, tvUsername, tvAcceptButton;

        public MyViewHolder(View view) {
            super(view);
            tvGender = (TextView) view.findViewById(R.id.tvGender);
            tvUsername = (TextView) view.findViewById(R.id.tvUsername);
            tvAcceptButton = (TextView) view.findViewById(R.id.tvAcceptButton);
            tvAcceptButton.setText("Send Request");
        }
    }


    public SuggestionsAdapter(Context context, List<SuggestionsListModel> suggestionsListModels, MyAdapterListener myAdapterListener) {
        this.context = context;
        this.suggestionsListModelList = suggestionsListModels;
        this.adapterListener = myAdapterListener;
    }


    @NonNull
    @Override
    public SuggestionsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.acceptance_list_item, parent, false);

        return new SuggestionsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestionsAdapter.MyViewHolder holder, int position) {
        holder.tvGender.setText(suggestionsListModelList.get(position).getGender());
        holder.tvUsername.setText(suggestionsListModelList.get(position).getName());
        String destAddress = suggestionsListModelList.get(position).getDestAddress();
        String srcAddress = suggestionsListModelList.get(position).getSrcAddress();
        int pairableUserID = suggestionsListModelList.get(position).getPairableUserID();
        Log.i(TAG, "onBindViewHolder: "+destAddress+"-->"+srcAddress+"-->"+pairableUserID);
        holder.tvAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, "Need to implement ", Toast.LENGTH_SHORT).show();
                if (adapterListener != null) {
                    adapterListener.AcceptOnClick(v,position,pairableUserID,holder);
                }
            }
        });
        if (adapterListener != null) {
            adapterListener.getHolder(holder);
        }

    }

    @Override
    public int getItemCount() {
        return suggestionsListModelList.size();
    }
}
