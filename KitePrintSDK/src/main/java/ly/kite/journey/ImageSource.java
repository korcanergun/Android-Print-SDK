/*****************************************************
 *
 * ImageSource.java
 *
 *
 * Modified MIT License
 *
 * Copyright (c) 2010-2015 Kite Tech Ltd. https://www.kite.ly
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The software MAY ONLY be used with the Kite Tech Ltd platform and MAY NOT be modified
 * to be used with any competitor platforms. This means the software MAY NOT be modified 
 * to place orders with any competitors to Kite Tech Ltd, all orders MUST go through the
 * Kite Tech Ltd platform servers. 
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 *****************************************************/

///// Package Declaration /////

package ly.kite.journey;


///// Import(s) /////

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ly.kite.KiteSDK;
import ly.kite.R;
import ly.kite.instagramphotopicker.InstagramPhoto;
import ly.kite.instagramphotopicker.InstagramPhotoPicker;
import ly.kite.photopicker.Photo;
import ly.kite.photopicker.PhotoPicker;
import ly.kite.product.Asset;


///// Class Declaration /////

/*****************************************************
 *
 * An image source.
 *
 *****************************************************/
public enum ImageSource
  {
  ///// Enum constants /////

  DEVICE ( R.color.image_source_background_device, R.drawable.ic_add_photo_white, R.string.IMAGES )
    {

    public void onPick( Fragment fragment, boolean preferSingleImage )
      {
      // If the caller would prefer a single image then use the system photo picker. Otherwise
      // use the photo picker.

      if ( preferSingleImage )
        {
        Intent intent = new Intent( Intent.ACTION_GET_CONTENT );
        intent.setType( "image/*" );

        try
          {
          fragment.startActivityForResult( Intent.createChooser( intent, fragment.getString( R.string.select_photo_from_device ) ), ACTIVITY_REQUEST_CODE_SINGLE_DEVICE_PICKER );
          }
        catch ( Exception exception )
          {
          Log.e( LOG_TAG, "Unable to start activity for chooser intent", exception );
          }
        }
      else
        {
        PhotoPicker.startPhotoPickerForResult( fragment, ACTIVITY_REQUEST_CODE_MULTIPLE_DEVICE_PICKER );
        }
      }

    }

  ,INSTAGRAM ( R.color.image_source_background_instagram, R.drawable.ic_add_instagram_white, R.string.INSTAGRAM )
    {

    public void onPick( Fragment fragment, boolean preferSingleImage )
      {
      // Clicking on the Instagram image source starts our Instagram image picker library

      KiteSDK kiteSDK = KiteSDK.getInstance( fragment.getActivity() );

      String instagramClientId    = kiteSDK.getInstagramClientId();
      String instagramRedirectURI = kiteSDK.getInstagramRedirectURI();

      InstagramPhotoPicker.startPhotoPickerForResult( fragment, instagramClientId, instagramRedirectURI, ACTIVITY_REQUEST_CODE_INSTAGRAM_PICKER );
      }

    }
//  ,FACEBOOK
    ;


  ///// Static Constant(s) /////

  private static final String  LOG_TAG                                      = "ImageSource";

  public  static final int     ACTIVITY_REQUEST_CODE_SINGLE_DEVICE_PICKER   = 10;
  public  static final int     ACTIVITY_REQUEST_CODE_MULTIPLE_DEVICE_PICKER = 11;
  public  static final int     ACTIVITY_REQUEST_CODE_INSTAGRAM_PICKER       = 12;


  ///// Member Variable(s) /////

  private int  mBackgroundColourResourceId;
  private int  mIconResourceId;
  private int  mLabelResourceId;


  ///// Static Method(s) /////

  /*****************************************************
   *
   * Interprets an activity result, and returns any assets.
   *
   *****************************************************/
  static public List<Asset> getAssetsFromResult( int requestCode, int resultCode, Intent returnedIntent )
    {
    if ( resultCode == Activity.RESULT_OK )
      {
      ArrayList<Asset> assetList = new ArrayList<>();


      // Check which activity has returned photos

      if ( requestCode == ACTIVITY_REQUEST_CODE_SINGLE_DEVICE_PICKER )
        {
        ///// Single device image /////

        Uri imageURI = returnedIntent.getData();

        assetList.add( new Asset( imageURI ) );

        return ( assetList );
        }
      else if ( requestCode == ACTIVITY_REQUEST_CODE_MULTIPLE_DEVICE_PICKER )
        {
        ///// Multiple device images /////

        Photo[] devicePhotos = PhotoPicker.getResultPhotos( returnedIntent );

        if ( devicePhotos != null )
          {
          for ( Photo devicePhoto : devicePhotos )
            {
            assetList.add( new Asset( devicePhoto.getUri() ) );
            }

          return ( assetList );
          }
        }
      else if ( requestCode == ACTIVITY_REQUEST_CODE_INSTAGRAM_PICKER )
        {
        ///// Instagram image(s) /////

        InstagramPhoto instagramPhotos[] = InstagramPhotoPicker.getResultPhotos( returnedIntent );

        if ( instagramPhotos != null )
          {
          for ( InstagramPhoto instagramPhoto : instagramPhotos )
            {
            assetList.add( new Asset( instagramPhoto.getFullURL() ) );
            }

          return ( assetList );
          }
        }
      }


    return ( null );
    }


  ///// Constructor(s) /////

  private ImageSource( int backgroundColourResourceId, int iconResourceId, int labelResourceId )
    {
    mBackgroundColourResourceId = backgroundColourResourceId;
    mIconResourceId             = iconResourceId;
    mLabelResourceId            = labelResourceId;
    }


  int backgroundColourResourceId()
    {
    return ( mBackgroundColourResourceId );
    }

  int iconResourceId()
    {
    return ( mIconResourceId );
    }

  int labelResourceId()
    {
    return ( mLabelResourceId );
    }


  /*****************************************************
   *
   * Called when this image source is clicked.
   *
   *****************************************************/
  abstract public void onPick( Fragment fragment, boolean preferSingleImage );

  }