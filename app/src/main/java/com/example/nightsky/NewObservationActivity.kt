package com.example.nightsky

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
// IMPORTANTE: Ajuste esses imports para seu pacote atual
import com.example.nightsky.R
import com.example.nightsky.databinding.ActivityNewObservationBinding
import com.example.nightsky.models.Observation
import com.example.nightsky.models.ObservationRepository
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class NewObservationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewObservationBinding
    private val TAG = "NewObservationActivity"
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var currentLat = 0.0
    private var currentLng = 0.0
    private var photoUri: String? = null
    private var currentPhotoPath: String? = null

    private val categories = listOf("Planeta", "Constelação", "Galáxia", "Cometa", "Estrela", "Nebulosa", "Outro")

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            currentPhotoPath?.let { path ->
                photoUri = Uri.fromFile(File(path)).toString()
                binding.ivPhotoPreview.setImageURI(Uri.parse(photoUri))
                binding.tvPhotoStatus.text = "📸 Foto capturada com sucesso"
                Log.d(TAG, "Photo captured: $photoUri")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewObservationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setupCategorySpinner()
        setupButtons()
        setupToolbar()
        captureLocation()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupCategorySpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = adapter
    }

    private fun setupButtons() {
        binding.btnCaptureFoto.setOnClickListener { openCamera() }
        binding.btnRefreshLocation.setOnClickListener { captureLocation() }
        binding.btnSave.setOnClickListener { saveObservation() }
        binding.btnCancel.setOnClickListener { finish() }
    }

    private fun captureLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            binding.tvLocationStatus.text = "⚠️ Permissão de localização necessária"
            // O ideal aqui seria pedir a permissão se não tiver
            return
        }

        binding.tvLocationStatus.text = "📡 Buscando sinal GPS..."

        val cancellationToken = CancellationTokenSource()
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cancellationToken.token)
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    currentLat = location.latitude
                    currentLng = location.longitude
                    binding.tvLocationStatus.text = "✅ GPS: %.4f°, %.4f°".format(currentLat, currentLng)
                } else {
                    binding.tvLocationStatus.text = "⚠️ Localização não disponível"
                }
            }
            .addOnFailureListener {
                binding.tvLocationStatus.text = "❌ Erro ao obter localização"
            }
    }

    private fun openCamera() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permissão de câmera necessária", Toast.LENGTH_SHORT).show()
            return
        }

        val photoFile = createImageFile()
        val photoUriFile = FileProvider.getUriForFile(this, "${packageName}.provider", photoFile)
        currentPhotoPath = photoFile.absolutePath

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, photoUriFile)
        }
        takePictureLauncher.launch(intent)
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("ASTRO_${timeStamp}_", ".jpg", storageDir)
    }

    private fun saveObservation() {
        val name = binding.etAstroName.text.toString().trim()
        val notes = binding.etNotes.text.toString().trim()
        val category = binding.spinnerCategory.selectedItem.toString()

        if (name.isEmpty()) {
            binding.etAstroName.error = "Informe o nome do objeto"
            return
        }

        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
        val newObs = Observation(
            id = 0,
            astroName = name,
            category = category,
            timestamp = timestamp,
            latitude = currentLat,
            longitude = currentLng,
            photoUri = photoUri,
            notes = notes.ifEmpty { "Sem descrição" }
        )

        ObservationRepository.add(newObs)
        Toast.makeText(this, "✨ Observação registrada!", Toast.LENGTH_SHORT).show()
        setResult(Activity.RESULT_OK)
        finish()
    }
}