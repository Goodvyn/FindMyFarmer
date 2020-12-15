package sheridan.capstone.findmyfarmer.Entities;

/**
 * @author Sohaib Hussain
 * Description: Entity Representing the Followers of the Farm, It stores information about
 *              Customer and the Farm that the Customer Followed
 * Date Modified: December 14th, 2020
 **/
public class Following {

    //Primary properties
    private int FollowingID;
    private int CustomerID;
    private int FarmID;

    //Empty constructor
    public Following(){}

    //Main constructor
    public Following(int FollowingID,int CustomerID,int FarmID){
        this.FarmID = FarmID;
        this.CustomerID = CustomerID;
        this.FollowingID = FollowingID;
    }

    //Getters
    public int getFarmID() {
        return FarmID;
    }
    public int getCustomerID() {
        return CustomerID;
    }
    public int getFollowingID() {
        return FollowingID;
    }

    //Setters
    public void setFarmID(int farmID) {
        FarmID = farmID;
    }
    public void setCustomerID(int customerID) {
        CustomerID = customerID;
    }
}
