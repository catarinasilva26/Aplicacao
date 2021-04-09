package intro.android.aplicacao.api

import retrofit2.http.*
import retrofit2.Call


interface EndPoints {

    @GET("/CM/api/situacoes")
    fun getSituacoes() : Call<List<Ocorrencia>>

    @FormUrlEncoded
    @POST("/CM/api/login")
    fun login(@Field("nome") nome: String, @Field("password") password: String): Call<OutputLogin>

    @FormUrlEncoded
    @POST("/CM/api/reportarSituacao")
    fun reportar(@Field("imagem") imagem: String,
                 @Field("descricao") descricao: String,
                 @Field("latitude") latitude: Int,
                 @Field("longitude") longitude: Int,
                 @Field("utilizar_id") utilizador_id: Int): Call<OutputReportar>
}