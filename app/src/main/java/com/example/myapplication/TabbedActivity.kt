package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class TabbedActivity : AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_tabbed)

            val viewPager = findViewById<ViewPager2>(R.id.viewPager)
            val tabLayout = findViewById<TabLayout>(R.id.tabLayout)

            viewPager.adapter = MyTabAdapter(this)

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = "Tab ${position + 1}"
            }.attach()
        }
    }

class MyTabAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 1

    override fun createFragment(position: Int): Fragment {
        return MovedFragment()
    }
}

