package sheridan.capstone.findmyfarmer.Customer.Controller

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.makeramen.roundedimageview.RoundedImageView
import kotlinx.android.synthetic.main.farmer_listing.view.*
import kotlinx.android.synthetic.main.imagelist_custom_row.view.*
import sheridan.capstone.findmyfarmer.R

/**
 * @author Sohaib Hussain
 * Description: Adapter for RecyclerView.Takes the List of Images and displays them on the
 *              recyclerview horizontally
 * Date Modified: December 14th, 2020
 **/
class ImageListToView (private val imageList: List<Bitmap>, private val listener: ImageListToView.OnItemClickListener)
    : RecyclerView.Adapter<ImageListToView.MyViewHolder>(){

    /*
      creates the base layout of each row in the recyclerview
    */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.imagelist_custom_row, parent, false)
        return MyViewHolder(
            itemView
        )
    }

    /*
        Assign each row with information to be displayed on each component,
        after the row has been initialised
     */
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       holder.imageView.setImageBitmap(imageList[position])
    }

    /*
        Decides the number of rows, based on the number of items in the Image list
    */
    override fun getItemCount(): Int {
        return imageList.size
    }

    /*
        Individually instantiates each row, with relative components like buttons, textviews etc and
        any listeners required
    */
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val imageView: ImageView = itemView.imagedisplay

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
