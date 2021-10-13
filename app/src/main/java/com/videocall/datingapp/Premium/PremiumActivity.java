package com.videocall.datingapp.Premium;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;
import com.videocall.datingapp.Main.Application;
import com.videocall.datingapp.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PremiumActivity extends Activity implements PaymentResultListener  {

    LinearLayout oneMonth;
    LinearLayout threeMonth;
    LinearLayout sixMonth;
    private static final String TAG = "@@"+PremiumActivity.class.getSimpleName();
    int premiumMonth=0;
    private FirebaseAuth firebaseAuth;








    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.premium_activity);
        oneMonth=findViewById(R.id.oneMonth);
        threeMonth=findViewById(R.id.threeMonth);
        sixMonth=findViewById(R.id.sixMonth);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        /*
         To ensure faster loading of the Checkout form,
          call this method as early as possible in your checkout flow.
         */
        Checkout.preload(getApplicationContext());

        // Payment button created by you in XML layout


        oneMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                premiumMonth=1;
//                startPayment(199);
                startPayment(1);
            }
        });
        threeMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                premiumMonth=3;
                startPayment(499);
            }
        });
        sixMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                premiumMonth=6;
                startPayment(799);
            }
        });


    }
    String currentUser="";
    String user_name="";
    String user_email="";

    public void startPayment(int amount) {



        try{
            firebaseFirestore = FirebaseFirestore.getInstance();
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            if (firebaseUser != null) {

                currentUser = firebaseUser.getUid();

                firebaseFirestore.collection("users")
                        .document(currentUser)
                        .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                                if (documentSnapshot != null && documentSnapshot.exists()) {

                                    user_email = documentSnapshot.getString("user_email");
                                    user_name = documentSnapshot.getString("user_name");


                                }
                            }
                        });
            }
        }catch (Exception e){

        }


        final Activity activity = this;

        final Checkout co = new Checkout();


        try {
            amount=amount*100;

            JSONObject options = new JSONObject();
            options.put("name", user_name);
            options.put("description", "Demoing Charges");
            options.put("send_sms_hash",true);
            options.put("allow_rotation", true);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", String.valueOf(amount));

            JSONObject preFill = new JSONObject();
            preFill.put("email", user_email);
            preFill.put("contact", "");

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    /**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();

            if (premiumMonth==1){
                Log.e(TAG, "onPaymentSuccess: "+Application.oneMonthPremium());
                UserSettings("user_premium", Application.oneMonthPremium());
            }else if (premiumMonth == 3){
                Log.e(TAG, "onPaymentSuccess: "+Application.threeMonthPremium());
                UserSettings("user_premium", Application.threeMonthPremium());
            }else if (premiumMonth == 6){
                Log.e(TAG, "onPaymentSuccess: "+Application.sixMonthPremium());
                UserSettings("user_premium", Application.sixMonthPremium());
            }else {
                UserSettings("user_premium", Application.oneMonthPremium());
                Toast.makeText(this, "Payment Successful: " + razorpayPaymentID +"something wrong.", Toast.LENGTH_SHORT).show();

                finish();
            }

        } catch (Exception e) {

            if (premiumMonth==1){

                UserSettings("user_premium", Application.oneMonthPremium());
            }else if (premiumMonth == 3){

                UserSettings("user_premium", Application.threeMonthPremium());
            }else if (premiumMonth == 6){

                UserSettings("user_premium", Application.sixMonthPremium());
            }else {
                UserSettings("user_premium", Application.oneMonthPremium());
                Toast.makeText(this, "Payment Successful: " + razorpayPaymentID +"something wrong.", Toast.LENGTH_SHORT).show();
            }
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;

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
                            if (user_setting != null && user_setting.equalsIgnoreCase("no")) {
                                Map<String, Object> mapUserFeeds = new HashMap<>();
                                mapUserFeeds.put(userString, userCheck);
                                firebaseFirestore.collection("users")
                                        .document(currentUser)
                                        .update(mapUserFeeds);
                            }else if (user_setting != null && !user_setting.equalsIgnoreCase("no")){
                                try {
                                    System.out.println(monthsBetween(sdf.parse(Application.getCurrentDateAndTime()), sdf.parse(user_setting)));

                                     premiumMonth = premiumMonth + monthsBetween(sdf.parse(Application.getCurrentDateAndTime()), sdf.parse(user_setting));

                                    Map<String, Object> mapUserFeeds = new HashMap<>();
                                    mapUserFeeds.put(userString, userCheck);
                                    firebaseFirestore.collection("users")
                                            .document(currentUser)
                                            .update(mapUserFeeds);

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    });
        }
    }
    String start = "01/01/2012";
    String end = "31/07/2014";
    DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    static int monthsBetween(Date a, Date b) {
        Calendar cal = Calendar.getInstance();
        if (a.before(b)) {
            cal.setTime(a);
        } else {
            cal.setTime(b);
            b = a;
        }
        int c = 0;
        while (cal.getTime().before(b)) {
            cal.add(Calendar.MONTH, 1);
            c++;
        }
        return c - 1;
    }
    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }
}
