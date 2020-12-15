package sheridan.capstone.findmyfarmer.LoginAndRegistration.Model

/**
 * @author Kartavyi Nikita
 * Description: This activity is a model of the login part of the application.
 * The firebase authentication logic is stored here, as well as the custom validation for the input.
 * On top of that, Database calls for retrieving customer data is based in this activity
 * Date Modified: December 14th, 2020
 **/

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.AccessToken
import com.google.firebase.auth.*
import sheridan.capstone.findmyfarmer.Database.AsyncResponse
import sheridan.capstone.findmyfarmer.Database.DatabaseAPIHandler
import sheridan.capstone.findmyfarmer.Database.ObjectConverter
import sheridan.capstone.findmyfarmer.Entities.Customer
import sheridan.capstone.findmyfarmer.Entities.Farmer
import sheridan.capstone.findmyfarmer.SessionDataHandler.SessionData
import java.text.SimpleDateFormat
import java.util.*


class LoginModel:ViewModel() {

    val user :MutableLiveData<FirebaseUser?> by lazy{
        MutableLiveData<FirebaseUser?>()
    }

    //logs in user with right credentials
    public fun login(auth: FirebaseAuth, activity: Activity, email: String, password: String, loginProgressBar: ProgressBar){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success - show log, update user value
                    Log.d("AUTHENTICATION", "login :success $user")
                    DatabaseAPIHandler(activity.applicationContext, AsyncResponse {
                        var customer = ObjectConverter.convertStringToObject(it, Customer::class.java, false) as Customer
                        if (customer != null) {
                            var sessionData = SessionData(activity)
                            if (customer.isFarmer) {
                                DatabaseAPIHandler(activity.applicationContext, AsyncResponse {
                                    var farmer = ObjectConverter.convertStringToObject(it, Farmer::class.java, false) as Farmer
                                    if (farmer != null) {
                                        sessionData.setUserDataForSession(farmer, customer)
                                    } else {
                                        Toast.makeText(activity.applicationContext, "Farmer For Customer: ${email} does not exist!", Toast.LENGTH_SHORT).show()
                                    }
                                    loginProgressBar.visibility = ProgressBar.GONE
                                    user.value = auth.currentUser
                                }).execute("/FarmerByCustomerID/${customer.customerID}")
                            } else {
                                sessionData.setUserDataForSession(null, customer)
                                loginProgressBar.visibility = ProgressBar.GONE
                                user.value = auth.currentUser
                            }
                        } else {
                            Toast.makeText(activity.applicationContext, "Customer: ${email} does not exist!", Toast.LENGTH_SHORT).show()
                        }

                    }).execute("/CustomerByEmail/${email}")
                } else {
                    loginProgressBar.visibility = ProgressBar.GONE
                    user.value = null
                    Toast.makeText(activity.applicationContext, "Incorrect email/password", Toast.LENGTH_SHORT).show()
                    // If sign in fails show log
                    Log.d("AUTHENTICATION", "login :failure")
                }
            }
    }

    //Registers the google Sign in as an authenticated user in Firebase, lasts only within a session
    public fun firebaseAuthWithGoogle(activity: Activity, auth: FirebaseAuth, idToken: String, bundle: Bundle?, loginProgressBar: ProgressBar) {
        val cred = GoogleAuthProvider.getCredential(idToken, null)
        loginProgressBar.visibility = ProgressBar.VISIBLE
        signInWithCredential(activity, cred, auth, "google",loginProgressBar)
    }

    //Registers the facebook Sign in as an authenticated user in Firebase, lasts only within a session
    public fun firebaseAuthWithFacebook(activity: Activity, auth: FirebaseAuth, token: AccessToken, loginProgressBar: ProgressBar){
        val cred =  FacebookAuthProvider.getCredential(token.token)
        loginProgressBar.visibility = ProgressBar.VISIBLE
        signInWithCredential(activity, cred, auth, "facebook",loginProgressBar)
    }

    private fun signInWithCredential(activity: Activity, credential: AuthCredential, auth: FirebaseAuth, platform: String,loginProgressBar: ProgressBar){
        auth.signInWithCredential(credential).addOnCompleteListener(activity){ task ->
            if(task.isSuccessful){
                // Sign in success - show log, update user value
                val email = auth.currentUser?.email
                DatabaseAPIHandler(activity.applicationContext, AsyncResponse {cust->
                    if (!cust.isNullOrBlank()) {
                        var customer = ObjectConverter.convertStringToObject(cust, Customer::class.java, false) as Customer
                        var sessionData = SessionData(activity)
                        if (customer.isFarmer) {
                            DatabaseAPIHandler(activity.applicationContext, AsyncResponse {farm->
                                if(!(farm.isNullOrBlank())){
                                    var farmer = ObjectConverter.convertStringToObject(farm, Farmer::class.java, false) as Farmer
                                    if (farmer != null) {
                                        sessionData.setUserDataForSession(farmer, customer)
                                    } else {
                                        Toast.makeText(activity.applicationContext, "Farmer For Customer: ${email} does not exist!", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                loginProgressBar.visibility = ProgressBar.GONE
                                user.value = auth.currentUser
                            }).execute("/FarmerByCustomerID/${customer.customerID}")
                        }
                        else{
                            sessionData.setUserDataForSession(null, customer)
                        }

                    }
                    else {
                        //create this user
                        var sessionData = SessionData(activity)
                        val dateFormat = SimpleDateFormat("yyyymmddhhmmss")
                        val date = dateFormat.format(Date())
                        var customer = Customer(1, email, email,date,false)
                        DatabaseAPIHandler(activity, AsyncResponse {resp->
                            var cust = ObjectConverter.convertStringToObject(resp,Customer::class.java,false) as Customer
                            if(cust != null){
                                sessionData.setUserDataForSession(null,cust)
                            }

                            loginProgressBar.visibility = ProgressBar.GONE
                            user.value = auth.currentUser

                            Log.d("AUTHENTICATION", "login with $platform :success $user")
                        }).execute("/addCustomer",customer)
                    }
                }).execute("/CustomerByEmail/${email}")
            }
            else{
                // If sign in fails show log
                loginProgressBar.visibility = ProgressBar.GONE
                user.value = null
                Log.d("AUTHENTICATION", "login with $platform :failure")
            }
        }
    }

    //validates the input in Email and Password fields according to the regex provided
    fun loginValidation(emailInput: EditText, passwordInput: EditText) : Boolean{
        var regexPattern= Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,20}$")
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput.text).matches()){
            emailInput.setError("Wrong email")
        }
        if(!passwordInput.text.matches(regexPattern)){
            passwordInput.setError(
                "Password must be 6 to 20 characters." +
                        " Password must include letters and numbers"
            )

        }
        return android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput.text).matches() &&
                passwordInput.text.matches(regexPattern)
    }
}
