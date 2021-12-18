package com.zitano.steadywin;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {
    View mnview;

    public ViewHolder(View itemView) {
        super(itemView);
        mnview = itemView;

        //item click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getBindingAdapterPosition());

            }
        });
        //item long click
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClickListener.onItemLongClick(v, getAdapterPosition());
                return true;
            }
        });


    }

    public void setTitle(String title) {
        TextView tvTitle =  mnview.findViewById(R.id.postTitle);
        tvTitle.setText(title);
    }

    public void setDetails(String details) {

        TextView txtdetails = mnview.findViewById(R.id.post);
        txtdetails.setText(details);

    }


    public void setTime(Long time) {

        TextView txtTime = mnview.findViewById(R.id.postTime);
        //long elapsedDays=0,elapsedWeeks = 0, elapsedHours=0,elapsedMin=0;
        long elapsedTime;
        long currentTime = System.currentTimeMillis();
        int elapsed = (int) ((currentTime - time) / 1000);
        if (elapsed < 60) {
            if (elapsed < 2) {
                txtTime.setText("Just Now");
            } else {
                txtTime.setText(elapsed + " sec ago");
            }
        } else if (elapsed > 604799) {
            elapsedTime = elapsed / 604800;
            if (elapsedTime == 1) {
                txtTime.setText(elapsedTime + " week ago");
            } else {

                txtTime.setText(elapsedTime + " weeks ago");
            }
        } else if (elapsed > 86399) {
            elapsedTime = elapsed / 86400;
            if (elapsedTime == 1) {
                txtTime.setText(elapsedTime + " day ago");
            } else {
                txtTime.setText(elapsedTime + " days ago");
            }
        } else if (elapsed > 3599) {
            elapsedTime = elapsed / 3600;
            if (elapsedTime == 1) {
                txtTime.setText(elapsedTime + " hour ago");
            } else {
                txtTime.setText(elapsedTime + " hours ago");
            }
        } else if (elapsed > 59) {
            elapsedTime = elapsed / 60;
            txtTime.setText(elapsedTime + " min ago");


        }

    }

    private ClickListener mClickListener;

    // Interface to handle callbacks
    public interface  ClickListener {
        void onItemClick (View view, int position);
        void onItemLongClick (View view, int position);
    }

    public void setOnClickListener (ClickListener clickListener) {
        mClickListener = clickListener;
    }
}

