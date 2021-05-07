package intro.android.aplicacao

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import intro.android.aplicacao.api.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Mapa : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var ocorrencias: List<Ocorrencia>
    private lateinit var ocorrenciasLive: LiveData<List<Ocorrencia>>
    private lateinit var voltarMenu : Button
    private lateinit var reportar : ImageButton

    private lateinit var filtro : SearchView

    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    private val UpdateReporteActivityRequestCode = 2

    lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //Shared Preferences Login
        preferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)

        val id = preferences.getInt("id", 0)

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

        //Listar todas as situações (Adicionar markers no mapa com as situações)
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getSituacoes()
        var position : LatLng

        call.enqueue(object : Callback<List<Ocorrencia>> {
            override fun onResponse(call: Call<List<Ocorrencia>>,response: Response<List<Ocorrencia>>) {
                if(response.isSuccessful){
                    ocorrencias = response.body()!!
                    for(ocorrencia in ocorrencias){
                        position = LatLng(ocorrencia.latitude.toString().toDouble(), ocorrencia.longitude.toString().toDouble())
                        if(ocorrencia.utilizador_id == id){
                            mMap.addMarker(MarkerOptions().position(position).title(ocorrencia.id.toString()).snippet("Descrição: " + ocorrencia.descricao))
                        } else {
                            mMap.addMarker(MarkerOptions().position(position).title(ocorrencia.id.toString()).snippet("Descrição: " + ocorrencia.descricao)).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                        }

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

    //Verifica a permissão a localização e depois insere o marcador na localização atual e adiciona uma animação
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

    //Click para obter info individual de uma situacao
    override fun onInfoWindowClick(p0: Marker?) {
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getSituacaoId(p0?.title)

        call.enqueue(object : Callback<List<Ocorrencia>>{
            override fun onResponse(call: Call<List<Ocorrencia>>, response: Response<List<Ocorrencia>>) {
                if(response.isSuccessful){
                    ocorrencias = response.body()!!
                    for(ocorrencia in ocorrencias){
                        val intent = Intent(this@Mapa, EditarReporte::class.java).apply {
                            putExtra(EXTRA_ID, ocorrencia.id.toString())
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

    //Resultado do UPDATE
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //Eliminar Situação
        if(resultCode == Activity.RESULT_OK && data != null && data.action == "REMOVE"){
            var id = data?.getStringExtra(EXTRA_ID)

            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.eliminarSituacaoId(id)
            call.enqueue(object : Callback<OutputEliminar>{
                override fun onFailure(call: Call<OutputEliminar>, t: Throwable) {
                    Toast.makeText(this@Mapa, "${t.message}", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<OutputEliminar>, response: Response<OutputEliminar>) {
                    if(response.isSuccessful){
                        val c: OutputEliminar = response.body()!!
                        Toast.makeText(this@Mapa, c.msg , Toast.LENGTH_LONG).show()
                        finish()
                    }
                }
            })
            return
        }

        //Atualizar Situação
        if(requestCode == UpdateReporteActivityRequestCode && resultCode == Activity.RESULT_OK){
            var descricao = data?.getStringExtra(EXTRA_DESCRICAO).toString()
            var latitude = data?.getStringExtra(EXTRA_LATITUDE).toString()
            var longitude = data?.getStringExtra(EXTRA_LONGITUDE).toString()
            var utilizador_id = data?.getStringExtra(EXTRA_UTILIZADOR_ID).toString().toInt()
            var id = data?.getStringExtra(EXTRA_ID)

            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call =  request.atualizarSituacaoId(id = id, imagem = "Imagem", descricao = descricao, latitude = latitude, longitude = longitude, utilizador_id = utilizador_id)

            call.enqueue(object : Callback<OutputAtualizar>{
                override fun onFailure(call: Call<OutputAtualizar>, t: Throwable) {
                    Toast.makeText(this@Mapa, "${t.message}", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<OutputAtualizar>, response: Response<OutputAtualizar>) {
                    if(response.isSuccessful){
                        val c: OutputAtualizar = response.body()!!
                        Toast.makeText(this@Mapa, c.msg , Toast.LENGTH_LONG).show()
                        finish()
                    }
                }

            })
        }else {
            Toast.makeText(this@Mapa, R.string.permissao_invalida, Toast.LENGTH_SHORT).show()
        }
    }

}