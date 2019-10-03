package com.example.telemedicine.ui.chats;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.telemedicine.R;
import com.example.telemedicine.ui.sample_pages.sample_appt;
import com.example.telemedicine.ui.sample_pages.sample_chat;
import com.example.telemedicine.ui.utilities.RecyclerItem;

public class ChatsFragment extends Fragment {

    private ChatsViewModel chatsViewModel;
    private Button testBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        chatsViewModel =
                ViewModelProviders.of(this).get(ChatsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_chats, container, false);
        final TextView textView = root.findViewById(R.id.chat_header);
        chatsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        // This is for the DEMO ONLY
        testBtn = (Button) root.findViewById(R.id.chat_1);
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), sample_chat.class);
                startActivity(intent);
            }
        });
        // END OF DEMO TESTING

        return root;
    }
}