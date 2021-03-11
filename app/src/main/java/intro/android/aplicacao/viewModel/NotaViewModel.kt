package intro.android.aplicacao.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import intro.android.aplicacao.db.NotaDB
import intro.android.aplicacao.db.NotaRepository
import intro.android.aplicacao.entities.Nota
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NotaRepository
    val totalNotas: LiveData<List<Nota>>

    init {
        val notasDao = NotaDB.getDataBase(application, viewModelScope).notaDao()
        repository = NotaRepository(notasDao)
        totalNotas = repository.totalNotas
    }

    fun insert(nota: Nota) = viewModelScope.launch(Dispatchers.IO){
        repository.inserir(nota)
    }
}