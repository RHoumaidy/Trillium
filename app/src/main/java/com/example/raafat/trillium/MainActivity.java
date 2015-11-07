package com.example.raafat.trillium;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.raafat.activities.LoginActivity;
import com.example.raafat.activities.OrginSiteActivity;
import com.example.raafat.activities.TrucksActivity;
import com.example.raafat.model.DestinationSite;
import com.example.raafat.model.Driver;
import com.example.raafat.model.OrginSite;
import com.example.raafat.model.OriginRecord;
import com.example.raafat.model.Truck;
import com.example.raafat.model.TruckDrivers;
import com.example.raafat.model.User;
import com.example.raafat.model.WorkShift;

import org.apache.http.impl.conn.LoggingSessionInputBuffer;

import java.sql.Time;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        User user1 = new User(22, "admin1", "admin1", true, false, false);
        User user2 = new User(33, "admin2", "admin2", false, true, false);
        User user3 = new User(44, "admin3", "admin3", false, false, true);

        user1.saveCurrent(MyApplication.db);
        user2.saveCurrent(MyApplication.db);
        user3.saveCurrent(MyApplication.db);

        Driver driver1 = new Driver(33, "DRIVER 1", "http://f1.imgci.com/PICTURES/CMS/28500/28513.jpg");
        Driver driver2 = new Driver(44, "DRIVER 2", "http://f1.imgci.com/PICTURES/CMS/28500/28513.jpg");
        Driver driver3 = new Driver(55, "DRIVER 3", "http://f1.imgci.com/PICTURES/CMS/28500/28513.jpg");
        Driver driver5 = new Driver(66, "DRIVER 5", "http://f1.imgci.com/PICTURES/CMS/28500/28513.jpg");
        Driver driver6 = new Driver(67, "DRIVER 6", "http://f1.imgci.com/PICTURES/CMS/28500/28513.jpg");
        Driver driver7 = new Driver(68, "DRIVER 7", "http://f1.imgci.com/PICTURES/CMS/28500/28513.jpg");
        Driver driver8 = new Driver(69, "DRIVER 8", "http://f1.imgci.com/PICTURES/CMS/28500/28513.jpg");
        Driver driver9 = new Driver(60, "DRIVER 9", "http://f1.imgci.com/PICTURES/CMS/28500/28513.jpg");
        Driver driver10 = new Driver(61, "DRIVER 10", "http://f1.imgci.com/PICTURES/CMS/28500/28513.jpg");
        Driver driver11 = new Driver(62, "DRIVER 11", "http://f1.imgci.com/PICTURES/CMS/28500/28513.jpg");
        Driver driver12 = new Driver(63, "DRIVER 12", "http://f1.imgci.com/PICTURES/CMS/28500/28513.jpg");
        Driver driver13 = new Driver(64, "DRIVER 13", "http://f1.imgci.com/PICTURES/CMS/28500/28513.jpg");
        Driver driver14 = new Driver(65, "DRIVER 14", "http://f1.imgci.com/PICTURES/CMS/28500/28513.jpg");
        Driver driver15 = new Driver(70, "DRIVER 15", "http://f1.imgci.com/PICTURES/CMS/28500/28513.jpg");
        Driver driver16 = new Driver(71, "DRIVER 16", "http://f1.imgci.com/PICTURES/CMS/28500/28513.jpg");
        Driver driver17 = new Driver(72, "DRIVER 17", "http://f1.imgci.com/PICTURES/CMS/28500/28513.jpg");
        Driver driver18 = new Driver(73, "DRIVER 18", "http://f1.imgci.com/PICTURES/CMS/28500/28513.jpg");
        Driver driver19 = new Driver(74, "DRIVER 19", "http://f1.imgci.com/PICTURES/CMS/28500/28513.jpg");
        Driver driver20 = new Driver(75, "DRIVER 20", "http://f1.imgci.com/PICTURES/CMS/28500/28513.jpg");
        Driver driver21 = new Driver(76, "DRIVER 21", "http://f1.imgci.com/PICTURES/CMS/28500/28513.jpg");
        Driver driver22 = new Driver(77, "DRIVER 22", "http://f1.imgci.com/PICTURES/CMS/28500/28513.jpg");
        Driver driver23 = new Driver(78, "DRIVER 23", "http://f1.imgci.com/PICTURES/CMS/28500/28513.jpg");
        Driver driver24 = new Driver(79, "DRIVER 24", "http://f1.imgci.com/PICTURES/CMS/28500/28513.jpg");

        driver1.saveCurrent(MyApplication.db);
        driver2.saveCurrent(MyApplication.db);
        driver3.saveCurrent(MyApplication.db);
        driver5.saveCurrent(MyApplication.db);
        driver6.saveCurrent(MyApplication.db);
        driver7.saveCurrent(MyApplication.db);
        driver8.saveCurrent(MyApplication.db);
        driver9.saveCurrent(MyApplication.db);
        driver10.saveCurrent(MyApplication.db);
        driver11.saveCurrent(MyApplication.db);
        driver12.saveCurrent(MyApplication.db);
        driver13.saveCurrent(MyApplication.db);
        driver14.saveCurrent(MyApplication.db);
        driver15.saveCurrent(MyApplication.db);
        driver16.saveCurrent(MyApplication.db);
        driver17.saveCurrent(MyApplication.db);
        driver18.saveCurrent(MyApplication.db);
        driver19.saveCurrent(MyApplication.db);
        driver20.saveCurrent(MyApplication.db);
        driver21.saveCurrent(MyApplication.db);
        driver22.saveCurrent(MyApplication.db);
        driver23.saveCurrent(MyApplication.db);
        driver24.saveCurrent(MyApplication.db);

        Truck truck1 = new Truck(33, "TRUCK 1");
        Truck truck2 = new Truck(44, "TRUCK 3");
        Truck truck3 = new Truck(55, "TRUCK 4");
        Truck truck4 = new Truck(66, "TRUCK 5");
        Truck truck5 = new Truck(62, "TRUCK 6");
        Truck truck6 = new Truck(61, "TRUCK 7");
        Truck truck7 = new Truck(63, "TRUCK 8");
        Truck truck8 = new Truck(64, "TRUCK 9");
        Truck truck9 = new Truck(65, "TRUCK 10");
        Truck truck10 = new Truck(67, "TRUCK 11");
        Truck truck11 = new Truck(68, "TRUCK 12");
        Truck truck12 = new Truck(69, "TRUCK 13");
        Truck truck13 = new Truck(60, "TRUCK 14");
        Truck truck14 = new Truck(74, "TRUCK 15");
        Truck truck15 = new Truck(664, "TRUCK 16");
        Truck truck16 = new Truck(6656, "TRUCK 17");
        Truck truck17 = new Truck(669, "TRUCK 18");
        Truck truck18 = new Truck(6698, "TRUCK 19");
        Truck truck19 = new Truck(6678, "TRUCK 20");
        Truck truck20 = new Truck(668, "TRUCK 21");
        Truck truck21 = new Truck(6684, "TRUCK 22");
        Truck truck22 = new Truck(6699, "TRUCK 23");
        Truck truck23 = new Truck(66845, "TRUCK 24");
        Truck truck24 = new Truck(665, "TRUCK 25");
        Truck truck25 = new Truck(6641, "TRUCK 26");
        Truck truck26 = new Truck(66852, "TRUCK 27");
        Truck truck27 = new Truck(664446, "TRUCK 28");
        Truck truck28 = new Truck(66001, "TRUCK 29");
        Truck truck29 = new Truck(667, "TRUCK 30");

        truck1.saveCurrent(MyApplication.db);
        truck2.saveCurrent(MyApplication.db);
        truck3.saveCurrent(MyApplication.db);
        truck4.saveCurrent(MyApplication.db);
        truck5.saveCurrent(MyApplication.db);
        truck6.saveCurrent(MyApplication.db);
        truck7.saveCurrent(MyApplication.db);
        truck8.saveCurrent(MyApplication.db);
        truck9.saveCurrent(MyApplication.db);
        truck10.saveCurrent(MyApplication.db);
        truck11.saveCurrent(MyApplication.db);
        truck12.saveCurrent(MyApplication.db);
        truck13.saveCurrent(MyApplication.db);
        truck14.saveCurrent(MyApplication.db);
        truck15.saveCurrent(MyApplication.db);
        truck16.saveCurrent(MyApplication.db);
        truck17.saveCurrent(MyApplication.db);
        truck18.saveCurrent(MyApplication.db);
        truck19.saveCurrent(MyApplication.db);
        truck20.saveCurrent(MyApplication.db);
        truck21.saveCurrent(MyApplication.db);
        truck22.saveCurrent(MyApplication.db);
        truck23.saveCurrent(MyApplication.db);
        truck24.saveCurrent(MyApplication.db);
        truck25.saveCurrent(MyApplication.db);
        truck26.saveCurrent(MyApplication.db);
        truck27.saveCurrent(MyApplication.db);
        truck28.saveCurrent(MyApplication.db);
        truck29.saveCurrent(MyApplication.db);


