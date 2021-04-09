package intro.android.aplicacao.api

import android.media.Image

data class Ocorrencia(
    val id: Int,
    val imagem: String,
    val descricao : String,
    val latitude: Int,
    val longitude: Int,
    val utilizador_id: Int
)