package com.junkakeno.movieapp.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.junkakeno.movieapp.R
import com.junkakeno.movieapp.data.util.Status
import com.junkakeno.movieapp.databinding.FragmentDetailBinding
import com.junkakeno.movieapp.ui.viewmodel.MovieDetailViewModel
import timber.log.Timber

class MovieDetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var movieDetailViewModel: MovieDetailViewModel
    private var movieId:String = ""

    companion object {
        const val ID = "id"

        fun newInstance(id:String):MovieDetailFragment{
            val fragment = MovieDetailFragment()
            val arg = bundleOf(Pair(ID,id))
            fragment.arguments = arg
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this, object:OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                //Do nothing to disable back
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        arguments?.getString(ID)?.let { movieId = it }

        movieDetailViewModel = ViewModelProvider(this).get(MovieDetailViewModel::class.java)

        _binding = FragmentDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMovieDetail(movieId)

        binding.back.setOnClickListener {
            parentFragmentManager.popBackStackImmediate()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initMovieDetail(id:String){

        movieDetailViewModel.getMovie(id).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE

                    val movie = it.data

                    binding.barTitle.text = movie?.title

                    val options = RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)

                    try {
                        Glide.with(binding.movieImage.context)
                            .load(movie?.poster)
                            .error(R.drawable.ic_default_image)
                            .apply(options)
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .into(binding.movieImage)
                    }catch (e:Exception){
                        Timber.e("Failed to load movie image: ${e.localizedMessage}")
                    }

                    movie?.ratings?.forEachIndexed{ index, rating ->

                        when(index){
                            0 -> {
                                binding.ratingSource1.text =
                                    context?.resources?.getString(R.string.other_source_ratings,rating?.source, rating?.value)
                            }
                            1 -> {
                                binding.ratingSource2.text =
                                    context?.resources?.getString(R.string.other_source_ratings,rating?.source, rating?.value)
                            }
                        }
                    }

                    binding.imdbRating.text = context?.resources?.getString(R.string.imdb_rating,movie?.imdbRating)

                }
                Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    binding.errorMsg.visibility = View.VISIBLE
                    binding.errorMsg.text = it.message
                }
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        })

    }
}