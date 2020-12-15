package sheridan.capstone.findmyfarmer.LoginAndRegistration.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_registration.*
import kotlinx.android.synthetic.main.fragment_registration.view.*
import sheridan.capstone.findmyfarmer.LoginAndRegistration.Controller.LoginRegistrationInterface
import sheridan.capstone.findmyfarmer.LoginAndRegistration.Controller.ViewBehaviorInterface
import sheridan.capstone.findmyfarmer.R


private lateinit var registration_interface: LoginRegistrationInterface
private lateinit var viewBehaviorInterface: ViewBehaviorInterface
lateinit var email: String
class RegistrationFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_registration, container, false)
        registration_interface = activity as LoginRegistrationInterface
        viewBehaviorInterface = activity as ViewBehaviorInterface

        //validates the input and moves to the confirmation
        view.nextButton.setOnClickListener{

         if(registration_interface.Validation(view.newUserEmail,view.NameInput,view.password,view.repeatPassword)) {
             //Database Writing information to
             val directions = RegistrationFragmentDirections.
             actionRegistrationFragmentToFarmerConfirmationFragment(newUserEmail.text.toString(),
                 NameInput.text.toString(),password.text.toString())
             view.findNavController().navigate(directions)
             viewBehaviorInterface.hideKeyboard(view)
         }

        }
        return view
    }


    private fun sendData(){
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBehaviorInterface = activity as ViewBehaviorInterface
        viewBehaviorInterface.viewBehavior(FrameLayout)
    }

}