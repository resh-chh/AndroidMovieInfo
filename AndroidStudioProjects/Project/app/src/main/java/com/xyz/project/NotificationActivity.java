package com.xyz.project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NotificationActivity extends AppCompatActivity {

    EditText etWishes;
    Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        etWishes = (EditText) findViewById(R.id.etWishes);
        btnSend = (Button) findViewById(R.id.btnSend);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etWishes.getText().toString().length() == 0){
                    etWishes.setError("Enter your wish");
                    etWishes.requestFocus();
                    return;
                }

                Intent sentIntent = new Intent();
                sentIntent.setAction(Intent.ACTION_SEND);
                sentIntent.putExtra(Intent.EXTRA_TEXT, etWishes.getText().toString());
                sentIntent.setType("text/plain");
                startActivity(sentIntent);
            }
        });

    }
}
