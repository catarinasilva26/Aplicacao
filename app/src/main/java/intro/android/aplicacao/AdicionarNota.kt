package intro.android.aplicacao

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText

class AdicionarNota : AppCompatActivity() {

    private lateinit var editTitulo: EditText
    private lateinit var editConteudo: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicionar_nota)

         editTitulo = findViewById(R.id.editText_titulo)
        editConteudo = findViewById(R.id.editText_conteudo)
        val bt_adicionar = findViewById<Button>(R.id.bt_adicionar)
        bt_adicionar.setOnClickListener{
            val replyIntent = Intent()
            if(TextUtils.isEmpty(editTitulo.text) && TextUtils.isEmpty(editConteudo.text)){
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val titulo = editTitulo.text.toString()
                val conteudo = editConteudo.text.toString()
                replyIntent.putExtra(EXTRA_REPLY, titulo)
                replyIntent.putExtra(EXTRA_REPLY, conteudo)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }
    companion object{
        const val  EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"
    }
}
