package sheridan.capstone.findmyfarmer.Customer.Model

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.storage.StorageReference
import sheridan.capstone.findmyfarmer.Customer.Controller.FollowingListToView
import sheridan.capstone.findmyfarmer.Database.AsyncResponse
import sheridan.capstone.findmyfarmer.Database.DatabaseAPIHandler
import sheridan.capstone.findmyfarmer.Database.ObjectConverter
import sheridan.capstone.findmyfarmer.Entities.Customer
import sheridan.capstone.findmyfarmer.Entities.Farm
import sheridan.capstone.findmyfarmer.FarmerListing.Controller.FarmListToView
import sheridan.capstone.findmyfarmer.ImageHandler.DirectoryName
import sheridan.capstone.findmyfarmer.ImageHandler.FirebaseImagehandler
import sheridan.capstone.findmyfarmer.ImageHandler.StorageResponse
import sheridan.capstone.findmyfarmer.SessionDataHandler.SessionData
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author Sohaib Hussain
 * Description: Handles retrieving all followed farms from database
 * Date Modified: December 14th, 2020
 **/
class GetFollows(val activity: Activity, val adapter: FollowingListToView,val overlay: ArrayList<View>){

    private lateinit var sessionData: SessionData

    fun GetFollowedFarms(FollowedFarms: ArrayList<Farm>){
        sessionData = SessionData(activity)
        var customer = sessionData.customerData
        if(customer != null){
            DatabaseAPIHandler(activity, AsyncResponse {
                FollowedFarms.clear()
                if(it.count() > 2){
                    overlay[1].visibility = View.INVISIBLE
                    var AllFollowedFarms = ObjectConverter.convertStringToObject(it, Farm::class.java,true) as List<*>
                    for (farm in AllFollowedFarms){
                        var followedFarm = farm as Farm
                        var FIH = FirebaseImagehandler(DirectoryName.Farm,followedFarm.farmID,activity)
                        FIH.GetPrimaryImageFromFirebaseURL(object: StorageResponse {
                            @RequiresApi(Build.VERSION_CODES.N)
                            override fun processFinish(response: MutableList<StorageReference>?, bitmap: Optional<Bitmap>?, Url: Optional<String>?) {
                                if(Url != null && !(Url.get().isNullOrBlank())) {
                                    followedFarm.primaryImage = Url.get()
                                    farmInfoProcess(customer, followedFarm, FollowedFarms)
                                }
                                else{
                                    farmInfoProcess(customer, followedFarm, FollowedFarms)
                                }
                            }
                            override fun OnErrorListener(error: String?) {
                                farmInfoProcess(customer, followedFarm, FollowedFarms)
                            }
                        })
                    }
                }
                else{
                    overlay[0].visibility = View.VISIBLE
                    overlay[1].visibility = View.VISIBLE
                }

            }).execute("/getFollowedFarmsByCustomerID/${customer.customerID}")
        }
    }

    private fun farmInfoProcess(customer: Customer,followedFarm: Farm,FollowedFarms: ArrayList<Farm>){
        if(customer != null){
            DatabaseAPIHandler(activity, AsyncResponse {resp ->
                followedFarm.isFollowed = !(resp.isNullOrBlank())
                DatabaseAPIHandler(activity, AsyncResponse {resp2->
                    followedFarm.alreadyRated = !(resp2.isNullOrBlank())
                    DatabaseAPIHandler(activity, AsyncResponse {
                        if(it != null) {
                            followedFarm.followers = it.toInt()
                        }
                        FollowedFarms.add(followedFarm)
                        overlay[0].visibility = View.INVISIBLE
                        overlay[1].visibility = View.INVISIBLE
                        //notifying change on list
                        adapter.notifyDataSetChanged()
                    }).execute("/getFarmFollowers/${followedFarm.farmID}")
                }).execute("/getRatingByCustomerIDAndFarmID/${followedFarm.farmID}/${customer.customerID}")
            }).execute("/getFollowByCustomerIDAndFarmID/${followedFarm.farmID}/${customer.customerID}")
        }
        else{
            DatabaseAPIHandler(activity, AsyncResponse {
                if(it != null) {
                    followedFarm.followers = it.toInt()
                }
                FollowedFarms.add(followedFarm)
                overlay[0].visibility = View.INVISIBLE
                overlay[1].visibility = View.INVISIBLE
                //notifying change on list
                adapter.notifyDataSetChanged()
            }).execute("/getFarmFollowers/${followedFarm.farmID}")
        }
    }
}
