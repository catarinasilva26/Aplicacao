package intro.android.aplicacao.api

import retrofit2.http.*
import retrofit2.Call


interface EndPoints {
    @POST("/CM/api/editarSituacao/{id}")
    fun atualizarSituacaoId(@Path("id") id: String?) : Call<List<Ocorrencia>>

    @GET("/CM/api/situacoes")
    fun getSituacoes() : Call<List<Ocorrencia>>

    @GET("/CM/api/situacao/{id}")
    fun getSituacaoId(@Path("id") id: String?) : Call<List<Ocorrencia>>

    @FormUrlEncoded
    @POST("/CM/api/login")
    fun login(@Field("nome") nome: String, @Field("password") password: String): Call<OutputLogin>

    @FormUrlEncoded
    @POST("/CM/api/reportarSituacao")
    fun reportar(@Field("imagem") imagem: String,
                 @Field("descricao") descricao: String,
                 @Field("latitude") latitude: String,
                 @Field("longitude") longitude: String,
                 @Field("utilizador_id") utilizador_id: Int): Call<OutputReportar>
}