package com.zitano.steadywin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DailyTips extends Fragment {
    View view;
    RecyclerView mrecycler;
    LinearLayoutManager mlinearlayout;
    TextView loading;
    DatabaseReference mdatabasereference;
    private final String TAG = DailyTips.class.getSimpleName();

    FirebaseRecyclerAdapter<Model, ViewHolder> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Model> stnselect;
    private InterstitialAd mInterstatialAd;

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
        mrecycler = view.findViewById(R.id.recycler1);
        mrecycler.setHasFixedSize(false);
        mlinearlayout = new LinearLayoutManager(getContext());
        mrecycler.setLayoutManager(mlinearlayout);
        stnselect = new FirebaseRecyclerOptions.Builder<Model>().setQuery(mdatabasereference, Model.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Model, ViewHolder>(stnselect) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Model model) {
                final String item_key = getRef(position).getKey();
                holder.setTitle(model.getTitle());
                holder.setDetails(model.getBody());
                holder.setTime(model.getTime());
                loading.setVisibility(View.GONE);
                holder.setOnClickListener(new ViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        Intent adDetails = new Intent(v.getContext(), PostDetails.class);
                        adDetails.putExtra("postkey", item_key);
                        adDetails.putExtra("selection", "2+odds");

                        startActivity(adDetails);
                        loadInterst();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listviewcard, parent, false);
                ViewHolder viewHolder = new ViewHolder(itemView);
                return viewHolder;
            }

        };
        mrecycler.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
        return view;

    }

;

    @Override
    public void onStart() {
        super.onStart();
//The functionality here was transferred to @oncreate because this method is called evertime this fragment comes to view thats why it was refreshind
        //TODO learn ore about states in android

    }
    private InterstitialAd loadInterst() {
        AdRequest adRequest= new AdRequest.Builder().build();
        InterstitialAd.load(getContext(),getString(R.string.interstitial_ad),adRequest, new InterstitialAdLoadCallback(){
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstatialAd = interstitialAd;
                mInterstatialAd.show(getActivity());
                Log.i(TAG, "onAdLoaded");

                mInterstatialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.
                        Log.d("TAG", "The ad was dismissed.");
                    }



                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when fullscreen content is shown.
                        // Make sure to set your reference to null so you don't
                        // show it a second time.
                        mInterstatialAd = null;
                        Log.d("TAG", "The ad was shown.");
                    }
                });

            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                Log.i(TAG, loadAdError.getMessage());
                mInterstatialAd = null;

            }
        });
        return mInterstatialAd;
    }

}




