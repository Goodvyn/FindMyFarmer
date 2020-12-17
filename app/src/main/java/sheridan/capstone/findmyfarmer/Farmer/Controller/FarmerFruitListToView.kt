package sheridan.capstone.findmyfarmer.Farmer.Controller

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fruit_card.view.*
import kotlinx.android.synthetic.main.fruit_card.view.Fruit_Category
import kotlinx.android.synthetic.main.fruit_card.view.Fruit_img
import kotlinx.android.synthetic.main.fruit_card.view.Fruit_name
import kotlinx.android.synthetic.main.fruit_card_farmers.view.*
import sheridan.capstone.findmyfarmer.Customer.Model.SharedViewModel
import sheridan.capstone.findmyfarmer.Database.AsyncResponse
import sheridan.capstone.findmyfarmer.Database.DatabaseAPIHandler
import sheridan.capstone.findmyfarmer.Database.ObjectConverter
import sheridan.capstone.findmyfarmer.Entities.FarmProduct
import sheridan.capstone.findmyfarmer.Entities.Product
import sheridan.capstone.findmyfarmer.Farmer.Model.ProductManager

import sheridan.capstone.findmyfarmer.R

/**
 * @author Sohaib Hussain
 * Description: Adapter for RecyclerView.Takes the List of Products and displays the product data
 *              on the recyclerview. This adapter specifically handles farmer edit products,
 *              products that are editable by the farmer
 * Date Modified: December 14th, 2020
 **/
class FarmerFruitListToView (private val activity: Activity, var farmid : Int, val FruitList: List<Product>, private val listener: FarmerFruitListToView.OnItemClickListener)
    : RecyclerView.Adapter<FarmerFruitListToView.MyViewHolder>() {

    /*
        creates the base layout of each row in the recyclerview
    */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.fruit_card_farmers,
            parent, false
        )
        return MyViewHolder(
            itemView
        )
    }
    /*
        Decides the number of rows, based on the number of items in the Image list
    */
    override fun getItemCount() = FruitList.size

    /*
        Assign each row with information to be displayed on each component,
        after the row has been initialised
    */
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem = FruitList[position]
        if(!(currentItem.image.isNullOrBlank())){
            Picasso.get().load(currentItem.image).into(holder.Fruit_Image)
        }
        holder.Fruit_Name.text = currentItem.productName
        holder.Fruit_Cat.text = currentItem.productCategory
        var measurements = activity.resources.getStringArray(R.array.Product_Status)
        var index = measurements.indexOf(FruitList[position].status)
        holder.productStatus.setSelection(index)
    }

    /*
        Individually instantiates each row, with relative components like buttons, textviews etc and
        any listeners required
    */
    @SuppressLint("ResourceType")
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val Fruit_Image : RoundedImageView = itemView.Fruit_img
        val Fruit_Name: TextView = itemView.Fruit_name
        val Fruit_Cat: TextView = itemView.Fruit_Category
        val productStatus: Spinner = itemView.MeasurementType


        init {
            itemView.setOnClickListener(this)
            var productManager = ProductManager(activity)
            ArrayAdapter.createFromResource(
                activity,
                R.array.Product_Status,
                android.R.layout.simple_spinner_item
            ).also { arrayAdapter ->
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
                productStatus.adapter = arrayAdapter
            }



            productStatus.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                    var status = productStatus.selectedItem.toString()
                    productManager.UpdateStatus(status,FruitList.get(adapterPosition).productID,farmid,productStatus)

                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

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
