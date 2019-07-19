package com.example.firebaseauthenticationtest;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class VO_MemberActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    int []imgs = {R.drawable.mohila1, R.drawable.briddho1, R.drawable.mrito1, R.drawable.purush2, R.drawable.purush1, R.drawable.briddho1 };
    int []clrs = {R.color.colorPrimaryDark, R.color.below_good, R.color.risky, R.color.good, R.color.colorPrimaryDark};
    String []VO = {"Halima Begum", "Sushmita Sen", "Rezowan Talukder", "Nafsan Mia", "Elias Kanchon", "Zerina Begum"};
    int []info = {30, 50, 15, 12, 37, 28};

    ListView listView;
    ImageView img;
    TextView vo, voC;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volist);



        listView = findViewById(R.id.listV);
        CustomAdapter cAdapter = new CustomAdapter();
        listView.setAdapter(cAdapter);
        listView.setOnItemClickListener((AdapterView.OnItemClickListener) this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(this, A_MemberActivity.class));
    }

    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return VO.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }



        @SuppressLint("ResourceAsColor")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = getLayoutInflater().inflate(R.layout.custom_layout, parent, false);

            //position--;
            //img.setImageResource(clr[position]);
            vo = (TextView) view.findViewById(R.id.textView1);
            voC = (TextView) view.findViewById(R.id.textView1_count);
            img = (ImageView) view.findViewById(R.id.imageView1);

            img.setImageResource(imgs[position]);
            //img.setImageResource(clrs[position]);
            vo.setText(VO[position]);
            voC.setText("Loan taken: " + info[position] + " Thousand BDT");

            if(position == 2){
                view.setBackgroundColor(R.color.below_good);
            }
            else if(position == 4){
                view.setBackgroundColor(R.color.colorPrimaryDark);
            }
            else if(position == 4){
                view.setBackgroundColor(R.color.risky);
            }
            else{
                view.setBackgroundColor(R.color.good);

            }


            return view;
        }
    }
}
