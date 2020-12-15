package sheridan.capstone.findmyfarmer.LoginAndRegistration.Model

/**
 * @author Kartavyi Nikita
 * Description: This activity is a model of the registration part of the application.
 * The firebase registration logic is stored here, as well as the custom validation for the input.
 * On top of that, Database calls for retrieving customer data is based in this activity
 * Date Modified: December 14th, 2020
 **/

import android.app.Activity
import android.util.Log
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import sheridan.capstone.findmyfarmer.Database.AsyncResponse
import sheridan.capstone.findmyfarmer.Database.DatabaseAPIHandler
import sheridan.capstone.findmyfarmer.Database.ObjectConverter
import sheridan.capstone.findmyfarmer.Entities.Customer
import sheridan.capstone.findmyfarmer.Entities.Farmer
import sheridan.capstone.findmyfarmer.SessionDataHandler.SessionData


class RegistrationModel:ViewModel() {

    val user : MutableLiveData<FirebaseUser?> by lazy{
        MutableLiveData<FirebaseUser?>()
    }
    private lateinit var sessionData: SessionData

    //registration of the user in the firebase and database with the validated user input
    public fun register(auth: FirebaseAuth, activity: Activity, email: String,name:String, password: String,IsFarmer:Boolean,progressBar: ProgressBar) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    //if task is successful new user is added to the Firebase
                    //Creating customer to add to database
                    //Adding customer instance to database
                    sessionData = SessionData(activity)
                    var customer = Customer(1,name,email,password,IsFarmer)
                    DatabaseAPIHandler(activity, AsyncResponse {
                        var CustomerAdded = ObjectConverter.convertStringToObject(it,Customer::class.java,false) as Customer
                        if(CustomerAdded != null){
                            if(customer.isFarmer){
                                var farmer = Farmer(1,CustomerAdded.customerID)
                                DatabaseAPIHandler(activity, AsyncResponse {it2 ->
                                    var FarmerAdded = ObjectConverter.convertStringToObject(it2,Farmer::class.java,false) as Farmer
                                    if(FarmerAdded != null){
                                        sessionData.setUserDataForSession(FarmerAdded,CustomerAdded)
                                    }
                                    progressBar.visibility = ProgressBar.GONE
                                    user.value = auth.currentUser
                                }).execute("/addFarmer",farmer)
                            }
                            else{
                                sessionData.setUserDataForSession(null,CustomerAdded)
                                user.value = auth.currentUser
                                progressBar.visibility = ProgressBar.GONE
                            }
                        }
                        progressBar.visibility = ProgressBar.GONE
                    }).execute("/addCustomer",customer)

                    Log.d("REGISTRATION", "registration :success $user")
                } else {
                    // If registration fails show log
                    progressBar.visibility = ProgressBar.GONE
                    user.value = null
                    Toast.makeText(activity.applicationContext,"This email is in use already",Toast.LENGTH_SHORT).show()
                    Log.d("REGISTRATION", "registration :failure")
                }
            }
    }


    //email validation using the custom regex pattern
    public fun registerNameValidation(name: EditText):Boolean{
        var regexPattern= Regex("\\b([A-ZÀ-ÿ][-,a-z. ']+[ ]*)+")
        var nameValidated = false
        if(name.text.matches(regexPattern)){
            nameValidated = true
        }else{
            name.setError("Please enter properly formatted name")
        }
        return nameValidated

    }

    /**
     * @author: Afsar Ahmed & Nikita Kartavyi
     * validates user when registering
     * @param emailInput user input an follows default requirements of email
     * @param passwordInput user input and must abide by regex pattern
     * @param repeatPasswordInput user input and must match passwordInput as well as regex pattern
     *
     * @return true if validation of all parameters follow protocol
     */
    public  fun registerValidation(emailInput: EditText, passwordInput: EditText, repeatPasswordInput: EditText) : Boolean{
        var regexPattern= Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,20}$")
        var emailInputVerification: Boolean = false
        var passwordInputVerification: Boolean = false
        var repeatPasswordInputVerification: Boolean = false

        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput.text).matches()){
            emailInput.setError("Wrong email")
            emailInputVerification = false
        }else{
            emailInputVerification = true
        }
        if(!passwordInput.text.matches(regexPattern)){
            passwordInput.setError("Password must be 6 to 20 characters. Password must include letters and numbers")
            passwordInputVerification = false
        }else{
            passwordInputVerification = true
            if(repeatPasswordInput.text.toString().equals(passwordInput.text.toString(),false)) {
                repeatPasswordInputVerification = true
            }else{
                repeatPasswordInput.setError("Password doesn't match")
                repeatPasswordInputVerification = false
            }
        }

        return emailInputVerification && passwordInputVerification && repeatPasswordInputVerification
    }
}
