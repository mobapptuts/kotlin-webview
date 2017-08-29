package com.mobapptuts.kotlinwebview

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.Toast

/**
 * Created by nigelhenshaw on 2017/08/28.
 */
class HistoryDialogFragment : DialogFragment(){
    val history = "HISTORY"

    interface WebHistory {
        fun webpageSelected(webTitle: String)
    }

    lateinit var webHistory: WebHistory

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            webHistory = context as WebHistory
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val historyList = arguments.getStringArrayList(history).toTypedArray()
        val alertDialogBuilder = AlertDialog.Builder(activity)
                .setItems(historyList, DialogInterface.OnClickListener { dialogInterface, i ->
//                    Toast.makeText(activity, historyList[i], Toast.LENGTH_SHORT).show()
                    webHistory.webpageSelected(historyList[i])
                })
        val dialog = alertDialogBuilder.create()
        val listView = dialog.listView
        listView.background = ColorDrawable(Color.LTGRAY)
        listView.divider = ColorDrawable(Color.RED)
        listView.dividerHeight = 2
        return dialog
    }
}