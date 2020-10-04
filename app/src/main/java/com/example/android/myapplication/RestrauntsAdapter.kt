package com.example.android.myapplication

import SearchYelpQuery.Business
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.myapplication.RestrauntsAdapter.ViewHolder
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_restraunt.*


class RestrauntsAdapter(
    val list: List<Business>
) : RecyclerView.Adapter<ViewHolder>() {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(list[position])

    override fun getItemCount() = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_restraunt, parent, false)
        )


    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(item: Business) {
            name.text = item.name()
            distance.text = "${item.distance()}m"
//            Glide.with(containerView)
////                .load()
//////            .placeholder(R.drawable.ic_no_photo)
//////            .apply(glideRequestOptions)
////                .into(photo)
        }

    }
}