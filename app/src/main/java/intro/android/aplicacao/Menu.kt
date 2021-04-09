package intro.android.aplicacao

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class Menu : AppCompatActivity() {

    lateinit var preferences: SharedPreferences
    private lateinit var logout : Button
    private lateinit var mapa : Button
    private lateinit var reportar : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        preferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)

        logout = findViewById(R.id.logout)
        logout.setOnClickListener {
            val editor: SharedPreferences.Editor = preferences.edit()
            editor.clear()
            editor.apply()

            val intent = Intent(this@Menu, Login::class.java)
            startActivity(intent)
            finish()
        }

        mapa = findViewById(R.id.bt_mapa)
        mapa.setOnClickListener {
            val intent = Intent(this@Menu, Mapa::class.java)
            startActivity(intent)
            finish()
        }

        reportar = findViewById(R.id.bt_reportar)
        reportar.setOnClickListener {
            val intent = Intent(this@Menu, Reportar::class.java)
            startActivity(intent)
            finish()
        }

    }
}