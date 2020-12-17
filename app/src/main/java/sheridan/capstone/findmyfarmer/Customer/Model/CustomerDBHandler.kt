package sheridan.capstone.findmyfarmer.Customer.Model

import android.app.Activity
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import sheridan.capstone.findmyfarmer.Database.AsyncResponse
import sheridan.capstone.findmyfarmer.Database.DatabaseAPIHandler
import sheridan.capstone.findmyfarmer.Database.ObjectConverter
import sheridan.capstone.findmyfarmer.Entities.Customer
import sheridan.capstone.findmyfarmer.SessionDataHandler.SessionData
import sheridan.capstone.findmyfarmer.Users.CustomerActivity
import sheridan.capstone.findmyfarmer.Users.FarmerActivity
import sheridan.capstone.findmyfarmer.Users.AccountSettings

/**
 * @author Sohaib Hussain
 * Description: Handles changing password, name and email of the customer
 *              from account settings
 * @property activity references the activity this class is being used in
 * Date Modified: December 14th, 2020
 **/
class CustomerDBHandler (val activity: Activity){

    //Used for accessing customer data for the session
    private lateinit var sessionData: SessionData

    /**
     * Updates the customer in the database
     * @param customer Instance of customer to be updated to the database
     * @see AccountSettings for usage of this function
     */
    fun updateCustomer(customer: Customer){
        sessionData = SessionData(activity)
        DatabaseAPIHandler(activity,AsyncResponse {
            if(!it.isNullOrEmpty()){
                val customer = ObjectConverter.convertStringToObject(it,Customer::class.java,false) as Customer
                sessionData.setUserDataForSession(null,customer)
                if(customer.isFarmer){
                    val i = Intent(activity, FarmerActivity::class.java)
                    startActivity(activity,i,null)
                }
                else {
                    val i = Intent(activity, CustomerActivity::class.java)
                    startActivity(activity,i,null)
                }
            }
            else{

            }
        }).execute("/updateCustomer",customer)
    }
}