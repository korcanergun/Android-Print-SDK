package ly.kite.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ly.kite.R;
import ly.kite.print.ProductType;

public class ProductSelectionActivity extends Activity {

    private ListView listView;
    private ProductsListViewAdapter adapter;
    private ArrayList<Product> products;

    private void createSupportedProducts(){

        Product magnets = new Product(ProductType.MAGNETS, R.drawable.home_mags);
        Product square_stickers = new Product(ProductType.SQUARE_STICKERS, R.drawable.home_stickers);
        Product pols = new Product(ProductType.POLAROIDS, R.drawable.home_pols);
        Product polsxs = new Product(ProductType.MINI_POLAROIDS, R.drawable.home_pol_xs);
        Product mini_squares = new Product(ProductType.MINI_SQUARES, R.drawable.home_squaresxs);
        Product squares = new Product(ProductType.SQUARES, R.drawable.home_squaresxs);
        Product frames = new Product(ProductType.FRAMES_1X1_20CM, R.drawable.home_squaresxs);
        Product posters = new Product(ProductType.POSTER_A1_35CM , R.drawable.home_squaresxs);
        Product circle_stickers = new Product(ProductType.CIRCLE_STICKERS, R.drawable.home_squaresxs);

        products = new ArrayList<Product>();
        products.add(mini_squares);
        products.add(squares);
        products.add(magnets);
        products.add(pols);
        products.add(polsxs);
        products.add(square_stickers);
        products.add(circle_stickers);
        products.add(posters);
        products.add(frames);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_selection);

        listView = (ListView)findViewById(R.id.products_listview);

        createSupportedProducts();
        adapter = new ProductsListViewAdapter(this,products,new ProductsListViewListener() {
            @Override
            public void productItemSelected(Product item) {
                Log.i("Selected Row", item.getType().getDefaultTemplate());


                Intent intent = new Intent(ProductSelectionActivity.this, ProductDescriptionActivity.class);
                startActivity(intent);


                if (item.getType() == ProductType.POSTER_A1_35CM){





                }else if (item.getType() == ProductType.FRAMES_1X1_20CM){






                } else {





                }



            }
        });
        listView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_product_selection, menu);
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


    public class Product {
        private ProductType type;
        private int imageDrawable;

        public ProductType getType() {
            return type;
        }

        public int getImageDrawable() {
            return imageDrawable;
        }

        public Product(ProductType type, int imageDrawable) {
            this.type = type;
            this.imageDrawable = imageDrawable;
        }
    }


    public interface ProductsListViewListener{
        void productItemSelected(Product item);
    }

    private class ProductsListViewAdapter extends BaseAdapter{

        private Context ctx;
        private ArrayList<Product> items;
        private ProductsListViewListener listener;

        private ProductsListViewAdapter(Context ctx, ArrayList<Product> items, ProductsListViewListener listener) {
            this.ctx = ctx;
            this.items = items;
            this.listener = listener;
        }


        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {

            View rowView = convertView;

            if (rowView == null){

                LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.product_selection_item, null);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.image = (ImageView)rowView.findViewById(R.id.product_list_item_imageview);
                viewHolder.name = (TextView)rowView.findViewById(R.id.product_name);
                rowView.setTag(viewHolder);


            }

            ViewHolder holder = (ViewHolder) rowView.getTag();

           // holder.image.setImageDrawable(ctx.getResources().getDrawable(items.get(position).getImageDrawable()));
            Picasso.with(ProductSelectionActivity.this).load(ProductType.getProductSelectionListImageURL(items.get(position).getType())).into(holder.image);
            holder.image.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    listener.productItemSelected(products.get(position));
                }
            });


            holder.name.setText(products.get(position).getType().getProductName());

            return rowView;



        }

         class ViewHolder {
            public ImageView image;
            public TextView name;

        }

    }

}
