package intro.android.aplicacao

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import intro.android.aplicacao.api.EndPoints
import intro.android.aplicacao.api.OutputReportar
import intro.android.aplicacao.api.ServiceBuilder
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class Reportar : AppCompatActivity() {

    lateinit var preferences: SharedPreferences
    private lateinit var voltarMenu : Button
    private lateinit var editDescr: EditText
    private lateinit var imagemSelecionada: ImageView

    private var fileUri: Uri? = null
    private lateinit var bitmap: Bitmap
    private var mediaPath: String? = null
    private var postPath: String? = null


    val REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reportar)

        preferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)


        //Botão para voltar para o menu
        voltarMenu = findViewById(R.id.bt_menu)
        voltarMenu.setOnClickListener {
            val intent = Intent(this@Reportar, Menu::class.java)
            startActivity(intent)
            finish()
        }

        editDescr = findViewById(R.id.descricao)
        imagemSelecionada = findViewById(R.id.imagemSelecionada)

    }

    fun reportar(view: View) {

        val request = ServiceBuilder.buildService(EndPoints::class.java)

        val descricao = editDescr.text.toString()
        val id = preferences.getInt("id", 0)

        val call = request.reportar(imagem ="Imagem", descricao =  descricao, latitude = "37.5231028", longitude =  " -8.78671111 ", utilizador_id = id)

        call.enqueue(object : Callback<OutputReportar>{
            override fun onResponse(call: Call<OutputReportar>, response: Response<OutputReportar>) {
                if(response.isSuccessful){
                    val c: OutputReportar = response.body()!!
                    Toast.makeText(this@Reportar, c.msg , Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<OutputReportar>, t: Throwable) {
                Toast.makeText(this@Reportar, "${t.message}", Toast.LENGTH_LONG).show()
            }

        })
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
}