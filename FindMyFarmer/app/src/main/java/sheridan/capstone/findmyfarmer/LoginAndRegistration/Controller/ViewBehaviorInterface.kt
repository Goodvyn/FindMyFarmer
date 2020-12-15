package sheridan.capstone.findmyfarmer.LoginAndRegistration.Controller

/**
 * @author Kartavyi Nikita
 * Description: This interface is designed in order to offload the code from the view
 * to the controller. The reason for that is to separate the functionality, so the view
 * is just calling the functions on press of a dedicated element and all of the functionality is
 * in the LoginRegistrationController which implements this interface. This particular interface is
 * used for the visuals and user experience in Login/registration segment of the application
 * Hiding the keyboard when clicking outside of the input field and changing focus on the parent view
 * Date Modified: December 14th, 2020
 **/

import android.view.View

interface ViewBehaviorInterface {
    fun hideKeyboard(view: View)
    fun viewBehavior(parentView: View)
}
