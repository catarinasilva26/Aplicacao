package intro.android.aplicacao

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import intro.android.aplicacao.api.EndPoints
import intro.android.aplicacao.api.OutputLogin
import intro.android.aplicacao.api.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Botão de redirecionamento para as Notas
        val notas = findViewById<Button>(R.id.bt_notas)
        notas.setOnClickListener {
            val intent = Intent(this@Login, MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun login(view: View) {

        val editNome = findViewById<EditText>(R.id.nome)
        val editPass = findViewById<EditText>(R.id.password)

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.login(nome = editNome.toString(), password = editPass.toString())

        call.enqueue(object : Callback<OutputLogin>{
            override fun onResponse(call: Call<OutputLogin>, response: Response<OutputLogin>) {
                if (response.isSuccessful){
                    val c: OutputLogin = response.body()!!
                    Toast.makeText(this@Login, c.nome + "-" + c.password, Toast.LENGTH_LONG)
                }
            }

            override fun onFailure(call: Call<OutputLogin>, t: Throwable) {
                Toast.makeText(this@Login, "${t.message}", Toast.LENGTH_LONG)
            }
        })
    }


}