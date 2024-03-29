package com.catasoft.autoclub.ui.main.profilecars

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.catasoft.autoclub.R
import com.catasoft.autoclub.model.car.CarProfileModel
import com.catasoft.autoclub.util.getUserActionsVisibility
import com.catasoft.autoclub.util.isCurrentUser
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso
import timber.log.Timber


class CarsListAdapter(private val dataSet: List<CarProfileModel>, private val userUid: String, private val listener: CarItemListener) :
    RecyclerView.Adapter<CarsListAdapter.ViewHolder>() {

    interface CarItemListener{
        fun onCarClicked(car: CarProfileModel, view: View)
        fun onMoreOptionClickedOn(car: CarProfileModel, view: View)
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivCarAvatar: ImageView
        val tvCarMakeAndModel: TextView
        val card: MaterialCardView = view.findViewById(R.id.card)
        val btnMore: ImageView = view.findViewById(R.id.btnMore)

        init {
            // Define click listener for the ViewHolder's View.

            card.setOnClickListener{
                Timber.e("Click pe car card!")
                listener.onCarClicked(dataSet[adapterPosition], it)
            }

            if(isCurrentUser(userUid))
                btnMore.setOnClickListener {
                    listener.onMoreOptionClickedOn(dataSet[adapterPosition], it)
                }

            btnMore.visibility = getUserActionsVisibility(userUid)

            ivCarAvatar = view.findViewById(R.id.ivCarAvatar)
            tvCarMakeAndModel = view.findViewById(R.id.tvCarMakeAndModel)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.car_profile_card, viewGroup, false)

        return ViewHolder(view)
    }


    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
//        viewHolder.textView.text = dataSet[position]
        viewHolder.tvCarMakeAndModel.text = "${dataSet[position].make} ${dataSet[position].model}"
        Picasso.get().load(dataSet[position].avatarDownloadLink).into(viewHolder.ivCarAvatar)
        viewHolder.ivCarAvatar.transitionName = "shared_car_item_transition$position"
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
