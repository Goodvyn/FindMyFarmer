package sheridan.capstone.findmyfarmer.LoginAndRegistration.Controller

/**
 * @author Kartavyi Nikita
 * Description: This activity is acting as a controller for the whole registration/login segment
 * of the application. It decides what functions have to be called, passes data from View to Model
 * It implements two interfaces in order to offload some logic from the view
 * Date Modified: December 14th, 2020
 **/
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_farmer_confirmation.*
import kotlinx.android.synthetic.main.fragment_modified_login.*
import sheridan.capstone.findmyfarmer.LoginAndRegistration.Model.LoginModel
import sheridan.capstone.findmyfarmer.LoginAndRegistration.Model.RegistrationModel
import sheridan.capstone.findmyfarmer.LoginAndRegistration.Model.ResetModel
import sheridan.capstone.findmyfarmer.R
import sheridan.capstone.findmyfarmer.SessionDataHandler.SessionData
import sheridan.capstone.findmyfarmer.Users.CustomerActivity
import sheridan.capstone.findmyfarmer.Users.FarmerActivity

class LoginRegistrationController : AppCompatActivity(), LoginRegistrationInterface, ViewBehaviorInterface{

    private lateinit var auth: FirebaseAuth
    private var RC_SIGN_IN  = 9001
    private lateinit var sic : GoogleSignInClient
    private lateinit var callBackManager: CallbackManager
    private val loginModel: LoginModel by viewModels()
    private val registerModel: RegistrationModel by viewModels()
    private val resetModel : ResetModel by viewModels()
    private var user: FirebaseUser? = null
    private var registeredUser: Boolean = false
    private lateinit var sessionData: SessionData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = Firebase.auth
        callBackManager = CallbackManager.Factory.create()
        sessionData = SessionData(this)
        //observer for the user value in the LoginModel Class
        //if changed - log in to the farmerListing
        val authObserver = Observer<FirebaseUser?>{ newAuth ->
            user = newAuth
            if(user != null){
                updateUI(this,user)
            }else{
               /* Toast.makeText(applicationContext, "Incorrect email/password",
                    Toast.LENGTH_SHORT).show()*/
            }
        }

