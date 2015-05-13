package com.tdc.tdcwear;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.text.Html;

public class NotificationUtils {

    public static final String EXTRA_VOICE_REPLY = "extra_voice_reply";

    public static final int NOTIFICATION_ID = 1000;
    private static final String GROUP_KEY = "nextLevelApps";

    // Public methods ------------------------------------------------------------------------------
    public static void simpleNotification(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setLargeIcon(bitmapLargeIcon(context))
                .setColor(context.getResources().getColor(R.color.main_color))
                .setAutoCancel(true)
                .setContentTitle(context.getString(R.string.notification_simple_title))
                .setContentText(context.getString(R.string.notification_text))
                .setContentIntent(mainActivityPendingIntent(context))
                .setSmallIcon(R.drawable.ic_notification);

        dispatchNotification(context, builder);
    }

    public static void notificationWithAction(Context context, boolean onlyInWearable){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setLargeIcon(bitmapLargeIcon(context))
                .setColor(context.getResources().getColor(R.color.main_color))
                .setAutoCancel(true)
                .setContentTitle(context.getString(R.string.notification_action_title))
                .setContentText(context.getString(R.string.notification_text))
                .setContentIntent(mainActivityPendingIntent(context))
                .setSmallIcon(R.drawable.ic_notification);

        // Intent for notification action
        Intent itAction = new Intent(context, ActionDispatcherReceiver.class);
        itAction.putExtra(ActionDispatcherReceiver.EXTRA_ACTION,
                ActionDispatcherReceiver.ACTION_CALENDAR);
        PendingIntent pitAction = PendingIntent.getBroadcast(
                context,
                0,
                itAction,
                0);
        // Creating action
        NotificationCompat.Action action = new NotificationCompat.Action(
                R.drawable.ic_action_calendar,
                context.getString(R.string.notification_action_calendar),
                pitAction
        );
        // Properly adding action for mobile+wear or just for wear
        if (onlyInWearable){
            NotificationCompat.WearableExtender wearableExtender =
                    new NotificationCompat.WearableExtender();
            wearableExtender.addAction(action);
            builder.extend(wearableExtender);
        } else {
            builder.addAction(action);
        }

        dispatchNotification(context, builder);
    }

    public static void stackedNotification(Context context) {
        int count = ActionDispatcherReceiver.getUnreadCount(context) + 1;

        // When a notification is dismissed, we call the receiver to update the counter to zero
        PendingIntent deletePit =
                PendingIntent.getBroadcast(
                        context, 0, new Intent(context, ActionDispatcherReceiver.class), 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setLargeIcon(bitmapLargeIcon(context))
                .setColor(context.getResources().getColor(R.color.main_color))
                .setAutoCancel(true)
                .setContentTitle(context.getString(R.string.notification_title_with_count, count))
                .setContentText(context.getString(R.string.notification_text))
                .setContentIntent(mainActivityPendingIntent(context))
                .setSmallIcon(R.drawable.ic_notification)
                .setDeleteIntent(deletePit)
                .setGroup(GROUP_KEY);

        NotificationManagerCompat notificationManagerCompat =
                NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(NOTIFICATION_ID + count, builder.build());

        // This is necessary for mobile
        if (count > 1){
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            for (int i = count; i > 0; i--) {
                inboxStyle.addLine(Html.fromHtml(
                        context.getString(R.string.notification_text_with_count, i)));
            }
            inboxStyle.setBigContentTitle(context.getString(R.string.notification_count, count));
            inboxStyle.setSummaryText(context.getString(R.string.app_name));

            Notification summaryNotification = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setColor(context.getResources().getColor(R.color.main_color))
                    .setLargeIcon(bitmapLargeIcon(context))
                    .setStyle(inboxStyle)
                    .setGroup(GROUP_KEY)
                    .setGroupSummary(true)
                    .setDeleteIntent(deletePit)
                    .build();

            notificationManagerCompat.notify(NOTIFICATION_ID, summaryNotification);
        }
        ActionDispatcherReceiver.setUnreadCount(context, count);
    }

    public static void notificationWithPages(Context context){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setLargeIcon(bitmapLargeIcon(context))
                .setColor(context.getResources().getColor(R.color.main_color))
                .setAutoCancel(true)
                .setContentTitle(context.getString(R.string.notification_pages_title))
                .setContentText(context.getString(R.string.notification_text))
                .setContentIntent(mainActivityPendingIntent(context))
                .setSmallIcon(R.drawable.ic_notification);

        NotificationCompat.WearableExtender wearableExtender =
                new NotificationCompat.WearableExtender();

        NotificationCompat.Builder builderPage2 = new NotificationCompat.Builder(context)
                .setContentTitle(context.getString(R.string.notification_page2_title))
                .setStyle(
                        new NotificationCompat.BigTextStyle()
                                .bigText(context.getString(R.string.notification_page2_text)));

        wearableExtender.addPage(builderPage2.build());

        builder.extend(wearableExtender);

        dispatchNotification(context, builder);
    }

    public static void notificationWithVoiceReply(Context context){
        String replyLabel = context.getResources().getString(R.string.reply_question);
        String[] replyChoices = context.getResources().getStringArray(R.array.reply_choices);

        //TODO Usar TaskBuilder
        Intent replyIntent = new Intent(context, ReplyActivity.class);
        PendingIntent replyPendingIntent =
                PendingIntent.getActivity(context, 0, replyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteInput remoteInput = new RemoteInput.Builder(EXTRA_VOICE_REPLY)
                .setLabel(replyLabel)
                .setChoices(replyChoices)
                .build();

        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(R.drawable.ic_reply,
                        context.getString(R.string.reply_label), replyPendingIntent)
                        .addRemoteInput(remoteInput)
                        .build();

        NotificationCompat.WearableExtender wearableExtender =
                new NotificationCompat.WearableExtender().addAction(action);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(context.getString(R.string.notification_reply_title))
                        .setContentText(context.getString(R.string.notification_reply_text))
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .extend(wearableExtender);

        dispatchNotification(context, builder);
    }

    // Helper methods ------------------------------------------------------------------------------
    private static PendingIntent mainActivityPendingIntent(Context context){
        //TODO Use TaskBuilder class here...
        PendingIntent pit = PendingIntent.getActivity(
                context,
                0,
                new Intent(context, MainMobileActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        return pit;
    }

    private static Bitmap bitmapLargeIcon(Context context){
        return BitmapFactory.decodeResource(
                context.getResources(), R.drawable.logo_tdc_color);
    }

    private static void dispatchNotification(Context context, NotificationCompat.Builder builder){
        NotificationManagerCompat notificationManagerCompat =
                NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
    }
}
