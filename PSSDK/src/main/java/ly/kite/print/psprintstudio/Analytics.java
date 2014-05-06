package ly.kite.print.psprintstudio;

import android.content.Context;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import ly.kite.print.KitePrintSDK;
import ly.kite.print.PrintJob;
import ly.kite.print.PrintOrder;

/**
 * Created by deonbotha on 24/02/2014.
 */
public class Analytics {

    private static final String MIXPANEL_TOKEN_STAGING = "9684bce7831f24385bc7f5957e094cf3"; // staging/debug token
    private static final String MIXPANEL_TOKEN_LIVE = "83d83416109e5fc0025e25f4157c9782"; // live/production token

    private static String getMixpanelToken(KitePrintSDK.Environment env) {
        if (env == KitePrintSDK.Environment.LIVE) {
            return MIXPANEL_TOKEN_LIVE;
        } else {
            return MIXPANEL_TOKEN_STAGING;
        }
    }

    private static String getProductType(PrintOrder order) {
        PrintJob job = order.getJobs().get(0);
        switch (job.getProductType()) {
            case MAGNETS:
                return "Magnets";
            case MINI_SQUARES:
                return "Petite Squares";
            case MINI_POLAROIDS:
                return "Petite Polaroids";
            case SQUARES:
                return "Squares";
            case POLAROIDS:
                return "Polaroids";
            default:
                throw new UnsupportedOperationException("Unknown product type!");
        }
    }

    private static JSONObject getOrderProperties(PrintOrder order) {
        try {
            JSONObject p = new JSONObject();
            p.put("Product", getProductType(order));

            if (order.getProofOfPayment() != null) {
                p.put("Proof of Payment", order.getProofOfPayment());
                String paymentMethod = "Credit Card";
                if (order.getProofOfPayment().startsWith("AP-")) {
                    paymentMethod = "PayPal";
                }
                p.put("Payment Method", paymentMethod);
            }

            if (order.getLastPrintSubmissionError() != null) {
                p.put("Print Submission Success", "False");
                p.put("Print Submission Error", order.getLastPrintSubmissionError().getMessage());

            }

            if (order.getReceipt() != null) {
                p.put("Print Order Id", order.getReceipt());
                p.put("Print Submission Success", "True");
                p.put("Print Submission Error", "None");
            }

            if (order.getPromoCode() != null) {
                p.put("Voucher Code", order.getPromoCode());
                p.put("Voucher Discount", order.getPromoCodeDiscount().toString());
            }

            if (order.getNotificationEmail() != null) {
                p.put("Shipping Email", order.getNotificationEmail());
            }

            if (order.getNotificationPhoneNumber() != null) {
                p.put("Shipping Phone", order.getNotificationPhoneNumber());
            }

            if (order.getShippingAddress() != null) {
                p.put("Shipping Recipient", order.getShippingAddress().getRecipientName());
                p.put("Shipping Line 1", order.getShippingAddress().getLine1());
                p.put("Shipping Line 2", order.getShippingAddress().getLine2());
                p.put("Shipping City", order.getShippingAddress().getCity());
                p.put("Shipping County", order.getShippingAddress().getStateOrCounty());
                p.put("Shipping Postcode", order.getShippingAddress().getZipOrPostalCode());
                p.put("Shipping Country", order.getShippingAddress().getCountry().getName());
                p.put("Shipping Country Code2", order.getShippingAddress().getCountry().getCodeAlpha2());
                p.put("Shipping Country Code3", order.getShippingAddress().getCountry().getCodeAlpha3());

            }

            p.put("Cost", order.getCost().toString());
            p.put("Job Count", String.format("%d", order.getJobs().size()));
            p.put("Platform", "Android");

            return p;
        } catch (JSONException ex) {
            // will never happen.
            return new JSONObject();
        }
    }

    public static void trackShippingDetailsSupplied(Context context, KitePrintSDK.Environment env, PrintOrder order) {
        MixpanelAPI mixpanel = MixpanelAPI.getInstance(context, getMixpanelToken(env));
        mixpanel.track("Shipping Details Supplied", getOrderProperties(order));
        mixpanel.flush();
    }

    public static void trackPaymentCompleted(Context context, KitePrintSDK.Environment env, PrintOrder order) {
        JSONObject p = getOrderProperties(order);

        MixpanelAPI mixpanel = MixpanelAPI.getInstance(context, getMixpanelToken(env));
        mixpanel.track("Payment Completed", getOrderProperties(order));

        String productName = getProductType(order);
        try {
            JSONObject charge = new JSONObject();
            charge.put("Type", productName);
            charge.put("Amount", order.getCost().toString());
            mixpanel.track("Charge", charge);
        } catch (JSONException ex) {/* ignore */}

        mixpanel.getPeople().trackCharge(order.getCost().doubleValue(), p);
        mixpanel.getPeople().increment(String.format("Purchased %s Count", productName), order.getCost().doubleValue());
        mixpanel.getPeople().increment("Revenue", order.getCost().doubleValue());
        mixpanel.flush();
    }

    public static void trackOrderSubmission(Context context, KitePrintSDK.Environment env, PrintOrder order) {
        JSONObject p = getOrderProperties(order);
        MixpanelAPI mixpanel = MixpanelAPI.getInstance(context, getMixpanelToken(env));
        mixpanel.track("Print Order Submission", p);
        mixpanel.flush();
    }

}
