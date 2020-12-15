package sheridan.capstone.findmyfarmer.LoginAndRegistration.View

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_farmer_confirmation.view.*
import kotlinx.android.synthetic.main.fragment_registration.*
import sheridan.capstone.findmyfarmer.LoginAndRegistration.Controller.LoginRegistrationInterface
import sheridan.capstone.findmyfarmer.R

/**
 * @author Kartavyi Nikita
 * Description: This fragment is responsible for getting the confirmation from user if he/she is a
 * farmer or not. The que is sent to the database to specify what type of the account to create and
 * all the input from registration fragment is passed to the dedicated registration function
 * Date Modified: December 14th, 2020
 **/

class FarmerConfirmationFragment : Fragment() {

    private val args: FarmerConfirmationFragmentArgs by navArgs()
    private lateinit var email: String
    private lateinit var name: String
    private lateinit var password: String

    private lateinit var registrationInterface: LoginRegistrationInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        registrationInterface = activity as LoginRegistrationInterface
        val view = inflater.inflate(R.layout.fragment_farmer_confirmation, container, false)

        email = args.email
        name = args.name
        password = args.password

        view.YesFarmerBtn.setOnClickListener{
            registrationInterface.OnSignUpButtonClickListener(email, name, password, true)
            Log.d("ARGUMENT","No Farmer" +
                    "\nEmail: $email\n Name: $name\n Password: $password\n")
        }
        view.NoFarmerBtn.setOnClickListener{
            registrationInterface.OnSignUpButtonClickListener(email, name, password, false)
            Log.d("ARGUMENT","Yes Farmer" +
                    "\nEmail: $email\n Name: $name\n Password: $password\n")
        }
        return view
    }
}
