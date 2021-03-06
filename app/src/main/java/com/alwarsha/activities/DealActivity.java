package com.alwarsha.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alwarsha.app.AlwarshaApp;
import com.alwarsha.app.Deal;
import com.alwarsha.app.DealProduct;
import com.alwarsha.app.R;
import com.alwarsha.data.DealsProvider;
import com.alwarsha.data.ProductsCategoriesProvider;
import com.alwarsha.utils.Utils;
import com.alwarsha.utils.YesNoAlertMessage;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DealActivity extends BaseActivity {

    private String mDealNameId;
    //  private List<DealProduct> mProducts = new ArrayList<DealProduct>();
    private Deal deal;
    private int mTotal;
    private int mTotalDis;
    private AlwarshaApp mApp;
    private Socket client;
    private FileInputStream fileInputStream;
    private ByteArrayInputStream bufferedInputStream;
    private OutputStream outputStream;
    private Button mSendButton;
    private Button mCloseButton;
    // private String mOrdersToSend = "";
    TextView mCommentTextView;
    TextView mTotalTextView;
    TextView mTotalDisTextView;
    ListView mProductListView;
    private boolean divideDealFlag = false;
    LinkedHashMap<Integer, Integer> mProductsCounter = new LinkedHashMap<Integer, Integer>();
    LinkedHashMap<Integer, Integer> mSentProductsCounter = new LinkedHashMap<Integer, Integer>();
    private EditText mDiscountEdtitText;
    private Deal mPartDeal;

    private BaseAdapter mAdapter = new BaseAdapter() {
        private View.OnClickListener mOnButtonClicked = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView productIdView = (TextView) v.findViewById(R.id.productId);
                if(divideDealFlag){
                    for (DealProduct d : deal.getmProducts()) {
                        String productId = productIdView.getText().toString();
                        if (Integer.valueOf(productId) == d.getmId()) {
                            if(mPartDeal !=null){
                                d.setDeal_id(mPartDeal.getId(),DealActivity.this);
                                mPartDeal.addProduct(d, getApplicationContext());
                            }
                            DealProduct.DealProductStatus status = deal.delete_product(d.getmId(), getApplicationContext());
                            initProductsHashMap();
                            mAdapter.notifyDataSetChanged();
                            mTotalTextView.setText("Total = " + deal.getTotal());
                            mTotalDisTextView.setText("Total Dis = " + deal.getTotal_discount());
                            if (deal.getComment() != null) {
                                mCommentTextView.setText(deal.getComment());
                            }
                            return;

                        }
                    }

                }

                new AlertDialog.Builder(DealActivity.this)
                        .setTitle("Delete product")
                        .setMessage("Are you sure you want to delete product?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                for (DealProduct d : deal.getmProducts()) {
                                    String productId = productIdView.getText().toString();
                                    if (Integer.valueOf(productId) == d.getmId()) {

                                        // if (d.getStatus() == DealProduct.DealProductStatus.ORDERED) {
                                        DealProduct.DealProductStatus status = deal.delete_product(d.getmId(), getApplicationContext());
                                        if (status == DealProduct.DealProductStatus.SENT) {
                                            sendRemoveProduct(d.getmName("EN"));
                                        }

                                        initProductsHashMap();
                                        mAdapter.notifyDataSetChanged();
                                        mTotalTextView.setText("Total = " + deal.getTotal());
                                        mTotalDisTextView.setText("Total Dis = " + deal.getTotal_discount());
                                        if (deal.getComment() != null) {
                                            mCommentTextView.setText(deal.getComment());
                                        }
                                        break;
                                        //  }

                                    }
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        };

        @Override
        public int getCount() {
            return (mProductsCounter.size());
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View returnedValue;
            returnedValue = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, null);
            TextView productName = (TextView) returnedValue.findViewById(R.id.producrOneItemNameTextView);
            ImageView productImage = (ImageView) returnedValue.findViewById(R.id.productOneItemImageView);
            TextView productPrice = (TextView) returnedValue.findViewById(R.id.producrOneItemPriceLoTextView);
            TextView productCount = (TextView) returnedValue.findViewById(R.id.countTextView);
            TextView productIdView = (TextView) returnedValue.findViewById(R.id.productId);
            TextView commentTextView = (TextView) returnedValue.findViewById(R.id.producrOneItemComment);
            LinearLayout l = (LinearLayout) returnedValue.findViewById(R.id.producrOneItemCommentLayout);
            l.setVisibility(View.VISIBLE);

            returnedValue.setOnClickListener(mOnButtonClicked);

            DealProduct tempProduct = null;
            List keys = new ArrayList(mProductsCounter.keySet());
            Integer productId = (Integer) keys.get(position);
            for (DealProduct p : deal.getmProducts()) {
                if (p.getmId() == productId) {
                    tempProduct = p;
                }
            }
            productName.setText(tempProduct.getmName("EN"));
            productImage.setImageBitmap(Utils.getBitmapFromStorage(tempProduct.getmPictureName()));
            productPrice.setText("" + tempProduct.getmPrice());
            productCount.setText("" + mProductsCounter.get(tempProduct.getmId()));
            productIdView.setText("" + tempProduct.getmId());
            commentTextView.setText(tempProduct.getComment());


            return returnedValue;

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView deal_name;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal);
        mTotalTextView = (TextView) findViewById(R.id.totalTextView);
        mTotalDisTextView = (TextView) findViewById(R.id.totalDisTextView);
        mProductListView = (ListView) findViewById(R.id.deal_one_product_listView);
        mSendButton = (Button) findViewById(R.id.activityDealSendButton);
        mCloseButton = (Button) findViewById(R.id.activityDealCloseButton);

        mSendButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                DealActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        final YesNoAlertMessage resendDialog = new YesNoAlertMessage(
                                DealActivity.this, "Resnd", "Are you sure?", null);


                        resendDialog.show();

                        resendDialog.yes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                resendDialog.dismiss();
                                resendLastOrder();
                            }
                        });
                        resendDialog.no.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                resendDialog.dismiss();

                            }
                        });
                    }
                });
                return false;
            }
        });

        mCloseButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                DealActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        final YesNoAlertMessage resendDialog = new YesNoAlertMessage(
                                DealActivity.this, "Reclose", "Are you sure?", null);


                        resendDialog.show();

                        resendDialog.yes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                resendDialog.dismiss();
                                sendCloseDeal();
                            }
                        });
                        resendDialog.no.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                resendDialog.dismiss();

                            }
                        });
                    }
                });

                return false;
            }
        });

        mDiscountEdtitText = (EditText) findViewById(R.id.dealActivityDiscountEditText);

        mDiscountEdtitText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus == true) {

                } else {
                    try {
                        int discountPercent = Integer.valueOf(mDiscountEdtitText.getText().toString());
                        deal.setTotalDiscount(Float.valueOf(discountPercent), DealActivity.this);
                        String total = "Toatal Discount = " + String.valueOf(deal.getTotal() - deal.getTotal() * discountPercent / 100);
                        mTotalDisTextView.setText(total);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }
        });

        mCommentTextView = (TextView) findViewById(R.id.dealActivityCommentEditText);

        mApp = AlwarshaApp.getInstance();
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            mDealNameId = extras.getString("dealName");
        }

        deal_name = (TextView) findViewById(R.id.deal_name);
        deal_name.setText(mDealNameId);

    }

    @Override
    protected void onResume() {

        int total = 0;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");

        try {
            deal = new Deal(mDealNameId, getApplicationContext());
        } catch (Exception ex) {
            if (ex.getMessage().equals("OPEN Deal with same name already exist in DB")) {
                DealsProvider dp = DealsProvider.getInstace(DealActivity.this);
                deal = dp.getOpenDealByName(mDealNameId);
            }
        }

        if ((deal.getmProducts() != null) && (deal.getmProducts().size() > 0)) {
            mProductListView.setAdapter(mAdapter);
        }

        initProductsHashMap();

        mTotalTextView.setText("Total = " + deal.getTotal());
        mTotalDisTextView.setText("Total Dis = " + deal.getTotal_discount());
        if (deal.getComment() != null) {
            mCommentTextView.setText(deal.getComment());
        }
        super.onResume();
    }

    public void sedDiscountClick(View discountButton) {
        try {
            int discountPercent = Integer.valueOf(mDiscountEdtitText.getText().toString());
            deal.setTotalDiscount(Float.valueOf(discountPercent), DealActivity.this);
            String total = "Toatal Discount = " + String.valueOf(deal.getTotal() - deal.getTotal() * discountPercent / 100);
            mTotalDisTextView.setText(total);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initProductsHashMap() {
        if (mProductsCounter.size() > 0)
            mProductsCounter.clear();
        if (deal.getmProducts() == null) {
            return;
        }
        for (DealProduct d : deal.getmProducts()) {
            Integer productCounter = mProductsCounter.get(d.getmId());
            if (d.getComment() != null && d.getComment().trim().length() > 0) {
                productCounter = 1;
                mProductsCounter.put(d.getmId() + hashString(d.getComment()), productCounter);
                continue;
            } else if (productCounter != null) {
                productCounter++;
                mProductsCounter.put(d.getmId(), productCounter);
            } else {
                productCounter = 1;
                mProductsCounter.put(d.getmId(), productCounter);
            }

        }
    }

    private int hashString(String comment) {
        int hash = 7;
        for (int i = 0; i < comment.length(); i++) {
            hash = hash * 31 + comment.charAt(i);
        }
        return hash;
    }

    public void add(View addButton) {
        Intent i = new Intent(DealActivity.this, MenuMainActivity.class);
        i.putExtra("sender", DealActivity.class.getSimpleName());
        i.putExtra("dealId", mDealNameId);
        startActivity(i);
    }

    public void resendLastOrder() {
        String resentText = "******** Resend ********\n" + deal.getOrdersToSend();
        new sendToPrinterTask().execute(deal.getOrdersToSend());
    }

    public void sendRemoveProduct(String product) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy - MM - dd HH:mm");
        String currentDateandTime = "&&&&   Remove    &&&&\n" + sdf.format(new Date());

        String ordersToSend = currentDateandTime + '\r' + '\n' + '\n';
        ordersToSend += "Table number : " + mDealNameId + '\r' + '\n';
        ordersToSend += AlwarshaApp.m.getName() + '\r' + '\n';
        ordersToSend += product + '\t' + '\r' + '\n';
        new sendToPrinterTask().execute(ordersToSend);

    }

    public void send(View sendButton) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy - MM - dd HH:mm");
        String currentDateandTime = sdf.format(new Date());
        boolean sendBarProductsToPrint = false;
        boolean sendKitchenProductsToPrint = false;
        String ordersToSend = currentDateandTime + '\r' + '\n' + '\n';
        ordersToSend += "Table number : " + mDealNameId + '\r' + '\n';
        ordersToSend += AlwarshaApp.m.getName() + '\r' + '\n';
        ArrayList<String> printed = new ArrayList<String>();

        HashMap<String, Integer> sendBarBroducts = new HashMap<String, Integer>();
        HashMap<String, Integer> sendKitchenBroducts = new HashMap<String, Integer>();


        for (DealProduct dd : deal.getmProducts()) {
            if (dd.getStatus() == DealProduct.DealProductStatus.ORDERED && ProductsCategoriesProvider.getInstace(DealActivity.this).getProductCategory(String.valueOf(dd.getmCategoryId())).getmGroupId() == 1) {
                sendBarProductsToPrint = true;
                Integer productCounter = sendBarBroducts.get(dd.getmName("EN"));
                if (productCounter != null) {
                    productCounter++;
                } else {
                    productCounter = 1;
                }
                sendBarBroducts.put(dd.getmName("EN"), productCounter);
                dd.setStatus(DealProduct.DealProductStatus.SENT, getApplicationContext());
            }
        }

        for (DealProduct dd : deal.getmProducts()) {
            if (dd.getStatus() == DealProduct.DealProductStatus.ORDERED && ProductsCategoriesProvider.getInstace(DealActivity.this).getProductCategory(String.valueOf(dd.getmCategoryId())).getmGroupId() == 2) {
                sendKitchenProductsToPrint = true;
                Integer productCounter = sendKitchenBroducts.get(dd.getmName("EN"));
                if (productCounter != null) {
                    productCounter++;
                } else {
                    productCounter = 1;
                }
                sendKitchenBroducts.put(dd.getmName("EN"), productCounter);
                dd.setStatus(DealProduct.DealProductStatus.SENT, getApplicationContext());
            }
        }

        String barOrderTosend = "";
        String kitchenOrderTosend = ordersToSend;
        for (Map.Entry<String, Integer> entry : sendBarBroducts.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            ordersToSend += key + "\t" + value + "\n";
            barOrderTosend = ordersToSend;
        }

        for (Map.Entry<String, Integer> entry : sendKitchenBroducts.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            kitchenOrderTosend += key + "\t" + value + "\n";
            ordersToSend += key + "\t" + value + "\n";

        }

        deal.setOrdersToSend(ordersToSend);
        if (sendBarProductsToPrint) {
            new sendToPrinterTask().execute(barOrderTosend);
            sendBarProductsToPrint = false;
        }

        if (sendKitchenProductsToPrint) {
            new sendToPrinterTask().execute(kitchenOrderTosend);
            sendBarProductsToPrint = false;
        }
    }

    public void setDealComment(View SetCommentButton) {
        this.runOnUiThread(new Runnable() {
            public void run() {
                EditText comment = (EditText) findViewById(R.id.dealActivityCommentEditText);
                deal.setDealComment(comment.getText().toString(), getApplicationContext());
            }
        });
    }

    public void closeClicked(View closeButton) {

        this.runOnUiThread(new Runnable() {
            public void run() {
                final YesNoAlertMessage error = new YesNoAlertMessage(
                        DealActivity.this, "Exit", "Are you sure?", null);


                error.show();

                error.yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deal.close(getApplicationContext());
                        error.dismiss();
                        sendCloseDeal();
                        finish();
                    }
                });
                error.no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        error.dismiss();

                    }
                });
            }
        });
    }

    private void sendCloseDeal() {
        if (deal == null || deal.getmProducts() == null || deal.getmProducts().size() == 0)
            return;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy - MM - dd HH:mm");
        String currentDateandTime = sdf.format(new Date());
        LinkedHashMap<Integer, Integer> sentProductsCounter = new LinkedHashMap<Integer, Integer>();
        float total;

        String dealClose = "+++++++++++++++++++++++++++++++" + '\r' + '\n';
        dealClose += "Close deal" + '\r' + '\n';
        dealClose += currentDateandTime + '\r' + '\n' + '\n';
        dealClose += "Table number : " + mDealNameId + '\r' + '\n';
        dealClose += AlwarshaApp.m.getName() + '\r' + '\n';
        total = 0;
        for (DealProduct d : deal.getmProducts()) {
            dealClose += d.getmName("EN") + "  " + d.getmPrice() + " NIS" + "\n";
            total += d.getmPrice();
        }
        //   ArrayList<String> printed = new ArrayList<String>();
//        for (DealProduct d : deal.getmProducts()) {
//            if (d.getStatus() == DealProduct.DealProductStatus.SENT) {
//                for (DealProduct dd : deal.getmProducts()) {
//                    Integer productCounter = sentProductsCounter.get(d.getmId());
//                    if (productCounter != null) {
//                        productCounter++;
//                    } else {
//                        productCounter = 1;
//                    }
//                    sentProductsCounter.put(d.getmId(), productCounter);
//                }
//                continue;
//            }
//            boolean found = false;
//            for (String s : printed) {
//                if (s.equals(d.getmName("EN"))) {
//                    found = true;
//                    break;
//                }
//            }
//            if (!found) {
//                int sent = 0;
//                if (sentProductsCounter.get(d.getmId()) != null) {
//                    sent = sentProductsCounter.get(d.getmId());
//                }
//                int count = mProductsCounter.get(d.getmId()) - sent;
//                dealClose += d.getmName("EN") + '\t' + count + '\r' + '\n';
//                printed.add(d.getmName("EN"));
//            }
//            d.setStatus(DealProduct.DealProductStatus.SENT,getApplicationContext());
//        }

        dealClose += "---- Total =  " + total + '\r' + '\n';
        dealClose += "---- Total after discount =  " + String.valueOf(total - total * deal.getTotal_discount() / 100) + '\r' + '\n';


        new sendToPrinterTask().execute(dealClose);
    }

    private class sendToPrinterTask extends
            AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... arg0) {

            try {
                arg0[0] += "\n" + "\n" + "\n" + "\u001b" + "\u0069";
                int textLength = arg0[0].length();

                Socket client = new Socket("192.168.1.47", 9100);

                byte[] mybytearray = new byte[textLength];


                ByteArrayInputStream bufferedInputStream = new ByteArrayInputStream(arg0[0].getBytes());

                bufferedInputStream.read(mybytearray, 0, mybytearray.length);

                OutputStream outputStream = client.getOutputStream();

                outputStream.write(mybytearray, 0, mybytearray.length);
                outputStream.flush();
                bufferedInputStream.close();
                outputStream.close();
                client.close();


            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }

    public void closePartClicked(View v){
        divideDealFlag = true;
        AlertDialog.Builder editalert = new AlertDialog.Builder(DealActivity.this);

        editalert.setTitle("Insert new deal");
        editalert.setMessage("Enter deal name");


        final EditText input = new EditText(DealActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setHeight(100);
        editalert.setView(input);

        editalert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String dealName ="";
                if(input!= null && input.getText() != null)
                    dealName = input.getText().toString();
                if (dealName.length() == 0 || dealName.substring(0, 1).matches("[0-9]"))  {
                    final AlertDialog.Builder BadNameAlert = new AlertDialog.Builder(DealActivity.this);
                    BadNameAlert.setTitle("Bad Deal Name!");
                    BadNameAlert.setMessage("Deal Name cannot start with digit");
                    BadNameAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    });
                } else {
                    try{
                        mPartDeal = new Deal(dealName,DealActivity.this);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });

        editalert.show();

    }
}
