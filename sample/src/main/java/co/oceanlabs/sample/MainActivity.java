package co.oceanlabs.sample;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import ly.kite.print.Asset;
import ly.kite.print.KitePrintSDK;
import ly.kite.print.PrintJob;
import ly.kite.print.PrintOrder;
import ly.kite.print.ProductType;
import ly.kite.print.checkout.CheckoutActivity;

public class MainActivity extends Activity {

    private static final int SELECT_PICTURE = 1;
    private static final int REQUEST_CODE_CHECKOUT = 2;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        KitePrintSDK.initialize("REPLACE_WITH_YOUR_API_KEY", KitePrintSDK.Environment.TEST);
        imageView = (ImageView) findViewById(R.id.image_view);
    }

    public void onGalleryButtonClicked(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    public void onCheckoutButtonClicked(View view) {
        ArrayList<Asset> assets = new ArrayList<Asset>();
        assets.add(new Asset(R.drawable.instagram1));

        try {
            assets.add(new Asset(new URL("http://psps.s3.amazonaws.com/sdk_static/1.jpg")));
            assets.add(new Asset(new URL("http://psps.s3.amazonaws.com/sdk_static/2.jpg")));
            assets.add(new Asset(new URL("http://psps.s3.amazonaws.com/sdk_static/3.jpg")));
            assets.add(new Asset(new URL("http://psps.s3.amazonaws.com/sdk_static/4.jpg")));
        } catch (Exception ex) {}

        PrintOrder printOrder = new PrintOrder();
        printOrder.addPrintJob(PrintJob.createPrintJob(assets, ProductType.SQUARES));

        Intent intent = new Intent(this, CheckoutActivity.class);
        intent.putExtra(CheckoutActivity.EXTRA_PRINT_ORDER, (Parcelable) printOrder);
        intent.putExtra(CheckoutActivity.EXTRA_PRINT_ENVIRONMENT, CheckoutActivity.ENVIRONMENT_STAGING);
        intent.putExtra(CheckoutActivity.EXTRA_PRINT_API_KEY, "ba171b0d91b1418fbd04f7b12af1e37e42d2cb1e");
        startActivityForResult(intent, REQUEST_CODE_CHECKOUT);
    }

    public void submitPrintOrder(PrintOrder printOrder) {
        Intent intent = new Intent(this, CheckoutActivity.class);
        intent.putExtra(CheckoutActivity.EXTRA_PRINT_ORDER, (Parcelable) printOrder);
        startActivityForResult(intent, REQUEST_CODE_CHECKOUT);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CHECKOUT) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Successfully checked out!", Toast.LENGTH_LONG).show();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "User cancelled checkout :(", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

}
