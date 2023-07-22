package com.example.contact_list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val backgroundFragment = ContactsFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.navHost, backgroundFragment)
            .commit()

    }


}
