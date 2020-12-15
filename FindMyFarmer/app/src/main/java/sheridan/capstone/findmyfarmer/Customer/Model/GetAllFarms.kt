package sheridan.capstone.findmyfarmer.Customer.Model

import android.app.Activity
import android.graphics.Bitmap
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.storage.StorageReference
import sheridan.capstone.findmyfarmer.Database.AsyncResponse
import sheridan.capstone.findmyfarmer.Database.DatabaseAPIHandler
import sheridan.capstone.findmyfarmer.Database.ObjectConverter
import sheridan.capstone.findmyfarmer.Entities.Customer
import sheridan.capstone.findmyfarmer.Entities.Farm
import sheridan.capstone.findmyfarmer.Entities.Product
import sheridan.capstone.findmyfarmer.FarmerListing.Controller.FarmListToView
import sheridan.capstone.findmyfarmer.ImageHandler.DirectoryName
import sheridan.capstone.findmyfarmer.ImageHandler.FirebaseImagehandler
import sheridan.capstone.findmyfarmer.ImageHandler.StorageResponse
import sheridan.capstone.findmyfarmer.SessionDataHandler.SessionData
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author Sohaib Hussain
 * Description: Handles Retrieving all the farms for public view. This includes retrieving followers
 *              images etc.
 * Date Modified: December 14th, 2020
 **/
class GetAllFarms(val activity: Activity, val swipeRefreshLayout: SwipeRefreshLayout,val adapter: FarmListToView,val searchBar: SearchView,val overlay: ArrayList<View>) {

    private lateinit var sessionData: SessionData
    fun GetFarms(Farms: ArrayList<Farm>){
        sessionData = SessionData(activity)
        var customer = sessionData.customerData
        swipeRefreshLayout.isRefreshing = true
        DatabaseAPIHandler(activity,AsyncResponse{
            Farms.clear()
            if(it.count() > 2 || !it.isNullOrEmpty()){
                overlay[1].visibility = View.INVISIBLE
                var AllFarms = ObjectConverter.convertStringToObject(it, Farm::class.java,true) as List<*>
                for (farm in AllFarms){
                    var farmlistdata = farm as Farm
                    var FIH = FirebaseImagehandler(DirectoryName.Farm,farmlistdata.farmID,activity)
                    FIH.GetPrimaryImageFromFirebaseURL(object: StorageResponse{
                        @RequiresApi(Build.VERSION_CODES.N)
                        override fun processFinish(response: MutableList<StorageReference>?, bitmap: Optional<Bitmap>?, Url: Optional<String>?) {
                            if(Url != null && !(Url.get().isNullOrBlank())) {
                                farmlistdata.primaryImage = Url.get()
                                ProcessFarms(customer, farmlistdata, Farms)
                            }
                            else{
                                ProcessFarms(customer, farmlistdata, Farms)
                            }
                        }
                        override fun OnErrorListener(error: String?) {
                            ProcessFarms(customer, farmlistdata, Farms)
                        }
                    })
                }
            }else{
                swipeRefreshLayout.isRefreshing = false
                overlay[0].visibility = View.VISIBLE
                overlay[1].visibility = View.VISIBLE
            }
        }).execute("/Farms")
    }
    private fun ProcessFarms(customer: Customer?,farmlistdata:Farm,Farms: ArrayList<Farm>){
        if(customer != null){
            DatabaseAPIHandler(activity, AsyncResponse {resp ->
                farmlistdata.isFollowed = !(resp.isNullOrBlank())
                DatabaseAPIHandler(activity, AsyncResponse {resp2->
                    farmlistdata.alreadyRated = !(resp2.isNullOrBlank())
                    DatabaseAPIHandler(activity, AsyncResponse {
                        if(it != null) {
                            farmlistdata.followers = it.toInt()
                        }
                        DatabaseAPIHandler(activity, AsyncResponse {prods ->
                            if(!(prods.isNullOrBlank())) {
                                var products = ObjectConverter.convertStringToObject(prods,Product::class.java,true) as List<Product>
                                farmlistdata.products = products
                            }
                            Farms.add(farmlistdata);
                            //notifying change on list
                            adapter.notifyDataSetChanged()
                            swipeRefreshLayout.isRefreshing = false
                            overlay[0].visibility = View.INVISIBLE
                            searchBar.isEnabled = true
                        }).execute("/ProductsByFarmID/${farmlistdata.farmID}")
                    }).execute("/getFarmFollowers/${farmlistdata.farmID}")
                }).execute("/getRatingByCustomerIDAndFarmID/${farmlistdata.farmID}/${customer.customerID}")
            }).execute("/getFollowByCustomerIDAndFarmID/${farmlistdata.farmID}/${customer.customerID}")
        }
        else{
            DatabaseAPIHandler(activity, AsyncResponse {
                if(it != null) {
                    farmlistdata.followers = it.toInt()
                }
                DatabaseAPIHandler(activity, AsyncResponse {prods ->
                    if(!(prods.isNullOrBlank())) {
                        var products = ObjectConverter.convertStringToObject(prods,Product::class.java,true) as List<Product>
                        farmlistdata.products = products
                    }
                    Farms.add(farmlistdata)
                    //notifying change on list
                    adapter.notifyDataSetChanged()
                    swipeRefreshLayout.isRefreshing = false
                    overlay[0].visibility = View.INVISIBLE
                    searchBar.isEnabled = true
                }).execute("/ProductsByFarmID/${farmlistdata.farmID}")
            }).execute("/getFarmFollowers/${farmlistdata.farmID}")
        }
    }
}
