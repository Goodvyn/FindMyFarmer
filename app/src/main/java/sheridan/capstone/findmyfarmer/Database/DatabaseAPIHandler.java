package sheridan.capstone.findmyfarmer.Database;

import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import sheridan.capstone.findmyfarmer.Entities.Customer;
import sheridan.capstone.findmyfarmer.Entities.Farm;
import sheridan.capstone.findmyfarmer.Entities.FarmProduct;
import sheridan.capstone.findmyfarmer.Entities.Farmer;
import sheridan.capstone.findmyfarmer.Entities.Following;
import sheridan.capstone.findmyfarmer.Entities.Product;
import sheridan.capstone.findmyfarmer.Entities.Rating;

/**
 * @author Sohaib Hussain
 * Description: Database API handler handles the custom database api calls for
 *              executing basic crud function on the database using springboot.
 *              This Class handles all CRUD calls within one function
 * Date Modified: December 14th, 2020
 **/
public class DatabaseAPIHandler extends AsyncTask<Object,Object,String> {

    //Link to API
    private static final String API_BASE_URL = "http://farmerapi.us-east-2.elasticbeanstalk.com";
    private Context context;
    //Coroutine response handler, returns when a call has been completed or timed out
    public AsyncResponse del;

    public DatabaseAPIHandler(Context context, AsyncResponse asyncResponse)
    {
        this.context = context;
        del = asyncResponse;
    }

    //Adding Single instance of Object
    //Converts Customer to a JSONObject
    private JSONObject AddCustomer(Customer customer){
        Map<String, String> params = new HashMap();

        params.put("CustomerName", customer.getCustomerName());
        params.put("CustomerEmail",customer.getCustomerEmail());
        params.put("CustomerPassword", customer.getCustomerPassword());
        params.put("IsFarmer",String.valueOf(customer.getIsFarmer()));

        return new JSONObject(params);
    }
    //Converts Farmer to a JSONObject
    private JSONObject AddFarmer(Farmer farmer){
        Map<String, String> params = new HashMap();

        params.put("CustomerID", String.valueOf(farmer.getCustomerID()));

        return new JSONObject(params);
    }
    //Converts Product to a JSONObject
    private JSONObject AddProduct(Product product){
        Map<String, String> params = new HashMap();

        params.put("ProductName", product.getProductName());
        params.put("ProductCategory", product.getProductCategory());

        return new JSONObject(params);
    }
    //Converts Farm Product to a JSONObject
    private JSONObject AddFarmProduct(FarmProduct farmProduct){
        Map<String, String> params = new HashMap();

        params.put("FarmID", String.valueOf(farmProduct.getFarmerID()));
        params.put("ProductID", String.valueOf(farmProduct.getProductID()));
        params.put("Quantity",String.valueOf(farmProduct.getQuantity()));
        params.put("Unit",String.valueOf(farmProduct.getUnit()));

        return new JSONObject(params);
    }
    //Converts Farm to a JSONObject
    private JSONObject AddFarm(Farm farm){
        Map<String, String> params = new HashMap();

        params.put("Business_Name",farm.getBusinessName());
        params.put("Business_Description", farm.getBusinessDescription());
        params.put("Business_Rating",String.valueOf(farm.getBusinessRating()));
        params.put("City",farm.getCity());
        params.put("Street",farm.getStreet());
        params.put("Country",farm.getCountry());
        params.put("PostalCode",farm.getPostalCode());
        params.put("Province",farm.getProvince());
        params.put("Unit",String.valueOf(farm.getUnit()));
        params.put("FarmerID",String.valueOf(farm.getFarmerID()));

        return new JSONObject(params);
    }
    //Converts Rating to a JSONObject
    private JSONObject AddRating(Rating rating){
        Map<String, String> params = new HashMap();

        params.put("FarmID",String.valueOf(rating.getFarmID()));
        params.put("CustomerID", String.valueOf(rating.getCustomerID()));
        params.put("Rating",String.valueOf(rating.getRating()));
        params.put("Feedback",rating.getFeedback());
        return new JSONObject(params);
    }
    //Converts Follow to a JSONObject
    private JSONObject AddFollow(Following following){
        Map<String, String> params = new HashMap();

        params.put("CustomerID",String.valueOf(following.getCustomerID()));
        params.put("FarmID", String.valueOf(following.getFarmID()));

        return new JSONObject(params);
    }

