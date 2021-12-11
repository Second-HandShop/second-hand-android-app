package com.android.secondhand.useritems

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.textclassifier.SelectionEvent.ACTION_SHARE
import android.widget.Toast
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.secondhand.homepage.ItemsRecyclerViewAdapter
import com.android.secondhand.R
import com.android.secondhand.apis.Constant
import com.android.secondhand.apis.GsonRequest
import com.android.secondhand.editPage.ItemEditPageActivity
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import io.swagger.server.models.GetItemsByUserIdsResponse
import com.android.secondhand.models.Item
import com.android.secondhand.showPage.ItemShowPageActivity
import java.lang.reflect.Method

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ITEM_TYPE = "ITEM_TYPE"

/**
 * A simple [Fragment] subclass.
 * Use the [UserItemFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserItemFragment : Fragment(), UserItemsRecyclerViewAdapter.ShowUserItemClickListener {
    //"IN_THE_MARKET","BOUGHT","SOLD"
    private var itemType: String? = null
    private lateinit var rootView: View
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerViewAdapter: UserItemsRecyclerViewAdapter
    var items = ArrayList<Item>()
    lateinit var auth: FirebaseAuth
    lateinit var currentUserName: String
    lateinit var pullToRefresh: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            itemType = it.getString(ITEM_TYPE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_user_item, container, false)

        auth = FirebaseAuth.getInstance()
        //:TODO uncomment the code on the next line fetch from firebase
        currentUserName = auth.currentUser?.email.toString()//Constant.LOGGED_IN_USER_ID

        pullToRefresh = rootView.findViewById(R.id.pullToRefresh)
        when (itemType) {
            "IN_THE_MARKET" -> {
                val filter: (Item) -> Boolean = { item -> item.soldInfo == null}
                fetchItemsForUserFilteredWith(filter)
                pullToRefresh.setOnRefreshListener {
                    fetchItemsForUserFilteredWith(filter) // your code
                    pullToRefresh.isRefreshing = true
                }
            }
            "BOUGHT" -> {
                fetchBoughtItemsForUser()
                pullToRefresh.setOnRefreshListener {
                    fetchBoughtItemsForUser() // your code
                    pullToRefresh.isRefreshing = true
                }
            }
            "SOLD" -> {
                val filter: (Item) -> Boolean = { item -> item.soldInfo != null}
                fetchItemsForUserFilteredWith(filter)
                pullToRefresh.setOnRefreshListener {
                    fetchItemsForUserFilteredWith(filter) // your code
                    pullToRefresh.isRefreshing = true
                }
            }
        }

        return rootView
    }

    private fun fetchItemsForUserFilteredWith(filter: (Item) -> Boolean) {
        val requestQueue: RequestQueue = Volley.newRequestQueue(activity)

        var url = Constant.API_BASE_ADDRESS
        url = "$url?userIds=$currentUserName"

        val gsonRequest = GsonRequest(
            url,
            GetItemsByUserIdsResponse::class.java,
            null,
            { response ->
                System.out.println("Got the response\n")
                val itemsForUserMap = response.userIdsToIdsMap
                if (itemsForUserMap != null) {
                    items = itemsForUserMap[currentUserName]?.toCollection(ArrayList()) ?: arrayListOf()
                }
                items = items.filter(filter) as ArrayList<Item>
                updateRecylerView()
                pullToRefresh.isRefreshing = false
            },
            { error ->
                System.out.println("That did not work : \n" + error)
                items = arrayListOf()
                Toast.makeText(activity, "There was a problem retrieving data please retry after sometime",
                    Toast.LENGTH_LONG).show()
                pullToRefresh.isRefreshing = false
            }
        )
        requestQueue.add(gsonRequest)
    }

    private fun fetchBoughtItemsForUser() {
        items.clear()
        val requestQueue: RequestQueue = Volley.newRequestQueue(activity)

        var url = Constant.API_BASE_ADDRESS
        url = "$url/bought?userIds=$currentUserName"

        val gsonRequest = GsonRequest(
            url,
            GetItemsByUserIdsResponse::class.java,
            null,
            { response ->
                System.out.println("Got the response\n")
                val itemsForUserMap = response.userIdsToIdsMap
                itemsForUserMap?.values?.map { userItems ->
                    items.addAll(userItems.toCollection(ArrayList()))
                }
                updateRecylerView()
                pullToRefresh.isRefreshing = false
            },
            { error ->
                System.out.println("That did not work : \n" + error)
                items = arrayListOf()
                Toast.makeText(activity, "There was a problem retrieving data please retry after sometime",
                    Toast.LENGTH_LONG).show()
                pullToRefresh.isRefreshing = false
            }
        )
        requestQueue.add(gsonRequest)
    }

    private fun updateRecylerView() {
        recyclerViewAdapter = UserItemsRecyclerViewAdapter(items,itemType!!)
        recyclerViewAdapter.setListener(this)
        recyclerView = rootView.findViewById(R.id.items_container)
        recyclerView.layoutManager = GridLayoutManager(this.context, 2)
        recyclerView.adapter = recyclerViewAdapter
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of fragment ItemFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(type: String) =
            UserItemFragment().apply {
                arguments = Bundle().apply {
                    putString(ITEM_TYPE, type)
                }
            }
    }

    // from UserItemsRecyclerViewAdapter
    override fun onItemClick(item: Item) {
        when(itemType){
            "IN_THE_MARKET" -> {
                val intent = Intent(this.activity, ItemEditPageActivity::class.java)
                intent.putExtra("ITEM", item)
                startActivity(intent)
            }
            "BOUGHT", "SOLD" -> {
                val intent = Intent(this.activity, ItemShowPageActivity::class.java)
                intent.putExtra("ITEM", item)
                startActivity(intent)
            }
        }
    }

    override fun onOverflowMenuClickedFromAdapter(item: Item, view: View) {
        val popup = PopupMenu(this.context!!, view)
        val menuInflater = popup.menuInflater
        menuInflater.inflate(R.menu.menu_popup, popup.menu)
        popup.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.actionItemSold -> {
                    Toast.makeText(activity, "In item sold",
                        Toast.LENGTH_LONG).show()
                    return@setOnMenuItemClickListener true
                }
                R.id.actionShare -> {
                    val intent =  Intent(Intent.ACTION_SEND)
                    intent.putExtra(Intent.EXTRA_TEXT, item.description)
                    intent.type = "text/plain"
                    startActivity(intent)
                    Toast.makeText(activity, "share",
                        Toast.LENGTH_LONG).show()
                    return@setOnMenuItemClickListener true
                }
                else ->{
                    Toast.makeText(activity, "In nothing",
                        Toast.LENGTH_LONG).show()
                    return@setOnMenuItemClickListener false
                }
            }
        }
        // show icon on the popup menu!!
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popup.setForceShowIcon(true)
        }else{
            try {
                val fields = popup.javaClass.declaredFields
                for (field in fields) {
                    if ("mPopup" == field.name) {
                        field.isAccessible = true
                        val menuPopupHelper = field[popup]
                        val classPopupHelper =
                            Class.forName(menuPopupHelper.javaClass.name)
                        val setForceIcons: Method = classPopupHelper.getMethod(
                            "setForceShowIcon",
                            Boolean::class.javaPrimitiveType
                        )
                        setForceIcons.invoke(menuPopupHelper, true)
                        break
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        popup.show()
    }

    /*override fun onOverflowMenuClickedFromAdapter() {
        val popup = PopupMenu(this.context!!, view)
        val menuInflater = popup.menuInflater
        menuInflater.inflate(R.menu.menu_popup, popup.menu)
        popup.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.actionItemSold -> {
                    Toast.makeText(activity, "In item sold",
                        Toast.LENGTH_LONG).show()
                    return@setOnMenuItemClickListener true
                }
                R.id.actionShare -> {
                    Toast.makeText(activity, "share",
                        Toast.LENGTH_LONG).show()
                    return@setOnMenuItemClickListener true
                }
                else ->{
                    Toast.makeText(activity, "In nothing",
                        Toast.LENGTH_LONG).show()
                    return@setOnMenuItemClickListener false
                }
            }
        }
        // show icon on the popup menu!!
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popup.setForceShowIcon(true)
        }else{
            try {
                val fields = popup.javaClass.declaredFields
                for (field in fields) {
                    if ("mPopup" == field.name) {
                        field.isAccessible = true
                        val menuPopupHelper = field[popup]
                        val classPopupHelper =
                            Class.forName(menuPopupHelper.javaClass.name)
                        val setForceIcons: Method = classPopupHelper.getMethod(
                            "setForceShowIcon",
                            Boolean::class.javaPrimitiveType
                        )
                        setForceIcons.invoke(menuPopupHelper, true)
                        break
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        popup.show()
    }*/
}