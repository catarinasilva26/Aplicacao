package intro.android.aplicacao.api

import retrofit2.http.*
import retrofit2.Call


interface EndPoints {

    @FormUrlEncoded
    @POST("login")
    fun login(@Field("dados")nome: String?, password:String?): Call<OutputLogin>
}