    //Adding List of Objects all at once
    //Converts Customers to JSONArray
    private JSONArray AddCustomers(List<Customer> customers) {
        JSONArray jsonArray = new JSONArray();
        try {
            for (Customer customer : customers) {
                JSONObject params = new JSONObject();

                params.put("CustomerName", customer.getCustomerName());
                params.put("CustomerEmail", customer.getCustomerEmail());
                params.put("CustomerPassword", customer.getCustomerPassword());
                params.put("IsFarmer",customer.getIsFarmer());

                jsonArray.put(params);
            }
            return jsonArray;
        }catch (Exception ex){
            System.out.println(ex);
            return null;
        }

    }
    //Converts Farmers to JSONArray
    private JSONArray AddFarmers(List<Farmer> farmers) {
        JSONArray jsonArray = new JSONArray();
        try {
            for (Farmer farmer : farmers) {
                JSONObject params = new JSONObject();

                params.put("CustomerID", String.valueOf(farmer.getCustomerID()));

                jsonArray.put(params);
            }
            return jsonArray;
        }catch (Exception ex){
            System.out.println(ex);
            return null;
        }

    }
    //Converts Products to JSONArray
    private JSONArray AddProducts(List<Product> products) {
        JSONArray jsonArray = new JSONArray();
        try {
            for (Product product : products) {
                JSONObject params = new JSONObject();

                params.put("ProductName", product.getProductName());
                params.put("ProductCategory", product.getProductCategory());

                jsonArray.put(params);
            }
            return jsonArray;
        }catch (Exception ex){
            System.out.println(ex);
            return null;
        }

    }
    //Converts FarmProducts to JSONArray
    private JSONArray AddFarmProducts(List<FarmProduct> farmProducts){
        JSONArray jsonArray = new JSONArray();
        try {
            for (FarmProduct farmProduct : farmProducts) {
                JSONObject params = new JSONObject();

                params.put("FarmID", farmProduct.getFarmerID());
                params.put("ProductID", farmProduct.getProductID());
                params.put("Quantity",String.valueOf(farmProduct.getQuantity()));
                params.put("Unit",String.valueOf(farmProduct.getUnit()));

                jsonArray.put(params);
            }
            return jsonArray;
        }catch (Exception ex){
            System.out.println(ex);
            return null;
        }

    }
    //Converts Farms to JSONArray
    private JSONArray AddFarms(List<Farm> farms){
        JSONArray jsonArray = new JSONArray();
        try {
            for (Farm farm : farms) {
                JSONObject params = new JSONObject();

                params.put("Business_Name",farm.getBusinessName());
                params.put("Business_Description", farm.getBusinessDescription());
                params.put("Business_Rating",String.valueOf(farm.getBusinessRating()));
                params.put("City",farm.getCity());
                params.put("Street",farm.getStreet());
                params.put("Country",farm.getCountry());
                params.put("PostalCode",farm.getPostalCode());
                params.put("Province",farm.getProvince());
                params.put("Unit",String.valueOf(farm.getUnit()));
                params.put("FarmerID",String.valueOf(farm.getFarmerID()));

                jsonArray.put(params);
            }
            return jsonArray;
        }catch (Exception ex){
            System.out.println(ex);
            return null;
        }
    }
    //Converts Ratings to JSONArray
    private JSONArray AddRatings(List<Rating> ratings){
        JSONArray jsonArray = new JSONArray();
        try {
            for (Rating rating : ratings) {
                JSONObject params = new JSONObject();

                params.put("FarmID",String.valueOf(rating.getFarmID()));
                params.put("CustomerID", String.valueOf(rating.getCustomerID()));
                params.put("Rating",String.valueOf(rating.getRating()));
                params.put("Feedback",rating.getFeedback());

                jsonArray.put(params);
            }
            return jsonArray;
        }catch (Exception ex){
            System.out.println(ex);
            return null;
        }
    }
    //Converts Follows to JSONArray
    private JSONArray AddFollows(List<Following> followings){
        JSONArray jsonArray = new JSONArray();
        try {
            for (Following following : followings) {
                JSONObject params = new JSONObject();

                params.put("CustomerID",String.valueOf(following.getCustomerID()));
                params.put("FarmID", String.valueOf(following.getFarmID()));

                jsonArray.put(params);
            }
            return jsonArray;
        }catch (Exception ex){
            System.out.println(ex);
            return null;
        }
    }


