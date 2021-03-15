package intro.android.aplicacao.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import intro.android.aplicacao.entities.Nota

@Dao
interface NotaDao {
    @Query("SELECT * FROM nota_tabela ORDER BY id DESC")
    fun getNotas(): LiveData<List<Nota>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun inserir(nota: Nota)

    @Query("DELETE FROM nota_tabela WHERE id == :id")
    suspend fun eliminarNota(id: Int)

    @Query("UPDATE nota_tabela SET titulo=:titulo, conteudo=:conteudo WHERE id ==:id")
    suspend fun atualizarNota(titulo: String, conteudo: String, id: Int)
}