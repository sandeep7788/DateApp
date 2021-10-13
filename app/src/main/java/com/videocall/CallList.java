package com.videocall;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.videocall.datingapp.CounterClass;
import com.videocall.datingapp.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CallList extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    String currentUser = "";
    private final ArrayList<CounterClass> listdata = new ArrayList();
    MyListAdapter adapter;
    Toolbar toolbarSettingsToolbar;
    ProgressBar ProgressBar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_call_list);

        toolbarSettingsToolbar = findViewById(R.id.toolbarSettingsToolbar);
        ProgressBar1 = findViewById(R.id.ProgressBar1);

        ProgressBar1.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbarSettingsToolbar);
        getSupportActionBar().setTitle("Users Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbarSettingsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        if (firebaseUser != null) {
            currentUser = firebaseUser.getUid();
        }

        try {


            firebaseFirestore.collection("users")
                    .document(currentUser)
                    .collection("calling_users")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task!=null){
                                for (QueryDocumentSnapshot querySnapshot : task.getResult()) {
                                    if (task.getResult()!=null){
                                        CounterClass mCounterClass = querySnapshot.toObject(CounterClass.class);
                                        listdata.add(mCounterClass);
                                    }

                                }
                            }


                            adapter.notifyDataSetChanged();


                            ProgressBar1.setVisibility(View.GONE);
                        }
                    });

            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            adapter = new MyListAdapter(listdata);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            Toast.makeText(this, "no data found.", Toast.LENGTH_SHORT).show();
            finish();
        }

    }
}

class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder> {
    private ArrayList<CounterClass> listdata = new ArrayList();

    // RecyclerView recyclerView;
    public MyListAdapter(ArrayList<CounterClass> listdata) {
        this.listdata = listdata;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CounterClass myListData = listdata.get(position);
        holder.textView.setText(myListData.getFinal_name());

        Picasso.get().load(myListData.getFinal_image_url()).error(R.drawable.profile_image).into(holder.imageView);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "id: " + myListData.getFinal_UUID(), Toast.LENGTH_LONG).show();
            }
        });
        holder.textViewSeriesNumber.setText(String.valueOf(position + 1));
        holder.textViewCounter.setText(myListData.getFinal_count_time());
        holder.textViewUUID.setText(myListData.getFinal_UUID());
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView imageView;
        public TextView textView;
        public TextView textViewSeriesNumber;
        public TextView textViewCounter;
        public TextView textViewUUID;
        public ConstraintLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.imageView);
            this.textView = itemView.findViewById(R.id.textView);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            textViewSeriesNumber = itemView.findViewById(R.id.textViewSeriesNumber);
            textViewCounter = itemView.findViewById(R.id.textViewCounter);
            textViewUUID = itemView.findViewById(R.id.textViewUUID);
        }
    }
}