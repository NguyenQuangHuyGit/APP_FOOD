package com.example.appfood.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appfood.R;
import com.example.appfood.interfaces.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PredictionAdapter  extends RecyclerView.Adapter<PredictionAdapter.PredictionViewHolder> {
    private List<String> predictions;
    private Context context;

    public PredictionAdapter(Context context, List<String> predictions){
        this.context = context;
        this.predictions = predictions;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setPredictions() {
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PredictionAdapter.PredictionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_prediction_item, parent, false);
        return new PredictionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PredictionAdapter.PredictionViewHolder holder, int position) {
        String prediction = predictions.get(position);
        holder.txtHint.setText(prediction);
    }

    @Override
    public int getItemCount() {
        return predictions.size();
    }

    public class PredictionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtHint;

        private OnItemClickListener listener;

        public PredictionViewHolder(@NonNull View itemView) {
            super(itemView);
            txtHint = itemView.findViewById(R.id.txtHint);
            itemView.setOnClickListener(this);
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(v, position,false);
            }
        }
    }
}
