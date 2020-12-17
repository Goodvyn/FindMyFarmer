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
    private String image;
    private String Status;

    //Main constructor
    public Product(int ProductID, String ProductName,String ProductCategory){
        this.ProductID = ProductID;
        this.ProductName = ProductName;
        this.ProductCategory = ProductCategory;
    }

    //getters
    public int getProductID() { return ProductID; }
    public String getProductCategory() { return ProductCategory; }
    public String getProductName() { return ProductName; }

    //Helper getters
    public String getImage() { return image; }
    public String getStatus(){return Status; }

    //setters
    public void setProductCategory(String productCategory) { ProductCategory = productCategory; }
    public void setProductName(String productName) { ProductName = productName; }

    //Helper setters
    public void setImage(String image) { this.image = image; }
    public void setStatus(String status) { Status = status; }
}
