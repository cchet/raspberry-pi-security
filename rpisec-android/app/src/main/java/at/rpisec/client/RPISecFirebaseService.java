package at.rpisec.client;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Philipp Wurm.
 */

public final class RPISecFirebaseService extends FirebaseInstanceIdService {

    public RPISecFirebaseService()
    {
        System.out.println("[DEBUG] RPISecFirebaseService constructor");
    }

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {

    }
}
