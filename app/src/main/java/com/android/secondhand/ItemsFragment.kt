package com.android.secondhand

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.secondhand.apis.Constant
import com.android.secondhand.apis.GsonRequest
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import io.swagger.server.models.GetItemsByUserIdsResponse
import io.swagger.server.models.Item


class ItemsFragment : Fragment() {

    // static categories
    val categories = listOf<String>("All", "Household", "Furniture", "Books & Supplies", "Electronics", "Cars")
//        ,"Clothing & Shoes", "Sports & Outdoors", "Accessories", "Pet Supplies", "Musical Instruments", "Games & Toys", "Others")

    lateinit var tab: String
    lateinit var root: View
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerViewAdapter: ItemsRecyclerViewAdapter
    var items = ArrayList<Item>()
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tab = categories[ it.getInt("TAB_POSITION") ]
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_items, container, false)
        //:TODO uncomment the code on the next line
        val currentUserName = "bhatttrahul712@gmail.com"//auth.currentUser?.displayName
        val requestQueue: RequestQueue = Volley.newRequestQueue(activity)

        val gsonRequest = GsonRequest(
            Constant.API_BASE_ADDRESS,
            GetItemsByUserIdsResponse::class.java,
            null,
            { response ->
                System.out.println("Response: %s".format(response.toString()))
                val itemsForUserMap = response.userIdsToIdsMap
                if (itemsForUserMap != null) {
                    items = itemsForUserMap[currentUserName]?.toCollection(ArrayList()) ?: arrayListOf()
                }
                recyclerViewAdapter = ItemsRecyclerViewAdapter(items)
                recyclerView = root.findViewById<RecyclerView>(R.id.items_container)
                recyclerView.layoutManager = GridLayoutManager(this.context, 2)
                recyclerView.adapter = recyclerViewAdapter
            },
            { error ->
                System.out.println("That did not work : \n" + error)
                items = arrayListOf()
                Toast.makeText(activity, "There was a problem retrieving data please retry after sometime",
                    Toast.LENGTH_LONG).show()
            }
        )
        requestQueue.add(gsonRequest)

        // filter posts by category (tab)
//        if(tab.equals("All")){
//            items = ArrayList(allPosts)
//        }else{
//            allPosts.forEach {
//                if(it.category.equals(tab))
//                    items.add(it)
//            }
//        }

        // set up RecyclerView


        return root
    }

    companion object{
        @JvmStatic
        fun newInstance(position: Int) =
            ItemsFragment().apply {
                arguments = Bundle().apply {
                    putInt("TAB_POSITION", position)
                }
            }
    }
}