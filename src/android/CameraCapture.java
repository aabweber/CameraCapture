package com.aabweber.cameracapture;


import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

//import static com.googlecode.javacv.cpp.opencv_core.cvFlip;
//import static com.googlecode.javacv.cpp.opencv_highgui.cvSaveImage;


//import com.googlecode.javacv.CanvasFrame;
//import com.googlecode.javacv.FrameGrabber;
//import com.googlecode.javacv.FrameGrabber;
//import com.googlecode.javacv.VideoInputFrameGrabber;
//import com.googlecode.javacv.cpp.opencv_core.IplImage;
//import com.googlecode.javacv.CanvasFrame;

//import android.hardware.Camera;
//import android.hardware.Camera.*;
//import android.hardware.Camera.Size;
//import android.hardware.Camera.PictureCallback;

//import android.app.Activity;
import android.app.AlertDialog;
//import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.io.FileOutputStream;
import com.googlecode.javacpp.Loader;
import com.googlecode.javacv.cpp.opencv_objdetect;

import android.graphics.PixelFormat;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_objdetect.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;

import android.util.*;
//import java.io.File;



class myPictureCallback implements PictureCallback {
	public void init(CameraCapture parent){
		this.parent = parent;
		Log.e("CameraCapture", "Init");
	}
	private CameraCapture parent;
	
	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		Log.e("CameraCapture", "Taken");
		try{
			JSONObject retVal = new JSONObject();
			retVal.put("photo", data.toString());
			this.parent.cb.success(retVal);
		}catch(Exception e){
			this.parent.cb.error("CANT_GET_DATA");
		}
	}
}


public class CameraCapture extends CordovaPlugin {
    public static final String TAG = "CameraCapture";
	public static final String ACTION_GET = "get";
	
	public CallbackContext cb;

    public void takePictureNoPreview(Camera myCamera){
		if(myCamera!=null){
			try{
//				Context mCtx = cordova.getActivity().getApplicationContext();
				SurfaceView dummy = new SurfaceView(cordova.getActivity().getApplicationContext());
//				dummy.getHolder()
				SurfaceHolder previewHolder = dummy.getHolder();
				myCamera.setPreviewDisplay(previewHolder);    
				
//				WindowManager wm = (WindowManager) mCtx.getSystemService(Context.WINDOW_SERVICE);
//				WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
//				            WindowManager.LayoutParams.WRAP_CONTENT,
//				            WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
//				            WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
//				            PixelFormat.TRANSLUCENT);        
//				wm.addView(dummy, params);
				
//				myCamera.setPreviewTexture(dummy);
				
//			    previewHolder.addCallback(dummySurfaceCallback);
//			    previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
			    
			    myCamera.startPreview(); 
				myCamera.takePicture(null, this.getJpegCallback(), this.getJpegCallback());
			}catch(Exception e){
				Log.e(TAG, "TakePicture error: " + e.getMessage());
			}finally{
				myCamera.release();
			}      
		}else{
			//booo, failed!
			Log.e(TAG, "NO CAMERA");
		}
	}
	
	
	private PictureCallback getJpegCallback(){
		myPictureCallback jpeg = new myPictureCallback();
		jpeg.init(this);
		return jpeg;
	}


	private Camera cam = null;
	
	private Camera openFrontFacingCamera() {
	   int cameraCount = 0;
	   Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
	   cameraCount = Camera.getNumberOfCameras();
	   for ( int camIdx = 0; camIdx < cameraCount; camIdx++ ) {
	       Camera.getCameraInfo( camIdx, cameraInfo );
	       if ( cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT  ) {
	           try {
	        	   Log.e(TAG, "CamId: " + camIdx);
	               cam = Camera.open( camIdx );
	           } catch (RuntimeException e) {
	               Log.e(TAG, "Camera failed to open: " + e.getLocalizedMessage());
	           }
	       }
	   }
	
	   return cam;
	}
	
	@Override
	public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
		try {
            if (ACTION_GET.equals(action)) {
            	Log.e(TAG, "HERE!!!");
//            	this.cb = callbackContext;
            	cordova.getThreadPool().execute(new Runnable() {
            		public void run() {
            			this.takePictureNoPreview(callbackContext);
            		}
            	});

//                
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
    	this.cam = this.openFrontFacingCamera();
	}

}