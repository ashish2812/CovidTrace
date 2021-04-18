package com.educationhub.covidtrace.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.educationhub.covidtrace.R;

public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    int image[] = {
            R.drawable.pie_chart,
            R.drawable.realtime_update,
            R.drawable.news_corona,
            R.drawable.placeicon,
            R.drawable.madeinindia


    };
    int title[] = {R.string.pie_chart,
            R.string.real_time_update,
            R.string.corona_news_update,
            R.string.location,
            R.string.made_in_india};

    int descriptions[] = {R.string.description,
            R.string.real_time_update_Description,
            R.string.corona_news_description,
            R.string.location_description,
            R.string.made_with_in_india};

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider, container, false);

        ImageView imageView = view.findViewById(R.id.imageView);
        TextView heading = view.findViewById(R.id.sliderHeading);
        TextView description = view.findViewById(R.id.sliderDescription);

        imageView.setImageResource(image[position]);
        heading.setText(title[position]);
        description.setText(descriptions[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }
}
