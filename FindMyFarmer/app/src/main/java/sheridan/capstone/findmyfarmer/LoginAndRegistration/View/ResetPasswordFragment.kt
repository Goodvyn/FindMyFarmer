package sheridan.capstone.findmyfarmer.LoginAndRegistration.View

/**
 * @author Kartavyi Nikita
 * Description: This fragment is responsible for getting the email input for the password reset
 * Date Modified: December 14th, 2020
 **/


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_reset_password.*
import kotlinx.android.synthetic.main.fragment_reset_password.view.*
import sheridan.capstone.findmyfarmer.LoginAndRegistration.Controller.LoginRegistrationInterface
import sheridan.capstone.findmyfarmer.LoginAndRegistration.Controller.ViewBehaviorInterface
import sheridan.capstone.findmyfarmer.R

class ResetPasswordFragment : Fragment() {

    private lateinit var viewBehaviorInterface: ViewBehaviorInterface
    private lateinit var loginRegistrationInterface: LoginRegistrationInterface
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_reset_password, container, false)
        loginRegistrationInterface = activity as LoginRegistrationInterface
        view.sendButton.setOnClickListener{
            loginRegistrationInterface.OnSendResetButtonClickListener(editTextTextEmailAddress)
        }
        view.backButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_resetPasswordFragment_to_loginFragment)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBehaviorInterface = activity as ViewBehaviorInterface
        viewBehaviorInterface.viewBehavior(resetPasswordConstraintLayout)
    }
}
