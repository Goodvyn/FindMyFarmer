package sheridan.capstone.findmyfarmer.Customer.Model

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.storage.StorageReference
import sheridan.capstone.findmyfarmer.Customer.View.Maps
import sheridan.capstone.findmyfarmer.Entities.Farm
import sheridan.capstone.findmyfarmer.ImageHandler.DirectoryName
import sheridan.capstone.findmyfarmer.ImageHandler.FirebaseImagehandler
import sheridan.capstone.findmyfarmer.ImageHandler.StorageResponse
import sheridan.capstone.findmyfarmer.R
import sheridan.capstone.findmyfarmer.SessionDataHandler.SessionData
import sheridan.capstone.findmyfarmer.Users.PhotoPicker
import java.io.InputStream
import java.util.*

/**
 * @author Sohaib Hussain
 * Description: Image dialog fragment that display 3 items camera, Farm gallery and phone gallery.
 *              The customer can add an image from phone gallery or camera and once they add an
 *              image, the image is saved in the farm gallery, where the user can pick their primary
 *              image.
 * Date Modified: December 14th, 2020
 **/
class ImageDialog(directoryName: DirectoryName): AppCompatDialogFragment(){

    private var bitmap: Bitmap? = null
    private var directoryName = directoryName
    private lateinit var viewModel: SharedViewModel
    private lateinit var sessionData: SessionData
    private lateinit var FIH2 : FirebaseImagehandler

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.image_dialog,null)
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        if(directoryName == DirectoryName.Farm){
            var farm = viewModel.getFarmData().value
            if (farm != null) {
                FIH2 = FirebaseImagehandler(directoryName,farm.farmID,context)
            }
            else{
                FIH2 = FirebaseImagehandler(directoryName,0,context)
            }
        }
        else{
            sessionData = SessionData(activity)
            var customer  = sessionData.customerData
            if(customer != null){
                FIH2 = FirebaseImagehandler(directoryName,customer.customerID,context)
            }
            else{
                FIH2 = FirebaseImagehandler(directoryName,0,context)
            }
        }

        val cameraButton = view.findViewById<Button>(R.id.Camera)
        val galleryButton = view.findViewById<Button>(R.id.gallery)
        val FarmgalleryButton = view.findViewById<Button>(R.id.FarmGallery)


        val startForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val intent = result.data
                    var stream: InputStream? = null
                    try {
                        // recyle unused bitmaps
                        bitmap?.recycle()
                        if (intent != null) {
                            stream = intent.data?.let { context?.contentResolver?.openInputStream(it) }
                        }
                        bitmap = BitmapFactory.decodeStream(stream);
                        FIH2.UploadImageToFirebase(bitmap,object :StorageResponse{
                            override fun processFinish(response: MutableList<StorageReference>?, bitmap: Optional<Bitmap>?, Url: Optional<String>?) {
                                viewModel.setFirebaseImageHandler(FIH2);
                                dismiss()
                              findNavController().navigate(R.id.action_fragment_farm_manager_to_fragment_photo_picker2)
                            }
                            override fun OnErrorListener(error: String?) { System.out.println(error) }
                        })

                    } catch (ex: Exception) {
                        System.out.println(ex)
                    } finally {
                        try {
                            stream?.close()
                        } catch (ex2: Exception) {
                            System.out.println(ex2)
                        }
                    }
                }
            }

        val startCameraForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val intent = result.data
                    var stream: InputStream? = null
                    try {
                        // recyle unused bitmaps
                        bitmap?.recycle()
                        if (intent != null) {
                            bitmap = intent.extras?.get("data") as Bitmap
                            FIH2.UploadImageToFirebase(bitmap,object :StorageResponse{
                                override fun processFinish(response: MutableList<StorageReference>?,bitmap: Optional<Bitmap>?, Url: Optional<String>?) {
                                    viewModel.setFirebaseImageHandler(FIH2)
                                    dismiss()
                                  findNavController().navigate(R.id.action_fragment_farm_manager_to_fragment_photo_picker2)
                                }
                                override fun OnErrorListener(error: String?) { System.out.println(error) }
                            })
                        }
                    } catch (ex: Exception) {
                        System.out.println(ex)
                    } finally {
                        try {
                            stream?.close()
                        } catch (ex2: Exception) {
                            System.out.println(ex2)
                        }
                    }
                }
            }

        cameraButton.setOnClickListener {
            if (context?.let { it1 -> ContextCompat.checkSelfPermission(it1, Manifest.permission.CAMERA) } == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startCameraForResult.launch(intent)
            } else {
                val requestPermissionLauncher =
                    registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                        if (isGranted) {
                            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            startCameraForResult.launch(intent)
                        } else {
                            Toast.makeText(
                                context,
                                "Camera Permission is needed to use this functionality",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }

        galleryButton.setOnClickListener {
            var intt = Intent()
            intt.setType("image/*")
            intt.setAction(Intent.ACTION_GET_CONTENT)
            intt.addCategory(Intent.CATEGORY_OPENABLE)
            startForResult.launch(intt)
        }

        FarmgalleryButton.setOnClickListener {
            viewModel.setFirebaseImageHandler(FIH2)
            dismiss()
            this.findNavController().navigate(R.id.action_fragment_farm_manager_to_fragment_photo_picker2)
        }

        builder.setView(view)

        return builder.create()
    }
}
