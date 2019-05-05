package com.ta.slk.sistemlayanankegiatan;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dd.CircularProgressButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.ta.slk.sistemlayanankegiatan.Model.PostData;
import com.ta.slk.sistemlayanankegiatan.Rest.ApiClient;
import com.ta.slk.sistemlayanankegiatan.Rest.ApiInterface;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddInvitation extends AppCompatActivity {
    DatePickerDialog datePickerDialog;
    SimpleDateFormat simpleDateFormat;
    CircularProgressButton button;
    String textDialog;
    TextInputEditText name,contact,description,day,location,clock;
    TextInputEditText upload;
    ImageView mImageView;
    File mFileURI;
    String imagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_invitation);


        initComponents();
        button.setText("Tambah Kegiatan");
        button.setIdleText("Tambah KEGIATAN");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String dateTime = day.getText().toString()+" "+clock.getText().toString();
//                Toast.makeText(getApplicationContext(),dateTime,Toast.LENGTH_SHORT).show();
                postData();
            }
        });

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTime();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final CharSequence[] dialogitem = {"Tampilkan","Gallery"};
                AlertDialog.Builder builder = new AlertDialog.Builder(AddInvitation.this);
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                Dialog dialog1=new Dialog(v.getContext(),R.style.ZoomImageDialog);
                                dialog1.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                                dialog1.setContentView(R.layout.zoom_image);
                                ImageView imageView = dialog1.findViewById(R.id.zoom_image);
                                Glide.with(v.getContext()).load(new File(imagePath)).into(imageView);
                                dialog1.show();
                                break;
                            case 1:
                                final Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_PICK);
                                Intent intentChoice = Intent.createChooser(
                                        intent,"Pilih Gambar untuk di upload");
                                startActivityForResult(intentChoice,2);
                                break;
                        }
                    }
                }).create().show();
            }
        });
    }

    public void showDialog(){
        final Calendar calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year,month,dayOfMonth);
                day.setText(simpleDateFormat.format(newDate.getTime()));
            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void showTime(){
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        final int mSecond = c.get(Calendar.SECOND);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        clock.setText(" "+hourOfDay + ":" + minute+":"+mSecond);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        File storageDir = Environment.getExternalStorageDirectory();
        File image = File.createTempFile(
                "slkg",  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, 1);
            }
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
        mImageView.setImageURI(contentUri);
        imagePath = mCurrentPhotoPath;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == 1){
            galleryAddPic();
        }

        if (resultCode == RESULT_OK && requestCode == 2){
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
                upload.setText(imagePath);
                cursor.close();
            }else{
                Toast.makeText(getApplicationContext(), "Foto gagal di-load", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initComponents(){
        day = findViewById(R.id.name_day);
        name = findViewById(R.id.name_act);
        location = findViewById(R.id.name_location);
        contact = findViewById(R.id.contact_person);
        description = findViewById(R.id.name_description);
        upload = findViewById(R.id.insert_image);
        button = findViewById(R.id.btn_add);
        clock = findViewById(R.id.name_clock);
    }

    private void postData(){
        if(name.getText().toString().equals("")){
            name.setError("judul kegiatan belum di isi");
        }else if(day.getText().toString().equals("")){
            day.setError("tanggal kegiatan belum ter isi");
        }else if(clock.getText().toString().equals("")){
            clock.setError("jam kegiatan belum ter isi");
        }else if(description.getText().toString().equals("")){
            description.setError("deskripsi kegiatan belum ter isi");
        }else if(imagePath.equals("")){
            upload.setError("gambar belum dipilih");
        }else {
            button.setIndeterminateProgressMode(true);
            button.setProgress(1);
            ApiInterface mApiInterface = ApiClient.getClient().create(ApiInterface.class);
            MultipartBody.Part body = null;
            if (!imagePath.isEmpty()) {
                File file = new File(imagePath);

                RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), file);

                body = MultipartBody.Part.createFormData("picture", file.getName(),
                        requestFile);
            }

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().getRoot();
            final String key = reference.push().getKey();

            String dateTime = day.getText().toString() + " " + clock.getText().toString();

            RequestBody reqName = MultipartBody.create(MediaType.parse("multipart/form-data"),
                    (name.getText().toString().isEmpty()) ? "" : name.getText().toString());
            RequestBody reqCreated = MultipartBody.create(MediaType.parse("multipart/form-data"),
                    "1");
            RequestBody reqDate = MultipartBody.create(MediaType.parse("multipart/form-data"),
                    dateTime);
            RequestBody reqLocation = MultipartBody.create(MediaType.parse("multipart/form-data"),
                    (location.getText().toString().isEmpty()) ? "" : location.getText().toString());
            RequestBody reqDesription = MultipartBody.create(MediaType.parse("multipart/form-data"),
                    (description.getText().toString().isEmpty()) ? "" : description.getText().toString());
            RequestBody reqContact = MultipartBody.create(MediaType.parse("multipart/form-data"),
                    (contact.getText().toString().isEmpty()) ? "" : contact.getText().toString());
            RequestBody reqKey = MultipartBody.create(MediaType.parse("multipart/form-data"),
                    key);

            Call<PostData> mPostActivity;
            mPostActivity = mApiInterface.postActivity(body, reqName, reqCreated, reqLocation, reqContact, reqDate, reqDesription, reqKey);
            mPostActivity.enqueue(new Callback<PostData>() {
                @Override
                public void onResponse(Call<PostData> call, Response<PostData> response) {
                    if (response.body().getStatus().equals("success")) {
                        button.setProgress(100);
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        Map<String, Object> map = new HashMap<>();
                        map.put(key, "");
                        reference.updateChildren(map);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<PostData> call, Throwable t) {
                    button.setProgress(-1);
                    button.setProgress(0);
                    Toast.makeText(getApplicationContext(),"Cek koneksi interner",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
