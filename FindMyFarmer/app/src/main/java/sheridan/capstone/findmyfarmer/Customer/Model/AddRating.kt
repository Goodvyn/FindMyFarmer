package sheridan.capstone.findmyfarmer.Customer.Model

import android.app.Activity
import android.widget.ImageView
import sheridan.capstone.findmyfarmer.Database.AsyncResponse
import sheridan.capstone.findmyfarmer.Database.DatabaseAPIHandler
import sheridan.capstone.findmyfarmer.Entities.Rating


/**
 * @author Sohaib Hussain
 * Description: Handles adding a rating to the database
 * Date Modified: December 14th, 2020
 **/
class AddRating(private val activity: Activity,private val dialogue: RateItDialogue,val imageView: ImageView){

    //Adds rating to database
   fun addRating(rating: Rating){
        DatabaseAPIHandler(activity, AsyncResponse {
            imageView.visibility = ImageView.INVISIBLE;
            dialogue.dismiss()
        }).execute("/addRating",rating)
   }
}