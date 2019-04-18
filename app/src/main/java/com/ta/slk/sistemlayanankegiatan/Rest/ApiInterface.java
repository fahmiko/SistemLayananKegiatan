package com.ta.slk.sistemlayanankegiatan.Rest;
import com.ta.slk.sistemlayanankegiatan.Model.*;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface ApiInterface {
    @GET("Rest_slkg/activities")
    Call<GetActivities> getActivities();

    @GET("Rest_slkg/groups")
    Call<GetGroups> getGroups();

    @GET("Rest_slkg/users")
    Call<GetUsers> getUsers();

    @FormUrlEncoded
    @POST("Rest_slkg/sendinvitation")
    Call<PostData> sendInvitation(
            @Field("id[]") ArrayList<Integer> id,
            @Field("id_activity") String id_activity,
            @Field("action") String action
    );

    @FormUrlEncoded
    @POST("Rest_slkg/allbyid")
    Call<GetInvtActivities> getInvitationActivity(
            @Field("user_id") String user_id,
            @Field("action") String action
    );

    @FormUrlEncoded
    @POST("Rest_slkg/allbyid")
    Call<GetGroups> getGroupsById(
            @Field("user_id") String user_id,
            @Field("action") String action
    );

    @FormUrlEncoded
    @POST("Rest_slkg/allbyid")
    Call<GetActivities> getActiviesById(
            @Field("user_id") String user_id,
            @Field("action") String action
    );

    @FormUrlEncoded
    @POST("Rest_slkg/login")
    Call<GetUsers> getLoginUser(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("Rest_slkg/loginnip")
    Call<GetUsers> getLoginNip(
            @Field("nip") String nip,
            @Field("device_token") String device_token
    );

    @FormUrlEncoded
    @POST("Rest_slkg/invitation")
    Call<GetInvtActivities> getActivityStatus(
            @Field("id_member") String id,
            @Field("status") String status
    );

    @FormUrlEncoded
    @PUT("Rest_slkg/invitation")
    Call<PostData> putInvitationStatus(
            @Field("id_activity") String id_activity,
            @Field("id_member") String id_member,
            @Field("status") String status
    );


    @Multipart
    @POST("Rest_slkg/activity")
    Call<PostData> postActivity(
            @Part MultipartBody.Part file,
            @Part("name") RequestBody name,
            @Part("created_by") RequestBody created_by,
            @Part("location") RequestBody location,
            @Part("contact") RequestBody contact,
            @Part("date") RequestBody date,
            @Part("description") RequestBody description,
            @Part("comment_key") RequestBody key
    );
}
