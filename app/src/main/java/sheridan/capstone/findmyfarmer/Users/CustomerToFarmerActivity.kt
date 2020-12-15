package sheridan.capstone.findmyfarmer.Users

/**
 * Author:  Andrei Constantinescu
 **/
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import sheridan.capstone.findmyfarmer.Database.AsyncResponse
import sheridan.capstone.findmyfarmer.Database.DatabaseAPIHandler
import sheridan.capstone.findmyfarmer.Database.ObjectConverter
import sheridan.capstone.findmyfarmer.Entities.Customer
import sheridan.capstone.findmyfarmer.Entities.Farmer
import sheridan.capstone.findmyfarmer.R
import sheridan.capstone.findmyfarmer.SessionDataHandler.SessionData


class CustomerToFarmerActivity : AppCompatActivity() {

    private lateinit var sessionData: SessionData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_farmer_confirmation)


        val YesFarmer = findViewById<Button>(R.id.YesFarmerBtn)
        val NoFarmer =findViewById<Button>(R.id.NoFarmerBtn)
        sessionData = SessionData(this)
        var customer = sessionData.customerData
        YesFarmer.setOnClickListener {
            if(customer != null){
                if(!customer.isFarmer){
                    var farmer = Farmer(1,customer.customerID)
                    DatabaseAPIHandler(this, AsyncResponse {resp1->
                        if(!(resp1.isNullOrBlank())){
                            var FinalFarmer = ObjectConverter.convertStringToObject(resp1,
                                Farmer::class.java,false) as Farmer
                            var cust = Customer(customer.customerID
                                ,customer.customerName
                                ,customer.customerEmail
                                ,customer.customerPassword
                                ,true)

                            DatabaseAPIHandler(this, AsyncResponse {resp->
                                if(!(resp.isNullOrBlank())){
                                    var FinalCustomer = ObjectConverter.convertStringToObject(resp,
                                        Customer::class.java,false) as Customer
                                    sessionData.setUserDataForSession(FinalFarmer,FinalCustomer);
                                    val loggedIn = Intent(this, FarmerActivity::class.java)
                                    startActivity(loggedIn)
                                    this?.finish()
                                }
                            }).execute("/updateCustomer",cust)
                        }
                    }).execute("/addFarmer",farmer)
                }
            }
        }

        NoFarmer.setOnClickListener {
            val intent = Intent(this, CustomerActivity::class.java)

            startActivity(intent)
        }


    }
    }
