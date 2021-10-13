package com.videocall.datingapp.Profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;
import com.videocall.CallList;
import com.videocall.CallListAdmin;
import com.videocall.datingapp.Accounts.AccountsActivity;
import com.videocall.datingapp.R;
import com.videocall.datingapp.Settings.SettingsActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {


    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    String currentUser;

    CircleImageView imageViewProfileTabUsernameImage;
    CircleImageView imageViewProfileTabSettingsImage;
    CircleImageView imageViewProfileTabAccountImage;
    CircleImageView imageViewProfileTabPrivacyImage;

    TextView textViewProfileTabUsernameText;
    TextView textViewProfileTabLocationText;
    TextView textViewProfileTabSettingsText;
    TextView textViewProfileTabAccountText;
    TextView textViewProfileTabPrivacyText;
    TextView textViewProfileUuId;
    Button txtTotalCalls;
    Button txtTotalCallsAdmin;



    String call_counter="";

    private void getCounterAndUpdate() {

try {
    firebaseFirestore.collection("users")
            .document(currentUser)
            .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    if (documentSnapshot != null) {

                        call_counter = documentSnapshot.getString("call_counter");
                        if (call_counter!=null&&call_counter.isEmpty())
                            call_counter="0";
                        else
                            if (txtTotalCalls !=null && call_counter !=null)
                                txtTotalCalls.setText("Total calls: "+call_counter.toString());


                    }
                }
            });
}catch (Exception e) {

}


    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        imageViewProfileTabUsernameImage = view.findViewById(R.id.imageViewProfileTabUsernameImage);
        imageViewProfileTabSettingsImage = view.findViewById(R.id.imageViewProfileTabSettingsImage);
        imageViewProfileTabAccountImage = view.findViewById(R.id.imageViewProfileTabAccountImage);
        imageViewProfileTabPrivacyImage = view.findViewById(R.id.imageViewProfileTabPrivacyImage);

        textViewProfileTabUsernameText = view.findViewById(R.id.textViewProfileTabUsernameText);
        textViewProfileTabLocationText = view.findViewById(R.id.textViewProfileTabLocationText);
        textViewProfileTabSettingsText = view.findViewById(R.id.textViewProfileTabSettingsText);
        textViewProfileTabAccountText = view.findViewById(R.id.textViewProfileTabAccountText);
        textViewProfileTabPrivacyText = view.findViewById(R.id.textViewProfileTabPrivacyText);
        textViewProfileUuId = view.findViewById(R.id.textViewProfileUuId);
        txtTotalCalls = view.findViewById(R.id.txtTotalCalls);
        txtTotalCallsAdmin = view.findViewById(R.id.txtTotalCallsAdmin);

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

                                String userImage = documentSnapshot.getString("user_image");

                                String user_name = documentSnapshot.getString("user_name");
                                String[] splitUserName = user_name.split(" ");
                                String user_birthage = documentSnapshot.getString("user_birthage");

                                textViewProfileTabUsernameText.setText(splitUserName[0] + ", " + user_birthage);

                                if (userImage.equals("image")) {
                                    imageViewProfileTabUsernameImage.setImageResource(R.drawable.profile_image);
                                } else {
                                    Picasso.get().load(userImage).into(imageViewProfileTabUsernameImage);
                                }

                                String user_city = documentSnapshot.getString("user_city");
                                String user_state = documentSnapshot.getString("user_state");
                                String user_country = documentSnapshot.getString("user_country");

                                textViewProfileTabLocationText.setText(user_city + ", " + user_state + ", " + user_country);

                            }
                        }
                    });
//            getCounterAndUpdate();

            if (currentUser!=null)
                textViewProfileUuId.setText("Id: "+currentUser);
        }



        imageViewProfileTabUsernameImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                intent.putExtra("user_uid", currentUser);
                startActivity(intent);
            }
        });


        imageViewProfileTabSettingsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        imageViewProfileTabAccountImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AccountsActivity.class);
                startActivity(intent);
            }
        });

        imageViewProfileTabPrivacyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProfileEditActivity.class);
                startActivity(intent);
            }
        });


        txtTotalCalls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().startActivity(new Intent(getContext(), CallList.class));
            }
        });

        prefs = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);

        if (prefs.getBoolean("isAdmin",false)){
            txtTotalCallsAdmin.setVisibility(View.VISIBLE);
        }

        txtTotalCallsAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().startActivity(new Intent(getContext(), CallListAdmin.class));
            }
        });

        return view;


    }
    SharedPreferences prefs;
}
