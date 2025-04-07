package com.example.mapassesmenttask.ui.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mapassesmenttask.R
import com.example.mapassesmenttask.databinding.ActivityMapListBinding
import com.example.mapassesmenttask.ui.viewmodel.HomeViewModel
import org.koin.android.ext.android.inject


class MapListActivity : AppCompatActivity() {
    private var binding: ActivityMapListBinding? = null
    private val viewModel : HomeViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMapListBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding?.uiRecyclerView?.layoutManager = LinearLayoutManager(this)

        val adapter = Adapter(emptyList())
        binding?.uiRecyclerView?.adapter = adapter

        viewModel.mapResponseLD.observe(this){
            adapter.updateData(it)
        }

        binding?.uiIvBackButton?.setOnClickListener {
            finish()
        }
    }
}