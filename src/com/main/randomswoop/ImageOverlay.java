package com.main.randomswoop;

import static android.graphics.Paint.Style.STROKE;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.AsyncTask;

/**
 * Created with IntelliJ IDEA.
 * User: abandeali
 * Date: 12/5/13
 * Time: 10:29 PM
 */
/*
 * Copyright 2012-2013 Amazon Technologies, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *    http://aws.amazon.com/apache2.0
 *
 * This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Worker that knows how to overlay text onto an image.
 */
public class ImageOverlay
{

    private static final int MAX_FONT_SIZE = 640;
    private static final int BOTTOM_MARGIN = 10;
    private static final int TOP_MARGIN = 5;
    private static final int SIDE_MARGIN = 10;
    private static final String TOP_TEXT =
            "This is a long bit of text on the top, but don't worry, this algorithm will take care of it";
    // private static final String BOTTOM_TEXT =
    // "Some long text is too long for one line, and if we continue it gets really long indeed";
    private static final String BOTTOM_TEXT = "NOPE";

    private static final String INPUT_IMAGE = "/tmp/CHANGEME";
    private static final String CAPTION_FILE = "/tmp/CHANGEME.out";

    public static Bitmap overlay( Bitmap image, String topCaption,
            String bottomCaption, Context baseContext )
            throws IOException, InterruptedException
    {

        Canvas graphics = new Canvas( image );
        graphics.drawBitmap( image, 0, 0, null );
        drawStringCentered( graphics, topCaption, image, true, baseContext );
        graphics.drawBitmap( image, 0, 0, null );
        drawStringCentered( graphics, bottomCaption, image, false, baseContext );
        return image;
    }

