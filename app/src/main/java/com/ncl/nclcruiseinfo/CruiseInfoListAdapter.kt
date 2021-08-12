package com.ncl.nclcruiseinfo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ncl.nclcruiseinfo.databinding.ItemCruiseShipBinding

class CruiseInfoListAdapter(
    private val list: List<CruiseShipInfo>,
    private val onClickListener: (CruiseShipInfo) -> Unit
) :
    RecyclerView.Adapter<CruiseInfoListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ItemCruiseShipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onClickListener(item)
        }
    }

    override fun getItemCount() = list.size

    inner class ViewHolder(private val itemBinding: ItemCruiseShipBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(item: CruiseShipInfo) {
            itemBinding.shipNameLabel.text = item.shipName
            itemBinding.descLabel.text = item.shipDescription
            Glide.with(itemView.context)
                .load("http://ncl.com/" + item.imagePath.first().split("||").first())
                .circleCrop()
                .into(itemBinding.cruiseImageView)
        }
    }

}

