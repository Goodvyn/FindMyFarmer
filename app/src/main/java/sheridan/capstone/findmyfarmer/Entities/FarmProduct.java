package sheridan.capstone.findmyfarmer.Entities;

/**
 * @author Sohaib Hussain
 * Description: Entity Representing the Farm product. This Object handles the individual product
 *              in the farm, and information regarding the product like quantiy and unit of the
 *              quantity
 * Date Modified: December 14th, 2020
 **/
public class FarmProduct {
    private int FarmProductID;
    private int FarmID;
    private int ProductID;
    private String Status;

    //Empty constructor
    public FarmProduct(){ }

    //Main constructor
    public FarmProduct(int FarmProductID, int FarmID, int ProductID,String Status){
        this.FarmProductID = FarmProductID;
        this.FarmID = FarmID;
        this.ProductID = ProductID;
        this.Status = Status;
    }

    //Setters
    public void setFarmerID(int farmerID) { FarmID = farmerID; }
    public void setProductID(int productID) { ProductID = productID; }
    public void setStatus(String status) { Status = status; }

    //Getters
    public int getFarmerID() { return FarmID; }
    public int getFarmProductID() { return FarmProductID; }
    public int getProductID() { return ProductID; }
    public String getStatus() { return Status; }
}


