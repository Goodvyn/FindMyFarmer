package sheridan.capstone.findmyfarmer.Customer.Model

import android.R
import android.app.Activity
import android.widget.ImageView
import android.widget.Toast
import sheridan.capstone.findmyfarmer.Database.AsyncResponse
import sheridan.capstone.findmyfarmer.Database.DatabaseAPIHandler
import sheridan.capstone.findmyfarmer.Entities.Following

/**
 * @author Sohaib Hussain
 * Description: Handles deleting a Follow from the database, when the user unfollows
 * Date Modified: December 14th, 2020
 **/
class DeleteFollow(private val activity: Activity, private val imageView: ImageView) {

    fun removefollow(following: Following){
        DatabaseAPIHandler(activity, AsyncResponse {
            Toast.makeText(activity,"Unfollowed!", Toast.LENGTH_SHORT).show()
            imageView.setImageDrawable(activity.getDrawable(R.drawable.btn_star_big_off))
        }).execute("/deleteFollowByCustomerIDAndFarmID/${following.farmID}/${following.customerID}")
    }
}