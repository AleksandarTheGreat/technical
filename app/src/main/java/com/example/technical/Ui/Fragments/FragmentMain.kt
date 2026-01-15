package com.example.technical.Ui.Fragments

import android.graphics.Color
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

            binding.recyclerViewMain.apply {
                val adapter = MovieAdapter(requireContext(), parentFragmentManager,list);

                this.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL, false);
                this.setHasFixedSize(true)
                this.adapter = adapter;
            }
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
                        binding.progressBarMain.visibility = View.VISIBLE
                        if (newText != null && newText.isEmpty()){
                            viewModelMovie.loadAll({
                                binding.progressBarMain.visibility = View.GONE
                            }, { message ->
                                binding.progressBarMain.visibility = View.GONE
                                Toaster.alert(requireContext(), message);
                            });
                        } else {
                            viewModelMovie.loadSearchedMovies(newText!!, {
                                binding.progressBarMain.visibility = View.GONE
                            }, { message ->
                                binding.progressBarMain.visibility = View.GONE
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
            binding.progressBarMain.visibility = View.VISIBLE
            if (checkedIds.isEmpty()){
                viewModelMovie.loadAll({
                    binding.progressBarMain.visibility = View.GONE
                }, { message ->
                    binding.progressBarMain.visibility = View.GONE
                    Toaster.text(requireContext(), message);
                });
                Log.d("Tag", "Nothing is checked");
                return@setOnCheckedStateChangeListener
            }

            val id = checkedIds.first();
            val chip = group.findViewById<Chip>(id);
            val text = chip.text.toString();
            val tag = chip.tag.toString();

            Log.d("Tag", "${text} - ${tag}");
            viewModelMovie.loadGenreMovies(tag.toInt(), {
                binding.progressBarMain.visibility = View.GONE;
            }, { message ->
                binding.progressBarMain.visibility = View.GONE;
                Toaster.alert(requireContext(), message);
            });
        }

        binding.imageViewFavorites.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, FragmentFavorites.newInstance())
                .setReorderingAllowed(true)
                .addToBackStack("backstack")
                .commit();
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