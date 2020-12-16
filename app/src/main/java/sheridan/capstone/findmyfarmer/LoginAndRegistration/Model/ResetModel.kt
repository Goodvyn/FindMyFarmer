package sheridan.capstone.findmyfarmer.LoginAndRegistration.Model

/**
 * @author Kartavyi Nikita
 * Description: This activity is a model of the reset password part of the application.
 * Methods to connect with the firebase and validate the email to send the link to is based here
 * Date Modified: December 14th, 2020
 **/

import android.util.Log
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ResetModel: ViewModel() {


    val registeredEmail : MutableLiveData<Boolean> by lazy{
        MutableLiveData<Boolean>()
    }


    /**
     * @author Nikita Kartavyi
     * @param [emailInput] is the view of type [EditText] which is used to get the email address
     * of the user.
     * @Description Firebase validates that this email is registered in the database.
     * The variable [registeredEmail] is set to true or false depending on the result of the validation
     */
    public fun sendResetPasswordEmail(emailInput: EditText){
        Log.d("RESET", "Entered Function")
        Firebase.auth.sendPasswordResetEmail(emailInput.text.toString())
            .addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    registeredEmail.value = true
                    Log.d("RESET", "Email sent.")
                }else{
                    registeredEmail.value = false
                    Log.d("RESET", "Not Sent")
                }
            }
    }


    /**
     * @author Nikita Kartavyi
     * @param [emailInput] is the view of type [EditText] which is used to get the email address
     * @return [Boolean] true or false based on the result of the validation
     * of the user.
     * @Description Validates the email address, to ensure that there is no unnecessary server
     * processing. Sets the error message in the EditText which indicates if the input format is
     * incorrect.
     */
    public fun loginValidation(emailInput: EditText) : Boolean{
        var regexPattern= Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,20}$")
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput.text).matches()){
            emailInput.setError("Wrong email")
        }
        return android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput.text).matches()
    }
}










































