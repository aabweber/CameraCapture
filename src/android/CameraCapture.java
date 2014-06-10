package com.aabweber;


import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;

//import static com.googlecode.javacv.cpp.opencv_core.cvFlip;
//import static com.googlecode.javacv.cpp.opencv_highgui.cvSaveImage;


//import com.googlecode.javacv.CanvasFrame;
//import com.googlecode.javacv.FrameGrabber;
import com.googlecode.javacv.FrameGrabber;
import com.googlecode.javacv.VideoInputFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.CanvasFrame;


public class CameraCapture extends CordovaPlugin {
    public static final String TAG = "CameraCapture";
	public static final String ACTION_GET = "get";

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		try {
            if (ACTION_GET.equals(action)) {
//            	IplImage image;
                CanvasFrame canvas = new CanvasFrame("Web Cam");
                FrameGrabber grabber = new VideoInputFrameGrabber(0);
                grabber.start();
                IplImage img = grabber.grab();
//                grabber.grab();
//                public GrabberShow() {
//                    canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
//                }
            /*
                     JSONObject arg_object = args.getJSONObject(0);
                     Intent calIntent = new Intent(Intent.ACTION_EDIT)
                .setType("vnd.android.cursor.item/event")
                .putExtra("beginTime", arg_object.getLong("startTimeMillis"))
                .putExtra("endTime", arg_object.getLong("endTimeMillis"))
                .putExtra("title", arg_object.getString("title"))
                .putExtra("description", arg_object.getString("description"))
                .putExtra("eventLocation", arg_object.getString("eventLocation"));

               this.cordova.getActivity().startActivity(calIntent);
               */
               
               JSONObject retVal = new JSONObject();
//               retVal.put("image", img.toString());
               retVal.put("test", "123");
//               callbackContext.error(retVal);
               callbackContext.success(retVal);
               return true;
            }
            callbackContext.error("Invalid action");
            return false;
        } catch(Exception e) {
            System.err.println("Exception: " + e.getMessage());
            callbackContext.error(e.getMessage());
            return false;
        }
	}

	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
	    super.initialize(cordova, webView);
	    // your init code here
	}

}