package intro.android.aplicacao

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.View
import android.widget.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import intro.android.aplicacao.adapters.NotaAdapter
import intro.android.aplicacao.entities.Nota
import intro.android.aplicacao.viewModel.NotaViewModel


class MainActivity : AppCompatActivity(){

    private lateinit var notaViewModel: NotaViewModel
    private val newNotaActivityRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //Recycler View
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = NotaAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        //recyclerView.setOnClickListener(View.OnClickListener { Toast.makeText(this, "Click", Toast.LENGTH_LONG).show() })

        //View Model
        notaViewModel = ViewModelProvider(this).get(NotaViewModel::class.java)
        notaViewModel.totalNotas.observe(this, {notas -> notas?.let {adapter.setNotas(it)}})

        //Adicionar Nota
        val adicionar = findViewById<ImageButton>(R.id.adicionarNota)
        adicionar.setOnClickListener{val intent = Intent(this@MainActivity, AdicionarNota::class.java)
        startActivityForResult(intent, newNotaActivityRequestCode)}

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == newNotaActivityRequestCode && resultCode == Activity.RESULT_OK){
            var titulo = data?.getStringExtra(AdicionarNota.EXTRA_REPLY_TITULO).toString()
            var conteudo = data?.getStringExtra(AdicionarNota.EXTRA_REPLY_CONT).toString()
            var notas = Nota(titulo = titulo, conteudo = conteudo)
            notaViewModel.insert(notas)

        }else {
            Toast.makeText(applicationContext, "Campo vazio: não inserido", Toast.LENGTH_LONG).show()
        }
    }

}