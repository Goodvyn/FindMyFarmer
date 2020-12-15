package sheridan.capstone.findmyfarmer.Customer.View


/**
 * @author: Andrei Constantinecu
 * Sets up the Farmer Fragment
 */

import android.annotation.SuppressLint

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import sheridan.capstone.findmyfarmer.Customer.Model.*
import sheridan.capstone.findmyfarmer.Entities.Following
import sheridan.capstone.findmyfarmer.Entities.Product
import sheridan.capstone.findmyfarmer.Farmer.Controller.FruitListToView
import sheridan.capstone.findmyfarmer.R
import sheridan.capstone.findmyfarmer.SessionDataHandler.SessionData

class FarmerInfo : Fragment(),FruitListToView.OnItemClickListener{

    /*
   Takes the farmers information from the shared view model
   @returns fragment view
   */

    private lateinit var FarmName:TextView
    private lateinit var FarmDesc:TextView
    private lateinit var FarmRating : RatingBar
    private lateinit var FarmAddress:TextView
    private lateinit var FarmImage : ImageView
    private lateinit var To_Map: Button
    private lateinit var RateIt: ImageView
    private lateinit var FarmFollow : ImageView

    private var productlist = ArrayList<Product>()
    var ImageInt : Int =0
    private lateinit var viewModel: SharedViewModel
    private lateinit var sessionData: SessionData

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view:View=  inflater.inflate(R.layout.fragment_farmer_info, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        sessionData = SessionData(activity)
        FarmName  = view.findViewById(R.id.Name)
        FarmAddress = view.findViewById(R.id.Address)
        FarmDesc = view.findViewById(R.id.Desc)
        FarmRating = view.findViewById(R.id.Ratings)
        FarmImage = view.findViewById(R.id.icon)

        To_Map = view.findViewById(R.id.Maps)
        RateIt = view.findViewById(R.id.RateIt)
        FarmFollow = view.findViewById(R.id.FollowIcon)

        val recycleView : RecyclerView = view.findViewById(R.id.Fruit_List)

        var adapter = FruitListToView(productlist, this)
        recycleView.adapter = adapter
        recycleView.layoutManager = LinearLayoutManager(context)
        recycleView.setHasFixedSize(true)

        var farmproduct = activity?.let { GetAllProducts(it,adapter) }
        if (farmproduct != null) {
            farmproduct.farmProducts(productlist,viewModel.getFarmData().value!!.farmID)
        }

        RateIt.setOnClickListener{
            openDialog()
        }

        FarmFollow.setOnClickListener {
            if(FarmFollow.drawable.constantState == resources.getDrawable(android.R.drawable.btn_star_big_off).constantState){
                var Following = FollowingDialog(FarmFollow)

                val FragmentManager : FragmentManager? = activity?.supportFragmentManager
                if (FragmentManager != null) {
                    Following.show(FragmentManager, "Follow?")
                }
            }
            else{

                var deletedFollow = activity?.let { it1 -> DeleteFollow(it1,FarmFollow) }
                var farmid = viewModel.getFarmData().value!!.farmID
                var customerid = sessionData.customerData.customerID
                var following = Following(1,customerid,farmid)
                if (deletedFollow != null) {
                    deletedFollow.removefollow(following)
                }
            }
        }

        var farm = viewModel.getFarmData().value

        To_Map.setOnClickListener {


            this.findNavController().navigate(R.id.action_fragment_farmer_info_to_farmersMap)
        }
        if (farm != null) {
            if(farm.isFollowed){
               FarmFollow.setImageDrawable(context?.resources?.getDrawable(android.R.drawable.btn_star_big_on))
            }
            else{
                FarmFollow.setImageDrawable(context?.resources?.getDrawable(android.R.drawable.btn_star_big_off))
            }

            if(farm.alreadyRated){
                RateIt.visibility = View.INVISIBLE
                RateIt.isEnabled = false;
            }
            else{
                RateIt.visibility = View.VISIBLE
                RateIt.isEnabled = true;
            }
        }

        if(Firebase.auth.currentUser!!.isAnonymous){
            RateIt.visibility = View.INVISIBLE
            RateIt.isEnabled = false
            FarmFollow.visibility = View.INVISIBLE
            FarmFollow.isEnabled = false
        }
        return view
    }

    private fun openDialog() {
        val FragmentManager : FragmentManager? = activity?.supportFragmentManager
        val exampleDialog = RateItDialogue(RateIt)

        if (FragmentManager != null) {
            exampleDialog.show(FragmentManager,"Rate it")
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var farmdata= viewModel.getFarmData().value!!

        if(!(farmdata.primaryImage.isNullOrBlank())){
            Picasso.get().load(farmdata.primaryImage).into(FarmImage)
        }
        FarmName.text = farmdata.businessName
        var builder = StringBuilder()
        if(farmdata.unit != 0){
            builder.append(farmdata.unit.toString() + "-")
        }
        builder.append(farmdata.street + ", ")
        builder.append(farmdata.city + ", ")
        builder.append(farmdata.country + ", ")
        builder.append(farmdata.province +", ")
        builder.append(farmdata.postalCode)
        FarmAddress.text = builder.toString()
        FarmDesc.text = farmdata.businessDescription
        FarmRating.rating = farmdata.businessRating

    }

    override fun onItemClick(position: Int) {
        //Click Event For FruitList
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(
            true // default to enabled
        ) {
            override fun handleOnBackPressed() {

                findNavController().navigate(R.id.action_fragment_farmer_info_to_nav_market)

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,  // LifecycleOwner
            callback
        )
    }
}
