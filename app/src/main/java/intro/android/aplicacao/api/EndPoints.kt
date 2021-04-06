package intro.android.aplicacao.api

import retrofit2.http.*
import retrofit2.Call


interface EndPoints {

    @FormUrlEncoded
    @POST("/CM/api/login")
    fun login(@Field("nome") nome: String, @Field("password") password: String): Call<OutputLogin>
}