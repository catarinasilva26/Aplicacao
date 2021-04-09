package intro.android.aplicacao.api

import android.media.Image

data class Ocorrencia(
    val id: Int,
    val imagem: String,
    val descricao : String,
    val latitude: String,
    val longitude: String,
    val utilizador_id: Int
)