package com.example.nightsky.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
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

        adapter = ObservationAdapter(
            ObservationRepository.getAll().toMutableList()
        ) { observation ->

            AlertDialog.Builder(requireContext())
                .setTitle(observation.astroName)
                .setMessage(
                    """
                    Nome: ${observation.astroName}
                    
                    Notas:
                    ${observation.notes}
                    """.trimIndent()
                )
                .setPositiveButton("OK", null)
                .show()

            Log.d(tagName, "Observação clicada: ${observation.astroName}")
        }

        binding.recyclerLogs.layoutManager =
            LinearLayoutManager(requireContext())

        binding.recyclerLogs.adapter = adapter
    }

    private fun setupButtons() {

        binding.fabNewObs.setOnClickListener {

            val intent = Intent(
                requireContext(),
                NewObservationActivity::class.java
            )

            startActivity(intent)
        }
    }

    private fun refreshList() {

        val observations = ObservationRepository.getAll()

        if (::adapter.isInitialized) {
            adapter.updateData(observations)
        }

        binding.tvObsCount.text =
            "${observations.size} observações registradas"
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