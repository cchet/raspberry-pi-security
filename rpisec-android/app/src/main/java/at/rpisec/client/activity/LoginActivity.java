package at.rpisec.client.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import at.rpisec.client.R;
import at.rpisec.client.rest.OAuthCredentials;
import at.rpisec.client.rest.RestHelper;
import at.rpisec.client.util.PropertyUtil;
import at.rpisec.server.shared.rest.constants.ClientRestConstants;
import at.rpisec.server.shared.rest.model.FirebaseMessageResponse;
import at.rpisec.server.shared.rest.model.TokenResponse;

/**
 * @author Philipp Wurm <philipp.wurm@gmail.com>.
 */

public class LoginActivity extends AppCompatActivity {

    // only for debug ...
    private static final String DEBUG_LOGIN_TAG = "FireBaseLogin";

    //for development only
    private static OAuthCredentials oAuthCredentials;

    private static String DEV_BASE_ADDRESS = "";
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

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(DEBUG_LOGIN_TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(DEBUG_LOGIN_TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    LoginActivity.this.attemptLogin();
                    return true;
                }
                return false;
            }
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
            try {
                oAuthCredentials = new OAuthCredentials(username, password);
                boolean login = new ClientLoginOAuthTask().execute().get();

                if (login) {
                    if (oAuthCredentials != null && !oAuthCredentials.getToken().isEmpty()) {
                        mAuth.signInWithCustomToken(oAuthCredentials.getToken())
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        Log.v(DEBUG_LOGIN_TAG, "[DEBUG] signInWithCustomToken:onComplete:" + task.isSuccessful());
                                        System.out.println();

                                        if (!task.isSuccessful()) {
                                            Log.v(DEBUG_LOGIN_TAG, "[DEBUG] signInWithCustomToken:failed " + task.getException());
                                            Toast.makeText(LoginActivity.this, "Login-Error! Please try again later...", Toast.LENGTH_LONG).show();
                                        } else {
                                            try {
                                                boolean registrationSuccessful = new RegisterFCMTask().execute().get();

                                                if (registrationSuccessful) {
                                                    startIncidentImageActivity();
                                                } else {
                                                    Toast.makeText(LoginActivity.this, "Login-Error! Please try again later...", Toast.LENGTH_LONG).show();
                                                }

                                            } catch (InterruptedException | ExecutionException e) {
                                                Log.v(DEBUG_LOGIN_TAG, e.getMessage());
                                            }

                                            Log.v(DEBUG_LOGIN_TAG, "signInWithCustomToken:success");
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "Login-Error! Please try again later...", Toast.LENGTH_LONG).show();
                                Log.v(DEBUG_LOGIN_TAG, "[DEBUG] failed to sign in with custom token " + e.getLocalizedMessage());
                            }
                        });
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Login-Error! Please try again later...", Toast.LENGTH_LONG).show();
                }
            } catch (InterruptedException | ExecutionException e) {
                Log.e(DEBUG_LOGIN_TAG, e.getMessage());
            }
        }
    }

    private void startIncidentImageActivity() {
        Intent intent = new Intent(this, IncidentsActivity.class);
        intent.putExtra("OAuthCredentials", oAuthCredentials);
        startActivity(intent);
    }

    private boolean isUsernameValid(String username) {
        return !username.trim().isEmpty();
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 8;
    }

    private class ClientLoginOAuthTask extends AsyncTask<Void, Void, Boolean> {
        private final String UriRegToken = getDevBaseAddress() + "/rpisec" + ClientRestConstants.URI_CLIENT_LOGIN + "?deviceId=" + getGeneratedUUID();

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Log.v(DEBUG_LOGIN_TAG, "[DEBUG] UriRegToken: " + UriRegToken);

                if (oAuthCredentials != null) {
                    ResponseEntity<TokenResponse> response = getRpiSecRestTemplate().exchange(UriRegToken, HttpMethod.GET, RestHelper.getHttpEntity(oAuthCredentials.getUserName(), oAuthCredentials.getPassword()), TokenResponse.class);
                    if (response.getStatusCode() == HttpStatus.OK) {
                        TokenResponse res = response.getBody();
                        oAuthCredentials.setToken(res.getToken());
                        oAuthCredentials.setClientId(res.getClientId());
                        oAuthCredentials.setClientSecret(res.getClientSecret());

                        Log.v(DEBUG_LOGIN_TAG, "[ClientId] " + res.getClientId());
                        Log.v(DEBUG_LOGIN_TAG, "[ClientSecret] " + res.getClientSecret());
                        return true;
                    }
                }
            } catch (Exception e) {
                Log.v(DEBUG_LOGIN_TAG, e.getMessage());
            }
            return false;
        }
    }

    private class RegisterFCMTask extends AsyncTask<Void, Void, Boolean> {
        private final String fcmToken = FirebaseInstanceId.getInstance().getToken();
        private final String UriFCMToken = getDevBaseAddress() + "/rpisec" + ClientRestConstants.URI_REGISTER_FCM_TOKEN + "?deviceId=%s&fcmToken=" + fcmToken;

        @Override
        protected Boolean doInBackground(Void... params) {
            String fcmUri = String.format(UriFCMToken, getGeneratedUUID());

            Log.v(DEBUG_LOGIN_TAG, "[DEBUG] FCM_Token: " + fcmUri);

            // REGISTER FCM TOKEN /registerFcmToken
            ResponseEntity<FirebaseMessageResponse> fm = getRpiSecRestTemplate().exchange(fcmUri, HttpMethod.PUT, RestHelper.getHttpEntity(oAuthCredentials.getUserName(), oAuthCredentials.getPassword()), FirebaseMessageResponse.class);

            return (fm.getStatusCode() == HttpStatus.OK);
        }
    }
}

