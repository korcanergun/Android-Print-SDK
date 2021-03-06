/*****************************************************
 *
 * ImageSpec.java
 *
 *
 * Modified MIT License
 *
 * Copyright (c) 2010-2016 Kite Tech Ltd. https://www.kite.ly
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

package ly.kite.ordering;


///// Import(s) /////

import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import ly.kite.util.Asset;
import ly.kite.util.AssetFragment;
import ly.kite.util.StringUtils;


///// Class Declaration /////

/*****************************************************
 *
 * This class holds the details of an image for ordering
 * purposes: an asset fragment and a quantity.
 *
 * It replaces the AssetsAndQuantity class.
 *
 *****************************************************/
public class ImageSpec implements Parcelable
  {
  ////////// Static Constant(s) //////////

  @SuppressWarnings( "unused" )
  static private final String  LOG_TAG       = "ImageSpec";


  ////////// Static Variable(s) //////////

  static public final Parcelable.Creator<ImageSpec> CREATOR = new Parcelable.Creator<ImageSpec>()
    {
    public ImageSpec createFromParcel( Parcel in )
      {
      return ( new ImageSpec( in ) );
      }

    public ImageSpec[] newArray( int size )
      {
      return ( new ImageSpec[ size ] );
      }
    };


  ////////// Member Variable(s) //////////

  private AssetFragment  mAssetFragment;
  private int            mQuantity;

  private String         mCroppedForProductId;


  ////////// Static Initialiser(s) //////////


  ////////// Static Method(s) //////////

  /*****************************************************
   *
   * Finds the index of an asset.
   *
   *****************************************************/
  static public int findAsset( List<ImageSpec> searchImageSpecList, Asset soughtAsset )
    {
    int candidateImageIndex = 0;

    for ( ImageSpec candidateImageSpec : searchImageSpecList )
      {
      if ( candidateImageSpec.getAssetFragment().getAsset().equals( soughtAsset ) ) return ( candidateImageIndex );

      candidateImageIndex ++;
      }

    return ( -1 );
    }


  /*****************************************************
   *
   * Returns true if the asset is in the list.
   *
   *****************************************************/
  static public boolean assetIsInList( List<ImageSpec> searchImageSpecList, Asset soughtAsset )
    {
    return ( findAsset( searchImageSpecList, soughtAsset ) >= 0 );
    }


  /*****************************************************
   *
   * Returns true if both the image specs are null, or equal.
   *
   *****************************************************/
  static public boolean areBothNullOrEqual( ImageSpec imageSpec1, ImageSpec imageSpec2 )
    {
    if ( imageSpec1 == null && imageSpec2 == null ) return ( true );
    if ( imageSpec1 == null || imageSpec2 == null ) return ( false );

    return ( imageSpec1.equals( imageSpec2 ) );
    }


  /*****************************************************
   *
   * Returns true if both the image spec lists are null, or equal.
   *
   *****************************************************/
  static public boolean areBothNullOrEqual( List<ImageSpec> list1, List<ImageSpec> list2 )
    {
    if ( list1 == null && list2 == null ) return ( true );
    if ( list1 == null || list2 == null ) return ( false );

    if ( list1.size() != list2.size() ) return ( false );


    int index = 0;

    for ( ImageSpec imageSpec1 : list1 )
      {
      if ( ! ImageSpec.areBothNullOrEqual( imageSpec1, list2.get( index ) ) ) return ( false );

      index ++;
      }


    return ( true );
    }


  ////////// Constructor(s) //////////

  public ImageSpec( AssetFragment assetFragment, int quantity )
    {
    mAssetFragment = assetFragment;
    mQuantity      = quantity;
    }


  public ImageSpec( AssetFragment assetFragment )
    {
    this( assetFragment, 1 );
    }


  public ImageSpec( Asset asset )
    {
    this( new AssetFragment( asset ) );
    }


  public ImageSpec( Asset asset, RectF proportionalCropRectangle, int quantity )
    {
    this( new AssetFragment( asset, proportionalCropRectangle ), quantity );
    }


  private ImageSpec( Parcel sourceParcel )
    {
    mAssetFragment       = sourceParcel.readParcelable( AssetFragment.class.getClassLoader() );
    mQuantity            = sourceParcel.readInt();
    mCroppedForProductId = sourceParcel.readString();
    }


  ////////// Parcelable Method(s) //////////

  @Override
  public int describeContents()
    {
    return ( 0 );
    }


  @Override
  public void writeToParcel( Parcel targetParcel, int flags )
    {
    targetParcel.writeParcelable( mAssetFragment, flags );
    targetParcel.writeInt( mQuantity );
    targetParcel.writeString( mCroppedForProductId );
    }


  ////////// Method(s) //////////

  /*****************************************************
   *
   * Returns the asset fragment.
   *
   *****************************************************/
  public AssetFragment getAssetFragment()
    {
    return ( mAssetFragment );
    }


  /*****************************************************
   *
   * Returns the asset.
   *
   *****************************************************/
  public Asset getAsset()
    {
    return ( mAssetFragment.getAsset() );
    }


  /*****************************************************
   *
   * Returns the quantity.
   *
   *****************************************************/
  public int getQuantity()
    {
    return ( mQuantity );
    }


  /*****************************************************
   *
   * Sets the crop rectangle and the cropped for product
   * id.
   *
   *****************************************************/
  public void setProportionalCropRectangle( RectF proportionalCropRectangle, String croppedForProductId )
    {
    mAssetFragment.setProportionalRectangle( proportionalCropRectangle );

    setCroppedForProductId( croppedForProductId );
    }


  /*****************************************************
   *
   * Sets a new asset fragment.
   *
   *****************************************************/
  public void setImage( AssetFragment assetFragment, String croppedForProductId )
    {
    mAssetFragment = assetFragment;

    setCroppedForProductId( croppedForProductId );
    }


  /*****************************************************
   *
   * Sets the cropped for product id.
   *
   *****************************************************/
  public void setCroppedForProductId( String croppedForProductId )
    {
    mCroppedForProductId = croppedForProductId;
    }


  /*****************************************************
   *
   * Returns the user journey type that the edited asset
   * was intended for.
   *
   *****************************************************/
  public String getCroppedForProductId()
    {
    return ( mCroppedForProductId );
    }


  /*****************************************************
   *
   * Decrements the quantity by 1 and returns the new value.
   * Will not decrement past 0.
   *
   *****************************************************/
  public int decrementQuantity()
    {
    if ( mQuantity > 0 ) mQuantity --;

    return ( mQuantity );
    }


  /*****************************************************
   *
   * Increments the quantity by 1 and returns the new value.
   *
   *****************************************************/
  public int incrementQuantity()
    {
    return ( ++ mQuantity );
    }


  /*****************************************************
   *
   * Returns true if this image spec equals the supplied
   * image spec.
   *
   *****************************************************/
  @Override
  public boolean equals( Object otherObject )
    {
    if ( otherObject == null || ! ( otherObject instanceof ImageSpec ) )
      {
      return ( false );
      }

    ImageSpec otherImageSpec = (ImageSpec)otherObject;


    if ( otherImageSpec == this ) return ( true );


    return ( mAssetFragment.equals( otherImageSpec.mAssetFragment ) &&
             mQuantity == otherImageSpec.mQuantity &&
             StringUtils.areBothNullOrEqual( mCroppedForProductId, otherImageSpec.mCroppedForProductId ) );
    }


  ////////// Inner Class(es) //////////

  /*****************************************************
   *
   * ...
   *
   *****************************************************/

  }

