package com.example.a27;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    int feedCount = 0;
    private static int NOTIFY_ID = 100;
    private static String CHANNEL_ID = "Cat channel";
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onFeed(View view) {
        feedCount++;
        TextView feedTv = findViewById(R.id.feedTv);
        feedTv.setText("Накормленность: " + feedCount);
    }

    @Override
    protected void onPause() {
        if (NotificationManagerCompat.from(this).areNotificationsEnabled()) {

            createNotification();

            Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.cat)
                    .setContentTitle("Напоминание")
                    .setContentText("Пора кормить кота")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(contentIntent);

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            notificationManagerCompat.notify(NOTIFY_ID, builder.build());
        }
        else {
            Intent settingsIntent = new Intent();
            settingsIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            settingsIntent.setData(uri);
            startActivityForResult(settingsIntent, NOTIFICATION_PERMISSION_REQUEST_CODE);
        }

        super.onPause();
    }
    private void createNotification(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence name = "Name";
            String description = "Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,name,importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}