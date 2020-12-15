package sheridan.capstone.findmyfarmer.Customer.Model

/**
 * @author: Andrei Constantinecu
 * Sets up the Following dialogue view
 * @constructor FollowingDialog
 */

import android.app.Activity
import android.location.Geocoder
import android.os.AsyncTask
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import sheridan.capstone.findmyfarmer.Customer.View.MapResponse
import java.util.*

class MapHandler(val activity: Activity,var mapResponse : MapResponse) : AsyncTask<Any, Any, Any>() {


    override fun doInBackground(vararg params: Any?): Any {
        var geo = Geocoder(activity, Locale.getDefault())
        return geo.getFromLocationName(params[0].toString(), 1) as Object
    }

    override fun onPostExecute(result: Any) {
        mapResponse.onProcessComplete(result)
    }


}
