package com.live.streetview.navigation.earthmap.compass.map.activities

import android.Manifest
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.flaviofaria.kenburnsview.KenBurnsView
import com.google.android.gms.ads.AdView
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniBillingHelper
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds
import com.live.streetview.navigation.earthmap.compass.map.MainActivity
import com.live.streetview.navigation.earthmap.compass.map.R
import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityImageBinding
import java.io.File

class ImageActivity : AppCompatActivity() {
    var imageKen: KenBurnsView? = null
    var txtLast: TextView? = null
    var progressBarlast: ProgressBar? = null
    var viewHome: View? = null
    var viewHeart: View? = null
    var ViewShare: View? = null
    var ViewDownload: View? = null
    var favBtn: ImageView? = null
    var imageViewLastLast: ImageView? = null
    var sharedPreferences: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null
    var heart: String? = null
    var Cityname: String? = null
    var cityImage: String? = null
    var placeName: String? = null
    var countryName: String? = null
    var bindingImage: ActivityImageBinding? = null
    private val REQUEST_WRITE_PERMISSION = 1001
    private val REQUEST_READ_IMAGES_PERMISSION = 1002

     @RequiresApi(Build.VERSION_CODES.TIRAMISU)
     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_image);
        bindingImage = ActivityImageBinding.inflate(getLayoutInflater())
        setContentView(bindingImage!!.root)
        StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks(
            "StreetViewDisplayStreetViewCountryDisplayFullImageOnCreate",
            this
        )


        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        imageKen = findViewById<KenBurnsView>(R.id.imageKenbo)
        txtLast = findViewById<TextView>(R.id.textLasttxt)
        txtLast?.setSelected(true)
        progressBarlast = findViewById<ProgressBar>(R.id.progressbarLast)
        viewHome = findViewById<View>(R.id.ViewHome)
        favBtn = findViewById<ImageView>(R.id.imageView21)
        viewHeart = findViewById<View>(R.id.ViewHeart)
        ViewShare = findViewById<View>(R.id.ViewShare)
        ViewDownload = findViewById<View>(R.id.ViewDownload)
        imageViewLastLast = findViewById<ImageView>(R.id.lastImageView)
        sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        editor = sharedPreferences?.edit()
        //  heart=sharedPreferences.getString("key2","click");
        if (sharedPreferences?.getInt("key", 0) == 0) {
            favBtn!!.setImageDrawable(getResources().getDrawable(R.drawable.heartlast))
        } else {
            favBtn!!.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite))
        }
        cityImage = getIntent().getStringExtra("showimage")
        placeName = getIntent().getStringExtra("placeName")
        Cityname = getIntent().getStringExtra("Cityname")
        countryName = getIntent().getStringExtra("key")
        txtLast?.setText(placeName)
        // Glide.with(context).load(placeModel.getImageplace()).into(holder.imageplace);
