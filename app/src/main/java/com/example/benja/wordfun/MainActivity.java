package com.example.benja.wordfun;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.benja.wordfun.setlist.SetListFragment;


public class MainActivity extends AppCompatActivity {
    FrameLayout main_frame;
    Fragment setListFragment,curFragment;
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFragment();
    }

    private void initFragment(){
        main_frame=findViewById(R.id.main_frame);
        setListFragment=new SetListFragment();
        curFragment=setListFragment;
        fragmentManager=getSupportFragmentManager();

        fragmentManager.beginTransaction().add(R.id.main_frame,curFragment,curFragment.getClass().getName()).commit();

    }

}
