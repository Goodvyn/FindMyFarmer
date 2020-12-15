package sheridan.capstone.findmyfarmer.Farmer.Model

import android.app.Activity
import android.graphics.Bitmap
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.storage.StorageReference
import sheridan.capstone.findmyfarmer.Database.AsyncResponse
import sheridan.capstone.findmyfarmer.Database.DatabaseAPIHandler
import sheridan.capstone.findmyfarmer.Database.ObjectConverter
import sheridan.capstone.findmyfarmer.Entities.Farm
import sheridan.capstone.findmyfarmer.Farmer.Controller.HubListToView
import sheridan.capstone.findmyfarmer.ImageHandler.DirectoryName
import sheridan.capstone.findmyfarmer.ImageHandler.FirebaseImagehandler
import sheridan.capstone.findmyfarmer.ImageHandler.StorageResponse
import java.util.*

/**
 * @author Sohaib Hussain
 * Description: Handles retrieving all the farms belonging to the farmer, these farms are listed
 *              in the hubview to be editable
 * Date Modified: December 14th, 2020
 **/
class GetFarmersFarms(val activity: Activity, val swipeRefreshLayout: SwipeRefreshLayout, val adapter: HubListToView,val overlay: ArrayList<View>){

    /*
        Retrieves the farms of the farmers
    */
    fun GetHubFarms(HubFarms: ArrayList<Farm>,id: Int){
        swipeRefreshLayout.isRefreshing = true
        DatabaseAPIHandler(activity, AsyncResponse{
            HubFarms.clear()
            if(it.count() > 2){
                overlay[1].visibility = View.INVISIBLE
                var AllFarms = ObjectConverter.convertStringToObject(it, Farm::class.java,true) as List<*>
                for (farm in AllFarms){
                    var farmlistdata = farm as Farm
                    var FIH = FirebaseImagehandler(DirectoryName.Farm,farmlistdata.farmID,activity)
                    FIH.GetPrimaryImageFromFirebaseURL(object: StorageResponse {
                        @RequiresApi(Build.VERSION_CODES.N)
                        override fun processFinish(response: MutableList<StorageReference>?, bitmap: Optional<Bitmap>?, Url: Optional<String>?) {
                            if(Url != null && !(Url.get().isNullOrBlank())) {
                                farmlistdata.primaryImage = Url.get()
                                getFarmFollowers(farmlistdata,HubFarms)

                            } else{
                                getFarmFollowers(farmlistdata,HubFarms)
                            }
                        }
                        override fun OnErrorListener(error: String?) {
                            getFarmFollowers(farmlistdata,HubFarms)
                        }
                    })
                }
                swipeRefreshLayout.isRefreshing = false
            }
            else{
                swipeRefreshLayout.isRefreshing = false
                overlay[0].visibility = View.VISIBLE
                overlay[1].visibility = View.VISIBLE
            }

        }).execute("/FarmsByFarmerID/$id")
    }

    /*
        Handles returning the followers of the farm, alongside the retrieve of farms
    */
    private fun getFarmFollowers(farmlistdata: Farm, HubFarms: ArrayList<Farm>){
        DatabaseAPIHandler(activity, AsyncResponse {
            if(it != null) {
                farmlistdata.followers = it.toInt()
            }
            HubFarms.add(farmlistdata);
            //notifying change on list
            adapter.notifyDataSetChanged()
            swipeRefreshLayout.isRefreshing = false
            overlay[0].visibility = View.INVISIBLE
            overlay[1].visibility = View.INVISIBLE
        }).execute("/getFarmFollowers/${farmlistdata.farmID}")
    }
}
