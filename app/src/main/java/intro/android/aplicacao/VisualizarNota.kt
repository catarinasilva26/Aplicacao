package intro.android.aplicacao

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import intro.android.aplicacao.adapters.NotaAdapter
import intro.android.aplicacao.entities.Nota
import intro.android.aplicacao.viewModel.NotaViewModel

class VisualizarNota : AppCompatActivity() {

    private lateinit var notaViewModel: NotaViewModel

    private lateinit var editTitulo: EditText
    private lateinit var editConteudo: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visualizar_nota)


        //Nota selecionada
        val id = intent.getStringExtra(MainActivity.EXTRA_ID)
        val titulo = intent.getStringExtra(MainActivity.EXTRA_TITULO)
        val conteudo = intent.getStringExtra(MainActivity.EXTRA_CONTEUDO)

        //Preencher com os dados da nota selecionada
        editTitulo = findViewById(R.id.editText_titulo)
        editTitulo.setText(titulo)
        editConteudo = findViewById(R.id.editText_conteudo)
        editConteudo.setText(conteudo)

        //Bptão Voltar
        val bt_voltar = findViewById<Button>(R.id.bt_voltar)
        bt_voltar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //Botão Guardar
        val bt_guardar = findViewById<Button>(R.id.bt_guardar)
        bt_guardar.setOnClickListener{
            val replyIntent = Intent()
            if(TextUtils.isEmpty(editTitulo.text) || TextUtils.isEmpty(editConteudo.text)){
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val titulo = editTitulo.text.toString()
                replyIntent.putExtra(MainActivity.EXTRA_TITULO, titulo)

                val conteudo = editConteudo.text.toString()
                replyIntent.putExtra(MainActivity.EXTRA_CONTEUDO, conteudo)

                replyIntent.putExtra(MainActivity.EXTRA_ID, id.toString())

                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

}