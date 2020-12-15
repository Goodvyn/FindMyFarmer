package sheridan.capstone.findmyfarmer.Entities;
/**
 * @author Sohaib Hussain
 * Description: Entity Representing the Product, This is a product object the
 *              farmer picks from for listing FarmProducts in his Farm.
 * Date Modified: December 14th, 2020
 **/
public class Product {

    //primary properties
    private int ProductID;
    private String ProductName;
    private String ProductCategory;

    //Helper properties
    private int Quantity;
    private String image;
    private String Unit;

    //Main constructor
    public Product(int ProductID, String ProductName,String ProductCategory,int Quantity){
        this.ProductID = ProductID;
        this.ProductName = ProductName;
        this.ProductCategory = ProductCategory;
        this.Quantity = Quantity;
    }

    //getters
    public int getProductID() { return ProductID; }
    public String getProductCategory() { return ProductCategory; }
    public String getProductName() { return ProductName; }

    //Helper getters
    public int getQuantity() { return Quantity; }
    public String getImage() { return image; }
    public String getUnit() { return Unit; }

    //setters
    public void setProductCategory(String productCategory) { ProductCategory = productCategory; }
    public void setProductName(String productName) { ProductName = productName; }

    //Helper setters
    public void setQuantity(int quantity) { Quantity = quantity; }
    public void setImage(String image) { this.image = image; }
    public void setUnit(String unit) { Unit = unit; }
}
