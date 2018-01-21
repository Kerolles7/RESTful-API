package com.example.fady.finaal;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ProductInfo extends AppCompatActivity {

    public String image = "http://xtechlabs.net/commerce/api/images/products/";
    private String prestaProd = "http://xtechlabs.net/commerce/api/products/";
    private String prestaKey = "?ws_key=6F3JYKJ7MWWT8KRB8IH86MYP32CWUR2G";

    ProgressDialog mProgressDialog;

    TextView name, descrip, price, wholePrice,
    date, condit, availb, info;

    FeedProduct feedProduct;

    GridView mgrid;
    ImageView image1;

    ArrayList<FeedImage> ImageUrl = new ArrayList<>();

    ImageApabter apabter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        mgrid = findViewById(R.id.gridView1);
        image1 = findViewById(R.id.imageView3);
        name = findViewById(R.id.txtName);
        descrip = findViewById(R.id.txtDescrip);
        price = findViewById(R.id.txtPrice);
        wholePrice = findViewById(R.id.txtSalePrice);
        date = findViewById(R.id.txtDate);
        condit = findViewById(R.id.txtCondit);
        availb = findViewById(R.id.txtAvailb);
        info = findViewById(R.id.txtInfo);

        String ProdId = getIntent().getStringExtra("id_prod");
        new getProductInfo().execute(ProdId);

    }


    public class getProductInfo extends AsyncTask<String, Void, String>
    {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            mProgressDialog = new ProgressDialog(ProductInfo.this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);

            mProgressDialog.show();
        }

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

        private void findProductSimple(String id) throws Exception {
            URL url = new URL(prestaProd + id + prestaKey);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();


            String id_product = getXMLTag(doc, "id");
            String prod_name = getXMLTag(doc, "name");
            String prod_descp = getXMLTag(doc, "description_short");

            String textprice = getXMLTag(doc, "price");
            BigDecimal price223 = new BigDecimal(textprice);
            String prod_price = String.valueOf(price223.setScale(2, RoundingMode.HALF_EVEN));

            String textpric1e = getXMLTag(doc, "wholesale_price");
            BigDecimal price2231 = new BigDecimal(textpric1e);
            String prod_wholePice = String.valueOf(price2231.setScale(2, RoundingMode.HALF_EVEN));


            String prod_date = getXMLTag(doc, "date_add");
            String prod_condit = getXMLTag(doc, "condition");
            String prod_availb = getXMLTag(doc, "available_now");
            String prod_info = getXMLTag(doc, "description");


            feedProduct = new FeedProduct(id_product,prod_name,prod_descp,
                    prod_price,prod_wholePice,prod_date,prod_condit,prod_availb,prod_info);

            NodeList nList = doc.getElementsByTagName("image");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    String  value = getValue("id", eElement);

                    String imageUrl1 = image + id_product + "/" + value + prestaKey;


                    ImageUrl.add(new FeedImage(imageUrl1));

                }


            }

        }

        @Override
        protected void onPostExecute(String args) {
            super.onPostExecute(args);

            name.setText(feedProduct.getName());
            descrip.setText(android.text.Html.fromHtml(feedProduct.getDescription()));
            price.setText(feedProduct.getPrice());
            wholePrice.setText(feedProduct.getWholesale_price());
            date.setText(feedProduct.getDate_add());
            condit.setText(feedProduct.getCondition());
            availb.setText(feedProduct.getAvailble_now());
            info.setText(android.text.Html.fromHtml(feedProduct.getProd_info()));

            apabter = new ImageApabter(ProductInfo.this,ImageUrl);

            mgrid.setAdapter(apabter);

            mgrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Picasso.with(ProductInfo.this).load(ImageUrl.get(position).getUrl())
                            .placeholder(R.drawable.placeholder)
                            .fit()
                            .centerCrop().into(image1);

                }
            });

            Picasso.with(ProductInfo.this).load(ImageUrl.get(0).getUrl())
                    .placeholder(R.drawable.placeholder)
                    .fit()
                    .centerCrop().into(image1);
            

            mProgressDialog.dismiss();
        }

        private  String getValue(String sTag, Element eElement) {

            NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
                    .getChildNodes();
            Node nValue =  nlList.item(0);
            return nValue.getNodeValue();
        }


        private void find(String id) throws Exception {
            findProductSimple(id);
        }

        private String getXMLTag(Document xml, String tag) {
            return xml.getElementsByTagName(tag).item(0).getFirstChild().getTextContent();
        }


    }

    public class FeedImage {

        String url;
        public FeedImage(String url)
        {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }

    }




    }

