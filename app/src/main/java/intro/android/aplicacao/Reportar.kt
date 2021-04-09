package intro.android.aplicacao

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import intro.android.aplicacao.api.EndPoints
import intro.android.aplicacao.api.OutputReportar
import intro.android.aplicacao.api.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Reportar : AppCompatActivity() {

    private lateinit var voltarMenu : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reportar)

        //Botão para voltar para o menu
        voltarMenu = findViewById(R.id.bt_menu)
        voltarMenu.setOnClickListener {
            val intent = Intent(this@Reportar, Menu::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun reportar(view: View) {

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.reportar(imagem ="teste", descricao =  "teste", latitude = 7, longitude =  7, utilizador_id = 1)

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
}