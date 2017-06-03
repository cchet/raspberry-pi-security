package at.rpisec.client;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import at.rpisec.server.shared.rest.constants.ClientRestConstants;
import at.rpisec.server.shared.rest.model.FirebaseMessage;
import at.rpisec.server.shared.rest.model.TokenResponse;

/**
 * Created by Philipp Wurm.
 */

public class LoginActivity extends AppCompatActivity {

    // only for debug ...
    private static final String DEBUG_LOGIN_TAG = "FireBaseLogin";

    private static final String OAUTH_CREDENTIAL_HEADER_CONTENT_TYPE_NAME = "Content-Type";
    private static final String OAUTH_CREDENTIAL_HEADER_CONTENT_TYPE_VALUE = "application/x-www-form-urlencoded";
    private static final String OAUTH_CREDENTIAL_HEADER_AUTHORIZATION_NAME = "Authorization";
    private static final String OAUTH_CREDENTIAL_HEADER_AUTHORIZATION_TYPE = "Basic "; //!!! trailing whitespace is important !!!
    private static final String OAUTH_CREDENTIAL_BODY_AUTHORIZATION_KEY_USERNAME = "username";
    private static final String OAUTH_CREDENTIAL_BODY_AUTHORIZATION_KEY_PASSWORD = "password";

    private static final String MULTIVALUEMAP_FIELD_KEY = "key";
    private static final String MULTIVALUEMAP_FIELD_VALUE = "value";

    private static final String CREDENTIAL_CHARSET = "US-ASCII";

    //for development only
    private static String DEV_BASE_ADDRESS = "";
    private static HttpEntity<Object> httpEntity;
    private static RestTemplate rpiSecRestTemplate;
    private static String generatedUUID;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    // UI references.
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;

    private static String getGeneratedUUID() {
        return getGeneratedUUID(false);
    }

    private static String getGeneratedUUID(boolean generateNew) {
        if (generateNew || generatedUUID == null || generatedUUID.isEmpty()) {
            generatedUUID = UUID.randomUUID().toString();
        }

        return generatedUUID;
    }

    private static void initHttpCredentials(String userName, String password, MediaType mediaType) {
        if (httpEntity == null) {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(mediaType);

            final String authStr = Base64.encodeToString((userName + ":" + password).getBytes(Charset.forName(CREDENTIAL_CHARSET)), Base64.URL_SAFE);

            headers.add(OAUTH_CREDENTIAL_HEADER_AUTHORIZATION_NAME, OAUTH_CREDENTIAL_HEADER_AUTHORIZATION_TYPE + authStr);
            headers.add(OAUTH_CREDENTIAL_HEADER_CONTENT_TYPE_NAME, OAUTH_CREDENTIAL_HEADER_CONTENT_TYPE_VALUE);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();

            body.add(MULTIVALUEMAP_FIELD_KEY, OAUTH_CREDENTIAL_BODY_AUTHORIZATION_KEY_USERNAME);
            body.add(MULTIVALUEMAP_FIELD_VALUE, userName);
            body.add(MULTIVALUEMAP_FIELD_KEY, OAUTH_CREDENTIAL_BODY_AUTHORIZATION_KEY_PASSWORD);
            body.add(MULTIVALUEMAP_FIELD_VALUE, password);

            httpEntity = new HttpEntity<>(body, headers);
        }
    }

    private static HttpEntity<Object> getHttpEntity() {
        return httpEntity;
    }

    private static RestTemplate getRpiSecRestTemplate() {
        if (rpiSecRestTemplate == null) {
            rpiSecRestTemplate = new RestTemplate();
            rpiSecRestTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        }

        return rpiSecRestTemplate;
    }

    private String getDevBaseAddress() {
        if (DEV_BASE_ADDRESS.isEmpty()) {
            DEV_BASE_ADDRESS = "http://" + PropertyUtil.getConfigValue(this, "base_uri") + ":" + PropertyUtil.getConfigValue(this, "base_port");
        }

        return DEV_BASE_ADDRESS;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.username);
        //populateAutoComplete();

        //get instance of firebase authentication
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                Log.d(DEBUG_LOGIN_TAG, "onAuthStateChanged:signed_in:" + user.getUid());
            } else {
                // User is signed out
                Log.d(DEBUG_LOGIN_TAG, "onAuthStateChanged:signed_out");
            }
        };

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == R.id.login || id == EditorInfo.IME_NULL) {
                attemptLogin();
                return true;
            }
            return false;
        });

        Button mUsernameSignInButton = (Button) findViewById(R.id.username_sign_in_button);
        mUsernameSignInButton.setOnClickListener(view -> attemptLogin());
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid username, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid username address.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            mUsernameView.setError(getString(R.string.error_invalid_user));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {

            initHttpCredentials(username, password, MediaType.APPLICATION_JSON);

            try {
                String authToken = new RegisterUUIDTask().execute().get();

                if (!authToken.isEmpty()) {
                    mAuth.signInWithCustomToken(authToken)
                            .addOnCompleteListener(this, task -> {
                                System.out.println("[DEBUG] signInWithCustomToken:onComplete:" + task.isSuccessful());

                                if (!task.isSuccessful()) {
                                    System.out.println("[DEBUG] signInWithCustomToken:failed " + task.getException());
                                } else {

                                    new RegisterFCMTask().execute(getGeneratedUUID());

                                    Log.d(DEBUG_LOGIN_TAG, "signInWithCustomToken:success");
                                }
                            }).addOnFailureListener(e -> System.out.println("[DEBUG] failed to sign in with custom token " + e.getLocalizedMessage()));
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isUsernameValid(String username) {
        return !username.trim().isEmpty();
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 8;
    }

    private class RegisterUUIDTask extends AsyncTask<Void, Void, String> {
        private final String UriRegToken = getDevBaseAddress() + "/rpisec" + ClientRestConstants.URI_REGISTER + "?uuid=" + getGeneratedUUID();
        private final String UriToken = getDevBaseAddress() + "/rpisec" + ClientRestConstants.URI_GET_TOKEN + "?uuid=" + getGeneratedUUID();

        @Override
        protected String doInBackground(Void... params) {

            try {
                ResponseEntity<String> registerResponse = getRpiSecRestTemplate().exchange(UriRegToken, HttpMethod.PUT, getHttpEntity(), String.class);

                if (registerResponse.getStatusCode() == HttpStatus.OK) {
                    ResponseEntity<TokenResponse> response = getRpiSecRestTemplate().exchange(UriToken, HttpMethod.GET, getHttpEntity(), TokenResponse.class);

                    if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null && !response.getBody().getToken().isEmpty()) {
                        return response.getBody().getToken().trim();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return "";
        }
    }

    private class RegisterFCMTask extends AsyncTask<String, Void, Boolean> {
        private final String fcmToken = FirebaseInstanceId.getInstance().getToken();
        private final String UriFCMToken = getDevBaseAddress() + "/rpisec" + ClientRestConstants.URI_REGISTER_FCM_TOKEN + "?uuid=%s&fcmToken=" + fcmToken;

        @Override
        protected Boolean doInBackground(String... params) {

            String fcmUri = String.format(UriFCMToken, params[0].trim());

            System.out.println("[DEBUG] FCM_Token: " + fcmUri);

            // REGISTER FCM TOKEN /registerFcmToken
            ResponseEntity<FirebaseMessage> fm = getRpiSecRestTemplate().exchange(fcmUri, HttpMethod.PUT, getHttpEntity(), FirebaseMessage.class);

            return (fm.getStatusCode() == HttpStatus.OK);
        }
    }
}

