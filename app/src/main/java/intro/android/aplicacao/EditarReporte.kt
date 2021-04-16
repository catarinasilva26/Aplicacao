package intro.android.aplicacao

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView

class EditarReporte : AppCompatActivity() {
    val REQUEST_CODE = 100

    private lateinit var imagemSelecionada: ImageView
    private lateinit var editDescricao: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_reporte)

        //Reporte selecionado
        //Nota selecionada
        val utilizador_id = intent.getStringExtra(Mapa.EXTRA_UTILIZADOR_ID)
        val imagem = intent.getStringExtra(Mapa.EXTRA_IMAGE)
        val descricao = intent.getStringExtra(Mapa.EXTRA_DESCRICAO)
        val latitude = intent.getStringExtra(Mapa.EXTRA_LATITUDE)
        val longitude = intent.getStringExtra(Mapa.EXTRA_LONGITUDE)

        editDescricao = findViewById(R.id.descricao)
        editDescricao.setText(descricao)

        imagemSelecionada = findViewById(R.id.imagemSelecionada)
    }

    //Abrir a galeria para selecionar a foto pretendida
    fun imagem(view: View) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    //Mostrar no ecrã a imagem selecionada
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            imagemSelecionada.setImageURI(data?.data)
        }
    }

    fun editar(view: View) {}
}