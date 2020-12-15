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

/**
 * @author Sohaib Hussain
 * Description: Handles changing password, name and email of the customer
 *              from account settings
 * Date Modified: December 14th, 2020
 **/
class CustomerDBHandler (val activity: Activity){

    private lateinit var sessionData: SessionData
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