        //if the email in reset is registered and reset password was sent - hide keyboard, show Toast
        val resetObserver = Observer<Boolean> {
            newEmailStatus -> registeredUser = newEmailStatus
            var toastMessage: String = if(registeredUser){

                var layout : View = findViewById(R.id.resetPasswordConstraintLayout)
                hideKeyboard(layout)
                layout.requestFocus()
                "Check your E-mail. The reset password is sent"
            }else{
                "Something went wrong. This E-mail might not be registered"
            }
            Toast.makeText(applicationContext, toastMessage,
                Toast.LENGTH_LONG).show()
        }
        //observe when user value has been changed in the resetModel
        resetModel.registeredEmail.observe(this,resetObserver)
        //observe when user value has been changed in the registerModel
        registerModel.user.observe(this,authObserver)
        //observe when user value has been changed in the LoginModel
        loginModel.user.observe(this, authObserver)
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(this,currentUser)
    }

    //starts the google login pop-up, allowing the user to choose the google account for log in
    private fun googleLogIn(){
        //initializing google services for login
        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        sic = GoogleSignIn.getClient(this, gso)
        var signInGoogle = sic.signInIntent
        startActivityForResult(signInGoogle, RC_SIGN_IN)
    }

    //After the user chooses the account (Facebook or google), this handles the user data returned
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //Tries to Log in into google first to check if the credentials are approved by google or not
        if(requestCode == RC_SIGN_IN){
            //if all is good then the account object is returned back
            var googleAcc = GoogleSignIn.getSignedInAccountFromIntent(data)
            if (googleAcc != null) {
                try{
                    //breaking down the account object to retrieve the basic data from the account, like name, email, id etc
                    val acc = googleAcc.getResult(ApiException::class.java)!!
                    loginModel.firebaseAuthWithGoogle(this,auth,acc.idToken!!,bundle=null, loginProgressBar)
                }
                catch (e: Exception){
                    Log.w("GOOGLE SIGN IN FAILED", e)
                }
            }
            else{
                Toast.makeText(applicationContext, "Sign In Not Successful", Toast.LENGTH_LONG).show()
            }
        }
        else{
            callBackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

   //Opens next activity if the user signed in successfully
    private fun updateUI(context: Context, user: FirebaseUser?, extras: Bundle.() -> Unit = {}){
       var loggedIn : Intent
       var customer = sessionData.customerData

       if(customer != null){
           if(customer.isFarmer){
               loggedIn = Intent(applicationContext, FarmerActivity::class.java)
               loggedIn.putExtras(Bundle().apply(extras))
               loggedIn.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
               startActivity(loggedIn)
               this.finish()
           }
           else{
               loggedIn = Intent(applicationContext, CustomerActivity::class.java)
               loggedIn.putExtras(Bundle().apply(extras))
               loggedIn.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
               startActivity(loggedIn)
               this.finish()
           }
       }
   }

    //Run validation and login function with input provided by the user
    override fun OnLoginButtonClickListener(email: EditText, password: EditText) {
            if(loginModel.loginValidation(email, password)) {
                loginProgressBar.visibility = ProgressBar.VISIBLE
                loginModel.login( auth, this, email.text.toString(), password.text.toString(), loginProgressBar)

        }
    }

    //When sign up button is clicked - parse the information to the input validation and then signUp
    override fun OnSignUpButtonClickListener(email: String, name: String, password: String, isFarmer: Boolean) {
            progressRegistration.visibility = ProgressBar.VISIBLE
            registerModel.register(auth,this,email,name,password,isFarmer,progressRegistration)
    }

    //Open registration fragment on link click
    override fun OnRegisterLinkClickListener() {
        var navController = Navigation.findNavController(this,R.id.fragment_host)
        navController.navigate(R.id.action_loginFragment_to_registrationFragment)
    }

    //When google Sign In button is pressed - call googleLogIn
    override fun OnGoogleButtonClickListener() {
        googleLogIn()
    }

    //When facebook Sign In button is pressed - call facebook log in
    override fun OnFBLogInButtonClickListener() {
        FBSignIn.setPermissions("email")
        FBSignIn.registerCallback(callBackManager,
            object : FacebookCallback<LoginResult> { override fun onSuccess(result: LoginResult?) {
                    if (result != null) {
                        loginModel.firebaseAuthWithFacebook(this@LoginRegistrationController, auth,result.accessToken,loginProgressBar)
                    } else {
                        Toast.makeText(applicationContext, "Error getting Facebook Account",
                            Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onCancel() {
                }
                override fun onError(error: FacebookException?) {
                    Log.e("FacebookERROR", error.toString())
                }
            })
    }

    //open reset password fragment
    override fun OnResetPasswordButtonClickListener() {
        var navController = Navigation.findNavController(this,R.id.fragment_host)
        navController.navigate(R.id.action_loginFragment_to_resetPasswordFragment)
    }

    //send the reset password link to the email
    override fun OnSendResetButtonClickListener(email: EditText) {
        if(resetModel.loginValidation(email)){
            resetModel.sendResetPasswordEmail(email)
        }
    }

    //validation of the input for registration
    override fun Validation(email: EditText, name: EditText, password: EditText, repeatPassword: EditText):Boolean {
         val validatedSensitive = registerModel.registerValidation(email,password,repeatPassword)
         val validatedName = registerModel.registerNameValidation(name)
        return validatedSensitive && validatedName
    }

    //navigation to the fragment via fragment navigation
    override fun Navigate(FragmentId: Int) {
        var navController = Navigation.findNavController(this,R.id.fragment_host)
        navController.navigate(FragmentId)
    }

    //hide the keyboard
    override fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    //request focus on from all the input fields and hide a keyboard if touched outside of the current input field
    override fun viewBehavior(view: View) {
        view.requestFocus()
        view.setOnTouchListener{ view, m: MotionEvent ->
            hideKeyboard(view)
            view.requestFocus()
            true}
    }

}

