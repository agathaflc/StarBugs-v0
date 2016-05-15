package hk.ust.sight.starbugsv0;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Vector;

/**
 * Created by Benlai on 7/4/2016.
 */
public class imageprocessing extends AppCompatActivity {

    double ranking = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Error","fuck");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageprocessingtest);


    }

    public void Result(View view) {



        if(ranking > 1)
        {
            startActivity(new Intent(imageprocessing.this, resultsY2.class));

        }
        else if( ranking  >  0.25)
        {
            startActivity(new Intent(imageprocessing.this, resultsY1.class));
        }
        else
        {
            startActivity(new Intent(imageprocessing.this, resultsY0.class));
        }


    }

    public void GradedImagea2(View view) {
        InputStream is = this.getResources().openRawResource(+ R.drawable.a2);

        Bitmap y1 = BitmapFactory.decodeStream(is);

        ImageView redImage = (ImageView) findViewById(R.id.red);

       // ImageView blueImage = (ImageView) findViewById(R.id.blue);

        ImageView greenImage = (ImageView) findViewById(R.id.green);

        ImageView greyImage = (ImageView) findViewById(R.id.grey);

        ImageView oriImage = (ImageView) findViewById(R.id.ori);

        ImageView e1Image = (ImageView) findViewById(R.id.edge1);

     //   ImageView e2Image = (ImageView) findViewById(R.id.edge2);

      //  ImageView f1Image = (ImageView) findViewById(R.id.filter1);

      //  ImageView f2Image = (ImageView) findViewById(R.id.filter2);

        oriImage.setImageBitmap(y1);

        if (!OpenCVLoader.initDebug()) {
            Log.d("ERROR", "Unable to load OpenCV");
        }
       // int scale = 1;
       // int delta = 0;
       // int ddepth = CvType.CV_16S;

        Mat src = new Mat();


        Utils.bitmapToMat(y1, src);

        Mat red = new Mat();

        Mat green = new Mat();

        Core.extractChannel(src,red,0);

        Core.extractChannel(src,green,1);

     //   Core.extractChannel(src,blue,1);

        src = null;

        Bitmap bmred   = Bitmap.createBitmap(red.cols(), red.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(red, bmred);

      //  Bitmap bmblue  = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888);
      //  Utils.matToBitmap(blue, bmblue);

        Bitmap bmgreen = Bitmap.createBitmap(green.cols(), green.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(green, bmgreen);

        redImage.setImageBitmap(bmred);
        greenImage.setImageBitmap(bmgreen);
       // blueImage.setImageBitmap(bmgreen);

      //  Imgproc.GaussianBlur(src, src, new Size(3, 3), 0, 0, Core.BORDER_DEFAULT);

     //    Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);

//        Bitmap bmgrey = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888);
//        Utils.matToBitmap(gray, bmgrey);
//
//        blueImage.setImageBitmap(bmgrey);
       // greyImage.setImageBitmap(bmgrey);
//
//        Mat grad_x = new Mat();
//        Mat grad_y = new Mat();
//        Mat abs_grad_x = new Mat();
//        Mat abs_grad_y = new Mat();

       // Imgproc.Sobel(red, grad_x, ddepth, 1, 0, 3, 2, 0, Core.BORDER_DEFAULT);
       // Core.convertScaleAbs(grad_x, abs_grad_x);



       // Imgproc.Sobel(red, grad_y, ddepth, 0, 1, 3, 2, 0, Core.BORDER_DEFAULT);
       // Core.convertScaleAbs(grad_y, abs_grad_y);



      //  Core.addWeighted(abs_grad_x, 1, abs_grad_y, 1, 0, red);

        int area_disk = 0;

        for(int i = 0; i<red.rows(); i++)
        {
            for(int w =0; w<red.cols(); w++)
            {
                if( red.get(i,w)[0] > 240.0  )
                {
                    red.put(i,w,255);
                    area_disk++;
                }
                else
                {
                    red.put(i,w,0);
                }


            }
        }


        Bitmap bmedge = Bitmap.createBitmap(red.cols(), red.rows(), Bitmap.Config.ARGB_8888);

        Utils.matToBitmap(red, bmedge);
        greyImage.setImageBitmap(bmedge);



        int area_extrudate = 0;

        Mat green_f = green;

        for(int i = 0; i<green.rows(); i++)
        {
            for(int w =0; w<green.cols(); w++)
            {
                if( green.get(i,w)[0] < 140.0 )
                {
                    green_f.put(i,w,0);

                }
                else
                {
                    green_f.put(i,w,255);
                    area_extrudate++;
                    if(red.get(i,w)[0] == 255)
                    {
                        area_extrudate--;
                        green_f.put(i,w,0);

                    }
                }


            }
        }

        Bitmap bmgf = Bitmap.createBitmap(green_f.cols(), green_f.rows(), Bitmap.Config.ARGB_8888);

        Utils.matToBitmap(green_f, bmgf);
        e1Image.setImageBitmap(bmgf);

        red = null;

        green = null;

        green_f = null;

        ranking =  ( (double) area_extrudate/ (double) area_disk) ;

        Log.d("value","ranking:"+ranking);




    }

    public void GradedImageb2(View view) {
        InputStream is = this.getResources().openRawResource(+ R.drawable.b2);

        Bitmap y1 = BitmapFactory.decodeStream(is);

        ImageView redImage = (ImageView) findViewById(R.id.red);

        // ImageView blueImage = (ImageView) findViewById(R.id.blue);

        ImageView greenImage = (ImageView) findViewById(R.id.green);

        ImageView greyImage = (ImageView) findViewById(R.id.grey);

        ImageView oriImage = (ImageView) findViewById(R.id.ori);

        ImageView e1Image = (ImageView) findViewById(R.id.edge1);

        //   ImageView e2Image = (ImageView) findViewById(R.id.edge2);

        //  ImageView f1Image = (ImageView) findViewById(R.id.filter1);

        //  ImageView f2Image = (ImageView) findViewById(R.id.filter2);

        oriImage.setImageBitmap(y1);

        if (!OpenCVLoader.initDebug()) {
            Log.d("ERROR", "Unable to load OpenCV");
        }
        // int scale = 1;
        // int delta = 0;
        // int ddepth = CvType.CV_16S;

        Mat src = new Mat();


        Utils.bitmapToMat(y1, src);

        Mat red = new Mat();

        Mat green = new Mat();

        Core.extractChannel(src,red,0);

        Core.extractChannel(src,green,1);

        //   Core.extractChannel(src,blue,1);

        src = null;

        Bitmap bmred   = Bitmap.createBitmap(red.cols(), red.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(red, bmred);

        //  Bitmap bmblue  = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888);
        //  Utils.matToBitmap(blue, bmblue);

        Bitmap bmgreen = Bitmap.createBitmap(green.cols(), green.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(green, bmgreen);

        redImage.setImageBitmap(bmred);
        greenImage.setImageBitmap(bmgreen);
        // blueImage.setImageBitmap(bmgreen);

        //  Imgproc.GaussianBlur(src, src, new Size(3, 3), 0, 0, Core.BORDER_DEFAULT);

        //    Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);

//        Bitmap bmgrey = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888);
//        Utils.matToBitmap(gray, bmgrey);
//
//        blueImage.setImageBitmap(bmgrey);
        // greyImage.setImageBitmap(bmgrey);
//
//        Mat grad_x = new Mat();
//        Mat grad_y = new Mat();
//        Mat abs_grad_x = new Mat();
//        Mat abs_grad_y = new Mat();

        // Imgproc.Sobel(red, grad_x, ddepth, 1, 0, 3, 2, 0, Core.BORDER_DEFAULT);
        // Core.convertScaleAbs(grad_x, abs_grad_x);



        // Imgproc.Sobel(red, grad_y, ddepth, 0, 1, 3, 2, 0, Core.BORDER_DEFAULT);
        // Core.convertScaleAbs(grad_y, abs_grad_y);



        //  Core.addWeighted(abs_grad_x, 1, abs_grad_y, 1, 0, red);

        int area_disk = 0;

        for(int i = 0; i<red.rows(); i++)
        {
            for(int w =0; w<red.cols(); w++)
            {
                if( red.get(i,w)[0] > 220.0  )
                {
                    red.put(i,w,255);
                    area_disk++;
                }
                else
                {
                    red.put(i,w,0);
                }


            }
        }


        Bitmap bmedge = Bitmap.createBitmap(red.cols(), red.rows(), Bitmap.Config.ARGB_8888);

        Utils.matToBitmap(red, bmedge);
        greyImage.setImageBitmap(bmedge);



        int area_extrudate = 0;

        Mat green_f = green;

        for(int i = 0; i<green.rows(); i++)
        {
            for(int w =0; w<green.cols(); w++)
            {
                if( green.get(i,w)[0] < 160.0 )
                {
                    green_f.put(i,w,0);

                }
                else
                {
                    green_f.put(i,w,255);
                    area_extrudate++;
                    if(red.get(i,w)[0] == 255)
                    {
                        area_extrudate--;
                        green_f.put(i,w,0);

                    }
                }


            }
        }

        Bitmap bmgf = Bitmap.createBitmap(green_f.cols(), green_f.rows(), Bitmap.Config.ARGB_8888);

        Utils.matToBitmap(green_f, bmgf);
        e1Image.setImageBitmap(bmgf);

        red = null;

        green = null;

        green_f = null;

        ranking =  ( (double) area_extrudate/ (double) area_disk) ;

        Log.d("value","ranking:"+ranking);




    }


    public void GradedImagec2(View view) {

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // data/data/hk.ust,sight.starbugsv0/app_images/
        File directory = cw.getDir("images", Context.MODE_PRIVATE);

        String path =  directory.toString() + "/" + "test" + "_LC" + ".png";
        if(PatientInfo.patientName != null) {
            path = directory.toString() + "/" + PatientInfo.patientName + "_LC" + ".png";
        }
        Bitmap y1 = null;

        try {
            File f=new File(path);
            y1 = BitmapFactory.decodeStream(new FileInputStream(f));

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }



        //InputStream is = this.getResources().openRawResource(R.drawable.c2);


        ImageView redImage = (ImageView) findViewById(R.id.red);

        // ImageView blueImage = (ImageView) findViewById(R.id.blue);

        ImageView greenImage = (ImageView) findViewById(R.id.green);

        ImageView greyImage = (ImageView) findViewById(R.id.grey);

        ImageView oriImage = (ImageView) findViewById(R.id.ori);

        ImageView e1Image = (ImageView) findViewById(R.id.edge1);

        //   ImageView e2Image = (ImageView) findViewById(R.id.edge2);

        //  ImageView f1Image = (ImageView) findViewById(R.id.filter1);

        //  ImageView f2Image = (ImageView) findViewById(R.id.filter2);

        oriImage.setImageBitmap(y1);

        if (!OpenCVLoader.initDebug()) {
            Log.d("ERROR", "Unable to load OpenCV");
        }
        // int scale = 1;
        // int delta = 0;
        // int ddepth = CvType.CV_16S;

        Mat src = new Mat();


        Utils.bitmapToMat(y1, src);

        Mat red = new Mat();

        Mat green = new Mat();

        Core.extractChannel(src,red,0);

        Core.extractChannel(src,green,1);

        //   Core.extractChannel(src,blue,1);

        src = null;

        Bitmap bmred   = Bitmap.createBitmap(red.cols(), red.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(red, bmred);

        //  Bitmap bmblue  = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888);
        //  Utils.matToBitmap(blue, bmblue);

        Bitmap bmgreen = Bitmap.createBitmap(green.cols(), green.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(green, bmgreen);

        redImage.setImageBitmap(bmred);
        greenImage.setImageBitmap(bmgreen);
        // blueImage.setImageBitmap(bmgreen);

        //  Imgproc.GaussianBlur(src, src, new Size(3, 3), 0, 0, Core.BORDER_DEFAULT);

        //    Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);

//        Bitmap bmgrey = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888);
//        Utils.matToBitmap(gray, bmgrey);
//
//        blueImage.setImageBitmap(bmgrey);
        // greyImage.setImageBitmap(bmgrey);
//
//        Mat grad_x = new Mat();
//        Mat grad_y = new Mat();
//        Mat abs_grad_x = new Mat();
//        Mat abs_grad_y = new Mat();

        // Imgproc.Sobel(red, grad_x, ddepth, 1, 0, 3, 2, 0, Core.BORDER_DEFAULT);
        // Core.convertScaleAbs(grad_x, abs_grad_x);



        // Imgproc.Sobel(red, grad_y, ddepth, 0, 1, 3, 2, 0, Core.BORDER_DEFAULT);
        // Core.convertScaleAbs(grad_y, abs_grad_y);



        //  Core.addWeighted(abs_grad_x, 1, abs_grad_y, 1, 0, red);

        int area_disk = 0;

        for(int i = 0; i<red.rows(); i++)
        {
            for(int w =0; w<red.cols(); w++)
            {
                if( red.get(i,w)[0] > 240.0  )
                {
                    red.put(i,w,255);
                    area_disk++;
                }
                else
                {
                    red.put(i,w,0);
                }


            }
        }


        Bitmap bmedge = Bitmap.createBitmap(red.cols(), red.rows(), Bitmap.Config.ARGB_8888);

        Utils.matToBitmap(red, bmedge);
        greyImage.setImageBitmap(bmedge);



        int area_extrudate = 0;

        Mat green_f = green;

        for(int i = 0; i<green.rows(); i++)
        {
            for(int w =0; w<green.cols(); w++)
            {
                if( green.get(i,w)[0] < 160.0 )
                {
                    green_f.put(i,w,0);

                }
                else
                {
                    green_f.put(i,w,255);
                    area_extrudate++;
                    if(red.get(i,w)[0] == 255)
                    {
                        area_extrudate--;
                        green_f.put(i,w,0);

                    }
                }


            }
        }

        Bitmap bmgf = Bitmap.createBitmap(green_f.cols(), green_f.rows(), Bitmap.Config.ARGB_8888);

        Utils.matToBitmap(green_f, bmgf);
        e1Image.setImageBitmap(bmgf);

        red = null;

        green = null;

        green_f = null;

        ranking =  ( (double) area_extrudate/ (double) area_disk) ;

        Log.d("value","ranking:"+ranking);




    }



}