//        TruckDrivers truckDrivers1 = new TruckDrivers(0, 33, 33);
//        TruckDrivers truckDrivers2 = new TruckDrivers(2, 44, 44);
//        TruckDrivers truckDrivers3 = new TruckDrivers(3, 55, 55);
//        TruckDrivers truckDrivers4 = new TruckDrivers(4, 66, 66);


//        truckDrivers1.saveCurrent(MyApplication.db);
//        truckDrivers2.saveCurrent(MyApplication.db);
//        truckDrivers3.saveCurrent(MyApplication.db);
        //truckDrivers4.saveCurrent(MyApplication.db);

        OrginSite orginSite1 = new OrginSite(33, "ORGINE SITE 1");
        OrginSite orginSite2 = new OrginSite(44, "ORGINE SITE 2");
        OrginSite orginSite3 = new OrginSite(55, "ORGINE SITE 3");
        OrginSite orginSite4 = new OrginSite(66, "ORGINE SITE 4");
        OrginSite orginSite5 = new OrginSite(77, "ORGINE SITE 5");

        orginSite1.saveCurrent(MyApplication.db);
        orginSite2.saveCurrent(MyApplication.db);
        orginSite3.saveCurrent(MyApplication.db);
        orginSite4.saveCurrent(MyApplication.db);
        orginSite5.saveCurrent(MyApplication.db);

        DestinationSite destinationSite1 = new DestinationSite(33, "DESTINATION SITE 1");
        DestinationSite destinationSite2 = new DestinationSite(44, "DESTINATION SITE 2");
        DestinationSite destinationSite3 = new DestinationSite(55, "DESTINATION SITE 3");
        DestinationSite destinationSite4 = new DestinationSite(66, "DESTINATION SITE 4");
        DestinationSite destinationSite5 = new DestinationSite(77, "DESTINATION SITE 5");

        destinationSite1.saveCurrent(MyApplication.db);
        destinationSite2.saveCurrent(MyApplication.db);
        destinationSite3.saveCurrent(MyApplication.db);
        destinationSite4.saveCurrent(MyApplication.db);
        destinationSite5.saveCurrent(MyApplication.db);


        Time time = new Time(System.currentTimeMillis());
        WorkShift shift1 = new WorkShift(1, "Day", "06:00", "15:00");
        WorkShift shift2 = new WorkShift(2, "Night", "15:00", "06:00");

        shift1.saveCurrent(MyApplication.db);
        shift2.saveCurrent(MyApplication.db);


