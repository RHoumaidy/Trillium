package com.example.raafat.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.MultiSelectListPreference;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raafat.model.DestinationSite;
import com.example.raafat.model.Driver;
import com.example.raafat.model.OrginSite;
import com.example.raafat.model.Truck;
import com.example.raafat.model.TruckDrivers;
import com.example.raafat.model.User;
import com.example.raafat.trillium.MyApplication;
import com.example.raafat.trillium.Permission;
import com.example.raafat.trillium.R;
import com.squareup.picasso.Picasso;

import java.nio.channels.NotYetBoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends ActionBarActivity {


    private EditText userNameEditText;
    private EditText passWordEditText;
    private Button logInButton;
    private ImageView logoImageView;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        View view = LayoutInflater.from(this).inflate(R.layout.action_bar_layout,null);
        ((TextView)view.findViewById(R.id.actionBarDate)).setText(MyApplication.sdf3.format(System.currentTimeMillis()));

        actionBar.setCustomView(view);

        userNameEditText = (EditText) findViewById(R.id.userNameEtv);
        passWordEditText = (EditText) findViewById(R.id.passwordEtv);
        logInButton = (Button) findViewById(R.id.longinBtn);
        logoImageView = (ImageView) findViewById(R.id.iconImageView);

        Picasso.with(this)
                .load("http://trilliumholding.com.lb/th/images/mh_top_menu_logo.png")
                .placeholder(R.drawable.ic_launcher)
                .error(android.R.drawable.stat_notify_error)
                .into(logoImageView);


        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        });

        passWordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if((event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) ||
                        (actionId == EditorInfo.IME_ACTION_NEXT)||
                        (actionId == EditorInfo.IME_ACTION_DONE)||
                        (actionId == EditorInfo.IME_ACTION_GO)){

                    logIn();

                }

                return false;
            }
        });

    }


    private void logIn(){
        if (userNameEditText.getText().length() > 0) {
            if (passWordEditText.getText().length() > 0) {
                String userName = String.valueOf(userNameEditText.getText());
                String password = String.valueOf(passWordEditText.getText());

                final User user = User.isCorrect(userName, password, MyApplication.db);
                if (user == null)
                    Toast.makeText(LoginActivity.this, "incorrect info ", Toast.LENGTH_SHORT).show();

                else {

                    dialog = new Dialog(LoginActivity.this);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setContentView(R.layout.dialog_choose_driver_layout);
                    dialog.setTitle("Choose your Roll");
                    ListView lv = (ListView) dialog.findViewById(R.id.diaogSelectDriverLV);

                    final List<Permission> permissions = user.getUserPermissions();
                    List<String> userPermissions = new ArrayList<>();
                    for(Permission p : permissions)
                    userPermissions.add(p.getName());

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(LoginActivity.this,
                            android.R.layout.simple_list_item_checked,
                            userPermissions);
                    lv.setAdapter(adapter);

                    dialog.show();

                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent nextActivity = null;
                            Permission permission = permissions.get(position);
                            nextActivity = new Intent(LoginActivity.this,permission.getIntent());
                            if (nextActivity != null) {
                                LoginActivity.this.finish();
                                startActivity(nextActivity);
                                MyApplication.sp.edit()
                                        .putLong(User.COL_ID, user.getUserId())
                                        .putString(User.COL_USER_NAME, user.getUserName())
                                        .apply();
                            }
                            dialog.dismiss();

                        }
                    });


                }

            } else {
                Toast.makeText(LoginActivity.this, "please enter the password ", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(LoginActivity.this, "Please enter the User name", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_exit) {
            this.finish();
            System.exit(0);
            return true;
        }else if(id == R.id.action_sync){
            MyApplication.doneProcess = 0;
            MyApplication.currContext = this;
            MyApplication.showDialog("Downloading Data ...");
            Driver.getAllDriverFromUrl();
            User.getAllDriverFromUrl();
            Truck.getAllDriverFromUrl();
            OrginSite.getAllDriverFromUrl();
            DestinationSite.getAllDriverFromUrl();
            TruckDrivers.getAllDriverFromUrl();

        }

        return super.onOptionsItemSelected(item);
    }

    private boolean backPressed = false;

    @Override
    public void onBackPressed() {

        if(!backPressed) {
            backPressed = true;
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    backPressed = false;
                }
            }, 3500);
            Toast.makeText(this, ("Press again to exit "), Toast.LENGTH_LONG).show();
        }else
            super.onBackPressed();
    }


}
