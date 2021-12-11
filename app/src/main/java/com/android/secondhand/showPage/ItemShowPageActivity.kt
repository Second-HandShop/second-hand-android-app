package com.android.secondhand.showPage

import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.widget.NestedScrollView
import com.android.secondhand.R
import com.android.secondhand.apis.Constant
import com.android.secondhand.models.Item
import com.cloudinary.android.MediaManager
import com.google.android.exoplayer2.ExoPlayer
import com.smarteist.autoimageslider.SliderView
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.google.android.exoplayer2.source.ExtractorMediaSource

import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.TrackSelection
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.util.Util
import com.cloudinary.Transformation
import com.google.android.exoplayer2.SimpleExoPlayer

class ItemShowPageActivity : AppCompatActivity() {

    lateinit var item: Item
    lateinit var adapter: ImageSliderAdapter
    lateinit var sliderView: SliderView
    lateinit var scrollView: NestedScrollView
//    var player: ExoPlayer? = null
    var itemVideoUri: String? = null
    var itemImages: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_page)

        // retrieve the Item passed from ItemsFragment
        item = intent.extras?.get("ITEM") as Item

        // retrieve item data
        getItemImages()
        getItemVideo()
        bindItemData()


        // set up collapsing toolbar
        setSupportActionBar(findViewById(R.id.show_page_toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.title = if(item.name?.length!! > 20) item.name?.substring(0, 20) + "..." else item.name
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        // set up scrollview
        scrollView = findViewById(R.id.scrollView)
        scrollView.scrollTo(0,0)

        // set up image slider
        sliderView = findViewById<SliderView>(R.id.imageSlider)
        adapter = ImageSliderAdapter(itemImages, this)
        sliderView.setSliderAdapter(adapter)

        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM) //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!

        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        sliderView.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
        sliderView.indicatorSelectedColor = Color.WHITE
        sliderView.indicatorUnselectedColor = Color.GRAY
        sliderView.scrollTimeInSec = 4 //set scroll delay in seconds :

        sliderView.startAutoCycle()
    }

    fun bindItemData(){
        val userIdText = findViewById<TextView>(R.id.userIdText)
        val itemPrice = findViewById<TextView>(R.id.itemPrice)
        val itemDescription= findViewById<TextView>(R.id.itemDescription)
        val itemVideo = findViewById<CardView>(R.id.itemVideo)

        //TODO: get user id from firebase
        userIdText.text = Constant.LOGGED_IN_USER_ID
        itemPrice.setText("$ ${item.price}")
        itemDescription.setText(item.description)

        if(itemVideoUri == null){
            itemVideo?.visibility = View.GONE
        }

    }

    fun getItemImages(){
       item.resources?.filter{
            it.resourceType!!.toString().equals("image", ignoreCase = true)
        }!!.map {
            itemImages.add(it.resourceLink!!)
        }
    }

    fun getItemVideo(){
        item.resources?.filter{
            it.resourceType!!.toString().equals("video", ignoreCase = true)
        }!!.map {
            itemVideoUri = it.resourceLink
        }

        // TO-BE-DELETED
//        itemVideoUri = "https://res.cloudinary.com/dqg4lzcl8/video/upload/v1639190462/sample/y76slmcluohurgymnl2v.mp4"

//        val transformedUrl = MediaManager.get().url()
//            .transformation(
//                Transformation().height(320).width(480).resourceType("video").generate(itemVideoUri))
//

        // set up player
        itemVideoUri?.let { initializePlayer(it) }
    }

    fun initializePlayer(uri: String){
        // Create a default TrackSelector
        val bandwidthMeter: BandwidthMeter = DefaultBandwidthMeter()
        val videoTrackSelectionFactory: TrackSelection.Factory =
            AdaptiveTrackSelection.Factory(bandwidthMeter)
        val trackSelector: TrackSelector = DefaultTrackSelector(videoTrackSelectionFactory)

        //Initialize the player
        val player = ExoPlayerFactory.newSimpleInstance(this, trackSelector)

        //Initialize simpleExoPlayerView
        val playerView = findViewById<PlayerView>(R.id.exoplayer)
        playerView.player = player

        // Produces DataSource instances through which media data is loaded.
        val dataSourceFactory: DataSource.Factory =
            DefaultDataSourceFactory(this, Util.getUserAgent(this, "CloudinaryExoplayer"))

        // Produces Extractor instances for parsing the media data.
        val extractorsFactory: ExtractorsFactory = DefaultExtractorsFactory()

        // This is the MediaSource representing the media to be played.
        val videoUri: Uri = Uri.parse(uri)
        val videoSource: MediaSource = ExtractorMediaSource(
            videoUri,
            dataSourceFactory, extractorsFactory, null, null
        )

        // Prepare the player with the source.
        player.prepare(videoSource)

    }


}