package sheridan.capstone.findmyfarmer.Users

/**
 * Author:  Sohaib Hussain, Andrei Constantinescu
 **/

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.facebook.login.LoginManager
import com.firebase.ui.auth.AuthUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import sheridan.capstone.findmyfarmer.LoginAndRegistration.Controller.LoginRegistrationController

import sheridan.capstone.findmyfarmer.LoginAndRegistration.View.About
import sheridan.capstone.findmyfarmer.LoginAndRegistration.View.HelpClass
import sheridan.capstone.findmyfarmer.R
import sheridan.capstone.findmyfarmer.SessionDataHandler.SessionData

class FarmerActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener  {

    /*
    Similar to Customer Activity but the Hub option is made visible.
     */

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var NavigationView: NavigationView
    private lateinit var sessionData: SessionData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_farmer_view)

        val navHostFragment_ = supportFragmentManager.findFragmentById(R.id.Farmer_Nav_Host)as NavHostFragment


        val navController = navHostFragment_.navController






        sessionData = SessionData(this)
        var customer = sessionData.customerData
        val toolbarView: Toolbar = findViewById(R.id.toolbarD)

        drawerLayout = findViewById(R.id.drawerLayout)
        NavigationView = findViewById(R.id.nav_view)

        var menu = NavigationView.menu
        var wanttobefarmeritem = menu.findItem(R.id.WantToBeFarmer)
        wanttobefarmeritem.setEnabled(false)
        wanttobefarmeritem.setVisible(false)

        if(customer != null) {
            var usertext = NavigationView.getHeaderView(0).findViewById<TextView>(R.id.user)
            usertext.text = customer.customerName
        }


        NavigationView.setNavigationItemSelectedListener(this)


        setSupportActionBar(toolbarView)

        val toggle= ActionBarDrawerToggle(this,drawerLayout,
            toolbarView,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )


        drawerLayout.addDrawerListener(toggle)

        toggle.syncState()

        val bottomnav: BottomNavigationView = findViewById(R.id.bottom_nav_farmer)


            val menuNav : Menu = bottomnav.menu

            val hub : MenuItem = menuNav.findItem(R.id.nav_manage_hub)
            hub.setEnabled(true)
            hub.setVisible(true)

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
