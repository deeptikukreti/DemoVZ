package com.example.demovz.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.demovz.R
import com.example.demovz.databinding.ActivityMainBinding
import com.example.demovz.db.devices.Area
import com.example.demovz.db.devices.DevicesRoomDb
import com.example.demovz.db.events.Device
import com.example.demovz.util.ArrayListConverter
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        addDeviceData()
        val navView: BottomNavigationView? = binding?.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView?.setupWithNavController(navController)

    }
    private fun addDeviceData(){
        val db=DevicesRoomDb.getInstance(applicationContext).areaDeviceDao()
        if(db.getAllAreaWithDevices()!=null && db.getAllAreaWithDevices().isEmpty()) {
            val deviceList1 = ArrayList<Device>()
            deviceList1.add(Device("Living Room AC", false))
            deviceList1.add(Device("Living Room Led Lights", false))
            deviceList1.add(Device("Living Room Tube Light", false))
            deviceList1.add(Device("Living Room Fan", false))
            deviceList1.add(Device("TV", false))
            val area1 =
                Area(1, "Living Room", ArrayListConverter().fromStringArrayList(deviceList1))
            db.insert(area1)

            val deviceList2 = ArrayList<Device>()
            deviceList2.add(Device("Bed Room AC", false))
            deviceList2.add(Device("Bed Room Led Lights", false))
            deviceList2.add(Device("Bed Room Tube Light", false))
            deviceList2.add(Device("Bed Room Fan", false))
            val area2 = Area(2, "Bed Room", ArrayListConverter().fromStringArrayList(deviceList2))
            db.insert(area2)

            val deviceList3 = ArrayList<Device>()
            deviceList3.add(Device("Kitchen Lights", false))
            deviceList3.add(Device("Chimney", false))
            deviceList3.add(Device("Refrigerator", false))
            val area3 = Area(3, "Kitchen", ArrayListConverter().fromStringArrayList(deviceList3))
            db.insert(area3)

            val deviceList4 = ArrayList<Device>()
            deviceList4.add(Device("Lights", false))
            deviceList4.add(Device("Fan", false))
            val area4 =
                Area(4, "Dining Area", ArrayListConverter().fromStringArrayList(deviceList4))
            db.insert(area4)

            val deviceList5 = ArrayList<Device>()
            deviceList5.add(Device("Light1", false))
            deviceList5.add(Device("Light2", false))
            val area5 =
                Area(5, "Staircase", ArrayListConverter().fromStringArrayList(deviceList5))
            db.insert(area5)

            val deviceList6 = ArrayList<Device>()
            deviceList6.add(Device("Ceiling Light", false))
            deviceList6.add(Device("Hall Light1", false))
            deviceList6.add(Device("Hall Light2", false))
            deviceList6.add(Device("Hall Light3", false))
            deviceList6.add(Device("Hall Light4", false))
            val area6 =
                Area(6, "Center Hall", ArrayListConverter().fromStringArrayList(deviceList6))
            db.insert(area6)
        }
    }

}