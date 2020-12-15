package sheridan.capstone.findmyfarmer.Database;

/**
 * @author Sohaib Hussain
 * Description: Interface for handling response on Coroutines
 * Date Modified: December 14th, 2020
 **/
public interface AsyncResponse {
    //Responds when there is a call back from the API Request
    void processFinish(String response);
}
