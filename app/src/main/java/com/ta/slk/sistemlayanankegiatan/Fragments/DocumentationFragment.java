package com.ta.slk.sistemlayanankegiatan.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ta.slk.sistemlayanankegiatan.Adapter.DoctAdapter;
import com.ta.slk.sistemlayanankegiatan.Method.ClickListenner;
import com.ta.slk.sistemlayanankegiatan.Method.RecyclerTouchListener;
import com.ta.slk.sistemlayanankegiatan.Model.Documentation;
import com.ta.slk.sistemlayanankegiatan.Model.GetDocumentation;
import com.ta.slk.sistemlayanankegiatan.Model.PostData;
import com.ta.slk.sistemlayanankegiatan.R;
import com.ta.slk.sistemlayanankegiatan.Rest.ApiClient;
import com.ta.slk.sistemlayanankegiatan.Rest.ApiDocumentation;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocumentationFragment extends Fragment{
    ProgressBar progressBar;
    Bundle bundle;
    FloatingActionButton upload;
    RecyclerView recyclerView;
    TextView status;
    private String imagePath;
    ApiDocumentation service;
    List<Documentation> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_documentation,container,false);
        recyclerView = view.findViewById(R.id.recycler_content);
        progressBar = view.findViewById(R.id.progress_bar);
        status = view.findViewById(R.id.txt_status);
        upload = view.findViewById(R.id.btn_upload);
        final Fragment fragment = this;

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),4));
        service = ApiClient.getClient().create(ApiDocumentation.class);

        bundle = getArguments();
        loadData();
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent gallery = new Intent();
                gallery.setType("image/*").setAction(Intent.ACTION_PICK);
                Intent intentChoice = Intent.createChooser(gallery,"Pilih Gambar untuk di upload");
//                getActivity().startActivityForResult(intentChoice,1);
                fragment.startActivityForResult(intentChoice,1);
            }
        });
        return view;
    }

    private void loadData() {
        service.getDocumentation(bundle.get("id_activity").toString()).enqueue(new Callback<GetDocumentation>() {
            @Override
            public void onResponse(Call<GetDocumentation> call, Response<GetDocumentation> response) {
                if(response.code()==200){
                    progressBar.setVisibility(View.GONE);
                    list = response.body().getResult();
                    if(list.size()==0){
                        status.setVisibility(View.VISIBLE);
                    }else{
                        recyclerView.setAdapter(new DoctAdapter(getContext(),list));
                    }
                }
            }

            @Override
            public void onFailure(Call<GetDocumentation> call, Throwable t) {
                Snackbar.make(getView(),"Cek Koneksi Internet ",Snackbar.LENGTH_LONG).setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressBar.setVisibility(View.VISIBLE);
                        loadData();
                    }
                }).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 1){
            if (data==null){
                Toast.makeText(getContext(), "Foto gagal di-load", Toast.LENGTH_LONG).show();
            }
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor =  getContext().getContentResolver().query(selectedImage, filePathColumn, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imagePath =cursor.getString(columnIndex);

//                Picasso.with(getApplicationContext()).load(new File(imagePath)).fit().into(mImageView);
//                Glide.with(getApplicationContext()).load(new File(imagePath)).into(mImageView);
//                Toast.makeText(getContext(), "Berhasil di load", Toast.LENGTH_LONG).show();
                doUpload();
                cursor.close();
            }else{
                Toast.makeText(getContext(), "Foto gagal di-load", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void doDelete(String id){
        service.del_docs(id).enqueue(new Callback<PostData>() {
            @Override
            public void onResponse(Call<PostData> call, Response<PostData> response) {
                if(response.code()==200){
                    if(response.body().getStatus().equals("success")){
                        Toast.makeText(getContext(),"Berhasil di Hapus",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<PostData> call, Throwable t) {

            }
        });
    }

    private void doUpload(){
        MultipartBody.Part body = null;
        if (!imagePath.isEmpty()){
            File file = new File(imagePath);
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), file);
            body = MultipartBody.Part.createFormData("picture", file.getName(),
                    requestFile);
        }
        String id = bundle.getString("id_activity");
        RequestBody reqId = MultipartBody.create(MediaType.parse("multipart/form-data"),
                (id.isEmpty())?"":id);
        service.new_documentaton(body,reqId).enqueue(new Callback<PostData>() {
            @Override
            public void onResponse(Call<PostData> call, final Response<PostData> response) {
                if(response.code()==200){
                    if(response.body().getStatus().equals("success")){
                        loadData();
                        Snackbar.make(getView(),"Berhasil di upload ",Snackbar.LENGTH_LONG).setAction("undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                doDelete(response.body().getMessage());
                                loadData();
                            }
                        }).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<PostData> call, Throwable t) {
                Snackbar.make(getView(),"Cek Koneksi Internet",Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
