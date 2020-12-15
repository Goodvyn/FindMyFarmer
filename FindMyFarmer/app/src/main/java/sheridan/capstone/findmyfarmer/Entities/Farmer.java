package sheridan.capstone.findmyfarmer.Entities;

/**
 * @author Sohaib Hussain
 * Description: Entity Representing the Farmer. This entity makes a connection between a customer
 *              and a farmer.
 * Date Modified: December 14th, 2020
 **/
public class Farmer{
    private int FarmerID;
    private int CustomerID;

    //Empty constructor
    public Farmer(){ }

    //Main constructor
    public Farmer(int FarmerID,int CustomerID){
        this.FarmerID = FarmerID;
        this.CustomerID = CustomerID;
    }

    //Getters
    public int getFarmerID() { return FarmerID; }
    public int getCustomerID() { return CustomerID; }
}
