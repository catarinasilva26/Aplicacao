package intro.android.aplicacao

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import intro.android.aplicacao.api.EndPoints
import intro.android.aplicacao.api.ServiceBuilder

class EditarReporte : AppCompatActivity() {
    val REQUEST_CODE = 100

    lateinit var preferences: SharedPreferences
    private lateinit var imagemSelecionada: ImageView
    private lateinit var editDescricao: EditText
    private lateinit var voltarMenu : Button
    private lateinit var eliminarSituacao :Button

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    //Localização
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_reporte)

        //Inicializar o fusedLocationClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        //Id do utilizador logado
        preferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val id = preferences.getInt("id", 0)

        //Reporte selecionado
        val idReporte = intent.getStringExtra(Mapa.EXTRA_ID)
        val imagem = intent.getStringExtra(Mapa.EXTRA_IMAGE)
        val descricao = intent.getStringExtra(Mapa.EXTRA_DESCRICAO)
        val latitudeReporte = intent.getStringExtra(Mapa.EXTRA_LATITUDE)
        val longitudeReporte = intent.getStringExtra(Mapa.EXTRA_LONGITUDE)
        val utilizador_id = intent.getStringExtra(Mapa.EXTRA_UTILIZADOR_ID)

        editDescricao = findViewById(R.id.descricao)
        editDescricao.setText(descricao)

        imagemSelecionada = findViewById(R.id.imagemSelecionada)

        //Momento em que recebemos as coordenadas a partir do LocationResult e atribuimos a cada variável
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                super.onLocationResult(p0)
                lastLocation = p0?.lastLocation!!
                latitude =lastLocation.latitude
                longitude = lastLocation.longitude
            }
        }

        //Botão para voltar para o menu
        voltarMenu = findViewById(R.id.bt_menu)
        voltarMenu.setOnClickListener {
            val intent = Intent(this@EditarReporte, Menu::class.java)
            startActivity(intent)
            finish()
        }

        //Eliminar reporte
        val bt_eliminar = findViewById<ImageButton>(R.id.bt_eliminar)
        bt_eliminar.setOnClickListener {
            val reply = Intent()
            if(utilizador_id == id.toString()){
                //ID de reporte
                reply.putExtra(Mapa.EXTRA_ID, idReporte.toString())
                reply.setAction("REMOVE")
                setResult(Activity.RESULT_OK, reply)
            } else {
                setResult(Activity.RESULT_CANCELED, reply)
            }

            finish()
        }


        //Editar reporte
        val bt_guardar = findViewById<Button>(R.id.bt_guardar)
        bt_guardar.setOnClickListener {
            val replyIntent = Intent()
            if(utilizador_id == id.toString()){
                //Descrição
                val descricao = editDescricao.text.toString()
                replyIntent.putExtra(Mapa.EXTRA_DESCRICAO, descricao)

                //Mantém o utilizador id
                replyIntent.putExtra(Mapa.EXTRA_UTILIZADOR_ID, id.toString())

                //Localização
                replyIntent.putExtra(Mapa.EXTRA_LATITUDE, latitude.toString())
                replyIntent.putExtra(Mapa.EXTRA_LONGITUDE, longitude.toString())

                //ID de reporte
                replyIntent.putExtra(Mapa.EXTRA_ID, idReporte.toString())

                setResult(Activity.RESULT_OK, replyIntent)

            }else {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            }
            finish()
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