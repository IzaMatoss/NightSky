package com.example.nightsky.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nightsky.NewObservationActivity
import com.example.nightsky.adapters.ObservationAdapter
import com.example.nightsky.databinding.FragmentLogsBinding
import com.example.nightsky.models.ObservationRepository

class LogsFragment : Fragment() {

    private var _binding: FragmentLogsBinding? = null
    private val binding get() = _binding!!

    private val tagName = "LogsFragment"
    private lateinit var adapter: ObservationAdapter

    private val newObsLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            refreshList()
            Toast.makeText(requireContext(), "✨ Observação salva com sucesso", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupButtons()
        refreshList()
    }

    private fun setupRecyclerView() {
        adapter = ObservationAdapter(ObservationRepository.getAll().toMutableList()) { observation ->
            Toast.makeText(
                requireContext(),
                "${observation.astroName}: ${observation.notes}",
                Toast.LENGTH_SHORT
            ).show()
            Log.d(tagName, "Observação clicada: ${observation.astroName}")
        }

        binding.recyclerLogs.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerLogs.adapter = adapter
    }

    private fun setupButtons() {
        binding.fabNewObs.setOnClickListener {
            val intent = Intent(requireContext(), NewObservationActivity::class.java)
            newObsLauncher.launch(intent)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun refreshList() {
        val observations = ObservationRepository.getAll()
        adapter.updateData(observations)
        binding.tvObsCount.text = "${observations.size} observações registradas"
    }

    override fun onResume() {
        super.onResume()
        if (::adapter.isInitialized) {
            refreshList()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
