package com.ta.slk.sistemlayanankegiatan.Rest;
import com.ta.slk.sistemlayanankegiatan.Model.*;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {
    @GET("Rest_slkg/activities")
    Call<GetActivities> getActivities();

    @FormUrlEncoded
    @POST("Rest_slkg/allbyid")
    Call<GetInvtActivities> getInvitationActivity(
            @Field("user_id") String user_id,
            @Field("action") String action
    );



}
