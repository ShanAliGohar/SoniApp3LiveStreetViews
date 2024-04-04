package com.live.streetview.navigation.earthmap.compass.map

import android.app.Dialog
import android.app.Notification
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.*
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.live.streetview.navigation.earthmap.compass.map.activities.ChoseKmlTypeActivity
import com.live.streetview.navigation.earthmap.compass.map.activities.CountryActivities
import com.live.streetview.navigation.earthmap.compass.map.activities.MainChatViewActivity
import com.torrydo.floatingbubbleview.FloatingBubble
import com.torrydo.floatingbubbleview.FloatingBubbleService


class MyServiceKt : FloatingBubbleService() {
    var x_pos: Float = 0f
    var y_pos: Float = 0f

    override fun setupNotificationBuilder(channelId: String): Notification {
        return NotificationCompat.Builder(this, channelId)
            .setOngoing(true)
            .setSmallIcon(R.drawable.actionicon)
            .setContentTitle("bubble is running")
            .setContentText("click to do nothing")
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
    }
    override fun setupBubble(action: FloatingBubble.Action): FloatingBubble.Builder {
        return FloatingBubble.Builder(this)

            // set bubble icon attributes, currently only drawable and bitmap are supported
            .bubble(R.drawable.actionicon, 50, 50)
            // set style for bubble, fade animation by default
            .bubbleStyle(null)

            // set start location of bubble, (x=0, y=0) is the top-left
            .startLocation(0, 0)

            // enable auto animate bubble to the left/right side when release, true by default
            .enableAnimateToEdge(true)
            // set close-bubble icon attributes, currently only drawable and bitmap are supported
            .closeBubble(R.drawable.cancelshort, 50, 50)
            // set style for close-bubble, null by default
            .closeBubbleStyle(null)
            // show close-bubble, true by default
            .enableCloseBubble(true)
            // choose behavior of the bubbles
            // DYNAMIC_CLOSE_BUBBLE: close-bubble moving based on the bubble's location
            // FIXED_CLOSE_BUBBLE: bubble will automatically move to the close-bubble when it reaches the closable-area
            // enable bottom background, false by default
            .bottomBackground(false)

            // add listener for the bubble
            .addFloatingBubbleListener(object : FloatingBubble.Listener {
                override fun onDestroy() {}

                @RequiresApi(Build.VERSION_CODES.O)
                override fun onClick() {

                    clickToMove()

                    /* action.navigateToExpandableView() */// must override `setupExpandableView`, otherwise throw an exception
                }

                override fun onMove(x: Int, y: Int) {
                } // The location of the finger on the screen which triggers the movement of the bubble.

                override fun onUp(x: Int, y: Int) {
                    x_pos = x.toFloat()
                    y_pos = y.toFloat()
                }   // ..., when finger release from bubble

                override fun onDown(x: Int, y: Int) {} // ..., when finger tap the bubble
            })
            // set bubble's opacity
            .opacity(1f)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun clickToMove() {

        val dialog = Dialog(applicationContext); // Context, this, etc.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialoge);
        val wmlp = dialog.window!!.attributes
        wmlp.gravity = Gravity.TOP or Gravity.LEFT
        wmlp.x = x_pos.toInt() //x position
        wmlp.y = y_pos.toInt()
        dialog.window!!.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        dialog.findViewById<ImageView>(R.id.imageView48).setOnClickListener {
            dialog.dismiss();
            val intent = Intent(this, ChoseKmlTypeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            applicationContext.startActivity(intent)
        }
        dialog.findViewById<ImageView>(R.id.imageView49).setOnClickListener {
            dialog.dismiss();
            val intent = Intent(this, MainChatViewActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            applicationContext.startActivity(intent)

        }
        dialog.findViewById<ImageView>(R.id.imageView50).setOnClickListener {
            dialog.dismiss();
            val intent = Intent(this, GPSTESTINGActivity2::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            applicationContext.startActivity(intent)

        }
        dialog.findViewById<ImageView>(R.id.imageView51).setOnClickListener {
            dialog.dismiss();
            val intent = Intent(this, CountryActivities::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            applicationContext.startActivity(intent)

        }
        dialog.findViewById<ImageView>(R.id.imageView52).setOnClickListener {
            dialog.dismiss();

        }
        dialog.show();
    }

    /* override fun setupExpandableView(action: ExpandableView.Action): ExpandableView.Builder? {

         val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
         val wrapper: ViewGroup = object : FrameLayout(this) {
             override fun dispatchKeyEvent(event: KeyEvent): Boolean {
                 if (event.keyCode == KeyEvent.KEYCODE_BACK) {
                     action.popToBubble()
                     return true
                 }
                 return super.dispatchKeyEvent(event)
             }
         }
         val layout = inflater.inflate(R.layout.layout_view_test, wrapper)

         layout.findViewById<View>(R.id.card_view).setOnClickListener { v: View? ->
             Toast.makeText(this, "hello from card view from kotlin", Toast.LENGTH_SHORT).show();
             action.popToBubble()
         }
         return inflater

             // set view to expandable-view, passing both view and compose will cause a crash.
 //            .view(layout)

             // set composable to expandable-view, passing both view and compose will cause a crash.
 //            .compose {
 //                TestComposeView(
 //                    popBack = {
 //                        action.popToBubble()
 //                    }
 //                )
 //            }
 //
 //            // set the amount of dimming below the view.
 //            .dimAmount(0.8f)
 //
 //            // apply style for the expandable-view
 //            .expandableViewStyle(null)
 //
 //            // ddd listener for the expandable-view
 //            .addExpandableViewListener(object : ExpandableView.Listener {
 //                override fun onOpenExpandableView() {}
 //                override fun onCloseExpandableView() {}
 //            })
     }*/
}