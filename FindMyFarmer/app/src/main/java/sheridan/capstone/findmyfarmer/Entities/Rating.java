package sheridan.capstone.findmyfarmer.Entities;
/**
 * @author Sohaib Hussain
 * Description: Entity Representing the Rating. This Object holds information about the rating
 *              given to a farm.
 * Date Modified: December 14th, 2020
 **/
public class Rating {

    //Primary properties
    private int RatingID;
    private int FarmID;
    private int CustomerID;
    private float Rating;
    private String Feedback;

    //Empty constructor
    public Rating(){ }

    //Main constructor
    public Rating(int RatingID, int FarmID,int CustomerID,float Rating,String Feedback){
        this.RatingID = RatingID;
        this.FarmID = FarmID;
        this.CustomerID = CustomerID;
        this.Rating = Rating;
        this.Feedback = Feedback;
    }

    //Getters
    public int getCustomerID() {
        return CustomerID;
    }
    public int getFarmID() {
        return FarmID;
    }
    public float getRating() {
        return Rating;
    }
    public int getRatingID() {
        return RatingID;
    }
    public String getFeedback() {
        return Feedback;
    }

    //Setters
    public void setCustomerID(int customerID) {
        CustomerID = customerID;
    }
    public void setFarmID(int farmID) {
        FarmID = farmID;
    }
    public void setFeedback(String feedback) {
        Feedback = feedback;
    }
    public void setRating(float rating) {
        Rating = rating;
    }
    public void setRatingID(int ratingID) {
        RatingID = ratingID;
    }

}
