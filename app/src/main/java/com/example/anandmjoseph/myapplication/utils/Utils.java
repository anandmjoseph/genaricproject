package com.example.anandmjoseph.myapplication.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.example.anandmjoseph.myapplication.R;
import com.example.anandmjoseph.myapplication.app.AppConstants;
import com.example.anandmjoseph.myapplication.app.AppPreferences;

/**
 * Created by neva coders on 18/11/16.
 */

public class Utils {


    public static final String TAG = "Utils";
    private static final int MAX_IMAGE_DIMENSION = 150;

    private static ProgressDialog mPgDialog;
    private static Handler mPgBarHandler = new Handler();

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * Method to show progress bar
     *
     * @param context
     * @param message
     * @param listener
     */
    public static void showProgressBar(final Context context, final String message, final DialogInterface.OnCancelListener listener) {

        try {
            mPgBarHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mPgDialog != null) {
                        mPgDialog.dismiss();
                    }
                    mPgDialog = new ProgressDialog(context);
                    mPgDialog.setIndeterminate(true);
                    // mPgDialog.setMessage(message);
                    mPgDialog.setCanceledOnTouchOutside(false);
                    mPgDialog.setCancelable(true);
                    if (null != listener) {
                        mPgDialog.setOnCancelListener(listener);
                    }
                    mPgDialog.show();
                }
            });
        } catch (Exception e) {
        }
    }

    /**
     * Method to show progressbar
     *
     * @param context
     * @param message
     */
    public static void showProgressBar(final Context context, final String message) {
        if (context != null) {
            if (mPgDialog != null && mPgDialog.isShowing()) {
                return;
            }
            try {
                mPgBarHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mPgDialog != null) {
                            mPgDialog.dismiss();
                        }
                        mPgDialog = new ProgressDialog(context);
                        mPgDialog.setIndeterminate(true);
                        // mPgDialog.setMessage(message);
                        mPgDialog.setCanceledOnTouchOutside(false);
                        mPgDialog.setCancelable(false);
                        mPgDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        mPgDialog.show();
                        mPgDialog.setContentView(R.layout.layout_progressbar);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method to show progressbar
     *
     * @param context
     * @param message
     */
    public static void showProgressBar(final Context context, final int message) {

        if (mPgDialog != null && mPgDialog.isShowing()) {
            return;
        }
        try {
            mPgBarHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mPgDialog != null) {
                        mPgDialog.dismiss();
                    }
                    mPgDialog = new ProgressDialog(context);

                    mPgDialog.setIndeterminate(true);
                    mPgDialog.setMessage(context.getString(message));
                    mPgDialog.setCanceledOnTouchOutside(false);
                    mPgDialog.setCancelable(false);
                    mPgDialog.show();
                }
            });
        } catch (Exception e) {
        }
    }

    /**
     * dismissing the ProgressBar
     */
    public static void dismissProgressBar() {
        if (null != mPgDialog) {
            mPgBarHandler.post(new Runnable() {

                @Override
                public void run() {
                    // System.out.println("Is showing ===>>" + mPgDialog.isShowing());
                    // mPgDialog.dismiss();

                    try {
                        if ((mPgDialog != null) && mPgDialog.isShowing()) {
                            mPgDialog.dismiss();
                        }
                    } catch (final IllegalArgumentException e) {
                        // Handle or log or ignore
                    } catch (final Exception e) {
                        // Handle or log or ignore
                    } finally {
                        mPgDialog = null;
                    }
                }
            });
        }
    }

    /**
     * method to check the email is vali or not
     *
     * @param target
     * @return email valid status
     */
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    /**
     * Method to encode bitmap to base64 string
     *
     * @param image
     * @return base64 string
     */
    public static String encodeTobase64(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    /**
     * Method used for encode the file to base64 binary format
     *
     * @param file
     * @return encoded file format
     */
    public static String encodeFileToBase64Binary(File file) throws IOException {
        String encodedfile = null;
        byte[] bytes = loadFile(file);
        encodedfile = Base64.encodeToString(bytes, Base64.DEFAULT);

        return encodedfile;
    }

    /**
     * Method to load file
     *
     * @param file
     * @return
     * @throws IOException
     */
    private static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes;
    }

    /**
     * Decode bit map from a file.
     *
     * @param activity
     * @param uri
     * @return
     */
    public static Bitmap decodeSampledBitmapFromFile(Activity activity, Uri uri) throws IOException {

        // Uri uri = Uri.fromFile(new File(path));
        InputStream in;

        Matrix matrix = new Matrix();
        uri.getPath().toString();
        ExifInterface exifReader = new ExifInterface(uri.getPath().toString());
        int orientation = exifReader.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);

        if (orientation == ExifInterface.ORIENTATION_NORMAL) {

            // Do nothing. The original image is fine.

        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {

            matrix.postRotate(90);

        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {

            matrix.postRotate(180);

        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {

            matrix.postRotate(270);

        }

        ContentResolver contentResolver = activity.getContentResolver();
        try {
            final int IMAGE_MAX_SIZE = 1200000; // 1.2MP
            in = contentResolver.openInputStream(uri);

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);
            in.close();

            int scale = 1;
            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) > IMAGE_MAX_SIZE) {
                scale++;
            }

            Bitmap b;
            in = contentResolver.openInputStream(uri);
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image larger than target
                o = new BitmapFactory.Options();
                o.inSampleSize = scale;
                b = BitmapFactory.decodeStream(in, null, o);

                // resize to desired dimensions
                int height = b.getHeight();
                int width = b.getWidth();

                double y = Math.sqrt(IMAGE_MAX_SIZE / (((double) width) / height));
                double x = (y / height) * width;
                b = Bitmap.createBitmap(b, 0, 0, width, height, matrix, true);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x, (int) y, true);
                b.recycle();
                b = scaledBitmap;

            } else {
                b = BitmapFactory.decodeStream(in);
            }
            in.close();

            return b;
        } catch (IOException e) {

        }
        return null;
    }


    public static int getOrientation(Context context, Uri photoUri) {
    /* it's on the external media. */
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[] { MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);

        if (cursor.getCount() != 1) {
            return -1;
        }

        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public static Bitmap getCorrectlyOrientedImage(Context context, Uri photoUri) throws IOException {
        InputStream is = context.getContentResolver().openInputStream(photoUri);
        BitmapFactory.Options dbo = new BitmapFactory.Options();
        dbo.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, dbo);
        is.close();

        int rotatedWidth, rotatedHeight;
        int orientation = getOrientation(context, photoUri);

        if (orientation == 90 || orientation == 270) {
            rotatedWidth = dbo.outHeight;
            rotatedHeight = dbo.outWidth;
        } else {
            rotatedWidth = dbo.outWidth;
            rotatedHeight = dbo.outHeight;
        }

        Bitmap srcBitmap;
        is = context.getContentResolver().openInputStream(photoUri);
        if (rotatedWidth > MAX_IMAGE_DIMENSION || rotatedHeight > MAX_IMAGE_DIMENSION) {
            float widthRatio = ((float) rotatedWidth) / ((float) MAX_IMAGE_DIMENSION);
            float heightRatio = ((float) rotatedHeight) / ((float) MAX_IMAGE_DIMENSION);
            float maxRatio = Math.max(widthRatio, heightRatio);

            // Create the bitmap from file
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = (int) maxRatio;
            srcBitmap = BitmapFactory.decodeStream(is, null, options);
        } else {
            srcBitmap = BitmapFactory.decodeStream(is);
        }
        is.close();

    /*
     * if the orientation is not 0 (or -1, which means we don't know), we
     * have to do a rotation.
     */
        if (orientation > 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);

            srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(),
                    srcBitmap.getHeight(), matrix, true);
        }

        return srcBitmap;
    }


    /**
     * Gets the file path of the given Uri.
     */
    @SuppressLint("NewApi")
    public static String getPath(Uri uri, Context context) throws URISyntaxException {
        final boolean needToCheckUri = Build.VERSION.SDK_INT >= 19;
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        // deal with different Uris.
        if (needToCheckUri && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * Fix orientation of processed bitmap
     * @param file
     * @param context
     * @return
     */
    public static Bitmap fixOrientationBugOfProcessedBitmap(File file, Context context) {
        try {
            Bitmap bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 1000, 1000);


            if (getCameraPhotoOrientation(context, Uri.parse(file.getPath()), file.getPath()) == 0) {
                return bitmap;
            } else {
                Matrix matrix = new Matrix();
                matrix.postRotate(getCameraPhotoOrientation(context, Uri.parse(file.getPath()), file.getPath()));
                // recreate the new Bitmap and set it back
                Bitmap resizedBatteryImage = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                FileOutputStream fos = new FileOutputStream(file.getPath());
                resizedBatteryImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
                return resizedBatteryImage;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Get camera orientation when image is clicked
     * @param context
     * @param imageUri
     * @param imagePath
     * @return
     */
    public static int getCameraPhotoOrientation(Context context, Uri imageUri, String imagePath) {
        int rotate = 0;
        try {
            context.getContentResolver().notifyChange(imageUri, null);
            File imageFile = new File(imagePath);
            ExifInterface exif = new ExifInterface(
                    imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    /**
     * Decrease size of bitmap for preview
     *
     * @param path
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float) height / (float) reqHeight);
        }

        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float) width / (float) reqWidth);
        }


        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * Get SHA1 filename from given file
     * @param filePath
     * @return
     */
    public static String getSh1fileName(String filePath) {

        MessageDigest md = null;
        FileInputStream fis = null;
        try {
            md = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        try {
            fis = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        byte[] dataBytes = new byte[1024];

        int nread = 0;

        try {
            while ((nread = fis.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, nread);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] mdbytes = md.digest();

        //convert the byte to hex format
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < mdbytes.length; i++) {
            sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        System.out.println("Digest(in hex format):: " + sb.toString());

        return sb.toString();

    }


    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @author paulburke
     */
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static String setPathogenSeverityText(String severity) {

        String severityText = "";

        switch (Integer.parseInt(severity)){
            case 0:
                severityText = "Moderate";
                break;
            case 1:
                severityText = "Moderate";
                break;
            case 2:
                severityText = "Moderate";
                break;
            case 3:
                severityText = "Harmful";
                break;
            case 4:
                severityText = "Harmful";
                break;
            case 5:
                severityText = "Contagious";
                break;
            default:
                severityText = "Moderate";
                break;
        }

        return severityText;
    }

    public static String mixPanelTimeStamp() {
        TimeZone timeZone   = TimeZone.getTimeZone("UTC");
        Calendar cal        = Calendar.getInstance(timeZone);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(cal.getTime());
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    public static String getDateCurrentTimeZone(long timestamp) {

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timestamp);
            //calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Date currenTimeZone = (Date) calendar.getTime();
            return sdf.format(currenTimeZone);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}