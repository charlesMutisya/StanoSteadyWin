package com.zitano.steadywin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DailyTips extends Fragment {
    View view;
    RecyclerView mrecycler;
    LinearLayoutManager mlinearlayout;
    TextView loading;
    DatabaseReference mdatabasereference;
    FirebaseRecyclerAdapter<Model, ItemViewHolder> firebaseRecyclerAdapter;
AdView adView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dailytips, container, false);


        // mrecycler.setHasFixedSize(true);
        mdatabasereference = FirebaseDatabase.getInstance().getReference().child("jackpot").child("2+odds");
        loading = view.findViewById(R.id.loadwait);
        //mlinearlayout = new LinearLayoutManager(getContext());
        //mrecycler.setLayoutManager(mlinearlayout);

        adView= view.findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        return view;


    }

;

    @Override
    public void onStart() {
        super.onStart();

        mrecycler = view.findViewById(R.id.recycler1);
        mrecycler.setHasFixedSize(false);
        mlinearlayout = new LinearLayoutManager(getContext());
        mrecycler.setLayoutManager(mlinearlayout);
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Model, ItemViewHolder>(
                Model.class,
                R.layout.listviewcard,
                ItemViewHolder.class,
                mdatabasereference
        ) {
            @Override
            protected void populateViewHolder(ItemViewHolder viewHolder, Model model, int position) {
                final String item_key = getRef(position).getKey();
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDetails(model.getBody());
                viewHolder.setTime(model.getTime());
                loading.setVisibility(View.GONE);
                viewHolder.mnview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent adDetails = new Intent(v.getContext(), PostDetails.class);
                        adDetails.putExtra("postkey", item_key);
                        adDetails.putExtra("selection", "2+odds");
                        startActivity(adDetails);
                    }

                });
            }
        };
        mrecycler.setAdapter(firebaseRecyclerAdapter);


    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        View mnview;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mnview = itemView;
        }

        public void setTitle(String title) {
            TextView tvTitle = (TextView) mnview.findViewById(R.id.postTitle);
            tvTitle.setText(title);
        }

        public void setDetails(String details) {

            TextView txtdetails = (TextView) mnview.findViewById(R.id.post);
            txtdetails.setText(details);

        }

        public void setTime(Long time) {

            TextView txtTime = (TextView) mnview.findViewById(R.id.postTime);
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
    }
}




