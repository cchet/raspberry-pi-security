import at.rpisec.server.shared.rest.constants.FirebaseConstants;
import at.rpisec.server.shared.rest.model.FirebaseDatabaseItem;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.tasks.Task;
import com.google.firebase.tasks.Tasks;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * FCM-JAva-Client: http://bytefish.de/blog/fcmjava/
 *
 * @author Thomas Herzog <herzog.thomas81@gmail.com>
 * @since 04/28/17
 */
public class FirebaseLogicTrials {

    private FirebaseDatabase firebaseDatabase;
//    private static Semaphore s = new Semaphore(1);

    public FirebaseLogicTrials(@Qualifier("incidentFirebaseDatabase") FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    public void trials() {
//        try {
//            s.acquire();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        final FirebaseDatabase database = firebaseDatabase;

        database.setLogLevel(Logger.Level.INFO);
        //        database.goOnline();

        final Map<String, FirebaseDatabaseItem> data = new HashMap<String, FirebaseDatabaseItem>() {{
            put("1", new FirebaseDatabaseItem("incident occured", false, "jpg", "base64Data"));
            put("2", new FirebaseDatabaseItem("incident occured", false, "jpg", "base64Data"));
        }};

        DatabaseReference ref = database.getReference(FirebaseConstants.DB_ITEM_INCIDENT);

        Task t2 = database.getReference("incidents").setValue(data).addOnCompleteListener((task) -> {
            System.out.println("Callback called");
            database.goOffline();
            database.purgeOutstandingWrites();
//            s.release();
        });
        Tasks.whenAll(t2);
    }


    public static void main(String[] args) throws Throwable {
        final File file = Paths.get(System.getProperty("user.home") + "/rpisec-conf/firebase-account.json").toFile();
        if (!file.exists()) {
            throw new IllegalArgumentException("firebaseConfig: does not exist");
        }

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredential(FirebaseCredentials.fromCertificate(FileUtils.openInputStream(file)))
                .setDatabaseUrl("https://rpisec-89e15.firebaseio.com/")
                .build();

        final FirebaseApp app = FirebaseApp.initializeApp(options);
        final FirebaseLogicTrials logic = new FirebaseLogicTrials(FirebaseDatabase.getInstance(app));
        logic.trials();

        System.out.println("waiting for semaphore");

//        s.acquire();

        System.out.println("Done");
    }
}
