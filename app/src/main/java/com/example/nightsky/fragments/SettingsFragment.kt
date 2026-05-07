package com.example.nightsky.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.nightsky.databinding.FragmentSettingsBinding
import com.example.nightsky.models.ObservationRepository

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val TAG = "SettingsFragment"

    // Gerenciador de requisição de permissões
    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.forEach { (perm, granted) ->
            Log.d(TAG, "Permission $perm: ${if (granted) "GRANTED" else "DENIED"}")
        }
        updatePermissionStatus()
        Toast.makeText(requireContext(), "Permissões atualizadas", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d(TAG, "onCreateView: Inflating settings layout")
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: Loading permission status and stats")
        setupButtons()
        updatePermissionStatus()
        updateStats()
    }

    private fun setupButtons() {
        binding.btnRequestCamera.setOnClickListener {
            requestPermissionsLauncher.launch(arrayOf(Manifest.permission.CAMERA))
        }
        binding.btnRequestLocation.setOnClickListener {
            requestPermissionsLauncher.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ))
        }
        binding.btnRequestAll.setOnClickListener {
            requestPermissionsLauncher.launch(arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ))
        }
        binding.btnOpenSettings.setOnClickListener {
            // Abre as configurações do Android para o seu app específico
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = Uri.fromParts("package", requireContext().packageName, null)
            startActivity(intent)
        }
    }

    private fun updatePermissionStatus() {
        val ctx = requireContext()
        val cameraGranted = ActivityCompat.checkSelfPermission(ctx, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        val locationGranted = ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

        // Atualiza interface da Câmera
        binding.tvCameraStatus.text = if (cameraGranted) "✅ Câmera: Autorizada" else "❌ Câmera: Negada"
        binding.tvCameraStatus.setTextColor(resources.getColor(
            if (cameraGranted) android.R.color.holo_green_light else android.R.color.holo_red_light, null
        ))

        // Atualiza interface da Localização
        binding.tvLocationStatus.text = if (locationGranted) "✅ Localização: Autorizada" else "❌ Localização: Negada"
        binding.tvLocationStatus.setTextColor(resources.getColor(
            if (locationGranted) android.R.color.holo_green_light else android.R.color.holo_red_light, null
        ))

        val allGranted = cameraGranted && locationGranted
        binding.tvOverallStatus.text = if (allGranted) "🌟 Sistema operacional — Todas as permissões ativas"
        else "⚠️ Funcionalidade limitada — Conceda as permissões necessárias"

        Log.d(TAG, "Permission status — Camera: $cameraGranted | Location: $locationGranted")
    }

    private fun updateStats() {
        val allObs = ObservationRepository.getAll()
        val count = allObs.size

        // Agrupa as observações por categoria para gerar o relatório
        val categories = allObs.groupBy { it.category }

        val sb = StringBuilder()
        sb.append("📊 Total de observações: $count\n\n")

        if (allObs.isEmpty()) {
            sb.append("Nenhum registro encontrado.")
        } else {
            categories.forEach { (cat, list) ->
                sb.append("• $cat: ${list.size} registros\n")
            }
        }

        binding.tvStats.text = sb.toString()
    }

    override fun onResume() {
        super.onResume()
        // Atualiza sempre que o usuário voltar para a tela (caso ele tenha mudado permissões nas configs)
        updatePermissionStatus()
        updateStats()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}