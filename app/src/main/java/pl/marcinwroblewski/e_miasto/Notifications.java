package pl.marcinwroblewski.e_miasto;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import pl.marcinwroblewski.e_miasto.Activities.EventDetailsActivity;

/**
 * Created by Marcin Wróblewski on 17.10.2016.
 */

public class Notifications {

    public static void showNotification(Context context, String text) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setTicker("Nowe wydarzenie")
                .setContentTitle("E-miasto")
                .setContentText(text)
                .setColor(context.getResources().getColor(R.color.colorPrimaryDark))
                .setSmallIcon(R.drawable.ic_chat_white_24dp);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1337, builder.build());
    }

    public static void showEventNotification(Context context, String text, long eventId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setTicker("Nowe wydarzenie")
                .setContentTitle("E-miasto")
                .setContentText(text)
                .setColor(context.getResources().getColor(R.color.colorPrimaryDark))
                .setSmallIcon(R.drawable.ic_chat_white_24dp);

        Intent intent = new Intent(context, EventDetailsActivity.class);
        intent.putExtra("eventId", eventId);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(EventDetailsActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setContentIntent(resultPendingIntent);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1337, builder.build());
    }

}