    public static void main( String[] args ) throws InterruptedException
    {

        try
        {
            if ( new File( CAPTION_FILE ).exists() )
            {
                new File( CAPTION_FILE ).delete();
            }
            Bitmap image = BitmapFactory.decodeFile( INPUT_IMAGE );
            Canvas graphics = new Canvas( image );
            String captionTop = TOP_TEXT;
            String captionBottom = BOTTOM_TEXT;
            drawStringCentered( graphics, captionTop, image, true, null );
            drawStringCentered( graphics, captionBottom, image, false, null );

            // ImageIO.write(image, "png", new File(CAPTION_FILE));
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

    /**
     * Draws the given string centered, as big as possible, on either the top or
     * bottom 20% of the image given.
     */
    private static void drawStringCentered( Canvas g, String text,
            Bitmap image, boolean top, Context baseContext )
            throws InterruptedException
    {
        if ( text == null )
            text = "";

        int height = 0;
        int fontSize = MAX_FONT_SIZE;
        int maxCaptionHeight = image.getHeight() / 5;
        int maxLineWidth = image.getWidth() - SIDE_MARGIN * 2;
        String formattedString = "";
        Paint paint = new Paint( Paint.ANTI_ALIAS_FLAG );

        Paint stkPaint = new Paint( Paint.ANTI_ALIAS_FLAG );
        stkPaint.setStyle( STROKE );
        stkPaint.setStrokeWidth( 8 );
        stkPaint.setColor( Color.BLACK );

        // Typeface tf = Typeface.create("Arial", Typeface.BOLD);
        Typeface tf =
                Typeface.createFromAsset( baseContext.getAssets(),
                        "fonts/impact.ttf" );

        paint.setTypeface( tf );
        stkPaint.setTypeface( tf );
        do
        {

            paint.setTextSize( fontSize );

            // first inject newlines into the text to wrap properly
            StringBuilder sb = new StringBuilder();
            int left = 0;
            int right = text.length() - 1;
            while ( left < right )
            {

                String substring = text.substring( left, right + 1 );
                Rect stringBounds = new Rect();
                paint.getTextBounds( substring, 0, substring.length(),
                        stringBounds );
                while ( stringBounds.width() > maxLineWidth )
                {
                    if ( Thread.currentThread().isInterrupted() )
                    {
                        throw new InterruptedException();
                    }

                    // look for a space to break the line
                    boolean spaceFound = false;
                    for ( int i = right; i > left; i-- )
                    {
                        if ( text.charAt( i ) == ' ' )
                        {
                            right = i - 1;
                            spaceFound = true;
                            break;
                        }
                    }
                    substring = text.substring( left, right + 1 );
                    paint.getTextBounds( substring, 0, substring.length(),
                            stringBounds );

                    // If we're down to a single word and we are still too wide,
                    // the font is just too big.
                    if ( !spaceFound && stringBounds.width() > maxLineWidth )
                    {
                        break;
                    }
                }
                sb.append( substring ).append( "\n" );
                left = right + 2;
                right = text.length() - 1;
            }

            formattedString = sb.toString();

            // now determine if this font size is too big for the allowed height
            height = 0;
            for ( String line : formattedString.split( "\n" ) )
            {
                Rect stringBounds = new Rect();
                paint.getTextBounds( line, 0, line.length(), stringBounds );
                height += stringBounds.height();
            }
            fontSize--;
        }
        while ( height > maxCaptionHeight );

        // draw the string one line at a time
        int y = 0;
        if ( top )
        {
            y = TOP_MARGIN;
        }
        else
        {
            y = image.getHeight() - height - BOTTOM_MARGIN;
        }
        for ( String line : formattedString.split( "\n" ) )
        {
            // Draw each string twice for a shadow effect
            Rect stringBounds = new Rect();
            paint.getTextBounds( line, 0, line.length(), stringBounds );
            // paint.setColor(Color.BLACK);
            // g.drawText(line, (image.getWidth() - (int) stringBounds.width())
            // / 2 + 2, y + stringBounds.height() + 2, paint);

            paint.setColor( Color.WHITE );
            g.drawText( line,
                    (image.getWidth() - (int) stringBounds.width()) / 2, y
                            + stringBounds.height(), paint );

            // stroke
            Rect strokeBounds = new Rect();
            stkPaint.setTextSize( fontSize );
            stkPaint.getTextBounds( line, 0, line.length(), strokeBounds );
            g.drawText( line,
                    (image.getWidth() - (int) strokeBounds.width()) / 2, y
                            + strokeBounds.height(), stkPaint );

            y += stringBounds.height();
        }
    }

    public static boolean saveToSD( final Bitmap overlaid,
            final String sdPath, final String fileName )
    {
        boolean isItSaved = false;
        new AsyncTask<Void, Void, Void>()
        {

            @Override
            protected Void doInBackground( Void... arg0 )
            {

                File image = new File( sdPath, fileName );

                FileOutputStream outStream;
                try
                {

                    outStream = new FileOutputStream( image );
                    // resize image
                    Bitmap newoverlaid =
                            getResizedBitmap( overlaid, 1000, 1362 );
                    newoverlaid.compress( Bitmap.CompressFormat.PNG, 100,
                            outStream );

                    outStream.flush();
                    outStream.close();
                }
                catch ( FileNotFoundException e )
                {
                    e.printStackTrace();
                }
                catch ( IOException e )
                {
                    e.printStackTrace();
                }
                return null;
            }

            public Bitmap getResizedBitmap( Bitmap bm, int newHeight,
                    int newWidth )
            {
                int width = bm.getWidth();
                int height = bm.getHeight();
                float scaleWidth = ((float) newWidth) / width;
                float scaleHeight = ((float) newHeight) / height;
                // CREATE A MATRIX FOR THE MANIPULATION
                Matrix matrix = new Matrix();
                // RESIZE THE BIT MAP
                matrix.postScale( scaleWidth, scaleHeight );

                // "RECREATE" THE NEW BITMAP
                Bitmap resizedBitmap =
                        Bitmap.createBitmap( bm, 0, 0, width, height, matrix,
                                false );
                return resizedBitmap;
            }

            @Override
            protected void onPostExecute( Void result )
            {
                super.onPostExecute( result );
            }
        }.execute();

        return isItSaved;
    }

    public static void uploadToImgur( final String fileName, Context context )
    {
        // Uploader mUploader = new Uploader(context, fileName);
        // mUploader.execute();
    }

}
