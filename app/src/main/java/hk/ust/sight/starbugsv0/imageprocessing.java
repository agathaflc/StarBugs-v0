package hk.ust.sight.starbugsv0;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import static hk.ust.sight.starbugsv0.ImagesOverview.REQUEST_CAMERA;
import static hk.ust.sight.starbugsv0.ImagesOverview.SELECT_FILE;

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

    public void selectImage(final View view) {

        final CharSequence[] items = {"Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(imageprocessing.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                    if (items[item].equals("Choose from Gallery")) {

                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        Button bButton = (Button) view;
                        bButton.setText("Grade");
                        bButton.setOnClickListener(new View.OnClickListener() {

                            public void onClick(View v) {
                                GradedImagec2(v);
                            }
                        });
                    // Start the Intent
                    startActivityForResult(galleryIntent,SELECT_FILE);

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }


            }
        });
        builder.show();
    }

    public String getPath(Uri uri) {
        // just some safety built in
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


             if (requestCode == SELECT_FILE) {
            // The following part is taken from http://stackoverflow.com/questions/2169649/get-pick-an-image-from-androids-built-in-gallery-app-programmatically
            Uri selectedImageUri = data.getData();
            String selectedImagePath = getPath(selectedImageUri);

            BitmapFactory.Options options = new BitmapFactory.Options();

            Bitmap bm2 = BitmapFactory.decodeFile(selectedImagePath,options);


                 Bitmap resized = Bitmap.createScaledBitmap(bm2, (int) (bm2.getWidth()/3),(int) (bm2.getHeight()/3) , true);


            //Bitmap resized = Bitmap.createScaledBitmap(bm2, (int) 300, 300, true);


            String fileName = String.format("test1" + ".png");

            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            // /data/data/hk.ust,sight.starbugsv0/app_images/
            File directory = cw.getDir("images", Context.MODE_PRIVATE);

            File destination = new File(directory, fileName);

            //  File destination = new File(Environment.getExternalStorageDirectory(), fileName);
            FileOutputStream fo;
            try {
                fo = new FileOutputStream(destination);
                resized.compress(Bitmap.CompressFormat.PNG, 100, fo);
                destination.createNewFile();

                Toast.makeText(getApplicationContext(), "Image saved", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), destination.toString(), Toast.LENGTH_SHORT).show();

                fo.close();

                ImageView oriImage = (ImageView) findViewById(R.id.ori);
                oriImage.setImageBitmap(resized);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Settings not saved", Toast.LENGTH_SHORT).show();
            }

        }


    }

    public void GradedImagec2(View view) {

        EditText t2 = (EditText) findViewById(R.id.editText22);
        int disk_t = Integer.parseInt(t2.getText().toString());

        EditText t1 = (EditText) findViewById(R.id.editText11);

        int ex_t = Integer.parseInt(t1.getText().toString());


        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // data/data/hk.ust,sight.starbugsv0/app_images/
        File directory = cw.getDir("images", Context.MODE_PRIVATE);

        String path =  directory.toString() + "/" + "test1" + ".png";

        Bitmap y1 = null;

        try {
            File f=new File(path);
            y1 = BitmapFactory.decodeStream(new FileInputStream(f));

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        Toast.makeText(getApplicationContext(), "get Image", Toast.LENGTH_SHORT).show();

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

       // src = null;

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

        if(disk_t > 10) {
            for (int i = 0; i < red.rows(); i++) {
                for (int w = 0; w < red.cols(); w++) {
                    if (red.get(i, w)[0] > disk_t) {
                        red.put(i, w, 255);
                        area_disk++;
                    } else {
                        red.put(i, w, 0);
                    }


                }
            }
        }
        else
        {
            for (int i = 0; i < red.rows(); i++) {
                for (int w = 0; w < red.cols(); w++) {
                    if (red.get(i, w)[0] > 230) {
                        red.put(i, w, 255);
                        area_disk++;
                    } else {
                        red.put(i, w, 0);
                    }


                }
            }

        }
        int zz = 0;

        if(area_disk > (int) red.rows()*red.cols()/8 && disk_t == 0) {
            Core.extractChannel(src, red, 0);
            area_disk = 0;
            zz = 1;
            for (int i = 0; i < red.rows(); i++) {
                Core.extractChannel(src, red, 0);
                area_disk = 0;

                for (int w = 0; w < red.cols(); w++) {
                    if (red.get(i, w)[0] > 240.0) {
                        red.put(i, w, 255);
                        area_disk++;
                    } else {
                        red.put(i, w, 0);
                    }


                }
            }
        }

        int s = 0;
        if(area_disk < (int) red.rows()*red.cols()/20 && disk_t == 0) {
            Core.extractChannel(src, red, 0);
            area_disk = 0;
            s = 1;
            for (int i = 0; i < red.rows(); i++) {
                for (int w = 0; w < red.cols(); w++) {
                    if (red.get(i, w)[0] > 200.0) {
                        red.put(i, w, 255);
                        area_disk++;
                    } else {
                        red.put(i, w, 0);
                    }


                }
            }
        }
        if(area_disk < (int) red.rows()*red.cols()/20 && disk_t == 0)
        {
            s= 2;
            Core.extractChannel(src,red,0);
            area_disk = 0;
            for(int i = 0; i<red.rows(); i++)
            {
                for(int w =0; w<red.cols(); w++)
                {
                    if( red.get(i,w)[0] > 170.0  )
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



        }


        Bitmap bmedge = Bitmap.createBitmap(red.cols(), red.rows(), Bitmap.Config.ARGB_8888);

        Utils.matToBitmap(red, bmedge);
        greyImage.setImageBitmap(bmedge);

        Toast.makeText(getApplicationContext(), "Disk", Toast.LENGTH_SHORT).show();


        int area_extrudate = 0;

        Mat green_f = green;

        double threshold = 160.0;
        if(s ==1 && ex_t == 0)
        {
            threshold -= 10.0;
        }
        if(s ==2 && ex_t == 0)
        {
            threshold -= 20.0;
        }
        if(zz == 1 && ex_t == 0)
        {
            threshold +=10.0;

        }
        if( ex_t != 0)
        {
            threshold = ex_t;
        }
        for(int i = 0; i<green.rows(); i++)
        {

            for(int w =0; w<green.cols(); w++)
            {
                if( green.get(i,w)[0] < threshold )
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

        Toast.makeText(getApplicationContext(), "exudates", Toast.LENGTH_SHORT).show();
        red = null;

        green = null;

        green_f = null;

        if(area_disk != 0)
        ranking =  ( (double) area_extrudate/ (double) area_disk) ;


        else ranking = 0;
        Log.d("value","ranking:"+ranking);


        Toast.makeText(getApplicationContext(), "ranking:"+ranking , Toast.LENGTH_SHORT).show();



        Button bButton = (Button) view;
        bButton.setText("Image");
        bButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                selectImage(v);
            }
        });




    }



}


