package com.ta.slk.sistemlayanankegiatan;

import com.ta.slk.sistemlayanankegiatan.Activity.LoginActivity;
import com.ta.slk.sistemlayanankegiatan.Fragments.*;
import com.ta.slk.sistemlayanankegiatan.Fragments.UserInvitation;
import com.ta.slk.sistemlayanankegiatan.Method.Application;
import com.ta.slk.sistemlayanankegiatan.Method.Session;
import com.ta.slk.sistemlayanankegiatan.Model.PostData;
import com.ta.slk.sistemlayanankegiatan.Rest.ApiClient;
import com.ta.slk.sistemlayanankegiatan.Rest.ApiGroups;
import com.ta.slk.sistemlayanankegiatan.Rest.ApiMembers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.POST;

public class AdminContent extends AppCompatActivity {
    private BottomNavigationView mNavigationView;
    private FrameLayout mFrameLayout;
    private Toolbar toolbar;
    private ActivitiesFragment activitiesFragment;
    private GroupsFragment groupsFragment;
    private MembersFragment membersFragment;
    private ProfileFragment profileFragment;
    private com.ta.slk.sistemlayanankegiatan.Fragments.UserInvitation userInvitation;
    String TAG = "checking";
    String imagePath = "";
    TextInputEditText title, description, image;
    Button save, close;
    Boolean isSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_content);

        mNavigationView = findViewById(R.id.admin_nav);
        mFrameLayout = findViewById(R.id.admin_frame);

        isSuccess = false;
        activitiesFragment = new ActivitiesFragment();
        groupsFragment = new GroupsFragment();
        membersFragment = new MembersFragment();
        profileFragment = new ProfileFragment();

        setFragment(activitiesFragment);
//        getTimeAgo();

        toolbar = findViewById(R.id.my_toolbar);
