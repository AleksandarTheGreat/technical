package com.example.technical.Ui.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.technical.Helper.IEssentials
import com.example.technical.R
import com.example.technical.databinding.FragmentFavoritesBinding


class FragmentFavorites : Fragment(), IEssentials {

    private lateinit var binding: FragmentFavoritesBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.bind(inflater.inflate(R.layout.fragment_favorites, container, false));

        instantiateObjects()
        addEventListeners()

        return binding.root;
    }

    override fun instantiateObjects() {

    }

    override fun addEventListeners() {
        binding.imageViewBack.setOnClickListener {
            parentFragmentManager.popBackStack();
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            FragmentFavorites().apply {
                arguments = Bundle().apply {

                }
            }
    }
}