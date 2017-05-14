package at.rpisec.client;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import at.rpisec.server.shared.rest.constants.ClientRestConstants;
import at.rpisec.server.shared.rest.model.FirebaseMessage;
import at.rpisec.server.shared.rest.model.TokenResponse;

/**
 * Created by Philipp Wurm.
 */

public class LoginActivity extends AppCompatActivity /*implements LoaderCallbacks<Cursor>*/ {

    //for development only
    private static final String DEV_BASE_ADDRESS = "http://192.168.1.104:8080";

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    //private static final int REQUEST_READ_CONTACTS = 0;

    // only for debug ...
    private static final String DEBUG_LOGIN_TAG = "FireBaseLogin";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    // UI references.
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
/*    private View mProgressView;
    private View mLoginFormView;*/

    private static HttpEntity<Object> httpEntity;
    private static RestTemplate rpiSecRestTemplate;
    private static String generatedUUID;

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

            HttpAuthentication authHeader = new HttpBasicAuthentication(userName, password);
            HttpHeaders requestHeaders = new HttpHeaders();

            requestHeaders.setAuthorization(authHeader);
            requestHeaders.setAccept(Collections.singletonList(mediaType));

            httpEntity = new HttpEntity<>(requestHeaders);
        }
    }

    private static HttpEntity<Object> getHttpEntity() {
        return httpEntity;
    }

    private static RestTemplate getRpiSecRestTemplate()
    {
        if(rpiSecRestTemplate == null)
        {
            rpiSecRestTemplate = new RestTemplate();
            rpiSecRestTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        }

        return rpiSecRestTemplate;
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

/*        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);*/
    }

/*    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }*/

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

/*    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mUsernameView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }*/

    /**
     * Callback received when a permissions request has been completed.
     */
/*    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }*/

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

            initHttpCredentials(username, password, MediaType.ALL);

            try {
                String authToken = new RegisterUUIDTask().execute().get();

                if(!authToken.isEmpty()) {
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
        //TODO: Replace this with your own logic
        return !username.trim().isEmpty();
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
/*    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }*/

/*    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }*/

/*    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }*/

/*    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }*/

/*    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mUsernameView.setAdapter(adapter);
    }*/


/*    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }*/

    private class RegisterUUIDTask extends AsyncTask<Void, Void, String>
    {
        private final String UrlRegToken = DEV_BASE_ADDRESS + "/rpisec" + ClientRestConstants.URI_REGISTER + "?uuid=" + getGeneratedUUID();
        private final String UrlToken = DEV_BASE_ADDRESS + "/rpisec" + ClientRestConstants.URI_GET_TOKEN + "?uuid=" + getGeneratedUUID();

        @Override
        protected String doInBackground(Void... params) {

            ResponseEntity<String> registerResponse = getRpiSecRestTemplate().exchange(UrlRegToken, HttpMethod.PUT, getHttpEntity(), String.class);

            if (registerResponse.getStatusCode() == HttpStatus.OK) {
                ResponseEntity<TokenResponse> response = getRpiSecRestTemplate().exchange(UrlToken, HttpMethod.GET, getHttpEntity(), TokenResponse.class);

                if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null && !response.getBody().getToken().isEmpty()) {
                    return response.getBody().getToken().trim();
                }
            }

            return "";
        }
    }

    private class RegisterFCMTask extends AsyncTask<String, Void, Boolean>
    {
        private final String fcmToken = FirebaseInstanceId.getInstance().getToken();
        private final String UrlFCMToken = DEV_BASE_ADDRESS + "/rpisec" + ClientRestConstants.URI_REGISTER_FCM_TOKEN + "?uuid=%s&fcmToken=" + fcmToken;
        private final String UrlPostNotify = DEV_BASE_ADDRESS + "/rpisec" + ClientRestConstants.BASE_URI + "/notify";

        @Override
        protected Boolean doInBackground(String... params) {

            String fcmUri = String.format(UrlFCMToken, params[0].trim());

            System.out.println("[DEBUG] FCM_Token: " + fcmUri);

            // REGISTER FCM TOKEN /registerFcmToken
            ResponseEntity<FirebaseMessage> fm = getRpiSecRestTemplate().exchange(fcmUri, HttpMethod.PUT, getHttpEntity(), FirebaseMessage.class);

            if (fm.getStatusCode() == HttpStatus.OK) {

                // CALL /notify
                ResponseEntity<String> notifyResponse = getRpiSecRestTemplate().exchange(UrlPostNotify, HttpMethod.PUT, getHttpEntity(), String.class);

                if (notifyResponse.getStatusCode() == HttpStatus.OK) {
                    System.out.println("[DEBUG] notify send");
                    return true;
                } else {
                    System.out.println("[DEBUG] notify not send");
                }
            } else {
                System.out.println("[DEBUG] registerFcmToken not successful");
            }

            return false;
        }
    }
}

