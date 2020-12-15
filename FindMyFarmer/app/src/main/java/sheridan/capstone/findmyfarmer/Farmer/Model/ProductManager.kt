package sheridan.capstone.findmyfarmer.Farmer.Model



import android.app.Activity
import android.graphics.Bitmap
import android.os.Build
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.storage.StorageReference
import org.json.JSONArray
import sheridan.capstone.findmyfarmer.Database.AsyncResponse
import sheridan.capstone.findmyfarmer.Database.DatabaseAPIHandler
import sheridan.capstone.findmyfarmer.Database.ObjectConverter
import sheridan.capstone.findmyfarmer.Entities.FarmProduct
import sheridan.capstone.findmyfarmer.Entities.Product
import sheridan.capstone.findmyfarmer.Farmer.Controller.FarmerFruitListToView
import sheridan.capstone.findmyfarmer.ImageHandler.DirectoryName
import sheridan.capstone.findmyfarmer.ImageHandler.FirebaseImagehandler
import sheridan.capstone.findmyfarmer.ImageHandler.StorageResponse
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author Sohaib Hussain
 * Description: Handles all the database functionalities relating products
 * Date Modified: December 14th, 2020
 **/
class ProductManager(val activity: Activity) {

    /*
        Retrieves the list of products Unique categories from the database
    */
    fun GetProductCategoryList(categoryList: ArrayList<String>, adapter: ArrayAdapter<String>) {
        DatabaseAPIHandler(activity, AsyncResponse {
            if(!(it.isNullOrBlank())){
                categoryList.clear()
                var jsonArray = JSONArray(it)
                if(jsonArray != null){
                    for(x in 0..(jsonArray.length()-1)){
                        categoryList.add(jsonArray.getString(x))
                        adapter.notifyDataSetChanged()
                    }
                }
            }
            else{
                Toast.makeText(activity,"No products",Toast.LENGTH_SHORT).show()
            }
        }).execute("/getProductList")
    }

    /*
        Retrieves all the editable products a farm has listed
    */
    fun GetEditProducts(FarmProducts: ArrayList<Product> ,id: Int,Fruitadapter: FarmerFruitListToView, overlay: ArrayList<View>){
        DatabaseAPIHandler(activity, AsyncResponse{
            FarmProducts.clear()
            if(it.count() > 2){
                overlay[1].visibility = View.INVISIBLE
                var AllProducts = ObjectConverter.convertStringToObject(it, Product::class.java,true) as List<*>
                for (product in AllProducts){
                    var Productlistdata = product as Product
                    var FIH = FirebaseImagehandler(DirectoryName.Product,5180,activity)
                    FIH.GetImageURLFromFirebase(product.productName,object : StorageResponse {
                        @RequiresApi(Build.VERSION_CODES.N)
                        override fun processFinish(response: MutableList<StorageReference>?, bitmap: Optional<Bitmap>?, Url: Optional<String>?) {
                            if(Url != null && !(Url.get().isNullOrBlank())){
                                Productlistdata.image = Url.get()
                                getFarmersProducts(Productlistdata,FarmProducts,Fruitadapter,id, product,overlay)
                            }
                            else{
                                getFarmersProducts(Productlistdata,FarmProducts,Fruitadapter,id, product,overlay)
                            }
                        }
                        override fun OnErrorListener(error: String?) {
                            getFarmersProducts(Productlistdata,FarmProducts,Fruitadapter,id, product,overlay)
                        }
                    })
                }
            }
            else{
                overlay[0].visibility = View.VISIBLE
                overlay[1].visibility = View.VISIBLE
            }

        }).execute("/ProductsByFarmID/${id}")
    }

