package com.ta.slk.sistemlayanankegiatan.Rest;

import com.ta.slk.sistemlayanankegiatan.Model.*;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiGroups {
    @FormUrlEncoded
    @POST("Rest_groups/groupmember")
    Call<GetUsers> getMemberGroup(
            @Field("id_group") String id_group
    );
}
