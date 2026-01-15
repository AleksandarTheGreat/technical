package com.example.technical.Ui.Fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.technical.Api.MovieApi
import com.example.technical.Helper.IEssentials
import com.example.technical.Helper.Toaster
import com.example.technical.R
import com.example.technical.Ui.Adapter.MovieAdapter
import com.example.technical.ViewModel.ViewModelMovie
import com.example.technical.databinding.ActivityMainBinding
import com.example.technical.databinding.FragmentMainBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.getValue

class FragmentMain : Fragment(), IEssentials {

    private lateinit var binding: FragmentMainBinding;
    private val viewModelMovie: ViewModelMovie by activityViewModels()
    private val handler: Handler = Handler(Looper.getMainLooper());
    private var runnable: Runnable? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.bind(inflater.inflate(R.layout.fragment_main, container, false));

        instantiateObjects()
        addEventListeners()

        return binding.root;
    }

    override fun instantiateObjects() {
        viewModelMovie.mutableLiveDataMovies.observe(this) { list ->
            if (list.isEmpty()){
                Toaster.text(requireContext(), "The movie list is empty");
            }

            val adapter = MovieAdapter(requireContext(), parentFragmentManager,list);
            binding.recyclerViewMain.layoutManager = LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL, false);
            binding.recyclerViewMain.setHasFixedSize(true);
            binding.recyclerViewMain.adapter = adapter;
        }
    }

    override fun addEventListeners() {
        binding.searchViewMain.setOnQueryTextListener(
            object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {

                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (runnable != null) {
                        handler.removeCallbacksAndMessages(null);
                    }

                    runnable = Runnable {
                        if (newText != null && newText.isEmpty()){
                            viewModelMovie.loadAll({}, { message ->
                                Toaster.alert(requireContext(), message);
                            });
                        } else {
                            viewModelMovie.loadSearchedMovies(newText!!, {}, { message ->
                                Toaster.alert(requireContext(), message);
                            });
                        }
                        Log.d("Tag", newText);
                    }
                    handler.postDelayed(runnable!!, 300);

                    return true
                }
            }
        )

        binding.chipGroupMain.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isEmpty()){
                viewModelMovie.loadAll({}, {});
                Log.d("Tag", "Nothing is checked");
                return@setOnCheckedStateChangeListener
            }

            val id = checkedIds.first();
            val chip = group.findViewById<Chip>(id);
            val text = chip.text.toString();
            val tag = chip.tag.toString();

            Log.d("Tag", "${text} - ${tag}");
            viewModelMovie.loadGenreMovies(tag.toInt(), {}, { message ->
                Toaster.alert(requireContext(), message);
            });
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            FragmentMain().apply {
                arguments = Bundle().apply {

                }
            }
    }
}