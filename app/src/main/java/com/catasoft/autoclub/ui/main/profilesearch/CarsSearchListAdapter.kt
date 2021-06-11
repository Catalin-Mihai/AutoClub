package com.catasoft.autoclub.ui.main.profilesearch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.catasoft.autoclub.R
import com.catasoft.autoclub.model.car.Car
import com.catasoft.autoclub.model.user.UserSearchModel
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso

class CarsSearchListAdapter(private val dataSet: List<Car>, private val listener: CarItemListener) :
    RecyclerView.Adapter<CarsSearchListAdapter.ViewHolder>(){

        interface CarItemListener{
            fun onCarClicked(car: Car)
        }

        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder).
         */
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val ivProfilePhoto: ImageView = view.findViewById(R.id.ivCarPhoto)
            val tvCarMakeAndModel: TextView = view.findViewById(R.id.carMakeAndModel)
//            val tvOwner: TextView = view.findViewById(R.id.owner)
            val tvCarYear: TextView = view.findViewById(R.id.carYear)
            val tvNumberPlate: TextView = view.findViewById(R.id.carNumber)
            val card: MaterialCardView = view.findViewById(R.id.card)

            init {
                card.setOnClickListener{
                    listener.onCarClicked(dataSet[adapterPosition])
                }
            }
        }

        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            // Create a new view, which defines the UI of the list item
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.search_car_item, viewGroup, false)

            return ViewHolder(view)
        }


        // Replace the contents of a view (invoked by the layout manager)
        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

            // Get element from your dataset at this position and replace the
            // contents of the view with that element
//        viewHolder.textView.text = dataSet[position]
            Picasso.get().load(dataSet[position].avatarUri).into(viewHolder.ivProfilePhoto)
            viewHolder.tvCarMakeAndModel.text = "${dataSet[position].make} ${dataSet[position].model}"
            viewHolder.tvCarYear.text = dataSet[position].year.toString()
            viewHolder.tvNumberPlate.text = dataSet[position].numberPlate
//            viewHolder.tvOwner.text = dataSet[position].ownerUid
        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = dataSet.size
}