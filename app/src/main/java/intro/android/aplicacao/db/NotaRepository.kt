package intro.android.aplicacao.db

import androidx.lifecycle.LiveData
import intro.android.aplicacao.dao.NotaDao
import intro.android.aplicacao.entities.Nota

class NotaRepository(private  val notaDao: NotaDao) {

    val totalNotas: LiveData<List<Nota>> = notaDao.getNotas()

    suspend fun inserir(nota: Nota){
        notaDao.inserir(nota)
    }
}