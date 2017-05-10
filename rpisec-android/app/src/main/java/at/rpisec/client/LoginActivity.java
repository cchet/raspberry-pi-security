package at.rpisec.client;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;

import at.rpisec.server.shared.rest.constants.ClientRestConstants;
import at.rpisec.server.shared.rest.model.FirebaseMessage;
import at.rpisec.server.shared.rest.model.TokenResponse;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    //for development only
    private static final String DEV_BASE_ADDRESS = "http://192.168.1.104:8080";

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    // only for debug ...
    private static final String DEBUG_LOGIN_TAG = "FireBaseLogin";

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private static HttpAuthentication authHeader;
    private static HttpHeaders requestHeaders;
    private static HttpEntity<Object> httpEntity;

    private static HttpHeaders getRequestHeader(String userName, String password, MediaType mediaType)
    {
        /*
        String plainCreds = "willie:p@ssword";
byte[] plainCredsBytes = plainCreds.getBytes();
byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
String base64Creds = new String(base64CredsBytes);

HttpHeaders headers = new HttpHeaders();
headers.add("Authorization", "Basic " + base64Creds);

         */
/*        StringBuilder plainCreds = new StringBuilder();
        plainCreds.append(userName);
        plainCreds.append(":");
        plainCreds.append(password);

        StringBuilder authStr = new StringBuilder();
        authStr.append("Basic ");
        authStr.append(Base64.encodeToString(plainCreds.toString().getBytes(), Base64.URL_SAFE));

        System.out.println("[DEBUG] AuthStr: "+ authStr.toString());*/

        authHeader = new HttpBasicAuthentication(userName, password);
        requestHeaders = new HttpHeaders();
        requestHeaders.setAuthorization(authHeader);
        //requestHeaders.add("Authorization", authStr.toString());
        requestHeaders.setAccept(Collections.singletonList(mediaType));

        return requestHeaders;
    }

    private static void initHttpCredentials(String userName, String password, MediaType mediaType)
    {
        if(httpEntity == null)
            httpEntity = new HttpEntity<Object>(getRequestHeader(userName, password, mediaType));
    }

    private static HttpEntity<Object> getHttpEntity()
    {
        return httpEntity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

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
                // ...
            }
        };

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView,
                                          int id,
                                          KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
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

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
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
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {

            new UserLoginTask(email, password).execute();
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
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
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i,
                                         Bundle bundle) {
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
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader,
                               Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email,
                      String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                final String genUUID = UUID.randomUUID().toString();
                final String UrlRegToken = DEV_BASE_ADDRESS + "/rpisec" + ClientRestConstants.URI_REGISTER + "?uuid=" + genUUID;
                final String UrlToken = DEV_BASE_ADDRESS + "/rpisec" + ClientRestConstants.URI_GET_TOKEN + "?uuid=" + genUUID;
                final String UrlFCMToken = DEV_BASE_ADDRESS + "/rpisec" + ClientRestConstants.URI_REGISTER_FCM_TOKEN + "?uuid=" + genUUID + "&fcmToken=";
                final String UrlPostNotify = DEV_BASE_ADDRESS + "/rpisec" + ClientRestConstants.BASE_URI + "/notify";

                initHttpCredentials("admin", mPassword, MediaType.ALL);

                RestTemplate restTemplate = new RestTemplate();

                System.out.println("[DEBUG]: " + UrlToken);

                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                ResponseEntity<String> registerResponse = restTemplate.exchange(UrlRegToken, HttpMethod.PUT, getHttpEntity(), String.class);

                if (registerResponse.getStatusCode() == HttpStatus.OK) {

                    ResponseEntity<TokenResponse> response = restTemplate.exchange(UrlToken, HttpMethod.GET, getHttpEntity(), TokenResponse.class);

                    if (response.getBody() != null) {
                        ResponseEntity<FirebaseMessage> fm = restTemplate.exchange(UrlFCMToken + response.getBody().getToken(), HttpMethod.GET, getHttpEntity(), FirebaseMessage.class);
                        if (fm != null) {
                            MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
                            parts.add("msg", "TestMessage");

                            ResponseEntity<String> postResponse = restTemplate.exchange(UrlPostNotify, HttpMethod.POST, getHttpEntity(), String.class);

                            if (postResponse.getBody() != null && !postResponse.getBody().isEmpty()) {
                                Log.w(DEBUG_LOGIN_TAG, "postForObject:notnull");
                                //Toast.makeText(LoginActivity.this, "postForObject:notnull", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.w(DEBUG_LOGIN_TAG, "postForObject:null");
                                //Toast.makeText(LoginActivity.this, "postForObject:null", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.w(DEBUG_LOGIN_TAG, "getForObjectFM:null");
                            //Toast.makeText(LoginActivity.this, "getForObjectFM:null", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.w(DEBUG_LOGIN_TAG, "getForObjectToken:null");
                        //Toast.makeText(LoginActivity.this, "getForObjectToken:null", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            catch(HttpClientErrorException e){
                    System.out.println(e.getStatusCode());
                    System.out.println(e.getResponseBodyAsString());
                    e.printStackTrace();
                }
            catch(HttpServerErrorException e){
                    System.out.println(e.getStatusCode());
                    System.out.println(e.getResponseBodyAsString());
                    System.out.println(e.getResponseHeaders());
                    e.printStackTrace();
                }
            catch(Exception e){
                    e.printStackTrace();
                }

            mAuth.signInWithEmailAndPassword(mEmail, mPassword)
                    .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(DEBUG_LOGIN_TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                            if (!task.isSuccessful()) {
                                Log.w(DEBUG_LOGIN_TAG, "signInWithEmail:failed", task.getException());
                                Toast.makeText(LoginActivity.this, "signInWithEmail:failed", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d(DEBUG_LOGIN_TAG, "signInWithEmail:success");
                                Toast.makeText(LoginActivity.this, "signInWithEmail:success", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

