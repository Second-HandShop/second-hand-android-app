package com.android.secondhand

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.os.Environment
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Log
import java.io.*
import androidx.core.content.FileProvider
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import com.cloudinary.android.MediaManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import android.widget.*
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception
import android.widget.ArrayAdapter

import android.widget.Spinner



class ItemEditPageActivity : AppCompatActivity(), View.OnClickListener, ImagesRecyclerViewAdapter.OnImageClickFromAdapter {

    val IMAGE = 1
    val VIDEO = 2
    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_IMAGE_GALLERY = 2
    val REQUEST_VIDEO_GALLERY = 3
    val REQUEST_VIDEO_CAPTURE = 4

    lateinit var spinner: Spinner
    lateinit var videoView: VideoView
    lateinit var addImageIconBitmap : Bitmap //static add_new_image icon

    // for uploading to cloudinary
    var videoViewUri: Uri? = null
    var imagesAbosulePath = ArrayList<String>()

    // for handling two kinds of image click events
    var addNewImage: Boolean = true
    var replaceImageAtPosition: Int = 0

    // for dynamically display selected images
    val imagesBitmap = ArrayList<Bitmap>()
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerViewAdapter: ImagesRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_edit_page)

        // set up Toolbar
        setSupportActionBar(findViewById(R.id.edit_page_toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        //static add_new_image icon stored in drawer folder
        addImageIconBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.add_img_icon)
        imagesBitmap.add(addImageIconBitmap)

        // set up recycler view
        recyclerView = findViewById<RecyclerView>(R.id.images_container)
        recyclerViewAdapter = ImagesRecyclerViewAdapter(imagesBitmap)
        recyclerView.layoutManager = GridLayoutManager(this, 3)

        // image click events handler in Activity
        // either add in new image at the last position or replace an image at a specific position
        recyclerViewAdapter.setClickListener(this)
        recyclerView.adapter = recyclerViewAdapter

        // item video container
        videoView = findViewById<VideoView>(R.id.item_video)

        // click to add in video
        videoView.setOnClickListener(this)
        // long click to delete video
        videoView.setOnLongClickListener {
            if(videoViewUri != null){
                deleteVideo()
            }
            return@setOnLongClickListener true
        }

        // "postItem" button triggers uploading images & video (if any) to cloudinary
        findViewById<Button>(R.id.postItem).setOnClickListener(this)

        // set up spinner for item categories

        //get the spinner from the xml.
        val dropdown = findViewById<Spinner>(R.id.item_category)
        //create a list of items for the spinner.
        val items = arrayOf("Household", "Furniture", "Books & Supplies", "Electronics", "Cars")
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
        //set the spinners adapter to the previously created one.
        dropdown.adapter = adapter

    }


    override fun onClick(view: View?) {
        when(view!!.id){
            R.id.item_video -> {
                selectVideo()
            }
            R.id.postItem -> {

                uploadToCloudinary()
            }
        }
    }


    // Define in ImagesRecyclerViewAdapter
    // handlers for image click events
    override fun onImageClick(position: Int) {

        if(position == imagesBitmap.size - 1){  // static add_new_image icon is clicked
            addNewImage = true
        }else{                   // a previously selected image is clicked
            addNewImage = false
            replaceImageAtPosition = position
        }
        selectImage()
    }


    // Define in ImagesRecyclerViewAdapter
    // handlers for image click events
    override fun onImageLongClick(position: Int) {
        if(position != imagesBitmap.size - 1)
            deleteImage(position)
    }


    // restore the state of VideoView after an alert-dialog disappears
    // otherwise, VideoView becomes black after activity lost focus
    override fun onResume() {
        super.onResume()
        videoView.setVideoURI(videoViewUri)
        videoView.seekTo(1)
    }


    // handle "CAPTURE" and "SELECT FROM GALLERY" activity for image/video
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            var bitmap : Bitmap? = null

            if(requestCode == REQUEST_IMAGE_CAPTURE){

                // retrieve the file absolute paths stored during the file creation
                val absolutePath = imagesAbosulePath.get(imagesAbosulePath.size - 1)

                // retrieve bitmap for displaying image on RecyclerView
                bitmap = BitmapFactory.decodeFile(absolutePath)

            }else if(requestCode == REQUEST_IMAGE_GALLERY){
                // retrieve bitmap for displaying image on RecyclerView
                val selectedPhotoUri = data!!.data
                bitmap = MediaStore.Images.Media.getBitmap(this!!.contentResolver, selectedPhotoUri)

                // retrieve file absolute path for uploading to cloudinary
                val filePath = getPath(this, selectedPhotoUri, IMAGE)
                if(addNewImage)
                    imagesAbosulePath.add(imagesAbosulePath.size, filePath!!)
                else
                    imagesAbosulePath.set(replaceImageAtPosition, filePath!!)


            }else if(requestCode == REQUEST_VIDEO_GALLERY || requestCode == REQUEST_VIDEO_CAPTURE){

                val videoUri: Uri? = data!!.data
                // display the video on the VideoView
                videoView.setVideoURI(videoUri)
                // save for uploading to cloudinary
                videoViewUri = videoUri
                // hide the static add_new_image icon
                videoView.setBackground(null)
                // set to show the video scene on the first millionsecond
                videoView.seekTo(1)
            }

            // Selecting Images
            if(requestCode <=2){
                bitmap = resizeBitmap(bitmap!!) // scale image to fit into ImageView but retaining aspect ratio

                if(addNewImage){
                    imagesBitmap.add(imagesBitmap.size - 1, bitmap!!)
                }else{
                    imagesBitmap.set(replaceImageAtPosition, bitmap!!)
                }

                if(imagesBitmap.size > 6) imagesBitmap.removeAt(imagesBitmap.size - 1)
                recyclerViewAdapter.notifyDataSetChanged()
                recyclerView.swapAdapter(recyclerViewAdapter, true)
            }

        }
    }


    // pop up AlertDialog to show options for picking images
    private fun selectImage() {

        // check for permission
        if(checkForApplicationPermission()){

            val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
            val builder= AlertDialog.Builder(this)
            builder.setTitle("Add Photo!")

            builder.setItems(options, DialogInterface.OnClickListener { dialog, item ->
                if (options[item].equals("Take Photo")) {
                    dispatchTakePictureIntent()

                }
                else if (options[item].equals("Choose from Gallery")) {
                    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(intent, REQUEST_IMAGE_GALLERY)
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss()
                }
            })

            builder.show()
        }
    }


    // before starting capturing image, create a file for it
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.android.secondhand.provider",
                        it
                    )
                    // save a file absolute path for uploading to cloudinary
                    imagesAbosulePath.add(imagesAbosulePath.size, it.absolutePath)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        )
    }


    // pop up AlertDialog to show options for picking images
    private fun selectVideo() {
        // check for camera & File and Meida permission
        if(checkForApplicationPermission()){

            val options = arrayOf<CharSequence>("Make a Video", "Choose from Gallery", "Cancel")
            val builder= AlertDialog.Builder(this)
            builder.setTitle("Add Video!")

            builder.setItems(options, DialogInterface.OnClickListener { dialog, item ->
                if (options[item].equals("Make a Video")) {
                    Intent(MediaStore.ACTION_VIDEO_CAPTURE).also { takeVideoIntent ->
                        takeVideoIntent.resolveActivity(packageManager)?.also {
                            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE)
                        }
                    }
                }
                else if (options[item].equals("Choose from Gallery")) {
                    try{
                        val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
                        startActivityForResult(intent, REQUEST_VIDEO_GALLERY)
                    }catch (e: Exception){
                        e.printStackTrace()
                    }

                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss()
                }
            })
            builder.show()
        }
    }


    // pop up AlertDialog to show options to confirm or cancel deleting image
    private fun deleteImage(position: Int) {
        val options = arrayOf<CharSequence>("Delete", "Cancel")
        val builder= AlertDialog.Builder(this)
        builder.setTitle("Delete Image?")

        builder.setItems(options, DialogInterface.OnClickListener { dialog, item ->
            if (options[item].equals("Delete")) {

                imagesBitmap.removeAt(position)
                imagesAbosulePath.removeAt(position)
//                imagesAbosulePath = ArrayList<String>(imagesAbosulePath.filterNotNull())
//
                Log.i("tab", "after deleting one.. imagesAbsolutePath=${imagesAbosulePath.size}")
                // if not reaching the total limit of 6, show the static add_new_image icon at the end
                if(imagesBitmap.get(imagesBitmap.size - 1) != addImageIconBitmap) imagesBitmap.add(imagesBitmap.size, addImageIconBitmap)
                // refresh the RecyclerView
                recyclerViewAdapter.notifyDataSetChanged()
                recyclerView.swapAdapter(recyclerViewAdapter, true)
            }
            dialog.dismiss()
        })
        builder.show()
    }


    // pop up AlertDialog to show options to confirm or cancel deleting video
    private fun deleteVideo() {
        val options = arrayOf<CharSequence>("Delete", "Cancel")
        val builder= AlertDialog.Builder(this)
        builder.setTitle("Delete Video?")

        builder.setItems(options, DialogInterface.OnClickListener { dialog, item ->
            if (options[item].equals("Delete")) {
                // show the static add_new_image icon
                videoViewUri = null
                videoView.setBackgroundResource(R.drawable.add_img_icon)
            }
            dialog.dismiss()
        })
        builder.show()
    }


    private fun uploadToCloudinary() {

        val callback = object : UploadCallback {

            val itemName = findViewById<TextView>(R.id.itemDescription)

            override fun onStart(requestId: String) {
                itemName.setText("start")
            }

            override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                itemName.setText("Uploading... ")
            }

            override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                itemName.setText("image URL: " + resultData["url"].toString())
            }

            override fun onError(requestId: String?, error: ErrorInfo) {
                itemName.setText("error: " + error.getDescription())
            }

            override fun onReschedule(requestId: String?, error: ErrorInfo) {
                itemName.setText("Reshedule: " + error.getDescription())
            }
        }

        // upload video
        if(videoViewUri != null){
            val filePath = getPath(this, videoViewUri, VIDEO)
            MediaManager.get().upload(filePath)
            .option("folder", "sample")
            .option("resource_type", "video")
            .callback(callback).dispatch()
        }


        // upload images
        imagesAbosulePath.forEach {
            MediaManager.get().upload(it)
                .option("folder", "sample")
                .option("resource_type", "image")
                .callback(callback).dispatch()
        }

    }

    // get file absolute path for images selected from gallery and videos
    // paths for captured images have been stored during CAPTURE activity
    private fun getPath(context: Context, uri: Uri?, type: Int): String? {
        var result: String? = null
        var proj : Array<String>  = arrayOf()
        if(type == IMAGE){
            proj = arrayOf(MediaStore.Images.Media.DATA)
        }else if (type == VIDEO){
            proj = arrayOf(MediaStore.Video.Media.DATA)
        }

        val cursor: Cursor? = context.getContentResolver().query(uri!!, proj, null, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val column_index: Int = cursor.getColumnIndexOrThrow(proj[0])
                result = cursor.getString(column_index)
            }
            cursor.close()
        }
        if (result == null) {
            result = "Not found"
        }
        return result
    }


    // Resize the image to fit into the ImageView, retaining the aspect ratio
    private fun resizeBitmap(bitmapOrg: Bitmap): Bitmap{
        val width: Int = bitmapOrg.width
        val height: Int = bitmapOrg.height
        val newWidth = 350
        val newHeight = 350

        // calculate the scale
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height

        // createa matrix for the manipulation
        val matrix = Matrix()

        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight)

        // recreate the new Bitmap
        val resizedBitmap = Bitmap.createBitmap(
            bitmapOrg, 0, 0,
            width, height, matrix, true
        )

        return resizedBitmap
    }

    fun checkForApplicationPermission(): Boolean {
        val readMediaPermission = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        val writeMediaPermission = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val cameraPermission =
            ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA)

        val camera = cameraPermission == PackageManager.PERMISSION_GRANTED
        val read = readMediaPermission == PackageManager.PERMISSION_GRANTED
        val write = writeMediaPermission == PackageManager.PERMISSION_GRANTED

        if(camera && read && write){
            return true
        }else if(!camera && (!read || !write)){
            askForPermission("Camera, File and Media")
            return false
        }
        else if (!camera && read && write) {
            askForPermission("Camera")
            return false
        }else if(camera && (!read || !write)){
            askForPermission("File and Media")
            return false
        }

        return true
    }

    fun askForPermission(whatPermission: String){
        Snackbar.make(findViewById(R.id.postItem), "${whatPermission} permission needed.\nPlease set it in System Setting.", Snackbar.LENGTH_LONG)
            .setAction("DIMISS", View.OnClickListener {})
            .show()
    }

}