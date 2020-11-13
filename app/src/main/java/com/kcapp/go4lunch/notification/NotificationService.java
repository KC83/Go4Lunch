package com.kcapp.go4lunch.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.android.libraries.places.api.model.Place;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kcapp.go4lunch.MainActivity;
import com.kcapp.go4lunch.PlaceActivity;
import com.kcapp.go4lunch.R;
import com.kcapp.go4lunch.api.places.ApiGooglePlaces;
import com.kcapp.go4lunch.api.places.PlacesCallback;
import com.kcapp.go4lunch.api.places.PlacesRepositoryImpl;
import com.kcapp.go4lunch.api.services.App;
import com.kcapp.go4lunch.api.services.Constants;
import com.kcapp.go4lunch.api.services.InternetManager;
import com.kcapp.go4lunch.api.services.InternetManagerImpl;
import com.kcapp.go4lunch.di.Injection;
import com.kcapp.go4lunch.di.manager.UserManager;
import com.kcapp.go4lunch.model.User;
import com.kcapp.go4lunch.model.places.GooglePlaceDetailResponse;
import com.kcapp.go4lunch.model.places.GooglePlacesResponse;

import java.lang.reflect.Type;
import java.util.List;

public class NotificationService extends Worker {
    Context mContext;

    public NotificationService(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            // Get data
            Data data = getInputData();
            String placeId = data.getString(Constants.NOTIFICATION_PLACE_ID);
            String placeName = data.getString(Constants.NOTIFICATION_PLACE_NAME);
            String placeVicinity = data.getString(Constants.NOTIFICATION_PLACE_VICINITY);
            String usersJson = data.getString(Constants.NOTIFICATION_USERS_JSON);

            Type listType = new TypeToken<List<User>>() {}.getType();
            List<User> users = new Gson().fromJson(usersJson,listType);

            // Send notification
            sendNotification(mContext, mContext.getString(R.string.notification_title), getTextNotification(mContext, users, placeName, placeVicinity), placeId);

            return Result.success();
        } catch (Throwable throwable) {
            return Result.failure();
        }
    }

    private String getTextNotification(Context context, List<User> users, String placeName, String placeVicinity) {
        StringBuilder workmates = new StringBuilder();

        if (users.size() > 0) {
            workmates.append("\r\n").append(context.getString(R.string.workmates)).append(" : ");

            for (int i = 0; i < users.size(); i++) {
                if (i!=0) {
                    workmates.append(", ");
                }
                workmates.append(users.get(i).getUsername());
            }
        }

        StringBuilder message = new StringBuilder();
        message.append(placeName).append(" - ").append(placeVicinity);
        message.append(workmates);

        return message.substring(0);
    }
    private void sendNotification(Context context, String title, String message, String placeId) {
        //Log.d("SEND NOTIFICATION", "SEND NOTIFICATION :: "+title+" // "+message+" // "+placeId);

        // The Intent tha will be shown when the user click on the notification
        Intent intent = new Intent(context, PlaceActivity.class);
        intent.putExtra(Constants.PLACE_ID, placeId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        // Style of the notification
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle(title);
        bigTextStyle.bigText(message);

        // Create a Channel
        String channelId = context.getString(R.string.app_name);

        // Build a Notification object
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(context.getString(R.string.app_name))
                        .setContentText(context.getString(R.string.app_name))
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent)
                        .setStyle(bigTextStyle);

        // Add the Notification to the Notification Manager and show it.
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Support Version >= Android 8
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = context.getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        // Show notification
        notificationManager.notify(Constants.NOTIFICATION_TAG, Constants.NOTIFICATION_ID, notificationBuilder.build());
    }
}