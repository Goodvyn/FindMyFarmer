package sheridan.capstone.findmyfarmer.Customer.Model

import android.R
import android.app.Activity
import android.widget.ImageView
import android.widget.Toast
import sheridan.capstone.findmyfarmer.Database.AsyncResponse
import sheridan.capstone.findmyfarmer.Database.DatabaseAPIHandler
import sheridan.capstone.findmyfarmer.Entities.Following
import sheridan.capstone.findmyfarmer.Customer.View.FarmerInfo

/**
 * @author Sohaib Hussain
 * Description: Handles deleting a Follow from the database, when the user unfollows
 * @property activity references the activity this class is being used in
 * @property imageView reference to the image button for favourites, changes the button to off when
 *                      unsubscribed and on for when subsribed
 * Date Modified: December 14th, 2020
 **/
class DeleteFollow(private val activity: Activity, private val imageView: ImageView) {

    /**
     * Removes the followed farm from the users followed farm list
     * @param following Instance of Following to be added to the database
     * @see FarmerInfo usage of this function in the FarmerInfo class
     */
    fun removefollow(following: Following){
        DatabaseAPIHandler(activity, AsyncResponse {
            Toast.makeText(activity,"Unfollowed!", Toast.LENGTH_SHORT).show()
            imageView.setImageDrawable(activity.getDrawable(R.drawable.btn_star_big_off))
        }).execute("/deleteFollowByCustomerIDAndFarmID/${following.farmID}/${following.customerID}")
    }
}