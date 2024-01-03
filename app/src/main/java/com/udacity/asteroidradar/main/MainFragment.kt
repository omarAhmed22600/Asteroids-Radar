package com.udacity.asteroidradar.main

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    private lateinit var adapter: AsteroidsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        adapter = AsteroidsAdapter(AsteroidsAdapter.AsteroidListener { asteroid ->
            viewModel.onAsteroidClicked(asteroid)
        })

        viewModel.asteroidsList.observe(viewLifecycleOwner, Observer { asteroids ->
            if (asteroids.isNotEmpty()) {
                Log.i("MainFragement", "Astroids Changed:" + asteroids[0].codename)
                adapter.submitList(asteroids)
            }
        })
        viewModel.pictureOfDay.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.activityMainImageOfTheDay.contentDescription =
                    getString(R.string.this_is_nasa_s_picture_of_day_showing_a_live_picture_taken_by_nasa)
            }
        })
        viewModel.navigateToDetailFragment.observe(
            viewLifecycleOwner,
            Observer { navigationStatus ->
//            Log.i("MainFragment","Fragment Navigation:"+ (asteroid == null).toString())
                if (navigationStatus) {
                    findNavController().navigate(MainFragmentDirections.actionShowDetail(viewModel.selectedAsteroid.value!!))
                    viewModel.doneNavigating()
                }
            })
        viewModel.selectedFilter.observe(viewLifecycleOwner, Observer {
            viewModel.getAsteroids()
            Toast.makeText(this.context, "Feeds Refreshed on Filter", Toast.LENGTH_SHORT).show()
        })


        binding.asteroidRecycler.adapter = adapter

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_week_asteroids -> viewModel.onShowWeekSelected()
            R.id.show_today_asteroids -> viewModel.onShowTodaySelected()
            R.id.show_save_asteroids -> viewModel.onShowSavedSelected()
        }
        return true
    }
}
