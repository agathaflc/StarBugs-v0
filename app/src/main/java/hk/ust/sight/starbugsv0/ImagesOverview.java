package hk.ust.sight.starbugsv0;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Vector;

import org.opencv.core.CvType;

public class ImagesOverview extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_overview);

        // This part handles the "Grade" button
        findViewById(R.id.getGrade).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "result saved", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ImagesOverview.this, MainActivity.class));
            }
        });
    }
    // The following code handles select image function.
    // Code taken from http://www.theappguruz.com/blog/android-take-photo-camera-gallery-code-sample
    public static final int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    // Variables to store which button is pressed (and corresponds to which image)
    public static int whichButtonID, whichImageID;

    public void loadPhoto(final View imageView) {

        View tempImageView = imageView;
        String position = null;
        int idbutton = 0;
        switch (tempImageView.getId()) {
            case R.id.leftDiscAtCenter:
                position = "_LC";
                idbutton = R.id.getLeftEyeCenter;
                break;
            case R.id.rightDiscAtCenter:
                position = "_RC";
                idbutton = R.id.getRightEyeCenter;
                break;
            case R.id.leftDiscOnLeft:
                position = "_LL";
                idbutton = R.id.getLeftEyeLeft;
                break;
            case R.id.rightDiscOnLeft:
                position = "_RL";
                idbutton = R.id.getRightEyeLeft;
                break;
        }

        String fileName = String.format("test" + position + ".png");
        if (PatientInfo.patientName != null) {
            fileName = String.format(PatientInfo.patientName + position + ".png");
        }

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // /data/data/hk.ust,sight.starbugsv0/app_images/
        File directory = cw.getDir("images", Context.MODE_PRIVATE);

        File destination = new File(directory, fileName);


        AlertDialog.Builder imageDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);

        View layout = inflater.inflate(R.layout.custom_fullimage_dialog,
                (ViewGroup) findViewById(R.id.layout_root));
        ImageView image = (ImageView) layout.findViewById(R.id.fullimage);


        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(destination.getAbsolutePath(),bmOptions);
       // image.setImageBitmap(((BitmapDrawable)((ImageButton) tempImageView).getDrawable()).getBitmap());
        image.setImageBitmap(bitmap);
        imageDialog.setView(layout);
        imageDialog.setPositiveButton(new String("confirm"), new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        });

        imageDialog.setNegativeButton(new String("change image"), new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int which) {
                switch (imageView.getId()) {
                    case R.id.leftDiscAtCenter:
                        selectImage(findViewById(R.id.getLeftEyeCenter));

                        break;
                    case R.id.rightDiscAtCenter:
                        selectImage(findViewById(R.id.getRightEyeCenter));

                        break;
                    case R.id.leftDiscOnLeft:
                        selectImage(findViewById(R.id.getLeftEyeLeft));

                        break;
                    case R.id.rightDiscOnLeft:
                        selectImage(findViewById(R.id.getRightEyeLeft));

                        break;
                }

                dialog.dismiss();

            }

        });


        imageDialog.create();
        imageDialog.show();
    }

    public void gradeImage(final View view)
    {
        whichButtonID = view.getId();
        whichImageID = R.id.leftDiscAtCenter;

        String position = null;
        switch (whichButtonID) {
            case R.id.getLeftEyeCenter:
                position = "_LC";
                whichImageID = R.id.leftDiscAtCenter;
                break;
            case R.id.getRightEyeCenter:
                position = "_RC";
                whichImageID = R.id.rightDiscAtCenter;
                break;
            case R.id.getLeftEyeLeft:
                position = "_LL";
                whichImageID = R.id.leftDiscOnLeft;
                break;
            case R.id.getRightEyeLeft:
                position = "_RL";
                whichImageID = R.id.rightDiscOnLeft;
                break;
        }

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // data/data/hk.ust,sight.starbugsv0/app_images/
        File directory = cw.getDir("images", Context.MODE_PRIVATE);

        String path =  directory.toString() + "/" + "test" + position+ ".png";
        if(PatientInfo.patientName != null) {
            path = directory.toString() + "/" + PatientInfo.patientName + position + ".png";
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

        Bitmap resized2 = Bitmap.createScaledBitmap(y1,   (int) (y1.getWidth()/3),    (int) (y1.getWidth()/3), true);


        if (!OpenCVLoader.initDebug()) {
            Log.d("ERROR", "Unable to load OpenCV");
        }
        Mat src = new Mat();

        Utils.bitmapToMat(resized2, src);

        Mat red = new Mat();

        Mat green = new Mat();

        Core.extractChannel(src,red,0);

        Core.extractChannel(src,green,1);
        //src = null;

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
        int s = 0;

        if(area_disk < (int) red.rows()*red.cols()/20) {
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


        if(area_disk < (int) red.rows()*red.cols()/20) {
        Core.extractChannel(src, red, 0);
        area_disk = 0;
        s = 2;
        for (int i = 0; i < red.rows(); i++) {
            for (int w = 0; w < red.cols(); w++) {
                if (red.get(i, w)[0] > 170.0) {
                    red.put(i, w, 255);
                    area_disk++;
                } else {
                    red.put(i, w, 0);
                }


            }
        }
    }

        int area_pic = red.rows()*red.cols();


        int area_extrudate = 0;

        Mat green_f = green;

        double threshold = 160.0;
        if(s == 1)
        {
            threshold -=10.0;
        }
        if(s == 2)
        {
            threshold -=20.0;
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


        red = null;

        green = null;

        green_f = null;

        double ranking = 0;
        if(area_disk != 0)
            ranking =  ( (double) area_extrudate/ (double) area_disk) ;



        Log.d("value","ranking:"+ranking);


        Toast.makeText(getApplicationContext(), "result", Toast.LENGTH_SHORT).show();

        double ss = (double) area_disk / area_pic;
        if( ss < 0.05)
        {
            InputStream is = this.getResources().openRawResource(+ R.drawable.ug);

            Bitmap y2 = BitmapFactory.decodeStream(is);
            ImageButton ivImage = (ImageButton) findViewById(whichImageID);
            ivImage.setImageBitmap(y2);

         }
        else if(ranking < 0.25)
        {
            InputStream is = this.getResources().openRawResource(+ R.drawable.y0);

            Bitmap y2 = BitmapFactory.decodeStream(is);
            ImageButton ivImage = (ImageButton) findViewById(whichImageID);
            ivImage.setImageBitmap(y2);
        }
        else if(ranking> 1)
        {
            InputStream is = this.getResources().openRawResource(+ R.drawable.y2);

            Bitmap y2 = BitmapFactory.decodeStream(is);
            ImageButton ivImage = (ImageButton) findViewById(whichImageID);
            ivImage.setImageBitmap(y2);


        }
        else
        {

            InputStream is = this.getResources().openRawResource(+ R.drawable.y1);

            Bitmap y2 = BitmapFactory.decodeStream(is);
            ImageButton ivImage = (ImageButton) findViewById(whichImageID);
            ivImage.setImageBitmap(y2);
        }


    }



    public void selectImage(final View view) {


        whichButtonID = view.getId();

        final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ImagesOverview.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Gallery")) {

                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    // Start the Intent

                    Button bButton = (Button) view;
                    bButton.setText("Grade");
                    bButton.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                           gradeImage(v);
                        }
                    });

                    startActivityForResult(galleryIntent,SELECT_FILE);

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }



            }
        });
        builder.show();
    }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


            // this part is when we use the camera to take picture
        if (resultCode == RESULT_OK) if (requestCode == REQUEST_CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

          //  float ratio = (float) 1280 / (thumbnail.getHeight());

           Bitmap resized = Bitmap.createScaledBitmap(thumbnail, (int) (thumbnail.getWidth()/3), (int) (thumbnail.getHeight()/3), true);

            String position = null;
            switch (whichButtonID) {
                case R.id.getLeftEyeCenter:
                    position = "_LC";
                    break;
                case R.id.getRightEyeCenter:
                    position = "_RC";
                    break;
                case R.id.getLeftEyeLeft:
                    position = "_LL";
                    break;
                case R.id.getRightEyeLeft:
                    position = "_RL";
                    break;
            }
            String fileName = String.format("test" + position + ".png");
            if (PatientInfo.patientName != null) {
                fileName = String.format(PatientInfo.patientName + position + ".png");
            }


            //File destination = new File(Environment.getExternalStorageDirectory(), fileName);
            ContextWrapper cw = new ContextWrapper(getApplicationContext());
             //data/data/hk.ust,sight.starbugsv0/app_images/
           File directory = cw.getDir("images", Context.MODE_PRIVATE);

           File destination = new File(directory, fileName);
            FileOutputStream fo;
            try {

                fo = new FileOutputStream(destination);
                resized.compress(Bitmap.CompressFormat.PNG, 100, fo);
                destination.createNewFile();


                Toast.makeText(getApplicationContext(), destination.toString(), Toast.LENGTH_SHORT).show();

                fo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Handle which imageView will be changed according to which button pressed
            switch (whichButtonID) {
                case R.id.getLeftEyeCenter:
                    whichImageID = R.id.leftDiscAtCenter;
                    break;
                case R.id.getRightEyeCenter:
                    whichImageID = R.id.rightDiscAtCenter;
                    break;
                case R.id.getLeftEyeLeft:
                    whichImageID = R.id.leftDiscOnLeft;
                    break;
                case R.id.getRightEyeLeft:
                    whichImageID = R.id.rightDiscOnLeft;
                    break;
            }

            ImageButton ivImage = (ImageButton) findViewById(whichImageID);
            ivImage.setImageBitmap(resized);


        } else if (requestCode == SELECT_FILE) {
            // The following part is taken from http://stackoverflow.com/questions/2169649/get-pick-an-image-from-androids-built-in-gallery-app-programmatically
            Uri selectedImageUri = data.getData();
            String selectedImagePath = getPath(selectedImageUri);

           // Bitmap bm;

          //  BitmapFactory.Options options = new BitmapFactory.Options();
            BitmapFactory.Options options = new BitmapFactory.Options();
            //options.inJustDecodeBounds = true;
           // BitmapFactory.decodeFile(selectedImagePath, options);
           // final int REQUIRED_SIZE = 200;
       //     int scale = 1;
//            while (options.outWidth / scale / 2 >= REQUIRED_SIZE  && options.outHeight / scale / 2 >= REQUIRED_SIZE)
//                scale *= 2;
         //  options.inSampleSize = scale;
            //options.inJustDecodeBounds = false;
//            bm = BitmapFactory.decodeFile(selectedImagePath, options);

//            options.inSampleSize = 1;
           Bitmap bm2 = BitmapFactory.decodeFile(selectedImagePath,options);
//            float ratio = (float) 1280 / (bm2.getHeight());



            Bitmap resized = Bitmap.createScaledBitmap(bm2,  (int) (bm2.getWidth()/10),  (int) (bm2.getWidth()/10), true);
         //   Bitmap resized2 = Bitmap.createScaledBitmap(bm2,   (int) (bm2.getWidth()/3),    (int) (bm2.getWidth()/3), true);



            String position = null;
            switch (whichButtonID) {
                case R.id.getLeftEyeCenter:
                    position = "_LC";
                    break;
                case R.id.getRightEyeCenter:
                    position = "_RC";
                    break;
                case R.id.getLeftEyeLeft:
                    position = "_LL";
                    break;
                case R.id.getRightEyeLeft:
                    position = "_RL";
                    break;

            }
            String fileName = String.format("test" + position + ".png");
            if (PatientInfo.patientName != null) {
                fileName = String.format(PatientInfo.patientName + position + ".png");
            }

            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            // /data/data/hk.ust,sight.starbugsv0/app_images/
            File directory = cw.getDir("images", Context.MODE_PRIVATE);

            File destination = new File(directory, fileName);

           //  File destination = new File(Environment.getExternalStorageDirectory(), fileName);
            FileOutputStream fo;
            try {
                fo = new FileOutputStream(destination);
                bm2.compress(Bitmap.CompressFormat.PNG, 100, fo);
                destination.createNewFile();

                Toast.makeText(getApplicationContext(), "Image saved", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), destination.toString(), Toast.LENGTH_SHORT).show();

                fo.close();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Settings not saved", Toast.LENGTH_SHORT).show();
            }

            // Handle which imageView will be changed according to which button pressed
            switch (whichButtonID) {
                case R.id.getLeftEyeCenter:
                    whichImageID = R.id.leftDiscAtCenter;
                    break;
                case R.id.getRightEyeCenter:
                    whichImageID = R.id.rightDiscAtCenter;
                    break;
                case R.id.getLeftEyeLeft:
                    whichImageID = R.id.leftDiscOnLeft;
                    break;
                case R.id.getRightEyeLeft:
                    whichImageID = R.id.rightDiscOnLeft;
                    break;
            }

            ImageButton ivImage = (ImageButton) findViewById(whichImageID);
            ivImage.setImageBitmap(resized);

        }


    }

    /**
     * helper to retrieve the path of an image URI
     */
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
}
