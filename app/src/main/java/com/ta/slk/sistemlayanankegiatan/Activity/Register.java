package com.ta.slk.sistemlayanankegiatan.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dd.CircularProgressButton;
import com.ta.slk.sistemlayanankegiatan.Model.GetUsers;
import com.ta.slk.sistemlayanankegiatan.Model.PostData;
import com.ta.slk.sistemlayanankegiatan.Model.Users;
import com.ta.slk.sistemlayanankegiatan.R;
import com.ta.slk.sistemlayanankegiatan.Rest.ApiClient;
import com.ta.slk.sistemlayanankegiatan.Rest.ApiMembers;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {
    String id_member, name, action, imagePath;
    TextInputEditText txt_name,txt_username,txt_password,txt_address,txt_email,txt_phone;
    CircleImageView upload;
    CircularProgressButton button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initComponents();
        if(action.equals("Update")){
            componentUpdate();
        }
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                Intent intentChoice = Intent.createChooser(
                        intent,"Pilih Gambar untuk di upload");
                startActivityForResult(intentChoice,1);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doUpdate();
                button.setProgress(0);
            }
        });
    }

    private void initComponents() {
        id_member = getIntent().getStringExtra("id_member");
        name = getIntent().getStringExtra("name");
        action = getIntent().getStringExtra("action");

        txt_name = findViewById(R.id.re_name);
        txt_username = findViewById(R.id.re_username);
        txt_password = findViewById(R.id.re_password);
        txt_address = findViewById(R.id.re_address);
        txt_email= findViewById(R.id.re_email);
        txt_phone = findViewById(R.id.re_call);
        upload = findViewById(R.id.btn_upload);
        button = findViewById(R.id.btn_save);

        if(action.equals("Register")){
            txt_name.setText(name);
        }

        button.setText("SUBMIT");
        button.setIdleText("SUBMIT");
    }

    private void doUpdate(){
        if(txt_name.getText().toString().equals("")){
            txt_name.setError("nama belum diisi");
        }else if(txt_address.getText().toString().equals("")){
            txt_address.setError("alamat belum diisi");
        }else if(txt_email.getText().toString().equals("")){
            txt_email.setError("email belum diisi");
        }else if(txt_phone.getText().toString().equals("")){
            txt_phone.setError("no hp belum diisi");
        }else if(imagePath.equals("")){
            Toast.makeText(getApplicationContext(),"Gambar belum dipilih",Toast.LENGTH_SHORT).show();
        }else {
            if(action.equals("Register")) {
                if (txt_username.getText().toString().equals("")) {
                    txt_username.setError("username belum diisi");
                } else if (txt_password.getText().toString().equals("")) {
                    txt_password.setError("password belum diisi");
                }
            }
            button.setIndeterminateProgressMode(true);
            button.setProgress(1);
            MultipartBody.Part body = null;
                if (!imagePath.isEmpty()) {
                    File file = new File(imagePath);

                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), file);

                    body = MultipartBody.Part.createFormData("picture", file.getName(),
                            requestFile);
                }
                RequestBody reqName = MultipartBody.create(MediaType.parse("multipart/form-data"),
                        (txt_name.getText().toString().isEmpty()) ? "" : txt_name.getText().toString());
                RequestBody reqId = MultipartBody.create(MediaType.parse("multipart/form-data"),
                        id_member);
                RequestBody reqUsername = MultipartBody.create(MediaType.parse("multipart/form-data"),
                        (txt_username.getText().toString().isEmpty()) ? "" : txt_username.getText().toString());
                RequestBody reqPassword = MultipartBody.create(MediaType.parse("multipart/form-data"),
                        (txt_password.getText().toString().isEmpty()) ? "" : txt_password.getText().toString());
                RequestBody reqAddress = MultipartBody.create(MediaType.parse("multipart/form-data"),
                        (txt_address.getText().toString().isEmpty()) ? "" : txt_address.getText().toString());
                RequestBody reqContact = MultipartBody.create(MediaType.parse("multipart/form-data"),
                        (txt_phone.getText().toString().isEmpty()) ? "" : txt_phone.getText().toString());
                RequestBody reqEmail = MultipartBody.create(MediaType.parse("multipart/form-data"),
                        (txt_email.getText().toString().isEmpty()) ? "" : txt_email.getText().toString());
                RequestBody Reqaction = MultipartBody.create(MediaType.parse("multipart/form-data"),
                        action);

                ApiMembers members = ApiClient.getAuth().create(ApiMembers.class);
                Call<PostData> call = members.registerMember(body, reqId, reqName, reqUsername, reqPassword, reqAddress, reqContact, reqEmail, Reqaction);

                call.enqueue(new Callback<PostData>() {
                    @Override
                    public void onResponse(Call<PostData> call, Response<PostData> response) {
                        if (response.code() == 200) {
                            if (response.body().getStatus().equals("success")) {
                                button.setProgress(100);
                                Toast.makeText(getApplicationContext(), "Update sukses", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<PostData> call, Throwable t) {
                        button.setProgress(100);
                        finish();
                    }
                });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1) {
            if (data == null) {
                Toast.makeText(getApplicationContext(), "Foto gagal di-load", Toast.LENGTH_LONG).show();
            }
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imagePath = cursor.getString(columnIndex);

//                Picasso.with(getApplicationContext()).load(new File(imagePath)).fit().into(mImageView);
                Glide.with(getApplicationContext()).load(new File(imagePath)).into(upload);
                cursor.close();
            } else {
                Toast.makeText(getApplicationContext(), "Foto gagal di-load", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void componentUpdate(){
        ApiMembers members = ApiClient.getClient().create(ApiMembers.class);
        Call<GetUsers> call = members.profile();
        call.enqueue(new Callback<GetUsers>() {
            @Override
            public void onResponse(Call<GetUsers> call, Response<GetUsers> response) {
                if(response.code()==200){
                    if(response.body().getStatus().equals("success")){
                        txt_username.setVisibility(View.GONE);
                        txt_password.setVisibility(View.GONE);
                        txt_name.setText(response.body().getResult().get(0).getName());
                        txt_address.setText(response.body().getResult().get(0).getAddress());
                        txt_email.setText(response.body().getResult().get(0).getEmail());
                        txt_phone.setText(response.body().getResult().get(0).getPhoneNumber());
                        Glide.with(getApplicationContext()).load(ApiClient.BASE_URL+"/uploads/members/"+
                                response.body().getResult().get(0).getPhotoProfile()).into(upload);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetUsers> call, Throwable t) {

            }
        });

    }
}
