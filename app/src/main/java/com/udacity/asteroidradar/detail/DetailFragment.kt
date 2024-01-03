package com.udacity.asteroidradar.detail


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {
    private lateinit var viewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentDetailBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val asteroid = DetailFragmentArgs.fromBundle(requireArguments()).selectedAsteroid
        Log.i("DetailsFragment", asteroid.codename)
        binding.asteroid = asteroid



        viewModel.explanationDialogStatus.observe(viewLifecycleOwner) { displayExplanationDialog ->
            if (displayExplanationDialog) {
                displayAstronomicalUnitExplanationDialog()
                binding.helpButton.contentDescription =
                    getString(R.string.astronomica_unit_explanation)
                viewModel.onDisplayExplanationDialogDone()
            }
        }
        if (asteroid.isPotentiallyHazardous) {
            binding.activityMainHazardousOrNot.contentDescription =
                getString(R.string.potentially_hazardous_asteroid_image)
        } else {
            binding.activityMainHazardousOrNot.contentDescription =
                getString(R.string.not_hazardous_asteroid_image)

        }

        return binding.root
    }

    private fun displayAstronomicalUnitExplanationDialog() {
        val builder = AlertDialog.Builder(requireActivity())
            .setMessage(getString(R.string.astronomica_unit_explanation))
            .setPositiveButton(android.R.string.ok, null)
        builder.create().show()
    }
}
