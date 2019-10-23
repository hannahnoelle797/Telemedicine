package com.example.telemedicine.ui.settings;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.telemedicine.R;


public class SettingsFragment extends Fragment {

    private LinearLayout account_btn;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        account_btn = (LinearLayout)root.findViewById(R.id.account_btn);
        account_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.print("KFEOJAOJ#R#OJT$OJT%O$J");
            }
        });
        return root;
    }
}
