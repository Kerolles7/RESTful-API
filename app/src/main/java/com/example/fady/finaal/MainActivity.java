package com.example.fady.finaal;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static java.text.NumberFormat.getInstance;

public class MainActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener, RecyclerAdapter.ClickListener {

   private String[] Categroy = { "Categories....","Women", "Tops"};

    private String LOG_TAG = "XML";
    private String prestaProd = "http://xtechlabs.net/commerce/api/products/";
    private String prestaKey = "?ws_key=6F3JYKJ7MWWT8KRB8IH86MYP32CWUR2G";
    private String prestaCateg= "http://xtechlabs.net/commerce/api/categories/";

    RecyclerView recyclerView;

    ArrayList<FeedCategory> ProductList = new ArrayList<>();
    ArrayList<FeedCategory> ProductList1 = new ArrayList<>();

    RecyclerAdapter adapter;
    String test;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);

        recyclerView =  findViewById(R.id.recyclerview);
      //  recyclerView.addItemDecoration(new VerticalSpace(15));


        Spinner spin = findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item, Categroy);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spin.setAdapter(aa);

    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (position) {

            case 0:

                Toast.makeText(getApplicationContext(), "Please select a category !!", Toast.LENGTH_LONG).show();

                break;

            case 1:

                if (ProductList.isEmpty()) {
                    getCategProd(String.valueOf(3));
                }
                else {
                    adapter = new RecyclerAdapter(MainActivity.this, ProductList);

                    recyclerView.setAdapter(adapter);
                    adapter.setClickListener(MainActivity.this);
                }

                break;


            case 2:
                   if(ProductList1.isEmpty()) {
                         getCategProd(String.valueOf(4));
                    }
                    else {
                       adapter = new RecyclerAdapter(MainActivity.this, ProductList1);

                       recyclerView.setAdapter(adapter);
                       adapter.setClickListener(MainActivity.this);
                   }
                break;
        }

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void getCategProd(String prodID){
        try {

            new GetXMLFromServer().execute(prodID).get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void itemClicked(View view, int position) {
        Intent intent1 = new Intent(MainActivity.this, ProductInfo.class);
        intent1.putExtra("id_prod", String.valueOf(position +1));
        startActivity(intent1);

    }


    private class GetXMLFromServer extends AsyncTask<String, Void, String> {

        HttpHandler nh;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {

            String id_prod = strings[0];
            test = id_prod;
            String URL = prestaCateg + id_prod + prestaKey ;
            String res = "";
            nh = new HttpHandler();
            InputStream is = nh.CallServer(URL);
            if (is != null) {

                res = nh.StreamToString(is);

            } else {
                res = "NotConnected";
            }

            return res;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (!isNetworkAvailable()) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(R.string.alert_message)
                        .setTitle(R.string.alert_title)
                        .setCancelable(false)
                        .setPositiveButton(R.string.alert_positive,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        finish();
                                    }
                                });

                AlertDialog alert = builder.create();
                alert.show();

            } else if (isNetworkAvailable()) {

                ParseXML(result);
            }
        }

    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    private void ParseXML(String xmlString){

        try {

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xmlString));
            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT){

                if(eventType== XmlPullParser.START_TAG){

                    String name = parser.getName();
                    if(name.equals("product")) {

                        parser.nextTag();

                        if (parser.next() == XmlPullParser.TEXT) {
                            String UpdateFlag = parser.getText();
                          //  Log.d(LOG_TAG, "product:" + UpdateFlag);

                            if (test== String.valueOf(3)) {
                                setProduct(UpdateFlag);
                            }else {setProduct1(UpdateFlag);}
                        }
                    }

                }else if(eventType== XmlPullParser.END_TAG){

                }
                eventType = parser.next();
            }

        }catch (Exception e){
            Log.d(LOG_TAG,"Error in ParseXML()",e);
        }

    }


    private void setProduct(String prodID){
        try {
            new ProductRequester().execute(prodID).get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void setProduct1(String prodID){
        try {
            new ProductRequester1().execute(prodID).get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }




    private class ProductRequester extends AsyncTask<String, Void, String> implements RecyclerAdapter.ClickListener
    {



        @Override
        protected String doInBackground(String... params) {

            try {
                find(params[0]);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        private void findProduct(String id) throws Exception {
            URL url = new URL(prestaProd + id + prestaKey);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();

            String id_prod = getXMLTag(doc, "id");
            String id_image = getXMLTag(doc, "id_default_image");
            String name = getXMLTag(doc, "name");

           String textprice = getXMLTag(doc, "price");
            BigDecimal price223 = new BigDecimal(textprice);
            String finalprice = String.valueOf(price223.setScale(2, RoundingMode.HALF_EVEN));

         //   Log.d(LOG_TAG, "name:" + name);
           // Log.d(LOG_TAG, "finalprice:" + finalprice);

                ProductList.add(new FeedCategory(name, finalprice, id_prod, id_image));

        }

        private void find(String id) throws Exception {
              findProduct(id);

        }

        private String getXMLTag(Document xml, String tag) {
            return xml.getElementsByTagName(tag).item(0).getFirstChild().getTextContent();
        }

        @Override
        protected void onPostExecute(String args) {
            super.onPostExecute(args);

            adapter = new RecyclerAdapter(MainActivity.this, ProductList);
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            recyclerView.setAdapter(adapter);
            adapter.setClickListener(this);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void itemClicked(View view, int position) {

            Intent intent1 = new Intent(MainActivity.this, ProductInfo.class);
            intent1.putExtra("id_prod", String.valueOf(position +1));
            startActivity(intent1);

        }
    }



    private class ProductRequester1 extends AsyncTask<String, Void, String> implements RecyclerAdapter.ClickListener
    {

        @Override
        protected String doInBackground(String... params) {

            try {
                find(params[0]);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        private void findProduct(String id) throws Exception {
            URL url = new URL(prestaProd + id + prestaKey);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();

            String id_prod1 = getXMLTag(doc, "id");
            String id_image1 = getXMLTag(doc, "id_default_image");
            String name1 = getXMLTag(doc, "name");

            String textprice = getXMLTag(doc, "price");
            BigDecimal price223 = new BigDecimal(textprice);
            String finalprice1 = String.valueOf(price223.setScale(2, RoundingMode.HALF_EVEN));

          //  Log.d(LOG_TAG, "name:" + name1);
          //  Log.d(LOG_TAG, "finalprice1:" + finalprice1);

            ProductList1.add(new FeedCategory(name1, finalprice1, id_prod1, id_image1));
        }

        private void find(String id) throws Exception {
            findProduct(id);

        }

        private String getXMLTag(Document xml, String tag) {
            return xml.getElementsByTagName(tag).item(0).getFirstChild().getTextContent();
        }

        @Override
        protected void onPostExecute(String args) {
            super.onPostExecute(args);

            adapter = new RecyclerAdapter(MainActivity.this, ProductList1);
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            recyclerView.setAdapter(adapter);

                adapter.setClickListener(this);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void itemClicked(View view, int position) {
            Intent intent2 = new Intent(MainActivity.this, ProductInfo.class);
            intent2.putExtra("id_prod", String.valueOf(position +1));
            startActivity(intent2);

        }
    }


}
