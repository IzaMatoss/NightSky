package com.example.nightsky.fragments // Pacote ajustado

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.*
import com.example.nightsky.databinding.FragmentMapDataBinding
import com.example.nightsky.models.ObservationRepository

class MapDataFragment : Fragment() {

    private var _binding: FragmentMapDataBinding? = null
    private val binding get() = _binding!!
    private val TAG = "MapDataFragment"
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationCallback: LocationCallback? = null
    private var isTracking = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d(TAG, "onCreateView: Inflating map layout")
        _binding = FragmentMapDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: Setting up location services")

        // Inicializa o cliente de localização usando a Activity do Fragment
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        setupLocationCallback()
        setupButtons()
        loadObservationLocations()
    }

    private fun setupLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                // Pega a última localização válida da lista
                result.lastLocation?.let { updateLocationUI(it) }
            }
        }
    }

    private fun setupButtons() {
        binding.btnStartTracking.setOnClickListener {
            if (!isTracking) startLocationTracking()
            else stopLocationTracking()
        }
        binding.btnRefreshObs.setOnClickListener { loadObservationLocations() }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationTracking() {
        if (!hasLocationPermission()) {
            binding.tvLocationInfo.text = "⚠️ Permissão de localização não concedida.\nAcesse Configurações para habilitá-la."
            return
        }

        Log.d(TAG, "startLocationTracking: Starting GPS tracking")
        isTracking = true
        binding.btnStartTracking.text = "⏹ Parar Rastreamento"
        binding.tvSignalStatus.text = "📡 Conectando a satélites GPS..."
        binding.progressLocation.visibility = View.VISIBLE

        // Configuração moderna do LocationRequest (Google Play Services 21+)
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000L)
            .setMinUpdateIntervalMillis(2000L)
            .build()

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback!!, null)
        Toast.makeText(requireContext(), "GPS ativado", Toast.LENGTH_SHORT).show()
    }

    private fun stopLocationTracking() {
        Log.d(TAG, "stopLocationTracking: Stopping GPS updates")
        isTracking = false
        binding.btnStartTracking.text = "▶ Iniciar Rastreamento"
        binding.tvSignalStatus.text = "💤 GPS em standby"
        binding.progressLocation.visibility = View.GONE
        locationCallback?.let { fusedLocationClient.removeLocationUpdates(it) }
    }

    private fun updateLocationUI(location: Location) {
        Log.d(TAG, "Location updated: ${location.latitude}, ${location.longitude}")
        binding.tvSignalStatus.text = "✅ Sinal GPS ativo"
        binding.progressLocation.visibility = View.GONE
        binding.tvLatitude.text = "Latitude: %.6f°".format(location.latitude)
        binding.tvLongitude.text = "Longitude: %.6f°".format(location.longitude)
        binding.tvAltitude.text = "Altitude: %.1f m".format(location.altitude)
        binding.tvAccuracy.text = "Precisão: ±%.1f m".format(location.accuracy)

        val hemisphere = if (location.latitude >= 0) "Norte ↑" else "Sul ↓"
        val meridian = if (location.longitude >= 0) "Leste →" else "Oeste ←"

        binding.tvLocationInfo.text = "📍 Hemisfério $hemisphere | Meridiano $meridian\n" +
                "Velocidade: %.1f km/h".format(location.speed * 3.6f)
    }

    private fun loadObservationLocations() {
        val obs = ObservationRepository.getAll()
        if (obs.isEmpty()) {
            binding.tvObsLocations.text = "Nenhuma observação registrada ainda."
            return
        }

        val sb = StringBuilder()
        obs.take(5).forEachIndexed { i, o ->
            sb.append("${i + 1}. ${o.astroName} (${o.category})\n")
            sb.append("   📍 %.4f°, %.4f°\n".format(o.latitude, o.longitude))
            sb.append("   🕐 ${o.timestamp}\n\n")
        }
        if (obs.size > 5) sb.append("... e mais ${obs.size - 5} observações")

        binding.tvObsLocations.text = sb.toString()
        Log.d(TAG, "Loaded ${obs.size} observation locations")
    }

    private fun hasLocationPermission() =
        ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

    override fun onResume() {
        super.onResume()
        loadObservationLocations()
    }

    override fun onPause() {
        super.onPause()
        // Importante: parar o GPS ao sair da tela para economizar bateria
        if (isTracking) stopLocationTracking()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}