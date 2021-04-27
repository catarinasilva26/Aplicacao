package intro.android.aplicacao

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
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
    private lateinit var checkBox: CheckBox

    lateinit var sharedPreferences: SharedPreferences
    var isRemenbered = false

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
        checkBox = findViewById(R.id.lembrar)

        //Criar a variével para verificar se queremos login automático ou não
        sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        isRemenbered = sharedPreferences.getBoolean("CHECKBOX", false)

        //Verificamos aqui se o checkbox for selecionado redireciona para o Menu automaticamente
        if(isRemenbered){
            val intent = Intent(this@Login, Menu::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun login(view: View) {

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val nome = editNome.text.toString()
        val pass = editPass.text.toString()
        val checked = checkBox.isChecked

        val call = request.login(nome = nome, password = pass)

        call.enqueue(object : Callback<OutputLogin>{
            override fun onResponse(call: Call<OutputLogin>, response: Response<OutputLogin>) {
                if (response.isSuccessful){
                    val c: OutputLogin = response.body()!!
                    //Toast.makeText(this@Login, c.msg , Toast.LENGTH_LONG).show()
                    if(nome.isEmpty() && pass.isEmpty()){
                        Toast.makeText(this@Login, R.string.campos_vazios, Toast.LENGTH_SHORT).show()
                    } else{
                        if (c.status == "false"){
                            Toast.makeText(this@Login, c.msg , Toast.LENGTH_LONG).show()
                        }else{

                            val editor = sharedPreferences.edit()
                            editor.putInt("id", c.id)
                            editor.putString("nome", nome)
                            editor.putString("pass", pass)
                            editor.putBoolean("CHECKBOX", checked)
                            editor.apply()

                            val intent = Intent(this@Login, Menu::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<OutputLogin>, t: Throwable) {
                Toast.makeText(this@Login, "${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }


}