package com.test.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.test.app.R
import com.test.app.model.QrResponse

class DataShowAdapter(private val mContext: Context, private val mList: MutableList<QrResponse>) :
    RecyclerView.Adapter<DataShowAdapter.ViewHolder>() {
    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(characteristic: QrResponse) {
            view.findViewById<TextView>(R.id.tv_product_name).text =
                "Product Name  : " + characteristic.product?.partNumber
            view.findViewById<TextView>(R.id.tv_license_url).text =
                "License Url  : " + characteristic.lpId!!.substring(
                    characteristic.lpId!!.length - 5,
                    characteristic.lpId!!.length - 1
                )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater =
            mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view = inflater.inflate(
            R.layout.row_data,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}