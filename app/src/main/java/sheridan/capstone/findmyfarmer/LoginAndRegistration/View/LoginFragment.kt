package sheridan.capstone.findmyfarmer.LoginAndRegistration.View


/**
 * @author Kartavyi Nikita
 * Description: This fragment is responsible for getting the login input for the classic
 * authentication, or provide the user with options of Google/Facebook sign in. On top of that -
 * the customer can access registration and reset password pages
 * Date Modified: December 14th, 2020
 **/

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import kotlinx.android.synthetic.main.fragment_modified_login.*
import kotlinx.android.synthetic.main.fragment_modified_login.view.*
import sheridan.capstone.findmyfarmer.LoginAndRegistration.Controller.LoginRegistrationInterface
import sheridan.capstone.findmyfarmer.LoginAndRegistration.Controller.ViewBehaviorInterface
import sheridan.capstone.findmyfarmer.MainActivity
import sheridan.capstone.findmyfarmer.R
import sheridan.capstone.findmyfarmer.SplashScreenActivity


class LoginFragment : Fragment() {

    private lateinit var loginInterface: LoginRegistrationInterface
    private lateinit var viewBehaviorInterface: ViewBehaviorInterface


    //References for views and function calls when the specific elements are being clicked
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_modified_login, container, false)
        loginInterface = activity as LoginRegistrationInterface

        view.loginBtn.setOnClickListener{
            loginInterface.OnLoginButtonClickListener(view.inputEmail,view.inputPassword)
        }

        view.faceBookSignInBtn.setOnClickListener{
            view.FBSignIn.performClick()
        }
        view.googleSignInBtn.setOnClickListener{

            loginInterface.OnGoogleButtonClickListener()
        }

        view.FBSignIn.setOnClickListener{
            loginInterface.OnFBLogInButtonClickListener()
        }

        view.registerAccount.setOnClickListener{
            loginInterface.OnRegisterLinkClickListener()
        }
        view.forgotPswrdLink.setOnClickListener{
            loginInterface.OnResetPasswordButtonClickListener()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBehaviorInterface = activity as ViewBehaviorInterface
        viewBehaviorInterface.viewBehavior(constraintLayoutLogIn)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(
            true // default to enabled
        ) {
            override fun handleOnBackPressed() {
                val i = Intent(activity,  SplashScreenActivity::class.java)
                startActivity(i)
                (activity as Activity?)!!.overridePendingTransition(0, 0)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,  // LifecycleOwner
            callback
        )
    }
}
