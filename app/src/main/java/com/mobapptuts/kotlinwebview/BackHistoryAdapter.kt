package com.mobapptuts.kotlinwebview

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import kotlinx.android.synthetic.main.web_item_history.view.*

/**
 * Created by nigelhenshaw on 2017/09/07.
 */
class BackHistoryAdapter(val dialog: HistoryDialogFragment, val webview: WebView): RecyclerView.Adapter<BackHistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflator = LayoutInflater.from(parent.context)
        val webHistoryView = layoutInflator.inflate(R.layout.web_item_history, parent, false)
        return ViewHolder(webHistoryView)
    }

    override fun getItemCount() = webview.copyBackForwardList().currentIndex

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val webHistory = webview.copyBackForwardList()
        holder.title.text = webHistory.getItemAtIndex(position).title
        holder.favicon.setImageBitmap(webHistory.getItemAtIndex(position).favicon)

        holder.itemView.setOnClickListener {
            for (i in 0 until webHistory.currentIndex) {
                if (holder.title.text.equals(webHistory.getItemAtIndex(i).title)) {
                    webview.goBackOrForward(i - webHistory.currentIndex)
                    dialog.dismiss()
                    break
                }
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.title
        val favicon = itemView.favicon
    }
}