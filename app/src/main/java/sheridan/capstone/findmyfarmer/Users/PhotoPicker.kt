package sheridan.capstone.findmyfarmer.Users

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.StorageReference
import sheridan.capstone.findmyfarmer.Customer.Controller.ImageListToView
import sheridan.capstone.findmyfarmer.Customer.Model.SharedViewModel
import sheridan.capstone.findmyfarmer.ImageHandler.FirebaseImagehandler
import sheridan.capstone.findmyfarmer.ImageHandler.StorageResponse
import sheridan.capstone.findmyfarmer.R
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * @author Sohaib Hussain
 * Description: Handles listing of the farm gallery, that holds all the images that belong to the
 *              user. If an image is picked , the image picked becomes the primary image
 * Date Modified: December 14th, 2020
 **/
class PhotoPicker() : Fragment(), ImageListToView.OnItemClickListener  {
    private lateinit var viewModel: SharedViewModel
    private var imagelist = ArrayList<Bitmap>()
    private var references = HashMap<Bitmap,String>()
    private lateinit var FIH2 : FirebaseImagehandler
    private lateinit var progressbar: ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_photo_picker,container,false)

        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        FIH2 = viewModel.getImageHandler().value!!
        progressbar = view.findViewById(R.id.progbar)
        val recyclerView = view.findViewById<RecyclerView>(R.id.imageList)

        val adapter = ImageListToView(imagelist,this)
        recyclerView.layoutManager = GridLayoutManager(context,4)
        recyclerView.adapter = adapter

        refreshList(adapter)

        return view
    }

    //On image click, the image becomes a primary image
    override fun onItemClick(position: Int) {
        progressbar.visibility = View.VISIBLE
        FIH2.MakeImagePrimary(references[imagelist[position]],object : StorageResponse{
            override fun processFinish(response: MutableList<StorageReference>?, bitmap: Optional<Bitmap>?, Url: Optional<String>?) {
                    FIH2.GetPrimaryImageFromFirebaseURL(object :StorageResponse{
                        @RequiresApi(Build.VERSION_CODES.N)
                        override fun processFinish(response: MutableList<StorageReference>?, bitmap: Optional<Bitmap>?, Url: Optional<String>?) {
                            var f = viewModel.getFarmData().value
                            if (f != null) {
                                if (Url != null) {
                                    f.primaryImage=Url.get()
                                    viewModel.setFarmData(f)
                                }

                                progressbar.visibility = View.VISIBLE

                                findNavController().navigate(R.id.action_fragment_photo_picker_to_fragment_farm_manager2)
                            }
                        }
                        override fun OnErrorListener(error: String?) {
                            progressbar.visibility = View.INVISIBLE
                            findNavController().navigate(R.id.action_fragment_photo_picker_to_fragment_farm_manager2)
                        }
                    })
            }
            override fun OnErrorListener(error: String?) {
                progressbar.visibility = View.INVISIBLE
                findNavController().navigate(R.id.action_fragment_photo_picker_to_fragment_farm_manager2)
            }
        })
    }

    //downloads images to the local storage for easy and quick access
    fun refreshList(imageListToView: ImageListToView){
        FIH2.RefreshLocalStorage(object : StorageResponse {
            override fun processFinish(response: MutableList<StorageReference>?,bitmap: Optional<Bitmap>?, Url: Optional<String>?) {
                var imagelistnames = ArrayList<String>()
                var imagelistBitmap = ArrayList<Bitmap>()
                imagelistnames.addAll(FIH2.GetNamesOfImagesInLocalStorage())
                for(imagename: String in imagelistnames){
                    var bm = FIH2.loadImageFromStorage(imagename)
                    imagelistBitmap.add(bm)
                    references.put(bm,imagename);
                }
                imagelist.clear()
                imagelist.addAll(imagelistBitmap)
                imageListToView.notifyDataSetChanged()
            }
            override fun OnErrorListener(error: String?) { System.out.println(error) }
        })
    }
}
