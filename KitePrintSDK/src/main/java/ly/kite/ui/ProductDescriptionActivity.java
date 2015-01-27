package ly.kite.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import ly.kite.R;
import ly.kite.print.PrintOrder;
import ly.kite.print.ProductType;

public class ProductDescriptionActivity extends Activity {


    //TODO: Add ViewPagerIndicator To this.
    //TODO: Add a getIntent to keep the photos to be printed along the journey.


    private ViewPager viewPager;
    private ProductType productType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_description);

        this.productType = ProductType.productTypeFromTemplate(getIntent().getStringExtra("template_id"));
        viewPager = (ViewPager)findViewById(R.id.product_viewpager);

        int[] images = new int[] { R.drawable.home_mags,R.drawable.home_squaresxs};

        ViewPagerAdapter adapter = new ViewPagerAdapter(ProductType.getProductDescriptionCarouselImageURLs(productType));
        viewPager.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_product_description, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class ViewPagerAdapter extends PagerAdapter{

        private final String[] urls;
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(ProductDescriptionActivity.this);

            // int padding = ssContext.getResources().getDimensionPixelSize(0x7f040002);
            imageView.setPadding(0, 0, 0, 0);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Picasso.with(ProductDescriptionActivity.this).load(urls[position]).into(imageView);

                    ((ViewPager) container).addView(imageView, 0);
            return imageView;
        }

        public ViewPagerAdapter(String[] urls) {
            this.urls = urls;

        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((ImageView) object);
        }

        @Override
        public void destroyItem(ViewGroup ssContainer, int ssPosition,
                                Object ssObject) {
            ((ViewPager) ssContainer).removeView((ImageView) ssObject);
        }
    }

}
