package sheridan.capstone.findmyfarmer.Customer.Model

import android.app.Activity
import android.graphics.Bitmap
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.storage.StorageReference
import sheridan.capstone.findmyfarmer.Database.AsyncResponse
import sheridan.capstone.findmyfarmer.Database.DatabaseAPIHandler
import sheridan.capstone.findmyfarmer.Database.ObjectConverter
import sheridan.capstone.findmyfarmer.Entities.FarmProduct
import sheridan.capstone.findmyfarmer.Entities.Product
import sheridan.capstone.findmyfarmer.Farmer.Controller.FruitListToView
import sheridan.capstone.findmyfarmer.ImageHandler.DirectoryName
import sheridan.capstone.findmyfarmer.ImageHandler.FirebaseImagehandler
import sheridan.capstone.findmyfarmer.ImageHandler.StorageResponse
import sheridan.capstone.findmyfarmer.Customer.View.FarmerInfo
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author Sohaib Hussain
 * Description: Handles Retrieving all the farm products from the database. This includes images
 * @property activity reference to the activity this class is being used in
 * @property adapter Instance of adapter for listing fruits
 * Date Modified: December 14th, 2020
 **/
class GetAllProducts(private val activity: Activity,private val adapter: FruitListToView) {

    /**
     * Gets all Farms products
     * Gets images for the products
     * @param id reference of the Farm id passed on
     * @param products list of products that are updated for the farm
     * @see FarmerInfo usage of this function
     */
    fun farmProducts(products: ArrayList<Product>,id: Int) {
        products.clear()
        DatabaseAPIHandler(activity, AsyncResponse{
            var AllProducts = ObjectConverter.convertStringToObject(it, Product::class.java,true) as List<*>
            for (product in AllProducts){
                var Productlistdata = product as Product
                var FIH = FirebaseImagehandler(DirectoryName.Product,5180,activity)
                FIH.GetImageURLFromFirebase(product.productName,object : StorageResponse{
                    @RequiresApi(Build.VERSION_CODES.N)
                    override fun processFinish(response: MutableList<StorageReference>?, bitmap: Optional<Bitmap>?, Url: Optional<String>?) {
                        if(Url != null && !(Url.get().isNullOrBlank())){
                            Productlistdata.image = Url.get()
                            ManageFarmProducts(Productlistdata,products, id, product)
                        }
                        else{
                            ManageFarmProducts(Productlistdata,products, id, product)
                        }
                    }
                    override fun OnErrorListener(error: String?) {
                        ManageFarmProducts(Productlistdata,products, id, product)
                    }
                })
            }
        }).execute("/ProductsByFarmID/${id}")
    }

    /**
     * Helper function to avoid repetitive code
     * @see farmProducts for usage of this function
     */
    private fun ManageFarmProducts(Productlistdata: Product,products: ArrayList<Product>,id: Int,product: Product){
        DatabaseAPIHandler(activity,AsyncResponse { resp ->
            if(!(resp.isNullOrBlank())) {
                var farmProduct = ObjectConverter.convertStringToObject(resp,
                    FarmProduct::class.java,
                    false) as FarmProduct
                if(farmProduct != null) {
                    Productlistdata.status = farmProduct.status
                    products.add(Productlistdata)
                    //notifying change on list
                    adapter.notifyDataSetChanged()
                }
            } else {
                Toast.makeText(activity,"No Farm Product found!",Toast.LENGTH_SHORT).show()
            }
        }).execute("/FarmProductByFarmIDAndProductID/${id}/${product.productID}")
    }
}
