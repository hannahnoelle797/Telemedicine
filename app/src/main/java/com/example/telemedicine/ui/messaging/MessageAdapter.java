package com.example.telemedicine.ui.messaging;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telemedicine.R;
import com.example.telemedicine.models.Doctor;
import com.example.telemedicine.models.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MessageAdapter extends BaseAdapter {

    public static final int MSG_TYPE_LEFT=1;
    public static final int MSG_TYPE_RIGHT=0;

    private Context mContext;
    private List<Message> mchat;

    FirebaseUser firebaseUser;

    public MessageAdapter(Context mContext,List<Message> mchat){
        this.mchat=mchat;
        this.mContext=mContext;
    }
    @Override
    public int getCount(){
        return mchat.size();
    }
    @Override
    public Message getItem(int i){
        return mchat.get(i);
    }
    @Override
    public long getItemId(int i){
        return i;
    }

    public int getItemViewType(int position){
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if(mchat.get(position).getSenderId().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }
    @Override
    public View getView(int i, View cView, ViewGroup viewGroup){
        MessageViewHolder holder = new MessageViewHolder();
        LayoutInflater messageInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Message m = mchat.get(i);
        if(getItemViewType(i)==0){//message is sent by this user
            cView = messageInflater.inflate(R.layout.sent_message, null);
            holder.messageBody = (TextView)cView.findViewById(R.id.message_body);
            cView.setTag(holder);
            holder.messageBody.setText(m.getMessageContent());
        }else{//message is received by this user
            cView = messageInflater.inflate(R.layout.rec_message, null);
            holder.username = (TextView)cView.findViewById(R.id.name);
            holder.messageBody = (TextView)cView.findViewById(R.id.message_body);
            cView.setTag(holder);
            holder.username.setText(m.getSenderName());
            holder.messageBody.setText(m.getMessageContent());
        }
        return cView;
    }
}
