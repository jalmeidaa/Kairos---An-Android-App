package com.jalmeida.main;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jalmeida.R;
import com.jalmeida.data.Temperature;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by jalmeida on 4/24/16.
 */

public class CardViewActivity extends RecyclerView.Adapter<CardViewActivity.ViewHolder> {

    private static ArrayList<Temperature> cityTempValue;
    private TreeMap<String, Temperature> storeMap = MainActivity.storeMap;

    public CardViewActivity(ArrayList<Temperature> cityTemp) {
        cityTempValue = cityTemp;
    }

    @Override
    public CardViewActivity.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemLayoutView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.card_view, null);

        return new ViewHolder(itemLayoutView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final CardViewActivity.ViewHolder viewHolder, final int clickPosition) {

        final Temperature fp = cityTempValue.get(clickPosition);
        String celsius = Integer.toString(toCelsius(fp.getItem().getCondition().getTemp()));

        int id = fp.getItem().getCondition().getCode();
        int resourceId = MainActivity.getContext().getResources().getIdentifier("drawable/img_" + id, null, MainActivity.getContext().getPackageName());

        viewHolder.textViewCity.setText(fp.getLocation().getCity() + "," + fp.getLocation().getRegion() + ", " + fp.getLocation().getCountry());
        viewHolder.textViewCondition.setText(fp.getItem().getCondition().getText());
        viewHolder.textViewTemp.setText(celsius + (char) 0x00B0 + "C" + "/" + Integer.toString(fp.getItem().getCondition().getTemp()) + (char) 0x00B0 + fp.getUnits().getTemperature());
        Drawable weatherIconDrawable = MainActivity.getContext().getResources().getDrawable(resourceId);
        viewHolder.imageView.setImageDrawable(weatherIconDrawable);
        viewHolder.feed = fp;

        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(viewHolder.getAdapterPosition());
                storeMap.remove(fp.getTitle());
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewTemp;
        public TextView textViewCondition;
        public TextView textViewCity;
        public ImageView imageView;
        public ImageButton deleteButton;
        public Temperature feed;

        public ViewHolder(final View itemLayoutView) {
            super(itemLayoutView);

            textViewTemp = (TextView) itemLayoutView.findViewById(R.id.textViewTemp);
            textViewCity = (TextView) itemLayoutView.findViewById(R.id.textViewCity);
            textViewCondition = (TextView) itemLayoutView.findViewById(R.id.textViewCondition);
            imageView = (ImageView) itemLayoutView.findViewById(R.id.imageView);
            deleteButton = (ImageButton) itemLayoutView.findViewById(R.id.deleteButton);

            itemLayoutView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.getContext(), textViewCity.getText(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return cityTempValue.size();
    }

    public void deleteItem(int index) {
        cityTempValue.remove(index);
        notifyItemRemoved(index);
        notifyItemRangeRemoved(index, getItemCount());
    }

    public static int toCelsius(int fahrenheit) {
        return (fahrenheit - 32) * 5 / 9;
    }

}
