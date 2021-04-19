package intro.android.aplicacao

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import intro.android.aplicacao.api.EndPoints
import intro.android.aplicacao.api.Ocorrencia
import intro.android.aplicacao.api.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Mapa : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var ocorrencias: List<Ocorrencia>
    private lateinit var voltarMenu : Button
    private lateinit var reportar : ImageButton

    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    private val UpdateReporteActivityRequestCode = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //Iniciar fusedlocationproviderclient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

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
                        mMap.addMarker(MarkerOptions().position(position).title(ocorrencia.id.toString()).snippet("Descrição: " + ocorrencia.descricao))
                    }

                }
            }
            override fun onFailure(call: Call<List<Ocorrencia>>, t: Throwable) {
                Toast.makeText(this@Mapa, "${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }


    companion object{
        const val EXTRA_UTILIZADOR_ID = "utilizador_id"
        const val EXTRA_IMAGE = "image"
        const val EXTRA_DESCRICAO = "descricao"
        const val EXTRA_LATITUDE = "latitude"
        const val EXTRA_LONGITUDE = "longitude"
        const val EXTRA_ID = "id"

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        //val sydney = LatLng(41.4079700, -8.5197800)
        //mMap.addMarker(MarkerOptions().position(sydney).title("Marcado em Famalicão"))
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        mMap.setOnInfoWindowClickListener(this)
        setUpMap()
    }

    fun setUpMap(){
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        } else {
            //Insere um marcador com a minha localização
            mMap.isMyLocationEnabled = true

            //Após autorização de acesso à localização
            fusedLocationClient.lastLocation.addOnSuccessListener(this) {
                location ->

                if(location != null){
                    lastLocation = location
                    //Toast.makeText(this@Mapa, lastLocation.toString(), Toast.LENGTH_SHORT).show()
                    val currentLatLong = LatLng(location.latitude, location.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 12f))
                }
            }
        }
    }

    override fun onInfoWindowClick(p0: Marker?) {
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getSituacaoId(p0?.title)

        call.enqueue(object : Callback<List<Ocorrencia>>{
            override fun onResponse(call: Call<List<Ocorrencia>>, response: Response<List<Ocorrencia>>) {
                if(response.isSuccessful){
                    ocorrencias = response.body()!!
                    for(ocorrencia in ocorrencias){
                        val intent = Intent(this@Mapa, EditarReporte::class.java).apply {
                            putExtra(EXTRA_ID, ocorrencia.id)
                            putExtra(EXTRA_IMAGE, ocorrencia.imagem)
                            putExtra(EXTRA_DESCRICAO, ocorrencia.descricao)
                            putExtra(EXTRA_LATITUDE, ocorrencia.latitude)
                            putExtra(EXTRA_LONGITUDE, ocorrencia.longitude)
                            putExtra(EXTRA_UTILIZADOR_ID, ocorrencia.utilizador_id.toString())
                        }
                        startActivityForResult(intent, UpdateReporteActivityRequestCode)
                    }
                }
            }

            override fun onFailure(call: Call<List<Ocorrencia>>, t: Throwable) {
                Toast.makeText(this@Mapa, "${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == UpdateReporteActivityRequestCode && resultCode == Activity.RESULT_OK){
            var descricao = data?.getStringExtra(EXTRA_DESCRICAO).toString()
            var latitude = data?.getStringExtra(EXTRA_LATITUDE).toString()
            var longitude = data?.getStringExtra(EXTRA_LONGITUDE).toString()
            var utilizador_id = data?.getStringExtra(EXTRA_UTILIZADOR_ID).toString()

        }else {
            Toast.makeText(this@Mapa, "Permissão Inválida: Não pode editar", Toast.LENGTH_SHORT).show()
        }
    }

}