    //Updating Objects
    //Converts Customer to a JSONObject
    private JSONObject UpdateCustomer(Customer customer){
        Map<String, String> params = new HashMap();

        params.put("CustomerID",String.valueOf(customer.getCustomerID()));
        params.put("CustomerName", customer.getCustomerName());
        params.put("CustomerEmail",customer.getCustomerEmail());
        params.put("CustomerPassword", customer.getCustomerPassword());
        params.put("IsFarmer",String.valueOf(customer.getIsFarmer()));

        return new JSONObject(params);
    }
    //Converts Farm to a JSONObject
    private JSONObject UpdateFarm(Farm farm){
        Map<String, String> params = new HashMap();

        params.put("FarmID",String.valueOf(farm.getFarmID()));
        params.put("Business_Name",farm.getBusinessName());
        params.put("Business_Description", farm.getBusinessDescription());
        params.put("Business_Rating",String.valueOf(farm.getBusinessRating()));
        params.put("City",farm.getCity());
        params.put("Street",farm.getStreet());
        params.put("Country",farm.getCountry());
        params.put("PostalCode",farm.getPostalCode());
        params.put("Province",farm.getProvince());
        params.put("Unit",String.valueOf(farm.getUnit()));
        params.put("FarmerID",String.valueOf(farm.getFarmerID()));

        return new JSONObject(params);
    }
    //Converts Product to a JSONObject
    private JSONObject UpdateProduct(Product product){
        Map<String, String> params = new HashMap();

        params.put("ProductID",String.valueOf(product.getProductID()));
        params.put("ProductName", product.getProductName());
        params.put("ProductCategory", product.getProductCategory());

        return new JSONObject(params);
    }
    //Converts FarmProduct to a JSONObject
    private JSONObject UpdateFarmProduct(FarmProduct farmProduct){
        Map<String, String> params = new HashMap();

        params.put("FarmProductID", String.valueOf(farmProduct.getFarmProductID()));
        params.put("FarmID", String.valueOf(farmProduct.getFarmerID()));
        params.put("ProductID", String.valueOf(farmProduct.getProductID()));
        params.put("Quantity",String.valueOf(farmProduct.getQuantity()));
        params.put("Unit",String.valueOf(farmProduct.getUnit()));

        return new JSONObject(params);
    }



