package com.example.telemedicine.ui.messaging;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.telemedicine.R;
import com.example.telemedicine.models.Doctor;
import com.example.telemedicine.models.Message;
import com.example.telemedicine.models.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MessagesActivity extends AppCompatActivity {
    //CircleImageView profile_image;
    TextView username;
    DatabaseReference dbDoctor;
    DatabaseReference dbUser;
    DatabaseReference dbMessage;
    DatabaseReference dbChat;
    EditText text_send;
    ImageButton btn_send;
    MessageAdapter messageAdapter;
    List<Message> mchat;
    ListView lv;
    String chatId;
    String doctorId;
    String patientId;
    String userId;
    String name;
    boolean isDoctor = false;
    boolean status = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        username=findViewById(R.id.name);
        lv = (ListView)findViewById(R.id.messages_view);
        text_send=findViewById(R.id.editText);
        btn_send=findViewById(R.id.image_button);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Bundle b = getIntent().getExtras();
        chatId = b.getString("chatid");
        name = b.getString("name");
        dbChat = FirebaseDatabase.getInstance().getReference("Chat");
        dbDoctor = FirebaseDatabase.getInstance().getReference("Doctor");
        dbUser = FirebaseDatabase.getInstance().getReference("Users");
        //Step 1: determine if user is a doctor or patient.
        dbDoctor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    Doctor d = child.getValue(Doctor.class);
                    if(userId.equalsIgnoreCase(d.getDocID())) isDoctor = true;
                }
                if(isDoctor){
                    //Step 2: set correct username.
                    dbChat.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot child:dataSnapshot.getChildren()){
                                Chat m = child.getValue(Chat.class);
                                if(m.getChatId().equals(chatId)&& m.isStatus()){
                                    //username.setText(m.getPatientName());
                                    patientId = m.getPatientId();

                                }
                            }
                            readMessage();
                            //If send button is clicked, use sendMessage method.
                            btn_send.setOnClickListener(new View.OnClickListener(){

                                @Override
                                public void onClick(View v) {
                                    String msg=text_send.getText().toString();
                                    if(!msg.equals("")){
                                        sendMessage(userId, name, patientId, msg);
                                    }else {
                                        Toast.makeText(MessagesActivity.this,"You Can't Send Empty Message",Toast.LENGTH_SHORT).show();
                                    }
                                    text_send.getText();
                                }
                            });
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else{
                    //Step 2: set correct username.
                    dbChat.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot child:dataSnapshot.getChildren()){
                                Chat m = child.getValue(Chat.class);
                                if(m.getChatId().equals(chatId) && m.isStatus()){
                                    //username cannot be set here username.setText(m.getDoctorName());
                                    doctorId = m.getDoctorId();

                                }
                            }
                            readMessage();
                            //Send message logic
                            btn_send.setOnClickListener(new View.OnClickListener(){

                                @Override
                                public void onClick(View v) {
                                    String msg=text_send.getText().toString();

                                    if(!msg.equals("")){

                                        sendMessage(userId, name,doctorId, msg);
                                    }else {
                                        Toast.makeText(MessagesActivity.this,"You Can't Send Empty Message",Toast.LENGTH_SHORT).show();
                                    }
                                    text_send.getText();
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
    }

    private void sendMessage(String senderId, String sender, String receiver, String message){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        Date d = new Date();
        HashMap<String, Object> hashMap=new HashMap<>();
        hashMap.put("chatId", chatId);
        hashMap.put("senderName",sender);
        hashMap.put("senderId", senderId);
        hashMap.put("receiverId", receiver);
        hashMap.put("messageContent",message);
        String messageId = reference.child("Message").push().getKey();
        hashMap.put("messageId", messageId);
        reference.child("Message").child(messageId).setValue(hashMap);
        text_send.setText("");
    }

    private void readMessage(){
        mchat=new ArrayList<>();
        dbChat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot child :dataSnapshot.getChildren()){
                    Chat c = child.getValue(Chat.class);
                    if(chatId.equalsIgnoreCase(c.getChatId()) && !c.isStatus()){
                        status =false;
                    }
                }
                dbMessage=FirebaseDatabase.getInstance().getReference("Message");
                dbMessage.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mchat.clear();
                        for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                            Message chat=snapshot.getValue(Message.class);
                            if(chat.getChatId().equals(chatId) && status){
                                mchat.add(chat);
                            }
                        }

                        messageAdapter=new MessageAdapter(MessagesActivity.this, mchat);
                        lv.setAdapter(messageAdapter);

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
