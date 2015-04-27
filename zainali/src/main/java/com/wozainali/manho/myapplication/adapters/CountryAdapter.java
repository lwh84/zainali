package com.wozainali.manho.myapplication.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wozainali.manho.myapplication.R;
import com.wozainali.manho.myapplication.kml.Placemark;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CountryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Placemark> placemarks;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolderCountry(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.textview_adapter_country,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        Placemark placemark = placemarks.get(position);
        String countryName = placemark.getName();
        ((ViewHolderCountry)viewHolder).countryName.setText(countryName);
    }

    @Override
    public int getItemCount() {
        return placemarks.size();
    }

    public static class ViewHolderCountry extends RecyclerView.ViewHolder {
        TextView countryName;

        public ViewHolderCountry(View itemView) {
            super(itemView);
            countryName = (TextView) itemView.findViewById(R.id.country_name);
        }
    }

    public ArrayList<Placemark> getPlacemarks() {
        return placemarks;
    }

    public void setPlacemarks(ArrayList<Placemark> placemarks) {
        this.placemarks = placemarks;
        notifyDataSetChanged();
    }
}