    /*
        Retrieves the Types of products based on the category of the products
    */
    fun GetTypeOfCategory(TypeList: ArrayList<String>, category: String,adapter: ArrayAdapter<String>){
        DatabaseAPIHandler(activity, AsyncResponse {
            if(!(it.isNullOrBlank())){
                var productlist = ObjectConverter.convertStringToObject(it,Product::class.java,true) as List<*>
                if(productlist != null){
                    TypeList.clear()
                    for (product in productlist){
                        var prod = product as Product
                        if(prod.productCategory.compareTo(category,true) == 0){
                            TypeList.add(prod.productName)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }).execute("/Products")

    }

    /*
        Adds Farm Product when a farmer adds a product to their specific farm
    */
    fun AddFarmProduct(name: String,category: String,Farmid: Int,FarmProducts: ArrayList<Product>,Fruitadapter: FarmerFruitListToView,overlay: ArrayList<View>){
        DatabaseAPIHandler(activity, AsyncResponse {
            if(!(it.isNullOrBlank())){
                var farmProduct = FarmProduct(1,Farmid,it.toInt(),0,"lb(s)")
                DatabaseAPIHandler(activity, AsyncResponse {resp ->
                    if(!(resp.isNullOrBlank())){
                        GetEditProducts(FarmProducts,Farmid,Fruitadapter,overlay)
                    }
                    else{
                        Toast.makeText(activity,"Unable to add to List, this item might already exists in your inventory", Toast.LENGTH_SHORT).show()
                    }
                }).execute("/AddFarmProduct",farmProduct)
            }
            else{
                Toast.makeText(activity,"This Product does not exist", Toast.LENGTH_SHORT).show()
            }
        }).execute("/ProductIDByNameAndCategory/${name}/${category}")
    }

    /*
        Updates the Quantity and the Unit of the Quantity
    */
    fun UpdateQuantity(quantity: Int,unit:String,productId: Int,farmid: Int,editText: EditText){
        DatabaseAPIHandler(activity, AsyncResponse {
            if(!(it.isNullOrBlank())){
                var farmProduct = ObjectConverter.convertStringToObject(it,FarmProduct::class.java,false) as FarmProduct
                if(farmProduct != null){
                    farmProduct.quantity = quantity
                    farmProduct.unit = unit
                    DatabaseAPIHandler(activity, AsyncResponse {resp ->
                        if(!(resp.isNullOrBlank())){
                            editText.setText("${quantity}")
                        }
                    }).execute("/UpdateFarmProductQuantity",farmProduct)
                }
            }else{
                Toast.makeText(activity,"Couldnt find this product in this farm's Inventory",Toast.LENGTH_SHORT).show()
            }
        }).execute("/FarmProductByFarmIDAndProductID/${farmid}/${productId}")
    }

    /*
        Deletes a Farm Product when a farmer removes a product from their farm
    */
    fun DeleteFarmProduct(productId: Int,farmid: Int,adapter: FarmerFruitListToView,position: Int,FarmProducts: ArrayList<Product>,overlay: ArrayList<View>){
        DatabaseAPIHandler(activity, AsyncResponse {
            if(!it.isNullOrBlank()){
                FarmProducts.removeAt(position)
                adapter.notifyDataSetChanged()
            }
            else{
                Toast.makeText(activity,"Could not delete Product!",Toast.LENGTH_SHORT).show()
            }

            if(FarmProducts.size == 0){
                overlay[0].visibility = View.VISIBLE
                overlay[1].visibility = View.VISIBLE
            }

        }).execute("/deleteFarmProductByFarmIDAndProductID/${farmid}/${productId}")
    }

    /*
        Retrieves Farms products, used in GetEditproducts function of this class
     */
    private fun getFarmersProducts(Productlistdata: Product, FarmProducts: ArrayList<Product>, Fruitadapter: FarmerFruitListToView, id: Int, product: Product,overlay: ArrayList<View>){
        DatabaseAPIHandler(activity, AsyncResponse {resp ->
            if(!(resp.isNullOrBlank())){
                var farmProduct = ObjectConverter.convertStringToObject(resp,FarmProduct::class.java,false) as FarmProduct
                if (farmProduct != null){
                    Productlistdata.quantity = farmProduct.quantity
                    Productlistdata.unit = farmProduct.unit
                    FarmProducts.add(Productlistdata)
                    overlay[0].visibility = View.INVISIBLE
                    overlay[1].visibility = View.INVISIBLE
                    //notifying change on list
                    Fruitadapter.notifyDataSetChanged()
                }
            }
            else{
                Toast.makeText(activity,"No Farm Product found!",Toast.LENGTH_SHORT).show()
            }
        }).execute("/FarmProductByFarmIDAndProductID/${id}/${product.productID}")
    }
}
