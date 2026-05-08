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
    private val newObsLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->

        if (result.resultCode == Activity.RESULT_OK) {
            refreshList()
            Toast.makeText(
                requireContext(),
        }