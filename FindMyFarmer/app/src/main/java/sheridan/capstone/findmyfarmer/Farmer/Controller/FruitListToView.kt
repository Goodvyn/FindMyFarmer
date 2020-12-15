package sheridan.capstone.findmyfarmer.Farmer.Controller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fruit_card.view.*
import sheridan.capstone.findmyfarmer.Entities.Product

import sheridan.capstone.findmyfarmer.R

class FruitListToView (private val FruitList: List<Product>, private val listener: FruitListToView.OnItemClickListener)
    : RecyclerView.Adapter<FruitListToView.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.fruit_card,
            parent, false
        )
        return MyViewHolder(
            itemView
        )
    }

    override fun getItemCount() = FruitList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem = FruitList[position]
        if(!(currentItem.image.isNullOrBlank())){
            Picasso.get().load(currentItem.image).into(holder.Fruit_Image)
        }
        holder.Fruit_Name.text = currentItem.productName
        holder.Fruit_Cat.text = currentItem.productCategory
        holder.Fruit_quantity.text = currentItem.quantity.toString() + " " + currentItem.unit.toString()
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {


        val Fruit_Image : RoundedImageView = itemView.Fruit_img
        val Fruit_Name: TextView = itemView.Fruit_name
        val Fruit_Cat: TextView = itemView.Fruit_Category
        val Fruit_quantity :TextView = itemView.quantitytext


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
