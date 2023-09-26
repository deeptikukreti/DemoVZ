package com.example.demovz.util

import java.text.SimpleDateFormat

class CommonUtils {
    companion object {

        fun isDateAfter(startDate: String, endDate: String): Boolean {
            return try {
                val myFormatString = "dd-M-yyyy, hh:ss" // for example
                val df = SimpleDateFormat(myFormatString);
                val date1 = df.parse(endDate)
                val startingDate = df.parse(startDate);

                if (date1?.after(startingDate) == true) {
                    true
                } else date1.equals(startingDate)
            } catch (e: Exception) {
                false
            }
        }
    }
}