//        OriginRecord originRecord1 = new OriginRecord(33, 33, 33, System.currentTimeMillis(), System.currentTimeMillis(), 22);
//        OriginRecord originRecord2 = new OriginRecord(13, 33, 44, System.currentTimeMillis(), System.currentTimeMillis(), 44);
//        OriginRecord originRecord3 = new OriginRecord(23, 44, 44, System.currentTimeMillis(), System.currentTimeMillis(), 33);
//        OriginRecord originRecord4 = new OriginRecord(43, 44, 55, System.currentTimeMillis(), System.currentTimeMillis(), 44);
//        OriginRecord originRecord5 = new OriginRecord(53, 55, 66, System.currentTimeMillis(), System.currentTimeMillis(), 22);
//        OriginRecord originRecord6 = new OriginRecord(63, 55, 33, System.currentTimeMillis(), System.currentTimeMillis(), 44);
//        OriginRecord originRecord7 = new OriginRecord(73, 33, 66, System.currentTimeMillis(), System.currentTimeMillis(), 22);
//        OriginRecord originRecord8 = new OriginRecord(83, 55, 44, System.currentTimeMillis(), System.currentTimeMillis(), 22);
//        OriginRecord originRecord9 = new OriginRecord(903, 33, 33, System.currentTimeMillis(), System.currentTimeMillis(), 33);
//        OriginRecord originRecord10 = new OriginRecord(93, 44, 77, System.currentTimeMillis(), System.currentTimeMillis(), 22);

//        originRecord1.saveCurrent(MyApplication.db);
//        originRecord2.saveCurrent(MyApplication.db);
//        originRecord3.saveCurrent(MyApplication.db);
//        originRecord4.saveCurrent(MyApplication.db);
//        originRecord5.saveCurrent(MyApplication.db);
//        originRecord6.saveCurrent(MyApplication.db);
//        originRecord7.saveCurrent(MyApplication.db);
//        originRecord8.saveCurrent(MyApplication.db);
//        originRecord9.saveCurrent(MyApplication.db);
//        originRecord10.saveCurrent(MyApplication.db);


        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("ACTIVTY_FLAG", 0);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_exit) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
