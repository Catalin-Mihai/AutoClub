package com.catasoft.autoclub.ui.main.feed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.catasoft.autoclub.R
import com.catasoft.autoclub.model.car.CarProfileModel
import com.catasoft.autoclub.model.meet.Meet
import com.catasoft.autoclub.util.dateAndTimeToMillis
import com.catasoft.autoclub.util.formatMillisToDate
import com.catasoft.autoclub.util.formatMillisToTime
import com.catasoft.autoclub.util.takeFirstNLetters
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso


class MeetsListAdapter(private val dataSet: List<Meet>, private val listener: MeetItemListener) :
    RecyclerView.Adapter<MeetsListAdapter.ViewHolder>() {

    interface MeetItemListener{
        fun onMeetClicked(meet: Meet)
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val placeImageView: ImageView = view.findViewById(R.id.placeImageView)
        val placeNameTV: TextView = view.findViewById(R.id.placeNameTV)
        val timeTV: TextView = view.findViewById(R.id.timeTV)
        val dateTV: TextView = view.findViewById(R.id.dateTV)
        val placeAddressTV: TextView = view.findViewById(R.id.placeAddressTV)
        val descriptionTV: TextView = view.findViewById(R.id.descriptionTV)
        val meetCard: MaterialCardView = view.findViewById(R.id.meetCard)

        init {
            meetCard.setOnClickListener{
                listener.onMeetClicked(dataSet[adapterPosition])
            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.live_meet_item, viewGroup, false)

        return ViewHolder(view)
    }


    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
//        viewHolder.textView.text = dataSet[position]
        Picasso.get().load(dataSet[position].placePhotoLink).into(viewHolder.placeImageView)

        viewHolder.placeNameTV.text = dataSet[position].placeName
        viewHolder.placeAddressTV.text = takeFirstNLetters(dataSet[position].placeAddress, 20)
        viewHolder.timeTV.text = formatMillisToTime(dataSet[position].meetTime)
        viewHolder.dateTV.text = formatMillisToDate(dataSet[position].meetTime)

        if(dataSet[position].description == null || (dataSet[position].description != null && dataSet[position].description!!.trim().isEmpty())){
            viewHolder.descriptionTV.visibility = View.GONE
        }
        else {
            viewHolder.descriptionTV.visibility = View.VISIBLE
            viewHolder.descriptionTV.text = takeFirstNLetters(dataSet[position].description, 50)
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
