package intro.android.aplicacao

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class AdicionarNota : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicionar_nota)
    }
    companion object{
        const val  EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"
    }
}
