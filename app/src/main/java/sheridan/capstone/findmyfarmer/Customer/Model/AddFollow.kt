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
 * Date Modified: December 14th, 2020
 **/
class AddFollow(private val activity: Activity, private val dialogue: FollowingDialog,private val imageView: ImageView) {

    //Adds a follow
    fun addfollow(following: Following){
        DatabaseAPIHandler(activity, AsyncResponse {
            Toast.makeText(activity,"Following!", Toast.LENGTH_SHORT).show()
            imageView.setImageDrawable(activity.getDrawable(android.R.drawable.btn_star_big_on))
            dialogue.dismiss()
        }).execute("/addFollow",following)
    }
}