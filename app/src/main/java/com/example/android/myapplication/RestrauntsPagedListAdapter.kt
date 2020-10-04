package com.example.android.myapplication

import SearchYelpQuery.Business
import android.app.Notification
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android.myapplication.RestrauntsPagedListAdapter.ViewHolder
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_restraunt.*


class RestrauntsPagedListAdapter(
) : PagedListAdapter<Business, ViewHolder>(NotificationItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_restraunt, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { notification ->
            holder.bind(notification)
        }
    }

    /**
     * DiffUtil-callback нужен по умолчанию для PagedListAdapter.
     */
    object NotificationItemCallback : DiffUtil.ItemCallback<Business>() {
        override fun areContentsTheSame(oldItem: Business, newItem: Business) =
            oldItem == newItem

        override fun areItemsTheSame(oldItem: Business, newItem: Business) =
            oldItem.id() == newItem.id()
    }


    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(item: Business) {
            name.text = item.name()
            distance.text = "${item.distance()?.toInt()}m"
            Glide.with(containerView)
                .load(item.photos()?.firstOrNull())
                .into(photo)
        }

    }
}