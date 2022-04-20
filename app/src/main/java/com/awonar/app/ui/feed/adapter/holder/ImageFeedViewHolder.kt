package com.awonar.app.ui.feed.adapter.holder

import android.graphics.Bitmap
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemDefaultFeedBinding
import com.awonar.app.databinding.AwonarItemImagesFeedBinding
import com.awonar.app.ui.feed.adapter.FeedItem
import com.awonar.app.utils.ImageUtil
import com.awonar.app.widget.feed.ImagePreviewFeed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ImageFeedViewHolder constructor(private val binding: AwonarItemDefaultFeedBinding) :
    RecyclerView.ViewHolder(binding.root) {


    fun bind(item: FeedItem.ImagesFeeds, scope: CoroutineScope) {
        with(binding.awonarDefaultFeedItem) {
            avatar = item.avatar
            title = item.title
            subTitle = item.subTitle
            description = item.description
            likeCount = item.likeCount
            commentCount = item.commentCount
            sharedCount = item.sharedCount
        }
        scope.launch {
            val bitmaps: List<Bitmap?> = item.images.map {
                ImageUtil.getBitmap(it, binding.root.context)
            }
            val imagesView = ImagePreviewFeed(binding.root.context).apply {
                setImages(bitmaps.toMutableList())
            }
            binding.awonarDefaultFeedItem.addOptionView(imagesView)
        }
    }
}