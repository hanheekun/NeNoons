package com.pixelro.nenoons.test;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import com.pixelro.nenoons.R;

public class Adapter extends PagerAdapter {

    private int[] images = {R.drawable.test_01_dis_02,
            R.drawable.test_01_dis_01,
            R.drawable.test_guide_03};

    private int[] dots = {R.drawable.dot_3_1,
            R.drawable.dot_3_2,
            R.drawable.dot_3_3};

    private int[] descriptions = {R.string.test_guide_02,R.string.test_guide_03,R.string.test_guide_04};


    private LayoutInflater inflater;
    private Context context;

    public Adapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ConstraintLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.view_test_guide, container, false);
        ImageView imageView = (ImageView)v.findViewById(R.id.imageView_test_first_guide);
        imageView.setImageResource(images[position]);
        ImageView imageViewDot = (ImageView)v.findViewById(R.id.imageView_test_first_guide_dot);
        imageViewDot.setImageResource(dots[position]);
        TextView textView = (TextView)v.findViewById(R.id.textView_test_first_guide);
        textView.setText(descriptions[position]);
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.invalidate();
    }
}