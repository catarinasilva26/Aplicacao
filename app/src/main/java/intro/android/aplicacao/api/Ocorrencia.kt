package intro.android.aplicacao.api

import android.media.Image

data class Ocorrencia(
    val id: Int,
    val image: Image,
    val descricao : String,
    val latitude: String,
    val longitude: String,
    val utilizador_id: Int
)