//        Picasso.get().load(cityImage).into(imageKen);
        Glide.with(this).load(cityImage).addListener(object : RequestListener<Drawable?> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable?>?,
                isFirstResource: Boolean
            ): Boolean {
                progressBarlast?.setVisibility(View.VISIBLE)
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable?>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                progressBarlast?.setVisibility(View.GONE)
                return false
            }
        }).into(imageKen!!)
        listerns()
        //  initBannerAdImage();
        viewHome!!.setOnClickListener {
            val intent = Intent(this@ImageActivity, MainActivity::class.java)
            /* StreetViewAppSoniMyAppShowAds.meidationForClickSimpleSmartToolsLocation(ImageActivity.this,
                            StreetViewAppSoniMyAppAds.admobInterstitialAd,   intent);*/
        }
        viewHeart!!.setOnClickListener { SharePic(cityImage) }
        ViewShare!!.setOnClickListener { SharePic(cityImage) }
        ViewDownload!!.setOnClickListener {
            placeName?.let { it1 -> handleViewDownloadClick(it1, cityImage) }
        }

        streetViewBannerAdsSmall()
    }



    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun handleViewDownloadClick(placeName: String, cityImage: String?) {

       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU){
           if (checkWriteExternalStoragePermission()) {
               // Permission already granted, proceed with download
               Log.d("TAG", "Download clicked, permission already granted")
               // Call your download function here
               performDownload(placeName,cityImage)
           }
           else {
               // Permission not granted, request it
               if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                   // Permission has been previously requested but denied
                   Log.d("TAG", "Permission denied previously")
                   // You can handle permission denial here, like showing a message to the user
                   Toast.makeText(this, "Permission to write to external storage is required.", Toast.LENGTH_SHORT).show()
               } else {
                   // Request permission
                   Log.d("TAG", "Requesting permission...")
                   requestWriteExternalStoragePermission()
               }
           }

       }
      else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
          if (checkReadMediaImagesPermission()) {
              // Permission already granted, proceed with download
              Log.d("TAG", "Download clicked, permission already granted")
              // Call your download function here
              performDownload(placeName,cityImage)
          } else {
              // Permission not granted, request it
              if (shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES)) {
                  // Permission has been previously requested but denied
                  Log.d("TAG", "Permission denied previously")
                  // You can handle permission denial here, like showing a message to the user
                  Toast.makeText(this, "Permission to write to external storage is required.", Toast.LENGTH_SHORT).show()
              } else {
                  // Request permission
                  Log.d("TAG", "Requesting permission...")
                  requestWriteExternalStoragePermission()
              }
          }
      }

    }

    private fun checkWriteExternalStoragePermission(): Boolean {
        val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkReadMediaImagesPermission(): Boolean {
        val readImagePermission =  Manifest.permission.READ_MEDIA_IMAGES

        return ContextCompat.checkSelfPermission(this, readImagePermission) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestWriteExternalStoragePermission() {
        // only for gingerbread and newer versions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_MEDIA_IMAGES,),
                REQUEST_READ_IMAGES_PERMISSION
            )
        } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)

        {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,),
                REQUEST_WRITE_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_WRITE_PERMISSION || requestCode == REQUEST_READ_IMAGES_PERMISSION) {
           Log.d("TAG", "grant result value : $grantResults ")
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with the download

                Log.d("TAG", "Permission granted, downloading...")
                // Call your download function here
                placeName?.let { performDownload(it,cityImage) }
            }
            else{
                // Permission denied
                Log.d("TAG", "Permission denied")
                // You can handle permission denial here, like showing a message to the user
                Toast.makeText(this, "Permission denied to write to external storage.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun performDownload(placeName: String, cityImage: String?) {
        // Simulated download function
        Log.d("TAG", "performDownload: Downloading...")
        // Your download logic goes here
        DownLoadPic(placeName,cityImage)
    }

    private fun streetViewBannerAdsSmall() {
        val billingHelper = StreetViewAppSoniBillingHelper(this)
        val adContainer: LinearLayout = bindingImage!!.bannerAdPlace.adContainer
        val adView = AdView(this)
        adView.setAdUnitId(StreetViewAppSoniMyAppAds.banner_admob_inApp)
        adView.setAdSize(StreetViewAppSoniMyAppAds.getAdSize(this))
        if (billingHelper.isNotAdPurchased) {
            StreetViewAppSoniMyAppAds.loadSmartToolsBannerForMainMediation(
                adContainer, adView, this
            )
        } else {
            bindingImage!!.bannerID.visibility = View.GONE
        }
    }

    private fun listerns() {
//        onBackPressed();
        imageViewLastLast!!.setOnClickListener {
            StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks(
                "StreetViewDisplayStreetViewCountryDisplayFullImageOnBtnExit",
                this@ImageActivity
            )
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks(
                    "StreetViewDisplayStreetViewCountryDisplayFullImageOnCreate",
                    this@ImageActivity
                )
                StreetViewAppSoniMyAppShowAds.mediationBackPressedSimpleStreetViewLocation(
                    this@ImageActivity,
                    StreetViewAppSoniMyAppAds.admobInterstitialAd,
                    bindingImage!!.whiteView
                )
            }
        }

    private fun SharePic(cityImage: String?) {
        Log.d("TAG", "SharePic: ")
        val sendIntent = Intent()
        sendIntent.setAction(Intent.ACTION_SEND)
        sendIntent.putExtra(Intent.EXTRA_TEXT, cityImage)
        sendIntent.setType("text/plain")
        startActivity(sendIntent)
    }

    private fun DownLoadPic(placeName: String?, cityImage: String?) {
        try {
            val dm: DownloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val downloadUri = Uri.parse(cityImage)
            val request: DownloadManager.Request = DownloadManager.Request(downloadUri)
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                .setAllowedOverRoaming(false)
                .setTitle(placeName)
                .setMimeType("image/jpeg") // Your file type. You can use this code to download other file types also.
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_PICTURES,
                    File.separator + placeName + ".jpg"
                )
            dm.enqueue(request)
            Toast.makeText(this, "Image download started.", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.d("TAG_ERROR", "DownLoadPic: " + e.localizedMessage)
            Toast.makeText(this, "Image download failed.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clickOnPic() {
        if (sharedPreferences?.getInt("key", 0) == 0) {
            favBtn!!.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite))
            editor?.putInt("key", 1)
        } else {
            favBtn!!.setImageDrawable(getResources().getDrawable(R.drawable.heartlast))
            editor?.putInt("key", 0)
        }
        editor?.commit()
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {

            requestPermissions(
                arrayOf<String>(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), REQUEST_WRITE_PERMISSION
            )

        }
    }

    companion object {
        private const val REQUEST_WRITE_PERMISSION = 1001
    }
}
