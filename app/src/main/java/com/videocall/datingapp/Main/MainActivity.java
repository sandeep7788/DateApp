package com.videocall.datingapp.Main;

import android.Manifest;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import  androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sinch.android.rtc.SinchError;
import com.squareup.picasso.Picasso;
import com.videocall.datingapp.R;
import com.videocall.datingapp.Start.LoginActivity;
import com.videocall.datingapp.Start.RemindActivity;
import com.videocall.datingapp.Start.StartActivity;
import com.videocall.datingapp.sinchex.SinchBaseActivity;
import com.videocall.datingapp.sinchex.SinchServiceNew;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends SinchBaseActivity implements SinchServiceNew.StartFailedListener {

    CircleImageView circleImageViewProfileAvatar;
    ViewPager viewPager;
    MainAdapter adapter;
    HashMap<Integer, String> map = new HashMap<>();
    ArrayList<Integer> arrayInteger = new ArrayList<Integer>();
    NotificationManager notificationManagers;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 101;

    public void requestRead() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            readFile();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readFile();
            } else {
                // Permission Denied
                Toast.makeText(MainActivity.this, "Permission Denied to go setting and apply it", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    public void readFile() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        View viewTab0 = getLayoutInflater().inflate(R.layout.main_tab_icon, null);
        ImageView tabIcon0 = viewTab0.findViewById(R.id.tab_icon);
//        Picasso.get().load(R.drawable.tab_profile_off).into(tabIcon0);
        Application.setImage(R.drawable.tab_profile_off,tabIcon0);
        tabLayout.addTab(tabLayout.newTab().setCustomView(viewTab0));




        View viewTab1 = getLayoutInflater().inflate(R.layout.main_tab_icon, null);
        ImageView tabIcon1 = viewTab1.findViewById(R.id.tab_icon);
//        Picasso.get().load(R.drawable.tab_users_off).into(tabIcon1);
        Application.setImage(R.drawable.tab_users_off,tabIcon1);
        tabLayout.addTab(tabLayout.newTab().setCustomView(viewTab1));

        View viewTab2 = getLayoutInflater().inflate(R.layout.main_tab_icon, null);
        ImageView tabIcon2 = viewTab2.findViewById(R.id.tab_icon);
//        Picasso.get().load(R.drawable.tab_swipe_off).into(tabIcon2);
        Application.setImage(R.drawable.tab_swipe_off,tabIcon2);
        tabLayout.addTab(tabLayout.newTab().setCustomView(viewTab2));

        View viewTab3 = getLayoutInflater().inflate(R.layout.main_tab_icon, null);
        ImageView tabIcon3 = viewTab3.findViewById(R.id.tab_icon);
//        Picasso.get().load(R.drawable.tab_chat_off).into(tabIcon3);
        Application.setImage(R.drawable.tab_chat_off,tabIcon3);
        tabLayout.addTab(tabLayout.newTab().setCustomView(viewTab3));

        View viewTab4 = getLayoutInflater().inflate(R.layout.main_tab_icon, null);
        ImageView tabIcon4 = viewTab4.findViewById(R.id.tab_icon);
//        Picasso.get().load(R.drawable.tab_extra_off).into(tabIcon4);
        Application.setImage(R.drawable.tab_extra_off,tabIcon4);
        tabLayout.addTab(tabLayout.newTab().setCustomView(viewTab4));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        adapter = new MainAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);


        arrayInteger.add(11111);
        map.put(0, "hello");


        UserSettings("show_feeds", "yes");
        UserSettings("show_profile", "yes");
        UserSettings("show_status", "yes");

        UserSettings("share_location", "yes");
        UserSettings("share_birthage", "yes");
        UserSettings("share_visits", "yes");

        UserSettings("user_verified", "no");

//        UserSettings("user_premium", "no");
        UserSettings("call_counter", "0");

        UserSettings("user_mobile", "+000000000000");


        UserSettings("alert_match", "yes");
        UserSettings("alert_chats", "yes");
        UserSettings("alert_likes", "yes");
        UserSettings("alert_super", "yes");
        UserSettings("alert_visits", "yes");
        UserSettings("alert_follows", "yes");


        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());


                if (tab.getPosition() == 0) {
//                    Picasso.get().load(R.drawable.tab_profile_on).placeholder(R.drawable.tab_profile_off).into(tabIcon0);
                    Application.setImage(R.drawable.tab_profile_on,tabIcon0);
                }
                if (tab.getPosition() == 1) {
//                    Picasso.get().load(R.drawable.tab_users_on).placeholder(R.drawable.tab_users_off).into(tabIcon1);
                    Application.setImage(R.drawable.tab_users_on,tabIcon1);
                }
                if (tab.getPosition() == 2) {
//                    Picasso.get().load(R.drawable.tab_swipe_on).placeholder(R.drawable.tab_swipe_off).into(tabIcon2);
                    Application.setImage(R.drawable.tab_swipe_on,tabIcon2);
                }
                if (tab.getPosition() == 3) {
//                    Picasso.get().load(R.drawable.tab_chat_on).placeholder(R.drawable.tab_chat_off).into(tabIcon3);
                    Application.setImage(R.drawable.tab_chat_on,tabIcon3);
                }
                if (tab.getPosition() == 4) {
//                    Picasso.get().load(R.drawable.tab_extra_on).placeholder(R.drawable.tab_extra_off).into(tabIcon4);
                    Application.setImage(R.drawable.tab_extra_on,tabIcon4);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {


                if (tab.getPosition() == 0) {
//                    Picasso.get().load(R.drawable.tab_profile_off).placeholder(R.drawable.tab_profile_on).into(tabIcon0);
                    Application.setImage(R.drawable.tab_profile_off,tabIcon0);
                }
                if (tab.getPosition() == 1) {
//                    Picasso.get().load(R.drawable.tab_users_off).placeholder(R.drawable.tab_users_on).into(tabIcon1);
                    Application.setImage(R.drawable.tab_users_off,tabIcon1);
                }
                if (tab.getPosition() == 2) {
//                    Picasso.get().load(R.drawable.tab_swipe_off).placeholder(R.drawable.tab_swipe_on).into(tabIcon2);
                    Application.setImage(R.drawable.tab_swipe_off,tabIcon2);
                }
                if (tab.getPosition() == 3) {
//                    Picasso.get().load(R.drawable.tab_chat_off).placeholder(R.drawable.tab_chat_on).into(tabIcon3);
                    Application.setImage(R.drawable.tab_chat_off,tabIcon3);
                }
                if (tab.getPosition() == 4) {
//                    Picasso.get().load(R.drawable.tab_extra_off).placeholder(R.drawable.tab_extra_on).into(tabIcon4);
                    Application.setImage(R.drawable.tab_extra_off,tabIcon4);
                }

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


        tabLayout.getTabAt(2).select();

        String tabShow;
        tabShow = getIntent().getStringExtra("tab_show");
        if (tabShow != null) {
            switch (tabShow) {
                case "tab_profile":
                    tabLayout.getTabAt(0).select();
                    break;
                case "tab_users":
                    tabLayout.getTabAt(1).select();
                    break;
                case "tab_swipe":
                    tabLayout.getTabAt(2).select();
                    break;
                case "tab_chats":
                    tabLayout.getTabAt(3).select();
                    break;
                case "tab_feeds":
                    tabLayout.getTabAt(4).select();
                    break;
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.main_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarToolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(6);


        requestRead();

    }


    private void UserSettings(String userString, String userCheck) {

        if (firebaseUser != null) {

            String currentUser = firebaseUser.getUid();
            firebaseFirestore.collection("users")
                    .document(currentUser)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            String user_setting = task.getResult().getString(userString);
                            if (user_setting == null) {
                                Map<String, Object> mapUserFeeds = new HashMap<>();
                                mapUserFeeds.put(userString, userCheck);
                                firebaseFirestore.collection("users")
                                        .document(currentUser)
                                        .update(mapUserFeeds);
                            }
                        }
                    });
        }
    }

    private void OnlineUser() {
        String currentUser = firebaseUser.getUid();

        Map<String, Object> arrayOnlineUser = new HashMap<>();
        arrayOnlineUser.put("user_online", Timestamp.now());

        firebaseFirestore.collection("users")
                .document(currentUser)
                .update(arrayOnlineUser);
    }


    @Override
    protected void onStart() {
        super.onStart();

        try {

            firebaseAuth = FirebaseAuth.getInstance();
            firebaseUser = firebaseAuth.getCurrentUser();


            Application.appRunning = true;


            if (firebaseUser == null) {

                Intent intent = new Intent(MainActivity.this, StartActivity.class);
                startActivity(intent);
                finish();


            } else {


                String currentUser = firebaseUser.getUid();

                firebaseFirestore.collection("users")
                        .document(currentUser)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {

                                    String user_dummy = task.getResult().getString("user_dummy");

                                    if (user_dummy != null && user_dummy.equals("yes")) {

                                        Intent intent = new Intent(MainActivity.this, RemindActivity.class);
                                        intent.putExtra("user_uid", currentUser);
                                        startActivity(intent);
                                        finish();

                                    }
                                }
                            }
                        });

            }
        }catch (Exception e){
            Intent intent = new Intent(MainActivity.this, Splash_act.class);
            startActivity(intent);
            finish();
        }
    }


    private void UserStatus(String status) {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            String currentUser = firebaseUser.getUid();

            Map<String, Object> arrayUserStatus = new HashMap<>();
            arrayUserStatus.put("user_status", status);

            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            firebaseFirestore.collection("users")
                    .document(currentUser)
                    .update(arrayUserStatus);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            UserStatus("online");
            OnlineUser();

            if (Application.notificationManagerCompat != null) {
                Application.notificationManagerCompat.cancelAll();
            }
        }


    }

    @Override
    protected void onPause() {
        super.onPause();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            UserStatus("offline");
            Application.appRunning = false;
        }

    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    ///////////// sich services ///////////////////////////

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        super.onServiceConnected(componentName, iBinder);
        getSinchServiceInterface().setStartListener(this);


        if (!getSinchServiceInterface().isStarted()) {
            getSinchServiceInterface().startClient();
        }


        String username=getSinchServiceInterface().getUserName();
       // Toast.makeText(this, "connect to "+username, Toast.LENGTH_SHORT).show();
        Log.e("sinch callact : ","on service started");

    }

    @Override
    public void onStartFailed(SinchError error) {
        Log.e("sinch err : ","start failed : "+error.getMessage());
        startService(new Intent(this,SinchServiceNew.class));
        Toast.makeText(this,"err : "+ error.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStarted() {
        //   openPlaceCallActivity();
        Log.e("call on start ","onstarted ");
    }
}

