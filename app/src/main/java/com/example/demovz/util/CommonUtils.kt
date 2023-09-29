package com.example.demovz.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.example.demovz.R
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

        fun successDialog(context: Context, message: String) {
            val alertDialog = AlertDialog.Builder(context)
            alertDialog.setMessage(message)
            alertDialog.setCancelable(false)
            alertDialog.setPositiveButton("OK") { dialogInterface, _ ->
                dialogInterface.dismiss()
                (context as Activity).finish()
            }

            alertDialog.show()
        }
    }
}