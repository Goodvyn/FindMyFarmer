package sheridan.capstone.findmyfarmer.Farmer.View

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import sheridan.capstone.findmyfarmer.Customer.Model.FollowingDialog
import sheridan.capstone.findmyfarmer.Customer.Model.SharedViewModel
import sheridan.capstone.findmyfarmer.Entities.Farm
import sheridan.capstone.findmyfarmer.Farmer.Controller.HubListToView
import sheridan.capstone.findmyfarmer.Farmer.Model.FarmDeleteConfirmDialog
import sheridan.capstone.findmyfarmer.Farmer.Model.GetFarmersFarms
import sheridan.capstone.findmyfarmer.R
import sheridan.capstone.findmyfarmer.SessionDataHandler.SessionData


class FarmerHub : Fragment(),HubListToView.OnItemClickListener {


    private lateinit var viewModel: SharedViewModel
    private lateinit var sessionData: SessionData
    private var HubList = ArrayList<Farm>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        sessionData = SessionData(activity)
        var farmer = sessionData.farmerData


        val view: View = inflater.inflate(R.layout.fragment_farmer_hub, container, false)
        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.pullToRefresh)
        val recycleView: RecyclerView = view.findViewById(R.id.Hub_Recycle_View)
        val farmadd = view.findViewById<ImageView>(R.id.AddFarmImage)
        val PageOverlay = view.findViewById<View>(R.id.overlay)
        val noContextText = view.findViewById<View>(R.id.NoContentText)
        val overlay = ArrayList<View>()
        overlay.add(PageOverlay)
        overlay.add(noContextText)

        overlay[0].visibility = View.VISIBLE
        overlay[1].visibility = View.INVISIBLE

        val adapter = HubListToView(requireActivity(), HubList, this)
        recycleView.adapter = adapter
        recycleView.layoutManager = LinearLayoutManager(context)
        recycleView.setHasFixedSize(true)

        var getFarmersFarms = activity?.let { GetFarmersFarms(it, swipeRefreshLayout, adapter,overlay) }

        if (getFarmersFarms != null && farmer != null) {
            getFarmersFarms.GetHubFarms(HubList, farmer.farmerID)
        }

        swipeRefreshLayout.setOnRefreshListener {
            if (getFarmersFarms != null && farmer != null) {
                overlay[0].visibility = View.VISIBLE
                getFarmersFarms.GetHubFarms(HubList, farmer.farmerID)
            }
        }

        farmadd.setOnClickListener{
            this.findNavController().navigate(R.id.action_nav_manage_hub_to_fragment_farm_add)
        }

        var touchHelper  = ItemTouchHelper(itemTouchHelper)
        touchHelper.attachToRecyclerView(recycleView)

        return view
    }

    private val itemTouchHelper = object :ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return false
        }
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            var deleteFarm: FarmDeleteConfirmDialog =
                FarmDeleteConfirmDialog()

            deleteFarm.FarmDelete(HubList.get(viewHolder.adapterPosition))

            val FragmentManager : FragmentManager? = activity?.supportFragmentManager
            if (FragmentManager != null) {
                deleteFarm.show(FragmentManager, "delete?")
            }
        }

        override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
            RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                .addSwipeLeftBackgroundColor(ContextCompat.getColor(requireContext(),R.color.RedApp))
                .addSwipeLeftActionIcon(R.drawable.ic_delete_50)
                .create()
                .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

    override fun onItemClick(position: Int) {
        viewModel.setFarmData(HubList[position])
        this.findNavController().navigate(R.id.action_nav_manage_hub_to_fragment_farm_manager)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        viewModel.getFarmData()
        viewModel.getFarmers_Followers()
    }
}

