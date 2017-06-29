package com.xyz.project;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    ListView lvFriends;
    Button btnAddFriend;

    ArrayList<Friend> friendsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvFriends = (ListView) findViewById(R.id.lvFriends);
        btnAddFriend = (Button) findViewById(R.id.btnAddFriend);

        DatabaseHandler databaseHandler = new DatabaseHandler(MainActivity.this);
        friendsList = databaseHandler.getAllFriends();

        FriendListAdapter adapter = new FriendListAdapter(MainActivity.this, friendsList);
        lvFriends.setAdapter(adapter);

        lvFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, AddFriendActivity.class);
                int id1 = friendsList.get(position).getId();
                intent.putExtra("id", id1);
                startActivity(intent);
            }
        });

        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddFriendActivity.class);
                startActivity(intent);
            }
        });

        setRecurringAlarm(getApplicationContext());
    }

    private void setRecurringAlarm(Context context) {

        Calendar updateTime = Calendar.getInstance();
        updateTime.set(Calendar.HOUR_OF_DAY, 23);
        updateTime.set(Calendar.MINUTE, 58);

        Intent reminder = new Intent(context, AlarmReciever.class);
        PendingIntent bdayReminder = PendingIntent.getBroadcast(context, 0, reminder, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarms = (AlarmManager) MainActivity.this.getSystemService(ALARM_SERVICE);
        alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP, updateTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, bdayReminder);

        Log.d("BR", "alarm");

    }
}
