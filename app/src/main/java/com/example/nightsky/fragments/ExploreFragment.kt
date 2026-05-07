package com.example.nightsky.fragments
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nightsky.adapters.ExploreAdapter
import com.example.nightsky.databinding.FragmentExploreBinding
import com.example.nightsky.models.AstroDatabase

class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!
    private val TAG = "ExploreFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d(TAG, "onCreateView: Inflating explore layout")
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: Loading astro objects database")

        setupRecyclerView()
        setupFilterChips()
    }

    private fun setupRecyclerView() {
        val allObjects = AstroDatabase.getObjects()

        // Criando o adapter com a lista completa inicial
        val adapter = ExploreAdapter(allObjects) { astroObj ->
            Toast.makeText(requireContext(), "🔭 ${astroObj.name}: ${astroObj.visibility}", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "Explore item clicked: ${astroObj.name}")
        }

        binding.recyclerExplore.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }

        // Atualiza o contador de itens
        binding.tvExploreCount.text = "${allObjects.size} objetos no catálogo"
    }

    private fun setupFilterChips() {
        binding.chipAll.setOnClickListener { filterBy(null) }
        binding.chipPlanets.setOnClickListener { filterBy("Planeta") }
        binding.chipConstellations.setOnClickListener { filterBy("Constelação") }
        binding.chipGalaxies.setOnClickListener { filterBy("Galáxia") }
        binding.chipStars.setOnClickListener { filterBy("Estrela") }
    }

    private fun filterBy(type: String?) {
        // Lógica de filtragem usando o banco de dados estático
        val filtered = if (type == null) {
            AstroDatabase.getObjects()
        } else {
            AstroDatabase.getObjects().filter { it.type.contains(type, ignoreCase = true) }
        }

        // Atualiza a lista com os dados filtrados
        val adapter = ExploreAdapter(filtered) { astroObj ->
            Toast.makeText(requireContext(), "🔭 ${astroObj.name}", Toast.LENGTH_SHORT).show()
        }

        binding.recyclerExplore.adapter = adapter
        binding.tvExploreCount.text = "${filtered.size} objetos encontrados"
        Log.d(TAG, "Filter applied: $type — ${filtered.size} results")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Limpa o binding para evitar vazamento de memória (Memory Leak)
        _binding = null
    }
}