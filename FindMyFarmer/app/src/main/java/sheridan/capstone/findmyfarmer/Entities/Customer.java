package sheridan.capstone.findmyfarmer.Entities;
/**
 * @author Sohaib Hussain
 * Description: Entity Representing the Customer of the application. This class holds information
 *              personal information about the customer as well as if the customer is a farmer or
 *              not
 * Date Modified: December 14th, 2020
 **/
public class Customer {

    private int CustomerID;
    private String CustomerName;
    private String CustomerEmail;
    private String CustomerPassword;
    private boolean IsFarmer;

    //Empty constructor
    public Customer() { }

    //Basic constructor
    public Customer(int CustomerID,String CustomerName,String CustomerEmail, String CustomerPassword,Boolean IsFarmer){
        this.CustomerID = CustomerID;
        this.CustomerName = CustomerName;
        this.CustomerEmail = CustomerEmail;
        this.CustomerPassword = CustomerPassword;
        this.IsFarmer = IsFarmer;
    }

    //Setters
    public void setCustomerID(int customerID) { CustomerID = customerID; }
    public void setCustomerEmail(String customerEmail) {
        CustomerEmail = customerEmail;
    }
    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }
    public void setCustomerPassword(String customerPassword) { CustomerPassword = customerPassword; }
    public void setIsFarmer(Boolean IsFarmer){this.IsFarmer = IsFarmer;}

    //Getters
    public int getCustomerID() {
        return CustomerID;
    }
    public String getCustomerEmail() {
        return CustomerEmail;
    }
    public String getCustomerName() {
        return CustomerName;
    }
    public String getCustomerPassword() {
        return CustomerPassword;
    }
    public boolean getIsFarmer() { return IsFarmer; }
}