    //API calls handled for a list of objects
    private String SendListRequest(String url, List<?> objects){
        JSONArray listArray = new JSONArray();
        if(!(objects.isEmpty())){
            if(objects.get(0).getClass() == Customer.class){
                listArray = AddCustomers((List<Customer>) objects);
            }
            else if(objects.get(0).getClass() ==  Farmer.class){
                listArray = AddFarmers((List<Farmer>) objects);
            }
            else if(objects.get(0).getClass() == Product.class){
                listArray = AddProducts((List<Product>) objects);
            }
            else if(objects.get(0).getClass() == FarmProduct.class){
                listArray = AddFarmProducts((List<FarmProduct>) objects);
            }
            else if(objects.get(0).getClass() == Farm.class){
                listArray = AddFarms((List<Farm>)objects);
            }
            else if(objects.get(0).getClass() == Rating.class){
                listArray = AddRatings((List<Rating>)objects);
            }
            else if(objects.get(0).getClass() == Following.class){
                listArray = AddFollows((List<Following>)objects);
            }
        }
        try {
            RequestQueue rq = Volley.newRequestQueue(context);
            RequestFuture<JSONArray> requestFuture = RequestFuture.newFuture();
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, url, listArray, requestFuture, requestFuture);
            rq.add(request);

            String response = requestFuture.get().toString();
            Thread.sleep(1000);
            return response;
        }catch (Exception ex){
            System.out.println(ex);
            return null;
        }

    }
    //API calls handled for Single instance of object
    private String SendSingleObjectRequest(int method,String url,Object object){
        JSONObject obj = new JSONObject();
        //updating/deleting objects from database
        if(method == Request.Method.PUT){
            if(object.getClass() == Customer.class){
                obj = UpdateCustomer((Customer) object);
            }
            else if(object.getClass() == Farmer.class){
                obj = UpdateFarm((Farm) object);
            }
            else if(object.getClass() == Product.class){
                obj = UpdateProduct((Product) object);
            }
            else if(object.getClass() == Farm.class){
                obj = UpdateFarm((Farm) object);
            }
            else if(object.getClass() == FarmProduct.class){
                obj = UpdateFarmProduct((FarmProduct) object);
            }
            else{
                return null;
            }
        }
        else{
            if(object.getClass() == Customer.class){
                obj = AddCustomer((Customer) object);
            }
            else if(object.getClass() == Farmer.class){
                obj = AddFarmer((Farmer) object);
            }
            else if(object.getClass() == Product.class){
                obj = AddProduct((Product) object);
            }
            else if(object.getClass() == FarmProduct.class){
                obj = AddFarmProduct((FarmProduct) object);
            }
            else if(object.getClass() == Farm.class){
                obj = AddFarm((Farm) object);
            }
            else if(object.getClass() == Rating.class){
                obj = AddRating((Rating) object);
            }
            else if(object.getClass() == Following.class){
                obj = AddFollow((Following) object);
            }
            else{
                return null;
            }
        }

        try {
            RequestQueue rq = Volley.newRequestQueue(context);
            RequestFuture<JSONObject> requestFuture = RequestFuture.newFuture();
            JsonObjectRequest request = new JsonObjectRequest(method, url, obj, requestFuture, requestFuture);
            rq.add(request);

            String response = requestFuture.get().toString();
            return response;
        }catch (Exception ex){
            System.out.println(ex);
            return null;
        }
    }
    //API calls for a simple get request
    private String SendPlainRequest(int method, String url){
        try {
            RequestQueue rq = Volley.newRequestQueue(context);
            RequestFuture<String> requestFuture = RequestFuture.newFuture();
            StringRequest request = new StringRequest(method, url, requestFuture, requestFuture);
            rq.add(request);

            String response = requestFuture.get(5,TimeUnit.SECONDS);
            return response;
        }catch (Exception ex){
            System.out.println(ex);
            return null;
        }
    }

    //Api Call function
    //Params[0] = URL , Params[1] = requirement of the URL or null otherwise
    @Override
    protected String doInBackground(Object... objects) {
        String paramtype = "";
        List<?> list = null;
        String response = "";
        try{
            String url = API_BASE_URL + objects[0].toString();

            //Checks if the objects passed are null or not
            if(objects.length > 1){
                //checks if the passed object is a list or single object
                try{
                    list = (List<?>) objects[1];
                    paramtype = "list".toLowerCase();
                }catch (Exception ex){ }

                //if it is a list
                if(paramtype.compareToIgnoreCase("list")==0){
                   response = SendListRequest(url,list);
                }
                else{
                    if(objects[0].toString().toLowerCase().contains("update")){
                        response = SendSingleObjectRequest(Request.Method.PUT,url,objects[1]);
                    }
                    else if(objects[0].toString().toLowerCase().contains("add")){
                        response = SendSingleObjectRequest(Request.Method.POST,url,objects[1]);
                    }
                }
            }
            else{
                if(!(objects[0].toString().toLowerCase().contains("delete"))){
                    response = SendPlainRequest(Request.Method.GET,url);
                }
                else{
                    //PUT request for deleting
                    response = SendPlainRequest(Request.Method.PUT,url);
                }
            }
            return response;
        }catch (Exception ex){
            System.out.println(ex);
            return null;
        }
    }

    //Executed when doInBackground is finished running its tasks.
    @Override
    protected void onPostExecute(String s) {
        del.processFinish(s);
    }
}

