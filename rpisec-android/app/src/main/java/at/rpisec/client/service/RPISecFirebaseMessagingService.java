package at.rpisec.client.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Base64;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import at.rpisec.client.R;
import at.rpisec.client.activity.IncidentsActivity;
import at.rpisec.shared.rest.constants.FirebaseConstants;
import at.rpisec.shared.rest.model.FirebaseDatabaseItem;

/**
 * @author Philipp Wurm <philipp.wurm@gmail.com>.
 */

public class RPISecFirebaseMessagingService extends FirebaseMessagingService {

    private static final String DEBUG_FCM_TAG = "FirebaseMessaging";
    private static String lastIncidentKey = "";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0 && !lastIncidentKey.equals(remoteMessage.getData().get(FirebaseConstants.FBM_INCIDENT_MESSAGE_KEY_ID))) {
            lastIncidentKey = remoteMessage.getData().get(FirebaseConstants.FBM_INCIDENT_MESSAGE_KEY_ID);
            processData(remoteMessage.getData());
        }
    }

    private void processData(final Map<String, String> data) {
        if (data.containsKey(FirebaseConstants.FBM_INCIDENT_MESSAGE_KEY_ID)) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            if (database != null) {
                DatabaseReference incidentDBRef = database.getReference(FirebaseConstants.DB_ITEM_INCIDENT);
                if (incidentDBRef != null) {
                    Query incidentQuerry = incidentDBRef.child(data.get(FirebaseConstants.FBM_INCIDENT_MESSAGE_KEY_ID));
                    if (incidentQuerry != null) {
                        incidentQuerry.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                FirebaseDatabaseItem item = dataSnapshot.getValue(FirebaseDatabaseItem.class);
                                if (item != null) {

                                    String imageFileName = data.get(FirebaseConstants.FBM_INCIDENT_MESSAGE_KEY_ID) + "." + item.getDataType();
                                    File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

                                    byte[] imageAsBytes = Base64.decode(item.getBase64Data().getBytes(), Base64.DEFAULT);

                                    Bitmap bmp = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);

                                    FileOutputStream out = null;
                                    try {
                                        File storagePath = new File(storageDir, imageFileName);
                                        out = new FileOutputStream(storagePath);
                                        bmp.compress(Bitmap.CompressFormat.JPEG, 80, out); // bmp is your Bitmap instance
                                        // PNG is a lossless format, the compression factor (100) is ignored
                                    } catch (Exception e) {
                                        Log.e(DEBUG_FCM_TAG, e.getMessage());
                                    } finally {
                                        try {
                                            if (out != null) {
                                                out.close();
                                            }
                                        } catch (IOException e) {
                                            Log.e(DEBUG_FCM_TAG, e.getMessage());
                                        }
                                    }

                                    String msgTitle = data.get(FirebaseConstants.FBM_INCIDENT_MESSAGE_KEY_TITLE);
                                    if (msgTitle == null || msgTitle.isEmpty())
                                        msgTitle = "New message received!";


                                    String msgBody = data.get(FirebaseConstants.FBM_INCIDENT_MESSAGE_KEY_MESSAGE);
                                    if (msgBody == null || msgBody.isEmpty())
                                        msgBody = "A new message received!";

                                    sendNotification(msgTitle, msgBody);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }
        }
    }

    private void sendNotification(final String msgTitle, final String msgBody) {

        Intent intent = new Intent(this, IncidentsActivity.class);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle(msgTitle)
                .setContentText(msgBody)
                .setSound(defaultSoundUri);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        mBuilder.setContentIntent(resultPendingIntent);

        // Sets an ID for the notification
        int mNotificationId = 1;

        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }
}
