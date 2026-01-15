package com.example.technical.Ui

import android.app.ProgressDialog
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import com.example.technical.Api.MovieApi
import com.example.technical.Helper.IEssentials
import com.example.technical.Helper.Toaster
import com.example.technical.Model.Movie
import com.example.technical.R
import com.example.technical.Ui.Fragments.FragmentMain
import com.example.technical.ViewModel.ViewModelMovie
import com.example.technical.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), IEssentials {

    private lateinit var binding: ActivityMainBinding;
    private val viewModelMovie: ViewModelMovie by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(binding.root);
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        instantiateObjects()
        addEventListeners()
    }

    override fun instantiateObjects() {
        val fragmentManager = supportFragmentManager;
        fragmentManager.beginTransaction()
            .replace(binding.fragmentContainerView.id, FragmentMain.newInstance())
            .commit();

        val dialog = ProgressDialog(this);
        dialog.setTitle("Loading...");
        dialog.setCancelable(false);
        dialog.show()

        viewModelMovie.loadAll({
            dialog.dismiss();
            Toaster.text(this, "Successfully loaded movies");
        }, { message ->
            dialog.dismiss();
            Toaster.alert(this, message);
        });
    }

    override fun addEventListeners() {

    }

}