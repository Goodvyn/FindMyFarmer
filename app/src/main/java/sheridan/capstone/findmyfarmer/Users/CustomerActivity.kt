package sheridan.capstone.findmyfarmer.Users

/**
 * Author:  Andrei Constantinescu
 *
 **/

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.facebook.login.LoginManager
import com.firebase.ui.auth.AuthUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import sheridan.capstone.findmyfarmer.Customer.View.Following
import sheridan.capstone.findmyfarmer.Customer.View.Maps
import sheridan.capstone.findmyfarmer.Customer.View.MarketPlace
import sheridan.capstone.findmyfarmer.LoginAndRegistration.Controller.LoginRegistrationController
import sheridan.capstone.findmyfarmer.LoginAndRegistration.View.About
import sheridan.capstone.findmyfarmer.LoginAndRegistration.View.HelpClass
import sheridan.capstone.findmyfarmer.R
import sheridan.capstone.findmyfarmer.SessionDataHandler.SessionData


class CustomerActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {

    /*
    The customer activity. This activity holds all the fragments (Market, Following & Maps) In the
    Fragment Container View.
    Binds the nav controller to the bottom nav
    Sets customer_ID in a session variable to be used throughout for this activity.
     */

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var NavigationView: NavigationView
    private lateinit var sessionData: SessionData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_view)

        val navHostFragment_ = supportFragmentManager.findFragmentById(R.id.Customer_Nav_Host)as NavHostFragment


        val navController = navHostFragment_.navController


        sessionData = SessionData(this)
        var customer = sessionData.customerData
        val toolbarView: Toolbar = findViewById(R.id.toolbarD)

        drawerLayout = findViewById(R.id.drawerLayout)
        NavigationView = findViewById(R.id.nav_view)

        NavigationView.setNavigationItemSelectedListener(this)

        if(customer != null) {
            var usertext = NavigationView.getHeaderView(0).findViewById<TextView>(R.id.user)
            usertext.text = customer.customerName
        }


        setSupportActionBar(toolbarView)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout,
            toolbarView,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )


        drawerLayout.addDrawerListener(toggle)

        toggle.syncState()


        val bottomnav: BottomNavigationView = findViewById(R.id.Bottom_nav_customer)


        bottomnav.setupWithNavController(navController)

    }

    /**
     * @param item user clicks an item from the Navigation sidebar
     */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_logout ->{
                logOut()
                finish()
            }
            R.id.nav_account -> {
                val intent = Intent(this, AccountSettings::class.java)
                // start your next activity
                startActivity(intent)
            }
            R.id.WantToBeFarmer ->{
                val intent = Intent(this, CustomerToFarmerActivity::class.java)

                startActivity(intent)
            }

            //author: Afsar AHmed
            //both are just to activate when item is pressed
            R.id.About ->{
                startActivity(Intent(this, About::class.java))
            }

            R.id.Help ->{
                startActivity(Intent(this, HelpClass::class.java))
            }
        }

        return true
    }
    private fun logOut(){
        Firebase.auth.signOut()

        LoginManager.getInstance().logOut()
        AuthUI.getInstance().signOut(this).addOnCompleteListener(){
            sessionData.ClearAllData()
            startActivity(
                Intent(this,
                    LoginRegistrationController::class.java)
            )
        }
        Toast.makeText(applicationContext, "Logged out", Toast.LENGTH_LONG).show()
    }
}
