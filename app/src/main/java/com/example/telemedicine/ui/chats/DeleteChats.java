package com.example.telemedicine.ui.chats;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telemedicine.MainActivity;
import com.example.telemedicine.R;
import com.example.telemedicine.models.Chat;
import com.example.telemedicine.models.Doctor;
import com.example.telemedicine.ui.utilities.RecylerItemBtnDelete;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DeleteChats extends AppCompatActivity implements RecylerItemBtnDelete.OnReportClickListener{
    private String userId, chatList[];
    private DatabaseReference dbUser, dbDoctor, dbChat;
    private TextView header;
    private boolean isDoc = false;
    private RecyclerView rvChats;
    private RecyclerView.Adapter adaptChats;
    private RecyclerView.LayoutManager lmChats;
    private ArrayList<String> chatIds, listIds, listNames;
    private int index;
    private Button btn;
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dbDoctor = FirebaseDatabase.getInstance().getReference("Doctor");
        dbUser = FirebaseDatabase.getInstance().getReference("Users");
        dbChat = FirebaseDatabase.getInstance().getReference("Chat");
        setContentView(R.layout.activity_delete_chats);
        header = (TextView)findViewById(R.id.delete_chat_heading);
        btn = (Button)findViewById(R.id.button_delete_chat);
        btn.setClickable(false);
        btn.setBackgroundColor(Color.rgb(74,72,72));
        header.setText("Delete Chats");

        index = 0;
        listIds = new ArrayList<>();
        listNames = new ArrayList<>();
        chatIds = new ArrayList<>();
        dbDoctor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    Doctor d = child.getValue(Doctor.class);
                    if(userId.equalsIgnoreCase(d.getDocID()))isDoc = true;
                }
                if(isDoc){
                    chatIds.clear();
                    listNames.clear();
                    listIds.clear();
                    chatList = new String[1];
                    dbChat.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot child : dataSnapshot.getChildren()){
                                Chat c = child.getValue(Chat.class);
                                if(c.getDoctorId().equalsIgnoreCase(userId) && c.isStatus()){
                                    chatIds.add(c.getChatId());
                                    listNames.add(c.getPatientName());
                                    listIds.add(c.getPatientId());
                                }
                            }
                            if(listNames.size()>0){
                                chatList = listNames.toArray(new String[listNames.size()]);
                                populateRecyclerView();
                            }else{
                                chatList[0] = "No chats yet.";
                                populateRecyclerView();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else{
                    chatIds.clear();
                    listNames.clear();
                    listIds.clear();
                    chatList = new String[1];
                    dbChat.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot child : dataSnapshot.getChildren()){
                                Chat c = child.getValue(Chat.class);
                                if(c.getPatientId().equalsIgnoreCase(userId) && c.isStatus()){
                                    chatIds.add(c.getChatId());
                                    listNames.add(c.getDoctorName());
                                    listIds.add(c.getDoctorId());
                                }
                            }
                            if(listNames.size()>0){
                                chatList = listNames.toArray(new String[listNames.size()]);
                                populateRecyclerView();
                            }else{
                                chatList[0] = "No chats yet.";
                                populateRecyclerView();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void populateRecyclerView(){
        rvChats = (RecyclerView)findViewById(R.id.recycler_chat_delete);
        lmChats = new LinearLayoutManager(this);
        rvChats.setLayoutManager(lmChats);
        adaptChats = new RecylerItemBtnDelete(chatList, this);
        rvChats.setAdapter(adaptChats);
    }
    @Override
    public void OnReportClickListener(int pos){
        final int position = pos;
        btn.setClickable(true);
        btn.setBackgroundColor(Color.rgb(168, 56, 50));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbDoctor.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            Doctor d = child.getValue(Doctor.class);
                            if(d.getDocID().equalsIgnoreCase(userId)){
                                isDoc = true;
                            }
                        }
                        if(isDoc){
                            dbChat.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    ArrayList<Chat> chats = new ArrayList<>();
                                    long x = dataSnapshot.getChildrenCount();
                                    for(DataSnapshot child : dataSnapshot.getChildren()){
                                        x--;
                                        Chat c = child.getValue(Chat.class);
                                        if(c.getDoctorId().equalsIgnoreCase(userId)&&c.isStatus()){
                                            chats.add(c);
                                        }
                                        if(x==0){
                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                            chats.get(position).setStatus(false);
                                            reference.child("Chat/" + chats.get(position).getChatId()).setValue(chats.get(position));
                                            Intent i = new Intent(DeleteChats.this, MainActivity.class);
                                            startActivity(i);
                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }else{
                            dbChat.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    ArrayList<Chat> chats = new ArrayList<>();
                                    long x = dataSnapshot.getChildrenCount();
                                    for(DataSnapshot child : dataSnapshot.getChildren()){
                                        x--;
                                        Chat c = child.getValue(Chat.class);
                                        if(c.getPatientId().equalsIgnoreCase(userId)&&c.isStatus()){
                                            chats.add(c);
                                        }
                                        if(x==0){
                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                            reference.child("Chat/"+chats.get(position).getChatId()+"/status").setValue(false);
                                            Intent i = new Intent(DeleteChats.this, MainActivity.class);
                                            startActivity(i);

                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        isDoc = false;

    }
}
