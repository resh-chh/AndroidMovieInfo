package com.xyz.project;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AlarmReciever extends BroadcastReceiver {
    public AlarmReciever() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("BR", "Birthday Reminder");

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = df.format(c.getTime());

        String current[] = currentDate.split("-");
        int currentDay = Integer.parseInt(current[0]);
        int currentMonth = Integer.parseInt(current[1]);

        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        ArrayList<Friend> friendArrayList = databaseHandler.getAllFriends();

        for(Friend friend: friendArrayList) {
            System.out.println("Current date " + currentDay + "Birth date : " +friend.getDob());

            String friendDob[] = friend.getDob().split("-");

            int friendDay = Integer.parseInt(friendDob[0]);
            int friendMonth = Integer.parseInt(friendDob[1]);

            if(currentDay == friendDay && currentMonth == friendMonth) {
                sendBirthdayNotification(context, friend.getName());
                System.out.println("Match found");
            }
            else
                System.out.println("Not found");
        }
    }

    public void sendBirthdayNotification(Context context, String name){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent1 = new Intent(context, NotificationActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(android.R.drawable.ic_menu_always_landscape_portrait)
                .setContentTitle("Birthday Wishes")
                .setContentText("It's " + name + "'s Birthday Today!")
                .setAutoCancel(true);

        notificationManager.notify(100, builder.build());
    }
}
