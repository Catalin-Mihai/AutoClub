package com.catasoft.autoclub.ui.main.car

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.catasoft.autoclub.R
import com.catasoft.autoclub.ui.main.profilesearch.SearchListAdapter
import com.squareup.picasso.Picasso
import java.util.*


class CarDetailsPhotosAdapter(private val links: List<Uri>?, val listener: CarGalleryListener) :
    RecyclerView.Adapter<CarDetailsPhotosAdapter.ViewHolder>(){

    interface CarGalleryListener{
        fun imageClicked(imageView: ImageView, position: Int)
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val ivPhoto: ImageView
        init {
            ivPhoto = view.findViewById(R.id.ivCarPhoto)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cars_grid_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Picasso.get().load(links?.get(position)).into(holder.ivPhoto)
        holder.ivPhoto.setOnClickListener {
            listener.imageClicked(holder.ivPhoto, position)
        }
    }

    override fun getItemCount(): Int = links?.count() ?: 0

}