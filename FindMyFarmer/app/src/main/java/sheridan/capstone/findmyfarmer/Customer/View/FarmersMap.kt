package sheridan.capstone.findmyfarmer.Customer.View
/**
 * @author: Andrei Constantinecu
 */


import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import sheridan.capstone.findmyfarmer.Customer.Model.MapHandler
import sheridan.capstone.findmyfarmer.Customer.Model.SharedViewModel
import sheridan.capstone.findmyfarmer.R
import java.util.*


class FarmersMap : Fragment(), OnMapReadyCallback {

    /*
    * @return Farmers Map fragment that instantiates the map view for the specific farm.
    */


    private lateinit var address: Address

    private lateinit var mapview : MapView


    private lateinit var addresslist : List<Address>

    var Farmers_Address : String = ""

    var viewModel: SharedViewModel = SharedViewModel()

    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val View: View = inflater.inflate(R.layout.fragment_farmers_map, container, false)

        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        Farmers_Address= viewModel.getFarmData().value!!.street +
                ","+ viewModel.getFarmData().value!!.city + "," + viewModel.getFarmData().value!!.province + " "
        viewModel.getFarmData().value!!.postalCode

        mapview = View.findViewById(R.id.farmers_map)

        mapview.onCreate(savedInstanceState)
        mapview.onResume()


        mapview.getMapAsync(this)


        return View
    }

    override fun onMapReady(mMap: GoogleMap?) {

        val geo = Geocoder(requireActivity(), Locale.getDefault())
        MapHandler(requireActivity(), object : MapResponse {
            override fun onProcessComplete(Obj: Any) {
                addresslist = Obj as List<Address>
                if (addresslist.size != 0) {
                    address = addresslist.get(0)

                    mMap!!.addMarker(MarkerOptions().position(LatLng(address.latitude, address.longitude)).title(viewModel.getFarmData().value!!.businessName))

                    mMap!!.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                address.latitude,
                                address.longitude
                            ), 15f

                        )
                    )
                }
                else
                    Toast.makeText(requireActivity(),"No Address Found",Toast.LENGTH_LONG)

            }
        }).execute(Farmers_Address)




    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(
            true // default to enabled
        ) {
            override fun handleOnBackPressed() {

            findNavController().navigate(R.id.action_fragment_farmers_map_to_fragment_farmer_info)

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,  // LifecycleOwner
            callback
        )
    }

}
