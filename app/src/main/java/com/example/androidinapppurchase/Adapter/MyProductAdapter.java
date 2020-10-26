package com.example.androidinapppurchase.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.SkuDetails;
import com.example.androidinapppurchase.Interface.IProductClicListener;
import com.example.androidinapppurchase.MainActivity;
import com.example.androidinapppurchase.R;

import java.util.List;


public class MyProductAdapter extends RecyclerView.Adapter<MyProductAdapter.MyViewHolder>{
    MainActivity mainActivity;
    List<SkuDetails> skuDetailsList;
    BillingClient billingClient;

    public MyProductAdapter(MainActivity mainActivity, List<SkuDetails> skuDetailsList, BillingClient billingClient) {
        this.mainActivity = mainActivity;
        this.skuDetailsList = skuDetailsList;
        this.billingClient = billingClient;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(mainActivity.getBaseContext())
                .inflate(R.layout.layout_product_item, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.txt_product.setText(skuDetailsList.get(i).getTitle());

        //Product click
        myViewHolder.setiProductClicListener(new IProductClicListener() {
            @Override
            public void onProductClicListener(View view, int position) {
                //Launch Billing Folow
                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                        .setSkuDetails(skuDetailsList.get(i))
                        .build();
                billingClient.launchBillingFlow(mainActivity, billingFlowParams);
            }
        });
    }

    @Override
    public int getItemCount() {
        return skuDetailsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txt_product;
        IProductClicListener iProductClicListener;

        public void setiProductClicListener(IProductClicListener iProductClicListener) {
            this.iProductClicListener = iProductClicListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_product = (TextView)itemView.findViewById(R.id.txt_product_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            iProductClicListener.onProductClicListener(view, getAdapterPosition());
        }
    }
}
