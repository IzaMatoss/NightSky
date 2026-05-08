package com.example.nightsky.fragments

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
    private val TAG = "LogsFragment"
    private lateinit var adapter: ObservationAdapter

    // Launcher para receber o resultado da tela de nova observação
    private val newObsLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            refreshList()
            Toast.makeText(requireContext(), "🌟 Nova observação adicionada!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d(TAG, "onCreateView: Inflating logs layout")
        _binding = FragmentLogsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: Configuring RecyclerView")

        setupRecyclerView()
        setupFab()
        updateStats()
    }

    private fun setupRecyclerView() {
        // Criando o adapter com a lista vinda do repositório
        adapter = ObservationAdapter(
            ObservationRepository.getAll().toMutableList()
        ) { obs ->
            // Clique no item da lista
            Toast.makeText(requireContext(), "📍 ${obs.astroName} — ${obs.timestamp}", Toast.LENGTH_SHORT).show()
        }

        binding.recyclerLogs.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@`LogsFragment.kt`.adapter
        }
    }

    private fun setupFab() {
        binding.fabNewObs.setOnClickListener {
            Log.d(TAG, "FAB clicked: Opening NewObservationActivity")
            val intent = Intent(requireContext(), NewObservationActivity::class.java)
            newObsLauncher.launch(intent)
        }
    }

    private fun refreshList() {
        // Atualiza os dados do adapter
        adapter.updateData(ObservationRepository.getAll())
        updateStats()
        Log.d(TAG, "List refreshed. Total: ${ObservationRepository.count()}")
    }

    private fun updateStats() {
        val count = ObservationRepository.count()
        binding.tvObsCount.text = "$count observações registradas"
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: Refreshing observations list")
        refreshList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView: Cleaning up binding")
        _binding = null
    }
}