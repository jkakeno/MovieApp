package com.junkakeno.movieapp.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.junkakeno.movieapp.R
import com.junkakeno.movieapp.data.util.Status
import com.junkakeno.movieapp.databinding.FragmentSearchBinding
import com.junkakeno.movieapp.inteface.MovieSelectListener
import com.junkakeno.movieapp.ui.adapter.MovieCollectionAdapter
import com.junkakeno.movieapp.ui.viewmodel.MovieCollectionViewModel
import timber.log.Timber

class MovieSearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var collectionViewModel: MovieCollectionViewModel

    companion object{
        fun newInstance():MovieSearchFragment{
            return MovieSearchFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        collectionViewModel = ViewModelProvider(this).get(MovieCollectionViewModel::class.java)

        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSearchBar()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initSearchBar(){

        binding.searchView.isIconified = false

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(query: String?): Boolean {

                query?.let{
                    if(it.isNotEmpty() && it.isNotBlank()) {
                        initMovieSearch(it)
                    }else {
                        binding.errorMsg.visibility = View.VISIBLE
                        binding.errorMsg.text = ""
                        binding.moviesList.visibility = View.GONE
                    }
                }

                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {

                query?.let{
                    if(it.isNotEmpty() && it.isNotBlank()) {
                        initMovieSearch(it)
                    }else {
                        binding.errorMsg.visibility = View.VISIBLE
                        binding.errorMsg.text = ""
                        binding.moviesList.visibility = View.GONE
                    }
                }

                hideKeyboard(binding.searchView)

                return false
            }
        })

        binding.searchView.findViewById<ImageView>(R.id.search_close_btn).setOnClickListener {
            binding.searchView.isIconified = true
        }

    }

    fun hideKeyboard(searchBar: SearchView){
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchBar.windowToken,0)
    }

    private fun initMovieSearch(query:String){

        val listener = object: MovieSelectListener{
            override fun onMovieSelected(id: String) {

                hideKeyboard(binding.searchView)

                val movieDetailFragment = MovieDetailFragment.newInstance(id)
                val transaction = childFragmentManager.beginTransaction()
                transaction.replace(binding.subContainer.id,movieDetailFragment, MovieDetailFragment::class.java.simpleName)
                        .addToBackStack(MovieDetailFragment::class.java.simpleName).commitAllowingStateLoss()

            }
        }

        binding.moviesList.visibility = View.VISIBLE
        binding.errorMsg.visibility = View.GONE
        val adapter = MovieCollectionAdapter(mutableListOf(),listener)
        binding.moviesList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.moviesList.adapter = adapter

        collectionViewModel.getMovies(query).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    binding.errorMsg.visibility = View.GONE

                    it.data?.search?.let { list ->
                        adapter.setMovieList(list)
                        adapter.notifyDataSetChanged()
                    }
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