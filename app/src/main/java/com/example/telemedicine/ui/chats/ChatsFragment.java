package com.example.telemedicine.ui.chats;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telemedicine.MainActivity;
import com.example.telemedicine.models.Doctor;
import com.example.telemedicine.ui.messaging.MessagesActivity;
import com.example.telemedicine.ui.utilities.RecyclerItemBtn;

import com.example.telemedicine.R;
import com.example.telemedicine.ui.video_call.video_call;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.example.telemedicine.models.Chat;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatsFragment extends Fragment implements RecyclerItemBtn.OnReportClickListener{
    private ChatsViewModel chatsViewModel;
    private DatabaseReference dbChats;
    private DatabaseReference dbUser;
    private DatabaseReference dbDoctor;
    private RecyclerView rvChats;
    private RecyclerView.Adapter adaptChats;
    private RecyclerView.LayoutManager lmChats;
    //this user id will be the patientId.
    private ArrayList<String> chatListIds;
    private ArrayList<String> doctorIds;
    private ArrayList<String> patientIds;
    private ArrayList<String> doctorNames;
    private ArrayList<String> patientNames;
    private String[] chatList;
    private int buttonCheck;
    private View root;
    private Button btn;
    private String userId;
    private boolean isDoctor;
    private Intent intent;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        chatsViewModel =
                ViewModelProviders.of(this).get(ChatsViewModel.class);

        root = inflater.inflate(R.layout.fragment_chatpage, container, false);
        final TextView textView = root.findViewById(R.id.chat_heading);
        dbChats = FirebaseDatabase.getInstance().getReference("Chat");
        dbUser = FirebaseDatabase.getInstance().getReference("Users");
        dbDoctor = FirebaseDatabase.getInstance().getReference("Doctor");
        //get the list of chats the user has.
        doctorIds = new ArrayList<>();
        chatListIds = new ArrayList<>();
        patientIds = new ArrayList<>();
        doctorNames = new ArrayList<>();
        patientNames = new ArrayList<>();
        chatList = new String[1];
        buttonCheck = 0;
        isDoctor = false;
        setHasOptionsMenu(true);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dbDoctor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot child: dataSnapshot.getChildren()){
                    Doctor d = child.getValue(Doctor.class);
                    if(d.getDocID().equalsIgnoreCase(userId)) isDoctor = true;
                }
                if(isDoctor){
                    textView.setText("Chat with your patient(s)");
                    dbChats.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            chatListIds.clear();
                            patientNames.clear();
                            patientIds.clear();
                            for(DataSnapshot child : dataSnapshot.getChildren()){
                                Chat c = child.getValue(Chat.class);
                                if(userId.equalsIgnoreCase(c.getDoctorId())&&c.isStatus()){
                                    chatListIds.add(c.getChatId());
                                    patientIds.add(c.getPatientId());
                                    patientNames.add(c.getPatientName());
                                }
                            }
                            if(patientNames.size()>0){
                                chatList = patientNames.toArray(new String[patientNames.size()]);
                            }else{
                                chatList[0] = "No Chats yet.";
                            }
                            populateRecyclers(root);
                            btn = (Button)root.findViewById(R.id.button_new_chat);
                            buttonCheck = 0;
                            dbUser.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot child : dataSnapshot.getChildren()){
                                        buttonCheck++;
                                    }
                                    System.out.println("PatientNames.Size:  " + patientNames.size());
                                    if(buttonCheck==patientNames.size()){
                                        btn.setBackgroundColor(getResources().getColor(R.color.unClickBtn));
                                        btn.setClickable(false);
                                    }else{
                                        btn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent i;
                                                i = new Intent(getContext(), ChatCreation.class);
                                                startActivity(i);
                                            }
                                        });
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }

                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else{
                    dbChats.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            chatListIds.clear();
                            doctorIds.clear();
                            doctorNames.clear();
                            for(DataSnapshot child : dataSnapshot.getChildren()){
                                Chat c = child.getValue(Chat.class);
                                if(userId.equals(c.getPatientId()) && c.isStatus()){
                                    chatListIds.add(c.getChatId());
                                    doctorIds.add(c.getDoctorId());
                                    doctorNames.add(c.getDoctorName());
                                }
                            }
                            if(doctorNames.size()> 0){
                                chatList = doctorNames.toArray(new String[doctorNames.size()]);
                            }else{
                                chatList[0] = "No Chats yet.";
                            }
                            populateRecyclers(root);
                            btn = (Button)root.findViewById(R.id.button_new_chat);
                            buttonCheck = 0;
                            dbDoctor.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot child : dataSnapshot.getChildren()){
                                        Doctor d = child.getValue(Doctor.class);
                                        buttonCheck++;
                                    }
                                    if(buttonCheck==doctorNames.size()){
                                        btn.setBackgroundColor(getResources().getColor(R.color.unClickBtn));
                                        btn.setClickable(false);
                                    }else {
                                        btn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent i;
                                                i = new Intent(getContext(), ChatCreation.class);
                                                startActivity(i);
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
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

        return root;
    }
    @Override
    public void OnReportClickListener(int pos){
        intent = new Intent(getContext(), MessagesActivity.class);
        isDoctor = false;
        final int position = pos;
//       final String chatId = chatListIds.get(pos);
//       System.out.println("ChatId in Chat Fragment: " + chatId);
//       intent.putExtra("EXTRASESSIONID", chatId);
//       startActivity(intent);
        dbDoctor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    Doctor d = child.getValue(Doctor.class);
                    if(userId.equalsIgnoreCase(d.getDocID())){
                        isDoctor = true;
                    }
                }
                if(isDoctor){
                    dbChats.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ArrayList<String> chatListIds = new ArrayList<>();
                            String name = "";
                            long x = dataSnapshot.getChildrenCount();
                            for(DataSnapshot child : dataSnapshot.getChildren()){
                                x--;
                                Chat c = child.getValue(Chat.class);
                                if(userId.equalsIgnoreCase(c.getDoctorId()) && c.isStatus()){
                                    chatListIds.add(c.getChatId());
                                    name = c.getDoctorName();
                                }
                                if(x==0){
                                    final String chatId = chatListIds.get(position);
                                    intent.putExtra("chatid", chatId);
                                    intent.putExtra("name", name);
                                    startActivity(intent);
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else{

                    dbChats.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ArrayList<String> chatListIds = new ArrayList<>();
                            String name = "";
                            long x = dataSnapshot.getChildrenCount();
                            for(DataSnapshot child : dataSnapshot.getChildren()){
                                x--;
                                Chat c = child.getValue(Chat.class);
                                if(userId.equalsIgnoreCase(c.getPatientId()) && c.isStatus()){
                                    chatListIds.add(c.getChatId());
                                    name = c.getPatientName();
                                }
                                if(x==0){
                                    final String chatId = chatListIds.get(position);
                                    intent.putExtra("chatid", chatId);
                                    intent.putExtra("name", name);
                                    startActivity(intent);
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

    public void populateRecyclers(View root){
        rvChats = (RecyclerView)root.findViewById(R.id.recycler_chat);

        lmChats = new LinearLayoutManager(this.getActivity());
        rvChats.setLayoutManager(lmChats);
        adaptChats = new RecyclerItemBtn(chatList, this);
        rvChats.setAdapter(adaptChats);
    }
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.delete_chat_menu, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemId = item.getItemId();
        if(itemId == R.id.item1){
            intent = new Intent(getContext(), DeleteChats.class);
            startActivity(intent);
        } if (itemId == R.id.action_Video_Call) {
            startActivity(new Intent(getContext(), video_call.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
