package com.videocall.datingapp.sinchex;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.videocall.datingapp.R;


public abstract class SinchBaseActivity extends AppCompatActivity implements ServiceConnection {

    public static final String TAG = SinchBaseActivity.class.getSimpleName();
    private SinchServiceNew.SinchServiceInterface mSinchServiceInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApplicationContext().bindService(new Intent(this, SinchServiceNew.class), this,
                BIND_AUTO_CREATE);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        if (SinchServiceNew.class.getName().equals(componentName.getClassName())) {
            mSinchServiceInterface = (SinchServiceNew.SinchServiceInterface) iBinder;
            onServiceConnected();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        if (SinchServiceNew.class.getName().equals(componentName.getClassName())) {
            mSinchServiceInterface = null;
            onServiceDisconnected();
        }
    }

    protected void onServiceConnected() {
        // for subclasses
    }

    protected void onServiceDisconnected() {
        // for subclasses
    }

    protected SinchServiceNew.SinchServiceInterface getSinchServiceInterface() {
        return mSinchServiceInterface;
    }

    public void replaceFragment(int container, Fragment fragment, Boolean is_addtobackstack){
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(container ,fragment ,TAG);
      //  if (is_addtobackstack)
        //    fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void addFragment(int container, Fragment fragment, Boolean is_addtobackstack){
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(container ,fragment, TAG );
      //  if (is_addtobackstack)
       //     fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    AlertDialog purchaseDialog;
    AlertDialog mDialog;

    public void showAlertDialog(String message, Boolean cancelable, String positive, String negative, String neutral, DialogInterface.OnClickListener listener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(message);
        builder.setCancelable(cancelable);
        if (!TextUtils.isEmpty(positive)) {
            builder.setPositiveButton(positive, listener);
        }
        if (!TextUtils.isEmpty(negative)) {
            builder.setNegativeButton(negative, listener);
        }
        if (!TextUtils.isEmpty(neutral)) {
            builder.setNeutralButton(neutral, listener);
        }
        if (mDialog != null) {
            mDialog.dismiss();
        }
        mDialog = builder.create();

        mDialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                mDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(SinchBaseActivity.this,R.color.colorPrimary));
                mDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(SinchBaseActivity.this, R.color.colorPrimaryDark));
            }});
        mDialog.show();
    }

    public void hideDialog(){
        if (mDialog!=null)
            if(mDialog.isShowing())
        mDialog.dismiss();
    }


}
