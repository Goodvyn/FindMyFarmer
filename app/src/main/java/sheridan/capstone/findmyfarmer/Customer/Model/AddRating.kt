package sheridan.capstone.findmyfarmer.Customer.Model

import android.app.Activity
import android.widget.ImageView
import sheridan.capstone.findmyfarmer.Database.AsyncResponse
import sheridan.capstone.findmyfarmer.Database.DatabaseAPIHandler
import sheridan.capstone.findmyfarmer.Entities.Rating

/**
 * @author Sohaib Hussain
 * Description: Handles adding a rating to the database
 * @property activity references the activity this class is being used in
 * @property dialogue references the instance of the dialog
 * @property imageView references the imageview.
 * Date Modified: December 14th, 2020
 **/
class AddRating(private val activity: Activity,private val dialogue: RateItDialogue,val imageView: ImageView){

    /**
     * Adds rating to database
     * @param rating Instance of Rating to be added to the databasese
     * @see RateItDialogue for usage of this 
     */
   fun addRating(rating: Rating){
        DatabaseAPIHandler(activity, AsyncResponse {
            imageView.visibility = ImageView.INVISIBLE;
            dialogue.dismiss()
        }).execute("/addRating",rating)
   }
}