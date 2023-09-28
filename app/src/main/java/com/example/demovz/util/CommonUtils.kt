package com.example.demovz.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

class CommonUtils {
    companion object {

        @SuppressLint("SimpleDateFormat")
        fun isDateAfter(startDate: String, endDate: String): Boolean {
            return try {
                val myFormatString = "dd-M-yyyy, hh:ss"
                val df = SimpleDateFormat(myFormatString);
                val date1 = df.parse(endDate)
                val startingDate = df.parse(startDate);

                if (date1?.after(startingDate) == true) {
                    true
                } else date1!! == startingDate
            } catch (e: Exception) {
                false
            }
        }
    }
}