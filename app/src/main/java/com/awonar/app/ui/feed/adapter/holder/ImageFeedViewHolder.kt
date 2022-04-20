package com.awonar.app.ui.feed.adapter.holder

import android.graphics.Bitmap
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.AwonarItemImagesFeedBinding
import com.awonar.app.ui.feed.adapter.FeedItem
import com.awonar.app.utils.ImageUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ImageFeedViewHolder constructor(private val binding: AwonarItemImagesFeedBinding) :
    RecyclerView.ViewHolder(binding.root) {


    fun bind(item: FeedItem.ImagesFeeds, scope: CoroutineScope) {
        scope.launch {
            val bitmaps: List<Bitmap?> = item.images.map {
                ImageUtil.getBitmap(it, binding.root.context)
            }
            with(binding.awonarImagePreviewFeedItem) {
                setImages(bitmaps.toMutableList())
            }
        }
    }
}