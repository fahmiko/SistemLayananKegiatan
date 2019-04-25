package com.ta.slk.sistemlayanankegiatan.Rest;

import com.ta.slk.sistemlayanankegiatan.Model.GetDocumentation;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiDocumentation {
    @FormUrlEncoded
    @POST("Rest_documentation/byid")
    Call<GetDocumentation> getDocumentation(
            @Field("id_activity") String id_activity
    );
}
