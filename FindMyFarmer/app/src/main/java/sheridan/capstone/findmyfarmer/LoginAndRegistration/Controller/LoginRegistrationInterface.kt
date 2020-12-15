package sheridan.capstone.findmyfarmer.LoginAndRegistration.Controller


/**
 * @author Kartavyi Nikita
 * Description: This interface is designed in order to offload the code from the view
 * to the controller. The reason for that is to separate the functionality, so the view
 * is just calling the functions on press of a dedicated element and all of the functionality is
 * in the LoginRegistrationController which implements this interface. This particular interface is
 * used for the logical functionality of Login and registration segment of the application
 * Date Modified: December 14th, 2020
 **/
import android.text.Editable
import android.view.View
import android.widget.EditText
import org.w3c.dom.Text
import sheridan.capstone.findmyfarmer.LoginAndRegistration.View.LoginFragment

interface LoginRegistrationInterface {
    fun OnLoginButtonClickListener(email: EditText, password: EditText)
    fun OnSignUpButtonClickListener(email: String, name: String, password: String, isFarmer: Boolean)
    fun OnRegisterLinkClickListener()
    fun OnGoogleButtonClickListener()
    fun OnFBLogInButtonClickListener()
    fun OnResetPasswordButtonClickListener()
    fun OnSendResetButtonClickListener(email: EditText)
    fun Validation(email: EditText, name: EditText, password: EditText, repeatPassword: EditText):Boolean
    fun Navigate(FragmentId: Int)
}
