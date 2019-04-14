package com.ta.slk.sistemlayanankegiatan;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.ta.slk.sistemlayanankegiatan.Model.PostData;
import com.ta.slk.sistemlayanankegiatan.Rest.ApiClient;
import com.ta.slk.sistemlayanankegiatan.Rest.ApiInterface;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddInvitation extends AppCompatActivity {
    DatePickerDialog datePickerDialog;
    SimpleDateFormat simpleDateFormat;
    Button button;
    EditText name,contact,description,day,location;
    FloatingActionButton upload;
    ImageView mImageView;
    File mFileURI;
    String imagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_invitation);
        initComponents();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] dialogitem = {"Camera","Gallery"};
                AlertDialog.Builder builder = new AlertDialog.Builder(AddInvitation.this);
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                dispatchTakePictureIntent();
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

//                Picasso.with(getApplicationContext()).load(new File(imagePath)).fit().into(mImageView);
                Glide.with(getApplicationContext()).load(new File(imagePath)).into(mImageView);
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
        upload = findViewById(R.id.fab_image);
        mImageView = findViewById(R.id.img_inv);
        button = findViewById(R.id.btn_add);

    }

    private void postData(){
        ApiInterface mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        MultipartBody.Part body = null;
        if (!imagePath.isEmpty()){
            File file = new File(imagePath);

            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), file);

            body = MultipartBody.Part.createFormData("picture", file.getName(),
                    requestFile);
        }

        RequestBody reqName = MultipartBody.create(MediaType.parse("multipart/form-data"),
                (name.getText().toString().isEmpty())?"":name.getText().toString());
        RequestBody reqCreated = MultipartBody.create(MediaType.parse("multipart/form-data"),
                "1");
        RequestBody reqDate = MultipartBody.create(MediaType.parse("multipart/form-data"),
                (day.getText().toString().isEmpty())?"":day.getText().toString());
        RequestBody reqLocation = MultipartBody.create(MediaType.parse("multipart/form-data"),
                (location.getText().toString().isEmpty())?"":location.getText().toString());
        RequestBody reqDesription = MultipartBody.create(MediaType.parse("multipart/form-data"),
                (description.getText().toString().isEmpty())?"":description.getText().toString());
        RequestBody reqContact = MultipartBody.create(MediaType.parse("multipart/form-data"),
                (contact.getText().toString().isEmpty())?"":contact.getText().toString());

        Call<PostData> mPostActivity;
        mPostActivity = mApiInterface.postActivity(body,reqName,reqCreated,reqLocation,reqContact,reqDate,reqDesription);
        mPostActivity.enqueue(new Callback<PostData>() {
            @Override
            public void onResponse(Call<PostData> call, Response<PostData> response) {
                if(response.body().getStatus().equals("success")){
                    finish();
                }
                Log.d("msg", "onResponse: "+response.body().getMessage());
            }

            @Override
            public void onFailure(Call<PostData> call, Throwable t) {

            }
        });

    }
}
