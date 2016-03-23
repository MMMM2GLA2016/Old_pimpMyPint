package gla.istic.pimpmypint;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private TextView mLoggedInStatusTextView;
    private ProgressDialog mAuthProgressDialog;
    private Firebase mFirebaseRef;
    private AuthData mAuthData;
    private Firebase.AuthStateListener mAuthStateListener;

    private Button mPasswordLoginButton;
    private Button mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mPasswordLoginButton = (Button) findViewById(R.id.sign_in_button);
        mRegisterButton = (Button) findViewById(R.id.sign_up_button);

        mLoggedInStatusTextView = (TextView) findViewById(R.id.login_status);

        mFirebaseRef = new Firebase(getResources().getString(R.string.firebase_url));

        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle("Loading");
        mAuthProgressDialog.setMessage("Authenticating with Firebase...");
        mAuthProgressDialog.setCancelable(false);
        mAuthProgressDialog.show();

        mAuthStateListener = new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                mAuthProgressDialog.hide();
                setAuthenticatedUser(authData);
            }
        };

        mFirebaseRef.addAuthStateListener(mAuthStateListener);
    }

    private void setAuthenticatedUser(AuthData authData) {
        if (authData != null) {
            mPasswordLoginButton.setVisibility(View.GONE);
            mLoggedInStatusTextView.setVisibility(View.VISIBLE);
            String name = null;
            if (authData.getProvider().equals("password")) {
                name = authData.getUid();
            } else {
                Log.e(TAG, "Invalid provider: " + authData.getProvider());
            }
            if (name != null) {
                mLoggedInStatusTextView.setText("Logged in as " + name + " (" + authData.getProvider() + ")");
            }
        } else {

            mPasswordLoginButton.setVisibility(View.VISIBLE);
            mLoggedInStatusTextView.setVisibility(View.GONE);
        }
        this.mAuthData = authData;

        supportInvalidateOptionsMenu();
    }

    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private class AuthResultHandler implements Firebase.AuthResultHandler {

        private final String provider;

        public AuthResultHandler(String provider) {
            this.provider = provider;
        }

        @Override
        public void onAuthenticated(AuthData authData) {
            mAuthProgressDialog.hide();
            Log.i(TAG, provider + " auth successful");
            setAuthenticatedUser(authData);
        }

        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            mAuthProgressDialog.hide();
            showErrorDialog(firebaseError.toString());
        }
    }

    public void signIn() {
        mAuthProgressDialog.show();
        TextView emailTV= (TextView) findViewById(R.id.email);
        String email= (String) (emailTV != null ? emailTV.getText() : null);

        TextView passwordTV = (TextView) findViewById(R.id.password);
        String password= (String) (passwordTV != null ? passwordTV.getText() : null);

        if (email != null && password != null) {
            mFirebaseRef.authWithPassword(
                    email,
                    password,
                    new AuthResultHandler("password")
            );
        } else {
            Log.i(TAG, "Error on credentials");
        }
    }

    public void signUp() {
        Intent intent = new Intent(new SignUpActivity(), SignUpActivity.class);
        startActivity(intent);
    }

}

