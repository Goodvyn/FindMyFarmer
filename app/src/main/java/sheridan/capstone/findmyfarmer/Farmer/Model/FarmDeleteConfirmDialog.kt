package sheridan.capstone.findmyfarmer.Farmer.Model

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.navigation.fragment.findNavController
import sheridan.capstone.findmyfarmer.Entities.Farm
import sheridan.capstone.findmyfarmer.Farmer.View.FarmerHub
import sheridan.capstone.findmyfarmer.R
import sheridan.capstone.findmyfarmer.Users.CustomerActivity


/**
 * @author Sohaib Hussain
 * Description: Dialog fragment that handles a popup box which asks for confirmation from the
 *              user after the user has attempted to delete a farm before proceeding to either
 *              delete the farm or return without deleting.
 * Date Modified: December 14th, 2020
 **/
class FarmDeleteConfirmDialog(): AppCompatDialogFragment() {

    private lateinit var farm : Farm
    private lateinit var YesDelete: Button
    private lateinit var NoDelete : Button

    fun FarmDelete(farm_new: Farm) {
    	farm = farm_new
    }

    /*
        Initializes the dialog for show and sets the listeners for the Yes or No buttons
    */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater
        val view: View = inflater.inflate(R.layout.farm_delete_dialog, null)

        YesDelete = view.findViewById(R.id.YesDelete)
        NoDelete = view.findViewById(R.id.NoDelete)

        YesDelete.setOnClickListener {
            var farmDBHandler = FarmDBHandler(requireActivity(), null)
            farmDBHandler.deletefarm(farm)

            this.findNavController().navigate(R.id.action_nav_manage_hub_self)
            dismiss()
        }

        NoDelete.setOnClickListener {

            this.findNavController().navigate(R.id.action_nav_manage_hub_self)
            dismiss()
        }

        builder.setView(view)
        return builder.create()

    }
}
