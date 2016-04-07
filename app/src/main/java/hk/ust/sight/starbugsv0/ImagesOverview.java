package hk.ust.sight.starbugsv0;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

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
import java.util.Vector;

import org.opencv.core.CvType;

public class ImagesOverview extends AppCompatActivity {

    String[] mCurrentPhotoPath = new String[4];
    int[] mCurrentID = new int[4];

    int i = 0;

//    public void GradedImage(View view) {
//        InputStream is = this.getResources().openRawResource(R.drawable.a);
//        Bitmap y1 = BitmapFactory.decodeStream(is);
//
//        ImageView ivImage = (ImageView) findViewById(R.id.rightDiscAtCenter);
//
//        ivImage.setImageBitmap(y1);
//
//        if (!OpenCVLoader.initDebug()) {
//            Log.d("ERROR", "Unable to load OpenCV");
//        }
//        int scale = 1;
//        int delta = 0;
//        int ddepth = CvType.CV_16S;
//
//        Mat src = new Mat();
//        Mat gray = new Mat();
//        Vector<Mat> channels = new Vector<Mat>(3);
//
//        Core.split(src, channels);
//
//        Utils.bitmapToMat(y1, src);
//
//       Mat red = new Mat();
//
//        Mat green = new Mat();
//
//        Mat blue = new Mat();
//
//        Core.extractChannel(src,red,0);
//
//        Core.extractChannel(src,green,1);
//
//        Core.extractChannel(src,blue,2);
//
//        Imgproc.GaussianBlur(red, red, new Size(3, 3), 0, 0, Core.BORDER_DEFAULT);
//
//       // Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
//
////        Mat grad_x = new Mat();
////        Mat grad_y = new Mat();
////        Mat abs_grad_x = new Mat();
////        Mat abs_grad_y = new Mat();
//////
////       Imgproc.Sobel(red, grad_x, ddepth, 1, 0, 3, 10, 0.018, Core.BORDER_DEFAULT);
////        Core.convertScaleAbs(grad_x, abs_grad_x);
////
////
////        Imgproc.Sobel(red, grad_y, ddepth, 0, 1, 3, 10, 0.018, Core.BORDER_DEFAULT);
////        Core.convertScaleAbs(grad_y, abs_grad_y);
//////
////        Mat grad = new Mat();
////        Core.addWeighted( abs_grad_x, 0.5, abs_grad_y, 0.5, 0, grad );
////
////
////        Core.split(src,channels);
//
//
//
//       // Imgproc.Sobel(footMat, dst, ddepth, dx, dy);
//        Bitmap bm = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888);
//
//        Utils.matToBitmap(red, bm);
//
//
//        ivImage.setImageBitmap(bm);
//
//
//         //Imgproc.Sobel(tmp, dst, ddepth, dx, dy);
//
//
//        //  Bitmap thumbnail = BitmapFactory.decodeFile(mCurrentPhotoPath[0]);
//
//        //  ImageView ivImage = (ImageView) findViewById(R.id.rightDiscAtCenter);
//        //  ivImage.setImageBitmap(thumbnail);
//
//        // Bitmap thumbnail2 = thumbnail.copy(Bitmap.Config.ARGB_8888, true);
//        // Mat tmp =null;
//        // Utils.bitmapToMat(thumbnail2, tmp);
//
//
//        //Mat dst = null;
//        // int ddepth = -1; // destination depth. -1 maintains existing depth from source
//        // int dx = 1;
//        // int dy = 1;
//        // Imgproc.Sobel(tmp, dst, ddepth, dx, dy);
//
//        //  Imgproc.cvtColor(tmp, tmp, Imgproc.COLOR_RGB2GRAY);
//        //Imgproc.cvtColor(tmp, tmp, Imgproc.COLOR_GRAY2RGB, 4);
//        //  Utils.matToBitmap(tmp, thumbnail2);
//
////        Bitmap bmp  = null;
////        Mat tmp = null;
////        try {
////            //Imgproc.cvtColor(seedsImage, tmp, Imgproc.COLOR_RGB2BGRA);
////            Imgproc.cvtColor(dst, tmp, Imgproc.COLOR_GRAY2RGBA, 4);
////            bmp = Bitmap.createBitmap(tmp.cols(), tmp.rows(), Bitmap.Config.ARGB_8888);
////            Utils.matToBitmap(tmp, bmp);
////        }
////        catch (CvException e){}
//        // ivImage.setImageBitmap(thumbnail);
//
//    }

    /**
     * Created by Ilya Gazman on 3/6/2016.
     * http://stackoverflow.com/questions/17674634/saving-and-reading-bitmaps-images-from-internal-memory-in-android
     */
    public class ImageSaver {

        private String directoryName = "images";
        private String fileName = "image.png";
        private Context context;

        public ImageSaver(Context context) {
            this.context = context;
        }

        public ImageSaver setFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public ImageSaver setDirectoryName(String directoryName) {
            this.directoryName = directoryName;
            return this;
        }

        public void save(Bitmap bitmapImage) {
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(createFile());
                bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @NonNull
        private File createFile() {
            File directory = context.getDir(directoryName, Context.MODE_PRIVATE);
            return new File(directory, fileName);
        }

        public Bitmap load() {
            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(createFile());
                return BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_overview);

        findViewById(R.id.getGrade).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ImagesOverview.this, imageprocessing.class));
            }
        });


    }

    // The following code handles select image function.
    // Code taken from http://www.theappguruz.com/blog/android-take-photo-camera-gallery-code-sample
    public static final int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    // Variables to store which button is pressed (and corresponds to which image)
    public int whichButtonID, whichImageID;

    public void selectImage(View view) {
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
                    /*Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");*/
                    /*Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(
                            Intent.createChooser(i, "Select File"),
                            SELECT_FILE);*/
                    // in onCreate or any event where your want the user to
                    // select a file
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,
                            "Select Picture"), SELECT_FILE);
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
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                new ImageSaver(this).
                        setFileName(Long.toString(System.currentTimeMillis())).
                        setDirectoryName("patient").
                        save(thumbnail);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");

                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Handle which imageView will be changed according to which button pressed
                switch(whichButtonID)
                {
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

                ImageView ivImage = (ImageView) findViewById(whichImageID);
                ivImage.setImageBitmap(thumbnail);

            } else if (requestCode == SELECT_FILE) {
                /*Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null,
                        null);
                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();*/

                /*Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

                String selectedImagePath = cursor.getString(columnIndex);*/

                // The following part is taken from http://stackoverflow.com/questions/2169649/get-pick-an-image-from-androids-built-in-gallery-app-programmatically
                Uri selectedImageUri = data.getData();
                String selectedImagePath = getPath(selectedImageUri);

                Bitmap bm;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeFile(selectedImagePath, options);

                // Handle which imageView will be changed according to which button pressed
                switch(whichButtonID)
                {
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

                ImageView ivImage = (ImageView) findViewById(whichImageID);
                ivImage.setImageBitmap(bm);
            }
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
