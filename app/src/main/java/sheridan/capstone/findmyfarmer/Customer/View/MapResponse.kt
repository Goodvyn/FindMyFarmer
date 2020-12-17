package sheridan.capstone.findmyfarmer.Customer.View

/**
 * @author: Andrei Constantinecu
 * passes the response to the maps fragment view.
 * Date Modified: December 14th, 2020
 */

interface MapResponse {

    /*
    * @property: Obj - an object of type Any that is passed to be completed after background threads.
     */
    fun onProcessComplete(Obj: Any)
}
