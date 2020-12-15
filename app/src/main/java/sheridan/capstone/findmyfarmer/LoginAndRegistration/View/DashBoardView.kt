package sheridan.capstone.findmyfarmer.LoginAndRegistration.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.facebook.login.LoginManager
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_dash_board.*
import sheridan.capstone.findmyfarmer.LoginAndRegistration.Controller.LoginRegistrationController
import sheridan.capstone.findmyfarmer.R

class DashBoardView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)
        
        var logintext = findViewById<TextView>(R.id.LoginText)

        logintext.text = Firebase.auth.currentUser?.email + " " + Firebase.auth.currentUser?.displayName

        //LoginText.text = Firebase.auth.currentUser?.email + " " + Firebase.auth.currentUser?.displayName

        logOutBtn.setOnClickListener{
            logOut()
        }
    }

    private fun logOut(){
        Firebase.auth.signOut()

        LoginManager.getInstance().logOut()
        AuthUI.getInstance().signOut(this).addOnCompleteListener(){
            startActivity(Intent(this,
                LoginRegistrationController::class.java))
        }
        finish()

    }
}