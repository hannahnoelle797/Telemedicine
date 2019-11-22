package com.example.telemedicine.ui.chats;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.telemedicine.ui.utilities.RecyclerItemBtn;
import com.example.telemedicine.ui.utilities.RecyclerItemClickListener;
import com.example.telemedicine.ui.utilities.RecyclerItemOldBtn;

import com.example.telemedicine.R;
import com.example.telemedicine.ui.utilities.RecyclerItem;
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
    private ArrayList<String> doctorNames;
    private String[] chatList;

    private View root;
    private Button btn;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        chatsViewModel =
                ViewModelProviders.of(this).get(ChatsViewModel.class);

        root = inflater.inflate(R.layout.fragment_chatpage, container, false);
        final TextView textView = root.findViewById(R.id.chat_heading);
        dbChats = FirebaseDatabase.getInstance().getReference("Chats");
        dbUser = FirebaseDatabase.getInstance().getReference("Users");
        dbDoctor = FirebaseDatabase.getInstance().getReference("Doctor");
        //get the list of chats the user has.
        doctorIds = new ArrayList<>();
        chatListIds = new ArrayList<>();
        doctorNames = new ArrayList<>();
        chatList = new String[1];

        dbChats.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
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
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        btn = (Button)root.findViewById(R.id.button_new_chat);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i;
                i = new Intent(getContext(), ChatCreation.class);
                startActivity(i);
            }
        });
        return root;
    }
    @Override
    public void OnReportClickListener(int pos){
       Intent intent = new Intent(getContext(), MessageActivity.class);
       String chatId = chatListIds.get(pos);
       intent.putExtra("EXTRA_SESSION_ID", chatId);
       startActivity(intent);

    }

    public void populateRecyclers(View root){
        rvChats = (RecyclerView)root.findViewById(R.id.recycler_chat);
        lmChats = new LinearLayoutManager(this.getActivity());
        rvChats.setLayoutManager(lmChats);
        adaptChats = new RecyclerItemBtn(chatList, this);
        rvChats.setAdapter(adaptChats);
    }
}
