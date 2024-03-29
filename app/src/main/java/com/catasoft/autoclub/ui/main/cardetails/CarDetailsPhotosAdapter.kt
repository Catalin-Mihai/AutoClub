package com.catasoft.autoclub.ui.main.cardetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.catasoft.autoclub.R
import com.catasoft.autoclub.model.car.CarPhotoModel
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso


class CarDetailsPhotosAdapter(private val carPhotoModels: List<CarPhotoModel>?, val listener: CarGalleryListener) :
    RecyclerView.Adapter<CarDetailsPhotosAdapter.ViewHolder>(){

    interface CarGalleryListener{
        fun imageClicked(imageView: ImageView, position: Int)
        fun imageLongClicked(imageView: ImageView, position: Int)
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val ivPhoto: ImageView = view.findViewById(R.id.ivCarPhoto)
        val photoCard: MaterialCardView = view.findViewById(R.id.photoCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cars_grid_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Picasso.get().load(carPhotoModels?.get(position)?.photoUri).into(holder.ivPhoto)
        holder.photoCard.setOnClickListener {
            listener.imageClicked(holder.ivPhoto, position)
        }
        holder.photoCard.setOnLongClickListener {
            listener.imageLongClicked(holder.ivPhoto, position)
            true
        }
    }

    override fun getItemCount(): Int = carPhotoModels?.count() ?: 0

}