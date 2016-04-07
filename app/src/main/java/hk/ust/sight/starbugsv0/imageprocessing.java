package hk.ust.sight.starbugsv0;

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

import java.io.InputStream;
import java.util.Vector;

/**
 * Created by Benlai on 7/4/2016.
 */
public class imageprocessing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Error","fuck");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageprocessingtest);


    }

    public void GradedImage2(View view) {
        InputStream is = this.getResources().openRawResource(R.drawable.a);
        Bitmap y1 = BitmapFactory.decodeStream(is);

        ImageView redImage = (ImageView) findViewById(R.id.red);

        ImageView blueImage = (ImageView) findViewById(R.id.blue);

        ImageView greenImage = (ImageView) findViewById(R.id.green);

        ImageView greyImage = (ImageView) findViewById(R.id.grey);

        ImageView oriImage = (ImageView) findViewById(R.id.ori);

        ImageView e1Image = (ImageView) findViewById(R.id.edge1);

        ImageView e2Image = (ImageView) findViewById(R.id.edge2);

        ImageView f1Image = (ImageView) findViewById(R.id.filter1);

        ImageView f2Image = (ImageView) findViewById(R.id.filter2);

        oriImage.setImageBitmap(y1);

        if (!OpenCVLoader.initDebug()) {
            Log.d("ERROR", "Unable to load OpenCV");
        }
        int scale = 1;
        int delta = 0;
        int ddepth = CvType.CV_16S;

        Mat src = new Mat();
        Mat gray = new Mat();
        Vector<Mat> channels = new Vector<Mat>(3);

        Core.split(src, channels);

        Utils.bitmapToMat(y1, src);

        Mat red = new Mat();

        Mat green = new Mat();

        Core.extractChannel(src,red,0);

        Core.extractChannel(src,green,1);

     //   Core.extractChannel(src,blue,1);


        Bitmap bmred   = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(red, bmred);

      //  Bitmap bmblue  = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888);
      //  Utils.matToBitmap(blue, bmblue);

        Bitmap bmgreen = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(green, bmgreen);

        redImage.setImageBitmap(bmred);
        greenImage.setImageBitmap(bmgreen);
       // blueImage.setImageBitmap(bmgreen);

        Imgproc.GaussianBlur(src, src, new Size(3, 3), 0, 0, Core.BORDER_DEFAULT);

         Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);

        Bitmap bmgrey = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(gray, bmgrey);

        blueImage.setImageBitmap(bmgrey);
       // greyImage.setImageBitmap(bmgrey);

        Mat grad_x = new Mat();
        Mat grad_y = new Mat();
        Mat abs_grad_x = new Mat();
        Mat abs_grad_y = new Mat();

        Imgproc.Sobel(red, grad_x, ddepth, 1, 0, 3, 5, 0, Core.BORDER_DEFAULT);
        Core.convertScaleAbs(grad_x, abs_grad_x);



        Imgproc.Sobel(red, grad_y, ddepth, 0, 1, 3, 5, 0, Core.BORDER_DEFAULT);
        Core.convertScaleAbs(grad_y, abs_grad_y);


        Mat grad = new Mat();
        Core.addWeighted( abs_grad_x, 0.5, abs_grad_y, 0.5, 0, grad );


        Bitmap bmedge = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888);

        Utils.matToBitmap(grad, bmedge);

        greyImage.setImageBitmap(bmedge);




    }
}
