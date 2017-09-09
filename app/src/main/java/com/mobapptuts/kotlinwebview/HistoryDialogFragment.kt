package com.mobapptuts.kotlinwebview

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.webkit.WebView
import android.widget.Toast

/**
 * Created by nigelhenshaw on 2017/08/28.
 */
class HistoryDialogFragment : DialogFragment(){
    val selectBackAdapter = "SELECT_BACK_ADAPTER"

    interface WebHistory {
        fun getWebView(): WebView
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

        val backAdapter = arguments.getBoolean(selectBackAdapter)
        val recyclerView = RecyclerView(activity)
        if (backAdapter) {
            recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, true)
            recyclerView.adapter = BackHistoryAdapter(this, webHistory.getWebView())
        } else {
            recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            recyclerView.adapter = ForwardHistoryAdapter(this, webHistory.getWebView())
        }
        val itemDecoration = DividerItemDecoration(activity, LinearLayoutManager.VERTICAL)
        itemDecoration.setDrawable(ColorDrawable(Color.RED))
        recyclerView.addItemDecoration(itemDecoration)
        recyclerView.background = ColorDrawable(Color.LTGRAY)
        val alertDialogBuilder = AlertDialog.Builder(activity)
                .setView(recyclerView)
        return alertDialogBuilder.create()

    }
}