package br.com.puc.facebookproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

/**
 * Created by Felipe on 12/10/2015.
 */
public class menu extends Activity {

    private CallbackManager mCallBackManager;
    private FacebookCallback<LoginResult> mCallback= new FacebookCallback<LoginResult>() {
        private ProfileTracker mProfileTracker;

        @Override
        public void onSuccess(LoginResult loginResult) {
            mProfileTracker = new ProfileTracker() {
                @Override
                protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                    Log.v("facebook - profile", profile2.getFirstName());
                    Log.v("facebook - profile", profile2.getName());
                    mProfileTracker.stopTracking();
                }
            };
            mProfileTracker.startTracking();

            //VOLTA AO LOGIN
            Intent i = new Intent(menu.this, MainActivity.class);
            startActivity(i);
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }
    };





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        mCallBackManager = CallbackManager.Factory.create();
        LoginButton logoutButton =  (LoginButton) findViewById(R.id.logout_button);
        //loginButton.setReadPermissions("user_friends");

        logoutButton.registerCallback(mCallBackManager, mCallback);

        clienteExiste();

    }

    private void clienteExiste() {
        String method="verificaCliente";
        Profile profile = Profile.getCurrentProfile();

        BackgroundTask backgroundTask = new BackgroundTask(getApplicationContext(),this);
        backgroundTask.execute(method, profile.getName());
    }

    public void onButtonClick(View v) {
        if (v.getId() == R.id.bShareOnFacebook) {
            Intent i = new Intent(menu.this, shareonfacebook.class);
            startActivity(i);
        }
        if (v.getId() == R.id.bShareLocation) {
            Intent i = new Intent(menu.this, sharelocationgps.class);
            startActivity(i);
        }
        if (v.getId() == R.id.bShareActivity) {
            Intent i = new Intent(menu.this, shareactivity.class);
            startActivity(i);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mCallBackManager.onActivityResult(requestCode, resultCode, data)) {
            return;
        }
    }

    public void faceLogout(View view) {
        LoginManager.getInstance().logOut();
        Intent i = new Intent(menu.this, MainActivity.class);
        startActivity(i);
    }

}
