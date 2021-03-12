package intro.android.aplicacao

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class VisualizarNota : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visualizar_nota)

        /*val titulo = intent.getStringExtra(EXTRA_MESSAGE_TITULO)

        val editTitulo = findViewById<EditText>(R.id.editText_titulo).apply {
           text = titulo
        }*/

        val bt_voltar = findViewById<Button>(R.id.bt_voltar)
        bt_voltar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}