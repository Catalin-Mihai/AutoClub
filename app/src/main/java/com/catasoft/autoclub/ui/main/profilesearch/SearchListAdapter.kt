package com.catasoft.autoclub.ui.main.profilesearch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.catasoft.autoclub.R
import com.catasoft.autoclub.model.user.User
import com.catasoft.autoclub.model.user.UserSearchModel
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso


class SearchListAdapter(private val dataSet: List<UserSearchModel>, private val listener: UserItemListener) :
    RecyclerView.Adapter<SearchListAdapter.ViewHolder>() {

    interface UserItemListener{
        fun onUserClicked(user: UserSearchModel)
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivProfilePhoto: ImageView = view.findViewById(R.id.ivCarPhoto)
        val tvProfileName: TextView = view.findViewById(R.id.tvCarName)
        val carsLinearLayout: LinearLayout = view.findViewById(R.id.carsLinearLayout)
        val card: MaterialCardView = view.findViewById(R.id.card)

        init {
            card.setOnClickListener{
                listener.onUserClicked(dataSet[adapterPosition])
            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.profile_search_item, viewGroup, false)

        return ViewHolder(view)
    }


    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
//        viewHolder.textView.text = dataSet[position]
        viewHolder.tvProfileName.text = dataSet[position].name
        Picasso.get().load(dataSet[position].photoDownloadUrl).into(viewHolder.ivProfilePhoto)

        //Delete all cars
        viewHolder.carsLinearLayout.removeAllViews()

        dataSet[position].cars?.forEach {
            //Create the new one
            val textView: TextView = LayoutInflater.from(viewHolder.itemView.context)
                .inflate(R.layout.profile_search_user_car_item, viewHolder.itemView as ViewGroup, false) as TextView
            val text = viewHolder.itemView.context.resources
                .getString(R.string.profile_search_itme_car_text_placeholder, it.make, it.model, it.year.toString())
            textView.text = text
            //add it
            viewHolder.carsLinearLayout.addView(textView)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
