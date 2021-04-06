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
    private lateinit var editNome: EditText
    private lateinit var editPass: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Botão de redirecionamento para as Notas
        val notas = findViewById<Button>(R.id.bt_notas)
        notas.setOnClickListener {
            val intent = Intent(this@Login, MainActivity::class.java)
            startActivity(intent)
        }

        editNome = findViewById(R.id.nome)
        editPass = findViewById(R.id.password)
    }

    fun login(view: View) {

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val nome = editNome.text.toString()
        val pass = editPass.text.toString()
        val call = request.login(nome = nome, password = pass)

        call.enqueue(object : Callback<OutputLogin>{
            override fun onResponse(call: Call<OutputLogin>, response: Response<OutputLogin>) {
                if (response.isSuccessful){
                    val c: OutputLogin = response.body()!!
                    Toast.makeText(this@Login, c.msg , Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<OutputLogin>, t: Throwable) {
                Toast.makeText(this@Login, "${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }


}