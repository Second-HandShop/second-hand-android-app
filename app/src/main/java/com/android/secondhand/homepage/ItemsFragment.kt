package com.android.secondhand.homepage

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.secondhand.R
import com.android.secondhand.apis.Constant
import com.android.secondhand.apis.GsonRequest
import com.android.secondhand.showPage.ItemShowPageActivity
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import io.swagger.server.models.GetItemsByUserIdsResponse
import com.android.secondhand.models.Item
import java.io.Serializable


class ItemsFragment : Fragment(), ItemsRecyclerViewAdapter.ShowItemClickListener {

    // static categories
    val categories = listOf<String>("All", "Household", "Furniture", "Books and Supplies", "Electronics", "Cars")
//        ,"Clothing & Shoes", "Sports & Outdoors", "Accessories", "Pet Supplies", "Musical Instruments", "Games & Toys", "Others")

    lateinit var tab: String
    lateinit var root: View
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerViewAdapter: ItemsRecyclerViewAdapter
    var items = ArrayList<Item>()
    lateinit var auth: FirebaseAuth
    lateinit var currentUserName: String
    lateinit var pullToRefresh: SwipeRefreshLayout
    lateinit var searchBar: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tab = categories[ it.getInt("TAB_POSITION") ]
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_items, container, false)
        auth = FirebaseAuth.getInstance()
        //:TODO uncomment the code on the next line fetch from firebase
        currentUserName = auth.currentUser?.email.toString()

        searchBar = activity?.findViewById(R.id.searchBar)!!
        updateData()


        pullToRefresh = root.findViewById(R.id.pullToRefresh)
        pullToRefresh.setOnRefreshListener {
            updateData() // your code
            pullToRefresh.isRefreshing = true
        }

        return root
    }

    companion object{
        @JvmStatic
        fun newInstance(position: Int, searchString: String) =
            ItemsFragment().apply {
                arguments = Bundle().apply {
                    putInt("TAB_POSITION", position)
                }
            }
    }


    fun updateData() {
        items.clear()
        val requestQueue: RequestQueue = Volley.newRequestQueue(activity)

        var url = Constant.API_BASE_ADDRESS
        var questionMarkAdded = false
        if(!tab.equals("all", ignoreCase = true)) {
            url = "$url?categories=$tab"
            questionMarkAdded = true
        }

        var searchString : String? = null
        if (searchBar != null) {
            searchString = searchBar.query.toString()
        }

        if(searchString != null && searchString.isNotBlank()) {
            url = if(!questionMarkAdded) {
                "$url?"
            } else{
                "$url&"
            }
            url += "name=$searchString"
        }

        val gsonRequest = GsonRequest(
            url,
            GetItemsByUserIdsResponse::class.java,
            null,
            { response ->
                System.out.println("Response: %s".format(response.toString()))
                val itemsForUserMap = response.userIdsToIdsMap
                itemsForUserMap?.values?.map { userItems ->
                    items.addAll(userItems.toCollection(ArrayList()))
                }
                recyclerViewAdapter = ItemsRecyclerViewAdapter(items)

                recyclerView = root.findViewById<RecyclerView>(R.id.items_container)
                recyclerView.layoutManager = GridLayoutManager(this.context, 2)
                recyclerView.adapter = recyclerViewAdapter
                pullToRefresh.isRefreshing = false

                // set listener for ItemsRecyclerViewAdapter
                recyclerViewAdapter.setListener(this)
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

    // from ItemsRecyclerViewAdapter
    override fun onItemClick(item: Item) {
        val intent = Intent(this.activity, ItemShowPageActivity::class.java)
        intent.putExtra("ITEM", item as Serializable)
        startActivity(intent)
    }
}