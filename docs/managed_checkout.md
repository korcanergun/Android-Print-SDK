Submitting a Print Order with Managed Checkout
==============

The Kite Android Print SDK includes a robust checkout and payment experience that's proven to convert well with users. It can take care of the entire checkout process for you, no need to spend time building any user interfaces. 

This is the quickest approach to integration and perfect if you don't want to spend any time building a custom checkout experience.

If you don't want to use or customize the provided experience you can [build your own custom checkout UI](../README.md#custom-checkout).

_If you haven't already, see the [README](../README.md) for an initial overview and instructions for adding the SDK to your project._


Overview
--------
1. Create a `PrintOrder` containing details of the product(s) you want to print
2. Create and start a `CheckoutActivity` passing it the `PrintOrder` object you created in Step 1
3. Handle the `CheckoutActivity` result

Sample Code
-----------
1. See [Creating a Print Order](create_print_order.md) for details on creating the `PrintOrder`
2. Create and start a `CheckoutActivity` passing it the `PrintOrder` object you created in Step 1

    ```java
    // SomeActivity.java
    private static final int REQUEST_CODE_CHECKOUT = 1;

    public void submitPrintOrder(PrintOrder printOrder) {
        Intent intent = new Intent(this, CheckoutActivity.class);
        intent.putExtra(CheckoutActivity.EXTRA_PRINT_ORDER, (Parcelable) printOrder);
        startActivityForResult(intent, REQUEST_CODE_CHECKOUT);
    }
    ```
3. Handle the `CheckoutActivity` result

    ```java
    // Elsewhere in SomeActivity.java
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CHECKOUT) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Successfully checked out!", Toast.LENGTH_LONG).show();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "User cancelled checkout :(", Toast.LENGTH_LONG).show();
            }
        }
    }
    ```

Next Steps
----------

- That's all there is to it from an integration perspective! Submitted print orders will appear in the [developer dashboard](https://developer.psilov.eu/). You'll also need to register your payment details with us in the dashboard so that we can pay you when your users place orders.
- Alternatively you can [build your own custom checkout UI](../README.md#custom-checkout) for complete control of the checkout and payment process.