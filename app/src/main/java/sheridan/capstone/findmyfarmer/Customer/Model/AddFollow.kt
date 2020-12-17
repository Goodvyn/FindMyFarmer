package sheridan.capstone.findmyfarmer.Customer.Model

import android.app.Activity
import android.widget.ImageView
import android.widget.Toast
import sheridan.capstone.findmyfarmer.Database.AsyncResponse
import sheridan.capstone.findmyfarmer.Database.DatabaseAPIHandler
import sheridan.capstone.findmyfarmer.Entities.Following

/**
 * @author Sohaib Hussain
 * Description: Handles adding a farm followed by customer to the database
 * @property activity reference of the activity this class is being used in.
 * @property dialogue reference of the dialog box that pops up when user follows a farm
 * @property imageView reference to the image button for favourites, changes the button to off when
 *                      unsubscribed and on for when subsribed
 * Date Modified: December 14th, 2020
 **/
class AddFollow(private val activity: Activity, private val dialogue: FollowingDialog,private val imageView: ImageView) {

    /**
     * Inserts the farm followed in the database
     * @param following Instance of Following to be added to the database
     * @see FollowingDialog usage of this function in the FollowingDialog class
     */
    fun addfollow(following: Following){
        DatabaseAPIHandler(activity, AsyncResponse {
            Toast.makeText(activity,"Following!", Toast.LENGTH_SHORT).show()
            imageView.setImageDrawable(activity.getDrawable(android.R.drawable.btn_star_big_on))
            dialogue.dismiss()
        }).execute("/addFollow",following)
    }
}