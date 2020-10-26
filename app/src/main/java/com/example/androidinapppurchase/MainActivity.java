package com.example.androidinapppurchase;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.example.androidinapppurchase.Adapter.MyProductAdapter;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PurchasesUpdatedListener {

    BillingClient billingClient;

    Button loadProduct;
    RecyclerView recyclerProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupBilldingClient();

        //View
        loadProduct = (Button)findViewById(R.id.btn_load_product);
        recyclerProduct = (RecyclerView)findViewById(R.id.recycler_product);
        recyclerProduct.setHasFixedSize(true);
        recyclerProduct.setLayoutManager(new LinearLayoutManager(this));

        //Event
        loadProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (billingClient.isReady()){
                    SkuDetailsParams params = SkuDetailsParams.newBuilder()
                            .setSkusList(Arrays.asList("compracap8")) // Cambiar *exactrly product id* por el del producto. compracap8 es un ID de prueba
                            .setType(BillingClient.SkuType.INAPP)
                            .build();
                    billingClient.querySkuDetailsAsync(params, new SkuDetailsResponseListener() {
                        @Override
                        public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
                            if (billingResult.equals(BillingClient.BillingResponseCode.OK)){
                                loadProductToRecyclerView(skuDetailsList);
                            }
                            else {
                                Toast.makeText(MainActivity.this, "Cannot query product", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(MainActivity.this, "Billing client not ready", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadProductToRecyclerView(List<SkuDetails> skuDetailsList) {
        MyProductAdapter adapter = new MyProductAdapter(this, skuDetailsList, billingClient);
        recyclerProduct.setAdapter(adapter);
    }

    private void setupBilldingClient() {
        billingClient = BillingClient.newBuilder(this).setListener(this).build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.equals(BillingClient.BillingResponseCode.OK)){
                    Toast.makeText(MainActivity.this, "Success to connect to Billing", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this, ""+billingResult, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                Toast.makeText(MainActivity.this, "You are disconnected from Billing", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {
        Toast.makeText(this, "Purchase item"+purchases.size(), Toast.LENGTH_SHORT).show();
    }
}