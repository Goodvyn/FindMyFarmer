package sheridan.capstone.findmyfarmer.Farmer.Model

import android.app.Activity
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import sheridan.capstone.findmyfarmer.Database.AsyncResponse
import sheridan.capstone.findmyfarmer.Database.DatabaseAPIHandler
import sheridan.capstone.findmyfarmer.Entities.Farm
import sheridan.capstone.findmyfarmer.Farmer.View.FarmerHub
import sheridan.capstone.findmyfarmer.R

/**
 * @author Sohaib Hussain
 * Description: Handles Farms to be added, deleted or updated using the API Handler
 * Date Modified: December 14th, 2020
 **/
class FarmDBHandler(val activity: Activity, val progressbar: ProgressBar?) {

    /*
      Updates the farm in the database
     */
    fun updatefarm(farm: Farm){
        progressbar?.visibility = ProgressBar.VISIBLE
        DatabaseAPIHandler(activity, AsyncResponse {
            progressbar?.visibility = ProgressBar.INVISIBLE
        }).execute("/UpdateFarm",farm)
    }

    /*
        Adds a new farm in the database
    */
    fun addfarm(farm: Farm){
        progressbar?.visibility = ProgressBar.VISIBLE
        DatabaseAPIHandler(activity, AsyncResponse {
            progressbar?.visibility = ProgressBar.INVISIBLE
        }).execute("/addFarm",farm)
    }

    /*
        Delete the farm from the database
     */
    fun deletefarm(farm: Farm){
        DatabaseAPIHandler(activity, AsyncResponse {
            if (!(it.isNullOrBlank())){
                Toast.makeText(activity,"Deleted!",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(activity,"Deleted!",Toast.LENGTH_SHORT).show()
            }
        }).execute("/deleteFarmByID/${farm.farmID}")
    }
}
