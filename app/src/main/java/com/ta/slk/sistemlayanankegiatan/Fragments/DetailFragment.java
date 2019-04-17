package com.ta.slk.sistemlayanankegiatan.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ta.slk.sistemlayanankegiatan.R;

public class DetailFragment extends Fragment {
    public DetailFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_detail,container,false);
        Bundle activity = getArguments();
        TextView label = view.findViewById(R.id.section_label);
        String detail = activity.getString("id_activity");
        detail += "\n "+activity.getString("name");
        detail += "\n "+activity.getString("contact");
        detail += "\n "+activity.getString("date");
        detail += "\n "+activity.getString("contact");

        label.setText(detail);
        return view;
    }
}
