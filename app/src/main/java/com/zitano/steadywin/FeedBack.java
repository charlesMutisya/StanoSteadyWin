package com.zitano.steadywin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FeedBack extends AppCompatActivity {
    Button btnSubmit;
    EditText txtFeed, txtEmail;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        setContentView(R.layout.activity_feed_back);

        btnSubmit = findViewById(R.id.btnfeed);
        txtFeed= findViewById(R.id.txtFeed);
        txtEmail= findViewById(R.id.txtEmail);
        email= txtEmail.getText().toString();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email != null){
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "footatips@gmail.com", null));
                    //intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_EMAIL, email);
                    intent.putExtra(Intent.EXTRA_SUBJECT,"Steady Win Feedback");
                    intent.putExtra(Intent.EXTRA_TEXT, txtFeed.getText());
                    startActivity(Intent.createChooser(intent, "Send using"));

                } else {
                    Toast.makeText(FeedBack.this, "please provide your email address", Toast.LENGTH_SHORT).show();
                }

                }

        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id=item.getItemId();
        if(id==android.R.id.home){
            finish();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
