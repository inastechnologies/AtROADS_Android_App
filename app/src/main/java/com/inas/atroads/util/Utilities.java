package com.inas.atroads.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.inas.atroads.R;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class Utilities {

    private static String TAG = "Utilities";
    /**
     *
     * @param encodedImage
     * @return
     */
    public static Bitmap Base64ToBitmap(String encodedImage)
    {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    /**
     *
     * @param bitmap
     * @return
     */
    public static String BitmapToBase64(Bitmap bitmap)
    {
        int compressQuality = 100;// quality decreasing by 5 every loop.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,compressQuality,baos);
        byte[] b = baos.toByteArray();
        int streamLength = b.length;
        compressQuality -= 5;
        Log.d("compressBitmap", "Size: " + streamLength/1024+" kb");
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encImage;
    }

    /**
     *
     * @param context
     * @param bitmap
     * @return
     */
    public static Drawable BitmapToDrawable(Context context,Bitmap bitmap)
    {
        Drawable d = new BitmapDrawable(context.getResources(), bitmap);
        return d;
    }

    /**
     *
     * @param context
     * @param drawable
     * @return
     */
    public static Bitmap DrawableToBitmap(Context context, int drawable)
    {
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),drawable);

        return icon;
    }



    public static Bitmap ConvertURLImageToBitmap(String Url)
    {
        Bitmap image = null;
        try {
            URL url = new URL(Url);
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch(IOException e) {
            System.out.println(e);
        }
        return image;
    }
    /**
     *
     * @param view
     * @return
     */
    public static Bitmap imageViewToBitmap(ImageView view)
    {
        Bitmap bitmap = ((BitmapDrawable)view.getDrawable()).getBitmap();
        return bitmap;
    }


    public static void WriteLog(String fileName)
    {
        File outputFile = new File(Environment.DIRECTORY_DOCUMENTS+"Atroads/"+fileName+".txt");
        try {
            Runtime.getRuntime().exec("logcat -c");
            Runtime.getRuntime().exec("logcat -v time -f " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void WriteLogsToStorage(String FileName,String text)
    {
        File logFile = new File(Environment.DIRECTORY_DOCUMENTS+"Atroads/FileName.file");
        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try
        {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    public static boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if ((connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED)
                || (connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null && connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState() == NetworkInfo.State.CONNECTED))
        {
           // Toast.makeText(ctx, "Internet is connected", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "isNetworkAvailable: Internet is connected");
            return true;
        } else {
            Toast.makeText(ctx, "Internet is not connected", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    /******************************START OF GENERATE ORDER ID*******************************/
    public static String GenerateOrderID()
    {
        // create instance of Random class
        Random rand = new Random();

        // Generate random integers in range 0 to 9999
        int id = rand.nextInt(10000);
        // int rand_int2 = rand.nextInt(100000);

        String OrderId = "ORDER"+id;
        // Print random integers
        System.out.println("Random OrderID: "+OrderId);
        //  System.out.println("Random Integers: "+rand_int2);
        return OrderId;
    }

    /******************************END OF GENERATE ORDER ID*******************************/


    public static String GetCurrentDateTime()
    {
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        String dateToStr = format.format(today);
        System.out.println(dateToStr);
        return  dateToStr;
    }


    public static void hideKeyboard(View view,Context ctx) {
        try {
            InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch(Exception ignored) {
        }
    }

    public static String getCurrentDate()
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        return df.format(calendar.getTime());

    }
}
