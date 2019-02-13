package com.reynalddev.firebasedatabaseui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
public class MainActivity extends AppCompatActivity {

    //initiate all object
    Toolbar toolbar;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    RecyclerView rv;
    FirebaseRecyclerOptions<UserData> options;
    FirebaseRecyclerAdapter<UserData,UserDataRvHolder> adapter;
    UserData selectedUserData;
    String selectedKey;
    FloatingActionButton delButton, cancelButton;
    Byte deleteOPT;
    ArrayList <String> deleteSelected;
    Integer delCount = 0;
    Integer itemCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);
        //fill variable with object ands sync it with the Id
        delButton = (FloatingActionButton) findViewById(R.id.FabDel);
        cancelButton = (FloatingActionButton) findViewById(R.id.FabCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backProp();
            }
        });
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Firebase UI");
        toolbar.setSubtitle("User Data List");
        toolbar.inflateMenu(R.menu.main_menu);
        rv = (RecyclerView)findViewById(R.id.UserRv);
        deleteSelected = new ArrayList<String>();
        deleteOPT = 0;
        rv.setLayoutManager(new LinearLayoutManager(this));


        displayRv();

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteSelected.size()!=0){
                    for (String key : deleteSelected){
                        databaseReference.child(key).removeValue().addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    Toast.makeText(MainActivity.this, "Delete Succses", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                    backProp();
                }else {
                    Toast.makeText(MainActivity.this, "No Item Checked", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.addMenu :
                intent = new Intent(MainActivity.this, AddDataActivity.class);
                startActivity(intent);
                break;
            case R.id.delMenu :
                deleteAction();
                adapter.notifyDataSetChanged();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("RestrictedApi")
        private void deleteAction() {
        switch (deleteOPT){
            case 0 :
                itemCount = adapter.getItemCount();
                deleteOPT = 1;
                toolbar.getMenu().clear();
                toolbar.setTitle("Delete Items");
                toolbar.setSubtitle(delCount.toString()+"/"+itemCount.toString());
                delButton.setVisibility(View.VISIBLE);
                cancelButton.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
                break;
            case 1 :
                deleteOPT = 0;
                break;
        }
    }

    private void displayRv() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("UserData");


        options = new FirebaseRecyclerOptions.Builder<UserData>()
                    .setQuery(databaseReference,UserData.class)
                    .build();

        adapter = new FirebaseRecyclerAdapter<UserData, UserDataRvHolder>(options) {
                    @SuppressLint("RestrictedApi")
                    @Override
                    protected void onBindViewHolder(@NonNull final UserDataRvHolder holder, final int position, @NonNull final UserData model) {
                        holder.emailview.setText(model.getEmail());
                        holder.nameview.setText(model.getName());
                        holder.checkBox.setChecked(false);
                        if (deleteOPT == 0){
                            holder.checkBox.setVisibility(View.GONE);
                            deleteSelected.clear();
                            delCount = 0;
                            delButton.setVisibility(View.GONE);
                            cancelButton.setVisibility(View.GONE);
                        }else {
                            holder.checkBox.setVisibility(View.VISIBLE);
                            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    selectedKey = getSnapshots().getSnapshot(position).getKey();
                                    if (deleteSelected.contains(selectedKey)){
                                        deleteSelected.remove(selectedKey);
                                    }else {
                                        deleteSelected.add(selectedKey);
                                    }
                                    delCount = deleteSelected.size();
                                    toolbar.setSubtitle(delCount.toString()+"/"+itemCount.toString());
                                }
                            });
                        }

                        holder.setUserItemClickListenner(new UserItemClickListenner() {
                            @Override
                            public void onClick(View view, int position) {
                                selectedUserData = model;
                                selectedKey = getSnapshots().getSnapshot(position).getKey();
                                Intent intentForUpdate = new Intent(MainActivity.this, UpdateDataActivity.class );
                                intentForUpdate.putExtra("Key",selectedKey);
                                intentForUpdate.putExtra("Name",selectedUserData.getName());
                                intentForUpdate.putExtra("Email",selectedUserData.getEmail());
                                intentForUpdate.putExtra("DOB",selectedUserData.getDob());
                                intentForUpdate.putExtra("PWD",selectedUserData.getPwd());
                                intentForUpdate.putExtra("Gender",selectedUserData.getGender());
                                startActivity(intentForUpdate);

                            }
                        });


                    }

                    @NonNull
                    @Override
                    public UserDataRvHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View itemview = LayoutInflater.from(getBaseContext()).inflate(R.layout.userdata_rv_item, viewGroup, false);
                        return new UserDataRvHolder(itemview);
                    }
                };

        adapter.startListening();
        rv.setAdapter(adapter);

    }


    @SuppressLint("RestrictedApi")
    public void backProp(){
        deleteOPT = 0;
        adapter.notifyDataSetChanged();
        toolbar.inflateMenu(R.menu.main_menu);
        getSupportActionBar().setTitle("Firebase UI");
        toolbar.setSubtitle("User Data List");
        deleteSelected.clear();
        delCount = 0;
        delButton.setVisibility(View.GONE);
        cancelButton.setVisibility(View.GONE);
    }




    @Override
    public void onBackPressed() {
        if (deleteOPT==1){
            backProp();
        }else { super.onBackPressed();}
    }
}
