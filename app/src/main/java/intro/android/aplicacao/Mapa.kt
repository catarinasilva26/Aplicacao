package intro.android.aplicacao

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import intro.android.aplicacao.api.EndPoints
import intro.android.aplicacao.api.Ocorrencia
import intro.android.aplicacao.api.ServiceBuilder
import intro.android.aplicacao.api.Utilizador
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Mapa : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var ocorrencias: List<Ocorrencia>
    private lateinit var voltarMenu : Button
    private lateinit var reportar : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //Botão para voltar para o menu
        voltarMenu = findViewById(R.id.bt_menu)
        voltarMenu.setOnClickListener {
            val intent = Intent(this@Mapa, Menu::class.java)
            startActivity(intent)
            finish()
        }

        //Botão para reportar situação
        reportar = findViewById(R.id.reportarSituacao)
        reportar.setOnClickListener{
            val intent = Intent(this@Mapa, Reportar::class.java)
            startActivity(intent)
            finish()
        }

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getSituacoes()
        var position : LatLng

        call.enqueue(object : Callback<List<Ocorrencia>> {
            override fun onResponse(call: Call<List<Ocorrencia>>,response: Response<List<Ocorrencia>>) {
                if(response.isSuccessful){
                    ocorrencias = response.body()!!
                    for(ocorrencia in ocorrencias){
                        position = LatLng(ocorrencia.latitude.toString().toDouble(), ocorrencia.longitude.toString().toDouble())
                        mMap.addMarker(MarkerOptions().position(position).title(ocorrencia.descricao))
                    }
                }
            }
            override fun onFailure(call: Call<List<Ocorrencia>>, t: Throwable) {
                Toast.makeText(this@Mapa, "${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(41.4079700, -8.5197800)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marcado em Famalicão"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}