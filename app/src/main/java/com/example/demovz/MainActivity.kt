package com.example.demovz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.demovz.adapter.GroupListAdapter
import com.example.demovz.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), GroupListAdapter.OnItemClickListener {
    var binding :ActivityMainBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val grpAdapter = GroupListAdapter(arrayOf("Movie Time","Group 2","Group3")).apply{ setOnClickListener(this@MainActivity) }
        binding?.rvGrp?.adapter=grpAdapter

    }

    override fun onClicked(groupName:String) {
        startActivity(Intent(this,GroupDetailActivity::class.java).putExtra("GROUP_NAME",groupName))
    }
}