//        Drawable drawable;
//        SharedPreferences preferences = this.getSharedPreferences("login",MODE_PRIVATE);
//        String url = preferences.getString("photo",null);
//        ImageView imageView = findViewById(R.id.img_visi);
//        Glide.with(getApplicationContext()).load(url).into(imageView);
//        drawable = imageView.getDrawable();
        toolbar.setNavigationIcon(R.drawable.common_google_signin_btn_icon_dark_normal);

        toolbar.setTitle("Kegiatan");
        setSupportActionBar(toolbar);

        mNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_admin_activity:
                        setFragment(activitiesFragment);
                        toolbar.setVisibility(View.VISIBLE);
                        toolbar.setTitle("Kegiatan");
                        setSupportActionBar(toolbar);
                        return true;

                    case R.id.nav_admin_group:
                        setFragment(groupsFragment);
                        toolbar.setVisibility(View.VISIBLE);
                        toolbar.setTitle("Group");
                        setSupportActionBar(toolbar);
                        return true;
                    case R.id.nav_admin_logout:
                        finish();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.putExtra("message","logout");
                        startActivity(intent);
                        return false;
                    case R.id.nav_admin_member:
                        setFragment(membersFragment);
                        toolbar.setVisibility(View.VISIBLE);
                        toolbar.setTitle("Member");
                        setSupportActionBar(toolbar);
                        return true;
                    case R.id.nav_admin_setting:
                        setFragment(profileFragment);
                        toolbar.setVisibility(View.GONE);
                        toolbar.setTitle("Profile");
                        setSupportActionBar(toolbar);
                        return true;
                        default:
                            return false;
                }
            }
        });
    }
    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.admin_frame, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.menu_add_activity:
                intent = new Intent(getApplicationContext(), AddInvitation.class);
                startActivity(intent);
                break;
            case R.id.menu_add_group:
                final AlertDialog.Builder builder = new AlertDialog.Builder(AdminContent.this);
                LayoutInflater inflater = getLayoutInflater();
                final View dialog = inflater.inflate(R.layout.manage_groups,null);
                builder.setView(dialog).setTitle("Tambah Group").setIcon(R.drawable.group);
                title = dialog.findViewById(R.id.mg_title_text);
                description = dialog.findViewById(R.id.mg_desc_text);
                image = dialog.findViewById(R.id.mg_img_text);

                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Intent gallery = new Intent();
                        gallery.setType("image/*").setAction(Intent.ACTION_PICK);
                        Intent intentChoice = Intent.createChooser(gallery,"Pilih Gambar untuk di upload");
                        startActivityForResult(intentChoice,1);
                    }
                });

                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        insertGroup();
                        dialog.dismiss();
                    }

                }).setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();
                break;
            case R.id.menu_add_member:
                final AlertDialog.Builder builder2 = new AlertDialog.Builder(AdminContent.this);
                LayoutInflater inflater2 = getLayoutInflater();
                final View dialog2 = inflater2.inflate(R.layout.popup_manage_member,null);
                builder2.setView(dialog2).setTitle("Tambah Member").setIcon(R.drawable.ic_assignment_ind_black_24dp);
                final TextInputEditText nip = dialog2.findViewById(R.id.mb_nip);
                final TextInputEditText name = dialog2.findViewById(R.id.mb_name);

                builder2.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        insertMember(nip.getText().toString(),name.getText().toString());
                        dialog.dismiss();
                    }

                }).setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder2.show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void insertMember(String nip, String name) {
        ApiMembers members = ApiClient.getClient().create(ApiMembers.class);
        Call<PostData> call = members.addNip(nip, name);
        call.enqueue(new Callback<PostData>() {
            @Override
            public void onResponse(Call<PostData> call, Response<PostData> response) {
                if(response.code()==200){
                    Toast.makeText(getApplicationContext(),"Data berhasil ditambahkan",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PostData> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Cek Koneksi Interner",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTimeAgo() {
        String input = "1970-01-09 17:11:23.104";
        SimpleDateFormat parser = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
        Date date = null;
        try {
            date = parser.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = formatter.format(date);

        Log.d("date", formattedDate.toString());
    }

    private void insertGroup() {
        ApiGroups service = ApiClient.getClient().create(ApiGroups.class);
        MultipartBody.Part body = null;
        if (!imagePath.isEmpty()){
            File file = new File(imagePath);
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), file);
            body = MultipartBody.Part.createFormData("picture", file.getName(),
                    requestFile);
        }
        RequestBody reqName = MultipartBody.create(MediaType.parse("multipart/form-data"),
                (title.getText().toString().isEmpty())?"":title.getText().toString());
        RequestBody reqDes = MultipartBody.create(MediaType.parse("multipart/form-data"),
                (description.getText().toString().isEmpty())?"":description.getText().toString());

        Call<PostData> call = service.new_group(body,reqName,reqDes);
        call.enqueue(new Callback<PostData>() {
            @Override
            public void onResponse(Call<PostData> call, Response<PostData> response) {
                if(response.body().getStatus().equals("success")){
                    isSuccess = true;
                    Toast.makeText(getApplicationContext(),"Data Group Ditambahkan",Toast.LENGTH_SHORT).show();
                }else{
                    isSuccess = false;
                    Toast.makeText(getApplicationContext(),"Data Gagal Ditambahkan",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PostData> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1){
            if (data==null){
                Toast.makeText(getApplicationContext(), "Foto gagal di-load", Toast.LENGTH_LONG).show();
            }
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imagePath =cursor.getString(columnIndex);

//                Picasso.with(getApplicationContext()).load(new File(imagePath)).fit().into(mImageView);
//                Glide.with(getApplicationContext()).load(new File(imagePath)).into(mImageView);
                SharedPreferences preferences = getSharedPreferences("imgUrl",MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                try {
                    image.setText(imagePath);
                }catch (Exception e){

                }
                editor.putString("path",imagePath);
                editor.apply();

                cursor.close();
            }else{
                Toast.makeText(getApplicationContext(), "Foto gagal di-load", Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
