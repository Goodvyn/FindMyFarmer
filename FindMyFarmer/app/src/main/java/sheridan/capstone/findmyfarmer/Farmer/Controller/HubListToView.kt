package sheridan.capstone.findmyfarmer.Farmer.Controller

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.farmer_listing.view.*
import sheridan.capstone.findmyfarmer.Entities.Farm
import sheridan.capstone.findmyfarmer.R

class HubListToView (private val activity: Activity, val HubList: List<Farm>, private val listener: HubListToView.OnItemClickListener)
    : RecyclerView.Adapter<HubListToView.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.farmer_listing,
            parent, false
        )
        return MyViewHolder(
            itemView
        )
    }

    override fun getItemCount() = HubList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = HubList[position]
        if(!(currentItem.primaryImage.isNullOrBlank())){
            Picasso.get().load(currentItem.primaryImage).into(holder.imageView)
        }
        holder.Farmer_Name.text = currentItem.businessName
        holder.Farmers_Desc.text = currentItem.businessDescription
        holder.Rating.rating = currentItem.businessRating
        holder.Farmer_City.text = currentItem.city
        holder.Farmer_Followers.text = "Followers: ${currentItem.followers}"
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val imageView: RoundedImageView = itemView.ImageView
        val Farmer_Name: TextView = itemView.Name
        val Farmers_Desc: TextView = itemView.Desc
        val Rating: RatingBar = itemView.rating
        val Farmer_City: TextView = itemView.City
        val Farmer_Followers : TextView = itemView.Followers


        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }
    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }


}
