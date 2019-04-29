package com.ta.slk.sistemlayanankegiatan.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.ta.slk.sistemlayanankegiatan.R;
import com.ta.slk.sistemlayanankegiatan.Rest.ApiClient;

public class DetailFragment extends Fragment {
    public DetailFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view =  inflater.inflate(R.layout.fragment_detail,container,false);
        return view;
    }
}
