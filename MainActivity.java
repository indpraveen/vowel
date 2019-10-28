package com.pkg.praveentrial;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Product> productList,productList2;
    TextView roughBut,semiRough,placeOrder;
    TextView pName,pMrp,pPages,pDiscount,pFinalPrice,amtTill;
    private ProductAdapter productAdapter;
    private ListView listView;
    private  ArrayList<OrderedProduct> orderedProduct;
    Button submitBut;
    Spinner qtyOrder;
    RecyclerView recyclerView,recyclerView1;
    private Recycler_View_Adapter recycler_view_adapter,recycler_view_adapter1;
    ArrayList<OrderedProduct> orderedProducts;
    String text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        roughBut=(TextView)findViewById(R.id.rough_but);
        semiRough=(TextView)findViewById(R.id.semi_rough);
        amtTill=findViewById(R.id.tot_amtTill);

        pName=(TextView)findViewById(R.id.p_name);
        pMrp=(TextView)findViewById(R.id.p_mrp);
        pPages=(TextView)findViewById(R.id.p_page);
        pDiscount=(TextView)findViewById(R.id.p_discount);
        pFinalPrice=(TextView)findViewById(R.id.p_final_price);
        qtyOrder=(Spinner)findViewById(R.id.qty_order);

        recyclerView1=(RecyclerView)findViewById(R.id.recycleview1);
        recyclerView=(RecyclerView)findViewById(R.id.recycleview);

        productList=new ArrayList<>();
        productList2=new ArrayList<>();

        orderedProduct=new ArrayList<>();
        submitBut=(Button)findViewById(R.id.submit_order);
        placeOrder=(TextView)findViewById(R.id.placeOrder);



        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if((recycler_view_adapter!=null)&&(recycler_view_adapter.orderedItemString() != null)&&(recycler_view_adapter1!=null)&&(recycler_view_adapter1.orderedItemString() != null)) {
                    text = recycler_view_adapter.orderedItemString();
                    text = text+recycler_view_adapter1.orderedItemString();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Cart is Empty", Toast.LENGTH_SHORT).show();
                }

                if((text!=null)){

                    try {

                        String toNumber = "917509189668";
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+toNumber +"&text="+text));
                        startActivity(intent);
                        finish();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }

                ArrayList<OrderedProduct> orderedProducts = recycler_view_adapter.getOrderList();
                if(recycler_view_adapter1!=null) {
                    orderedProducts.addAll(recycler_view_adapter1.getOrderList());
                }
                Intent inty = new Intent(MainActivity.this,Order_screen.class);
              Bundle args = new Bundle();

                args.putSerializable("ARRAYLIST",(Serializable)orderedProducts);
                inty.putParcelableArrayListExtra("list",orderedProducts);
                inty.putExtra("BUNDLE",args);
                //startActivity(inty);
                try {
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getApplicationContext().openFileOutput("config.txt", Context.MODE_PRIVATE));

                    for (int i = 0; i < orderedProducts.size(); i++) {
                        outputStreamWriter.write(orderedProducts.get(i).getpName() + " ");
                        outputStreamWriter.write(orderedProducts.get(i).getpMrp() + " ");
                        outputStreamWriter.write(orderedProducts.get(i).getOrderedQty() + " ");
                        outputStreamWriter.write(orderedProducts.get(i).getpFinalPrice());
                        // outputStreamWriter.write(object.get(i).getAmt());
                        outputStreamWriter.write("\n");

                    }
                    outputStreamWriter.close();
                    Toast.makeText(getApplicationContext(), "Item added to File", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                Log.e("Exception", "File write failed: " + e.toString());
            }

           /*   Intent intent=new Intent(MainActivity.this,Order_screen.class);
                intent.putParcelableArrayListExtra("list",orderedProducts);
              startActivity(intent);*/


            }

        });

    }




    @Override
    public void onResume(){
        super.onResume();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()) {
            case R.id.action_create_order:

               /* ArrayList<OrderedProduct> object = new ArrayList<>();
                object.addAll(recycler_view_adapter1.getOrderList());
                object.addAll(recycler_view_adapter.getOrderList());
                Intent inty = new Intent(MainActivity.this, Order_screen.class);
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST", (Serializable) object);
                inty.putExtra("BUNDLE", args);
                startActivity(inty);*/


                return false;
        }
                 return super.onOptionsItemSelected(menuItem);

    }



 public void showRough(View view){

     try {
         InputStream is=getAssets().open("free.txt");
         BufferedReader br=new BufferedReader(new InputStreamReader(is));
         String line=br.readLine();
         while(line!= null){
             String[] lineArraye=line.split("\\s+");
             productList.add(new Product(lineArraye[0],lineArraye[1],lineArraye[2],lineArraye[3],lineArraye[4],R.mipmap.notebook));
            line=br.readLine();
         }
            br.close();
         is.close();





     }catch (IOException e){}

     //productAdapter=new ProductAdapter(this,productList);
     recycler_view_adapter=new Recycler_View_Adapter(this,productList);

     recyclerView.setAdapter(recycler_view_adapter);
     roughBut.setEnabled(false);


 }

 public void showSemi(View view){

     try {
         InputStream is=getAssets().open("semirough.txt");

         BufferedReader br=new BufferedReader(new InputStreamReader(is));
         String line=br.readLine();
         while(line!= null){
             String[] lineArraye=line.split("\\s+");
              productList2.add(new Product(lineArraye[0],lineArraye[1],lineArraye[2],lineArraye[3],lineArraye[4],R.mipmap.notebook));
             line=br.readLine();
         }
         br.close();
         is.close();

     }catch (IOException e){}


     recycler_view_adapter1=new Recycler_View_Adapter(this,productList2);

     recyclerView1.setAdapter(recycler_view_adapter1);
    semiRough.setEnabled(false);


 }



}
