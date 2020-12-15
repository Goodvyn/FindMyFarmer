package sheridan.capstone.findmyfarmer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.facebook.appevents.codeless.internal.ViewHierarchy.setOnClickListener
import com.facebook.login.Login
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import sheridan.capstone.findmyfarmer.LoginAndRegistration.Controller.LoginRegistrationController
import sheridan.capstone.findmyfarmer.SessionDataHandler.SessionData
import sheridan.capstone.findmyfarmer.Users.AnonymousUserActivity
import sheridan.capstone.findmyfarmer.Users.CustomerActivity
import sheridan.capstone.findmyfarmer.Users.FarmerActivity

class MainActivity : AppCompatActivity() {
    private lateinit var Login : Button
    private  lateinit var GetStarted : Button
    private lateinit var sessionData: SessionData
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.get_started)
        sessionData = SessionData(this)
        checkIfSignedInAccount()
        auth = Firebase.auth
        Login = findViewById(R.id.login_btn)
        GetStarted = findViewById(R.id.Get_Started)

        Login.setOnClickListener{
            startActivity(Intent(this, LoginRegistrationController::class.java))
            finish()
        }
        GetStarted.setOnClickListener {
            auth.signInAnonymously()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("Anonymous", "signInAnonymously:success")
                        val user = auth.currentUser
                        startActivity(Intent(this, AnonymousUserActivity::class.java))
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.d("Anonymous", "signInAnonymously:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    //Checks if the user in signed in the account
    //if not - go to the sign in page. if yes - go to a different activity
    private fun checkIfSignedInAccount() {

        val user = Firebase.auth.currentUser
        var customer = sessionData.customerData
        if(user!=null){
             if (user.isAnonymous){
                startActivity(Intent(this, AnonymousUserActivity::class.java))
            }
        }
        if (user != null && customer != null) {
            if(customer.isFarmer){
                startActivity(Intent(this,
                    FarmerActivity::class.java))
            }
            else{
                startActivity(Intent(this,
                    CustomerActivity::class.java))
            }

        }
        else {
            sessionData.ClearAllData()
        }
    }

    override fun onResume() {
        super.onResume()
        checkIfSignedInAccount()
    }
}
