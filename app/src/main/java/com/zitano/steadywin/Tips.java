package com.zitano.steadywin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Tips extends Fragment {

    View view;
    RecyclerView mrecycler;
    LinearLayoutManager mlinearlayout;
    TextView loading;
    DatabaseReference mdatabasereference;
    FirebaseRecyclerAdapter<Model, DailyTips.ItemViewHolder> firebaseRecyclerAdapter;
     private AdView adView;
    private InterstitialAd mInterstitialAd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.tips, container, false);


        mdatabasereference = FirebaseDatabase.getInstance().getReference().child("jackpot").child("dailyplays");
        loading = view.findViewById(R.id.loaddaily);

        adView= view.findViewById(R.id.adView3);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        mrecycler = view.findViewById(R.id.recycler);
        mrecycler.setHasFixedSize(false);
        mlinearlayout = new LinearLayoutManager(getContext());
        mrecycler.setLayoutManager(mlinearlayout);
    firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<Model, DailyTips.ItemViewHolder>(
            Model.class,
            R.layout.listviewcard,
            DailyTips.ItemViewHolder.class,
            mdatabasereference
    ) {
        @Override
        protected void populateViewHolder(DailyTips.ItemViewHolder itemViewHolder, Model model, int position) {
            final String item_key = getRef(position).getKey();
            itemViewHolder.setTitle(model.getTitle());
            itemViewHolder.setDetails(model.getBody());
            itemViewHolder.setTime(model.getTime());
            loading.setVisibility(View.GONE);
            itemViewHolder.mnview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent adDetails = new Intent(v.getContext(), PostDetails.class);
                    adDetails.putExtra("postkey", item_key);
                    adDetails.putExtra("selection", "dailyplays");
                    startActivity(adDetails);
                }

            });
        }
    };
        mrecycler.setAdapter(firebaseRecyclerAdapter);

    }



}
