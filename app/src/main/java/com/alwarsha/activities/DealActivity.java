package com.alwarsha.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alwarsha.app.AlwarshaApp;
import com.alwarsha.app.Deal;
import com.alwarsha.app.DealProduct;
import com.alwarsha.app.R;
import com.alwarsha.data.DealsProvider;
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
import java.util.LinkedHashMap;
import java.util.List;

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
    private String mOrdersToSend = "";
    TextView mTotalTextView;
    TextView mTotalDisTextView;
    ListView mProductListView;
    LinkedHashMap<Integer, Integer> mProductsCounter = new LinkedHashMap<Integer, Integer>();
    LinkedHashMap<Integer, Integer> mSentProductsCounter = new LinkedHashMap<Integer, Integer>();

    private BaseAdapter mAdapter = new BaseAdapter() {
        private View.OnClickListener mOnButtonClicked = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView productIdView = (TextView) v.findViewById(R.id.productId);
                new AlertDialog.Builder(DealActivity.this)
                        .setTitle("Delete product")
                        .setMessage("Are you sure you want to delete product?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                for (DealProduct d : deal.getmProducts()) {
                                    String productId = productIdView.getText().toString();
                                    if (Integer.valueOf(productId) == d.getmId()) {
                                        if (d.getStatus() == DealProduct.DealProductStatus.ORDERED) {
                                            deal.getmProducts().remove(d);
                                            initProductsHashMap();
                                            mAdapter.notifyDataSetChanged();
                                            break;
                                        }
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

        try{
            deal = new Deal(mDealNameId, getApplicationContext());
        }
        catch(Exception ex){
            if(ex.getMessage().equals("OPEN Deal with same name already exist in DB")){
                DealsProvider dp = DealsProvider.getInstace(DealActivity.this);
                deal = dp.getOpenDealByName(mDealNameId);
            }
        }

        if((deal.getmProducts() != null) && (deal.getmProducts().size() > 0)){
            mProductListView.setAdapter(mAdapter);
        }

        initProductsHashMap();

        mTotalTextView.setText("Total = " + deal.getTotal());
        mTotalDisTextView.setText("Total Dis = " + deal.getTotal_discount());
        super.onResume();
    }

    private void initProductsHashMap() {
        if (mProductsCounter.size() > 0)
            mProductsCounter.clear();
        if(deal.getmProducts() == null){
            return;
        }
        for (DealProduct d : deal.getmProducts()) {
            Integer productCounter = mProductsCounter.get(d.getmId());
            if (d.getComment() != null && d.getComment().trim().length() > 0) {
                productCounter = 1;
                mProductsCounter.put(d.getmId(), productCounter);
                continue;
            } else if (productCounter != null) {
                productCounter++;
            } else {
                productCounter = 1;
            }
            mProductsCounter.put(d.getmId(), productCounter);

        }
    }

    public void add(View addButton) {
        Intent i = new Intent(DealActivity.this, MenuMainActivity.class);
        i.putExtra("sender", DealActivity.class.getSimpleName());
        i.putExtra("dealId", mDealNameId);
        startActivity(i);
    }

    public void send(View sendButton) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy - MM - dd HH:mm");
        String currentDateandTime = sdf.format(new Date());

        mOrdersToSend = currentDateandTime + '\r' + '\n' + '\n';
        mOrdersToSend += "Table number : " + mDealNameId + '\r' + '\n';
        mOrdersToSend += AlwarshaApp.m.getName() + '\r' + '\n';
        ArrayList<String> printed = new ArrayList<String>();
        for (DealProduct d : deal.getmProducts()) {
            if (d.getStatus() == DealProduct.DealProductStatus.ORDERED) {
                for (DealProduct dd : deal.getmProducts()) {
                    Integer productCounter = mSentProductsCounter.get(d.getmId());
                    if (productCounter != null) {
                        productCounter++;
                    } else {
                        productCounter = 1;
                    }
                    mSentProductsCounter.put(d.getmId(), productCounter);
                }
                continue;
            }
            boolean found = false;
            for (String s : printed) {
                if (s.equals(d.getmName("EN"))) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                int sent = 0;
                if (mSentProductsCounter.get(d.getmId()) != null) {
                    sent = mSentProductsCounter.get(d.getmId());
                }
                int count = mProductsCounter.get(d.getmId()) - sent;
                mOrdersToSend += d.getmName("EN") + '\t' + count + '\r' + '\n';
                printed.add(d.getmName("EN"));
            }
            d.setStatus(DealProduct.DealProductStatus.SENT);
        }

//        for (Map.Entry<DealProduct, Integer> entry : mProducts.entrySet()) {
//            DealProduct key = entry.getKey();
//            Integer value = entry.getValue();
//
//            if (key.getStatus() == DealProduct.DealProductStatus.ORDERED) {
//                mOrdersToSend += key.getmName("EN") + '\t' + value + '\r' + '\n';
//            }
//        }
        new Task().execute(mOrdersToSend);
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
                        deal.close();
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

    private void sendCloseDeal(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy - MM - dd HH:mm");
        String currentDateandTime = sdf.format(new Date());
        LinkedHashMap<Integer, Integer> sentProductsCounter = new LinkedHashMap<Integer, Integer>();

        String dealClose = "+++++++++++++++++++++++++++++++" + '\r'  + '\n';
        dealClose += "Close deal" + '\r'  + '\n';
        dealClose += currentDateandTime + '\r' + '\n' + '\n';
        dealClose += "Table number : " + mDealNameId + '\r' + '\n';
        dealClose += AlwarshaApp.m.getName() + '\r' + '\n';
        ArrayList<String> printed = new ArrayList<String>();
        for (DealProduct d : deal.getmProducts()) {
            if (d.getStatus() == DealProduct.DealProductStatus.SENT) {
                for (DealProduct dd : deal.getmProducts()) {
                    Integer productCounter = sentProductsCounter.get(d.getmId());
                    if (productCounter != null) {
                        productCounter++;
                    } else {
                        productCounter = 1;
                    }
                    sentProductsCounter.put(d.getmId(), productCounter);
                }
                continue;
            }
            boolean found = false;
            for (String s : printed) {
                if (s.equals(d.getmName("EN"))) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                int sent = 0;
                if (sentProductsCounter.get(d.getmId()) != null) {
                    sent = sentProductsCounter.get(d.getmId());
                }
                int count = mProductsCounter.get(d.getmId()) - sent;
                dealClose += d.getmName("EN") + '\t' + count + '\r' + '\n';
                printed.add(d.getmName("EN"));
            }
            d.setStatus(DealProduct.DealProductStatus.SENT);
        }

        dealClose+="---- Total =  " + deal.getTotal() + '\r' + '\n';
        dealClose+="---- Total after discount =  " + String.valueOf(deal.getTotal() - deal.getTotal_discount()) + '\r' + '\n';


//        for (Map.Entry<DealProduct, Integer> entry : mProducts.entrySet()) {
//            DealProduct key = entry.getKey();
//            Integer value = entry.getValue();
//
//            if (key.getStatus() == DealProduct.DealProductStatus.ORDERED) {
//                mOrdersToSend += key.getmName("EN") + '\t' + value + '\r' + '\n';
//            }
//        }
        new Task().execute(dealClose);
    }

    private class Task extends
            AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            Log.d("fdsa", "fadf");
        }

        @Override
        protected Void doInBackground(String... arg0) {
            try {

                Log.d("FFF",arg0[0]);
                int textLength = arg0[0].length();

                Socket client = new Socket("192.168.1.19", 9100);

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
}
