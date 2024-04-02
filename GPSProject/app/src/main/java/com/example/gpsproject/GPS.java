package com.example.gpsproject;

import android.location.Location;
import java.io.Serializable;

public class GPS implements Serializable  {
        private String address;
        private String timeSpent;
        private Location location;

        public GPS(String address, String timeSpent, Location location){
            this.address = String.valueOf(address);
            this.timeSpent = timeSpent;
            this.location = location;

        }
        public String getAddress(){
            return address;
        }
        public String getTimeSpent() {
            return timeSpent;
        }
        public void setTimeSpent(String time){timeSpent = time;
        }

        public Location getLocation(){return location;}



}

