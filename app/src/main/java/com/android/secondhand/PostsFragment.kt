package com.android.secondhand

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class PostsFragment : Fragment() {

    // static categories
    val categories = listOf<String>("All", "Household", "Furniture", "Books & Supplies", "Electronics", "Cars")
//        ,"Clothing & Shoes", "Sports & Outdoors", "Accessories", "Pet Supplies", "Musical Instruments", "Games & Toys", "Others")

    lateinit var tab: String
    lateinit var root: View
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerViewAdapter: PostsRecyclerViewAdapter
    val allPosts = PostDataUtils.getPosts()
    var items = ArrayList<PostData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tab = categories[ it.getInt("TAB_POSITION") ]
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_posts, container, false)

        // filter posts by category (tab)
        if(tab.equals("All")){
            items = ArrayList(allPosts)
        }else{
            allPosts.forEach {
                if(it.category.equals(tab))
                    items.add(it)
            }
        }

        // set up RecyclerView
        recyclerViewAdapter = PostsRecyclerViewAdapter(items)
        recyclerView = root.findViewById<RecyclerView>(R.id.posts_container)
        recyclerView.layoutManager = GridLayoutManager(this.context, 2)
        recyclerView.adapter = recyclerViewAdapter

        return root
    }

    companion object{
        @JvmStatic
        fun newInstance(position: Int) =
            PostsFragment().apply {
                arguments = Bundle().apply {
                    putInt("TAB_POSITION", position)
                }
            }
    }
}