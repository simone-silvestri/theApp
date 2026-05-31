package com.simone.cfts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = "SignInActivity";
    private static final int RC_GOOGLE_SIGN_IN = 9001;

    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleClient;

    private LinearLayout signedOutPanel;
    private LinearLayout signedInPanel;
    private TextView signedInEmail;
    private TextView signedInStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        signedOutPanel = findViewById(R.id.signedOutPanel);
        signedInPanel  = findViewById(R.id.signedInPanel);
        signedInEmail  = findViewById(R.id.signedInEmail);
        signedInStatus = findViewById(R.id.signedInStatus);

        boolean firebaseReady = !FirebaseApp.getApps(this).isEmpty();
        if (!firebaseReady) {
            Toast.makeText(this,
                    "Firebase not configured — drop google-services.json into app/ and apply the plugin",
                    Toast.LENGTH_LONG).show();
            renderState();
            return;
        }
        firebaseAuth = FirebaseAuth.getInstance();

        String webClientId = resolveWebClientId();
        if (webClientId == null) {
            Toast.makeText(this,
                    "Google Sign-In not configured — drop google-services.json into app/",
                    Toast.LENGTH_LONG).show();
        } else {
            GoogleSignInOptions options = new GoogleSignInOptions.Builder(
                    GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(webClientId)
                    .requestEmail()
                    .build();
            googleClient = GoogleSignIn.getClient(this, options);
        }

        findViewById(R.id.btnGoogleSignIn).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { startGoogleSignIn(); }
        });
        findViewById(R.id.btnSignOut).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { signOut(); }
        });

        renderState();
    }

    public void closeSignIn(View v) { finish(); }

    private void startGoogleSignIn() {
        if (googleClient == null) return;
        Intent intent = googleClient.getSignInIntent();
        startActivityForResult(intent, RC_GOOGLE_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account == null || account.getIdToken() == null) return;
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                firebaseAuth.signInWithCredential(credential)
                        .addOnSuccessListener(result -> {
                            SyncManager.get(getApplicationContext()).onSignedIn(SignInActivity.this);
                            renderState();
                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "Firebase sign-in failed", e);
                            Toast.makeText(this, "Sign-in failed", Toast.LENGTH_SHORT).show();
                        });
            } catch (ApiException e) {
                Log.e(TAG, "Google sign-in failed " + e.getStatusCode(), e);
                Toast.makeText(this, "Google sign-in failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void signOut() {
        SyncManager.get(getApplicationContext()).onSignedOut();
        if (firebaseAuth != null) firebaseAuth.signOut();
        if (googleClient != null) googleClient.signOut();
        renderState();
    }

    private void renderState() {
        FirebaseUser user = firebaseAuth != null ? firebaseAuth.getCurrentUser() : null;
        if (user == null) {
            signedOutPanel.setVisibility(View.VISIBLE);
            signedInPanel.setVisibility(View.GONE);
        } else {
            signedOutPanel.setVisibility(View.GONE);
            signedInPanel.setVisibility(View.VISIBLE);
            signedInEmail.setText(user.getEmail() != null ? user.getEmail() : user.getDisplayName());
            signedInStatus.setText("Synced");
        }
    }

    /**
     * The google-services plugin generates a {@code default_web_client_id} string at build time
     * once {@code app/google-services.json} is present. We look it up via {@code getIdentifier}
     * so that the project still compiles before the plugin is applied.
     */
    private String resolveWebClientId() {
        int id = getResources().getIdentifier("default_web_client_id", "string", getPackageName());
        return id != 0 ? getString(id) : null;
    }
}
