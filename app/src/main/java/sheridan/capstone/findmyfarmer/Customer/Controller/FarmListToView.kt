


package sheridan.capstone.findmyfarmer.FarmerListing.Controller

/**
 * @author: Andrei Constantinecu
 * Sets up the farm list in the recycler view
 */

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.farmer_listing.view.*
import sheridan.capstone.findmyfarmer.Entities.Farm
import sheridan.capstone.findmyfarmer.R
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList

class FarmListToView(val FarmList: ArrayList<Farm>, private val listener: OnItemClickListener)
    : RecyclerView.Adapter<FarmListToView.MyViewHolder>(),Filterable {

    private lateinit var FarmListAll :ArrayList<Farm>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.farmer_listing, parent, false)
        FarmListAll = ArrayList(FarmList)
        return MyViewHolder(itemView)
    }



    override fun getItemCount() = FarmList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = FarmList[position]

        if(!(currentItem.primaryImage.isNullOrBlank())){
            Picasso.get().load(currentItem.primaryImage).into(holder.imageView)
        }
        holder.Farm_Name.text = currentItem.businessName
        holder.Farm_Desc.text = currentItem.businessDescription
        holder.Rating.rating = currentItem.businessRating
        holder.Farm_City.text = currentItem.city
        holder.Followers.text = "Followers: ${currentItem.followers}"
    }

    override fun getFilter(): Filter {
        return filter
    }

    private var filter = object: Filter(){
        override fun performFiltering(constraint: CharSequence?): FilterResults? {

            var filteredList = ArrayList<Farm>()

            if(constraint.toString().isEmpty()){
                filteredList.addAll(FarmListAll)
            }else{
                for(farm in FarmListAll){
                    var farmString = StringBuilder()
                    if(farm.products.size > 0){
                        for(product in farm.products){
                            farmString.append(farm.businessName + " ")
                            farmString.append(farm.businessDescription + " ")
                            farmString.append(farm.city + " ")
                            farmString.append(farm.country + " ")
                            farmString.append(farm.street + " ")
                            farmString.append(farm.unit.toString() + " ")
                            farmString.append(farm.postalCode + " ")
                            farmString.append(product.productName + " ")
                            farmString.append(product.productCategory + " ")
                        }
                    }
                    else{
                        farmString.append(farm.businessName + " ")
                        farmString.append(farm.businessDescription + " ")
                        farmString.append(farm.city + " ")
                        farmString.append(farm.country + " ")
                        farmString.append(farm.street + " ")
                        farmString.append(farm.unit.toString() + " ")
                        farmString.append(farm.postalCode + " ")
                    }

                    if(farmString.toString().toLowerCase().contains(constraint.toString().toLowerCase())){
                        filteredList.add(farm)
                    }
                }

            }

            var filterResults = FilterResults()
            filterResults.values = filteredList

            return filterResults;
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            FarmList.clear()
            FarmList.addAll(results?.values as Collection<Farm>)
            notifyDataSetChanged()
        }

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener {

        val imageView: RoundedImageView = itemView.ImageView
        val Farm_Name: TextView = itemView.Name
        val Farm_Desc: TextView = itemView.Desc
        val Rating: RatingBar = itemView.rating
        val Farm_City: TextView = itemView.City
        val Followers: TextView = itemView.Followers


        init { itemView.setOnClickListener(this) }

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

