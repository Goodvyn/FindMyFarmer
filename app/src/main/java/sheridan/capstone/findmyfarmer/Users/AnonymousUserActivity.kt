package sheridan.capstone.findmyfarmer.Users

/**
 * Author:  Andrei Constantinescu
 * Sets up the Anonymous activity.
 **/

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import sheridan.capstone.findmyfarmer.Customer.View.Following
import sheridan.capstone.findmyfarmer.Customer.View.Maps
import sheridan.capstone.findmyfarmer.Customer.View.MarketPlace
import sheridan.capstone.findmyfarmer.Farmer.View.FarmerHub
import sheridan.capstone.findmyfarmer.LoginAndRegistration.Controller.LoginRegistrationController
import sheridan.capstone.findmyfarmer.LoginAndRegistration.View.About
import sheridan.capstone.findmyfarmer.LoginAndRegistration.View.HelpClass
import sheridan.capstone.findmyfarmer.R
import sheridan.capstone.findmyfarmer.SessionDataHandler.SessionData

class AnonymousUserActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var NavigationView: NavigationView
    private lateinit var sessionData: SessionData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anonymous_user)

        val navHostFragment_ = supportFragmentManager.findFragmentById(R.id.Anon_Nav_Host)as NavHostFragment


        val navController = navHostFragment_.navController

        val toolbarView: Toolbar = findViewById(R.id.toolbarD)
        sessionData = SessionData(this)

        var customer = sessionData.customerData

        checkIfSignedInAccount()
        drawerLayout = findViewById(R.id.anon_drawer)
        NavigationView = findViewById(R.id.nav_anon_view)

        NavigationView.setNavigationItemSelectedListener(this)

        val bottomnav: BottomNavigationView = findViewById(R.id.Anon_Nav)

        val menuNav : Menu = bottomnav.menu

        val hub : MenuItem = menuNav.findItem(R.id.nav_manage_hub)

        val following : MenuItem = menuNav.findItem(R.id.nav_following)

        hub.setEnabled(false)
        hub.setVisible(false)
        following.setEnabled(false)
        following.setVisible(false)

        bottomnav.setupWithNavController(navController)

        setSupportActionBar(toolbarView)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout,
            toolbarView,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


    }

    /**
     * @param item user clicks an item from the Navigation sidebar
     */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_manage ->{
                startActivity(Intent(this, LoginRegistrationController::class.java))
                //this.finish()
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

    private fun checkIfSignedInAccount() {

        val user = Firebase.auth.currentUser
        var customer = sessionData.customerData
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
    override fun onBackPressed() {
        super.onBackPressed()

    }
}
