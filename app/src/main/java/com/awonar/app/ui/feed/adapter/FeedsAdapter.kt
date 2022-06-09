package com.awonar.app.ui.feed.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.*
import com.awonar.app.ui.feed.adapter.holder.*
import com.awonar.app.ui.profile.history.adapter.HistoryProfileItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import timber.log.Timber

class FeedsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val scope = CoroutineScope(Job() + Dispatchers.Main)

    var itemLists: MutableList<FeedItem> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    var onLoadMore: (() -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            FeedType.IMAGES_TYPE -> ImageFeedViewHolder(
                AwonarItemDefaultFeedBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            FeedType.NEWS_TYPE -> NewsFeedViewHolder(
                AwonarItemDefaultFeedBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            FeedType.BLANK_TYPE -> BlankViewHolder(
                AwonarItemBlankGrayBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            FeedType.LOADING_TYPE -> LoadingViewHolder(
                AwonarItemFeedLoadingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            FeedType.FEED_TYPE -> FeedViewHolder(
                AwonarItemDefaultFeedBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            FeedType.EMPTY_TYPE -> EmptyViewHolder(
                AwonarItemEmptyBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw Error("View type is not found!")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemLists[position]
        when (holder) {
            is ImageFeedViewHolder -> holder.bind(item as FeedItem.ImagesFeeds, scope)
            is NewsFeedViewHolder -> holder.bind(item as FeedItem.NewsFeed)
            is FeedViewHolder -> holder.bind(item as FeedItem.DefaultFeed, scope)
            is LoadingViewHolder -> holder.bind {
                Timber.e("loadmore")
                onLoadMore?.invoke()
            }
        }
    }

    override fun getItemCount(): Int = itemLists.size

    override fun getItemViewType(position: Int): Int = itemLists[position].type

}