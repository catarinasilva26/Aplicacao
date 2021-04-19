package intro.android.aplicacao

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
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

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var lastLocation: Location

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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        //Momento em que recebemos as coordenadas a partir do LocationResult e atribuimos a cada variável
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                super.onLocationResult(p0)
                lastLocation = p0?.lastLocation!!
                latitude =lastLocation.latitude
                longitude = lastLocation.longitude

            }
        }

        createLocationRequest()
    }

    //Criar o pedido para obter a localização de 10 em 10 segundos e com a maior precisão possivel
    private fun createLocationRequest(){
        locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    //onPause para otimizar a utilização da app
    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    //onResume onde iremos receber as coordenadas
    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    //Dispara para fazer pedido de coordenadas ao satélite
    private  fun startLocationUpdates(){
        if( ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        //Fazemos o pedido de coordenadas com base no request e no callback
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    fun reportar(view: View) {

        val request = ServiceBuilder.buildService(EndPoints::class.java)

        val descricao = editDescr.text.toString()
        val id = preferences.getInt("id", 0)

        val latitude = latitude
        val longitude = longitude

        val call = request.reportar(imagem ="Imagem", descricao =  descricao, latitude = latitude.toString(), longitude =  longitude.toString(), utilizador_id = id)

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