package com.junkakeno.movieapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.junkakeno.movieapp.R
import com.junkakeno.movieapp.inteface.MovieSelectListener
import com.junkakeno.movieapp.data.model.SearchItem
import com.junkakeno.movieapp.databinding.ItemMovieBinding
import timber.log.Timber


class MovieCollectionAdapter (val data:MutableList<SearchItem>,val listener: MovieSelectListener):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        data[position].let { (holder as ViewHolder).bind(it,listener) }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setMovieList(data:List<SearchItem>){
        this.data.addAll(data)
    }
}


class ViewHolder(val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(movie:SearchItem,listener:MovieSelectListener){

        binding.movieTitle.text = movie.title

        val options = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)

        try {
            Glide.with(binding.movieImage.context)
                .load(movie.poster)
                .error(R.drawable.ic_default_image)
                .apply(options)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.movieImage)
        }catch (e:Exception){
            Timber.e("Failed to load movie image: ${e.localizedMessage}")
        }

        binding.releaseYear.text = movie.year

        binding.root.setOnClickListener{
            movie.imdbID?.let { it1 -> listener.onMovieSelected(it1) }
        }
    }

}