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
    private int Quantity;
    private String Unit;

    //Empty constructor
    public FarmProduct(){ }

    //Main constructor
    public FarmProduct(int FarmProductID, int FarmID, int ProductID,int Quantity,String Unit){
        this.FarmProductID = FarmProductID;
        this.FarmID = FarmID;
        this.ProductID = ProductID;
        this.Quantity = Quantity;
        this.Unit = Unit;
    }

    //Setters
    public void setFarmerID(int farmerID) { FarmID = farmerID; }
    public void setProductID(int productID) { ProductID = productID; }
    public void setQuantity(int quantity) { Quantity = quantity; }
    public void setUnit(String unit) { Unit = unit; }

    //Getters
    public int getFarmerID() { return FarmID; }
    public int getFarmProductID() { return FarmProductID; }
    public int getProductID() { return ProductID; }
    public int getQuantity() { return Quantity; }
    public String getUnit() { return Unit; }
}


