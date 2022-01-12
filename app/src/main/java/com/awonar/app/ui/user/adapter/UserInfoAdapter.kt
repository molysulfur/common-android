package com.awonar.app.ui.user.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.awonar.app.databinding.*
import com.awonar.app.ui.user.adapter.holder.*

class UserInfoAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var itemList: MutableList<UserInfoItem> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            UserInfoType.TITLE_USER_INFO -> TitleViewHolder(AwonarItemTitleBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
            UserInfoType.SUBTITLE_USER_INFO -> SectionViewHolder(AwonarItemSectionBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
            UserInfoType.TEXT_USER_INFO -> TextViewHolder(AwonarItemListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
            UserInfoType.SOCIAL_USER_INFO -> SocialViewHolder(AwonarItemIconBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
            UserInfoType.BLANK_USER_INFO -> BlankViewHolder(AwonarItemDividerBlankBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ))
            UserInfoType.DIVIDER_USER_INFO -> DividerViewHolder(AwonarItemDividerBlankBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ))
            else -> throw Error("View Type is not found!")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemList[position]
        when (holder) {
            is TitleViewHolder -> holder.bind(item = item as UserInfoItem.TitleItem)
            is SectionViewHolder -> holder.bind(item = item as UserInfoItem.SubTitleItem)
            is TextViewHolder -> holder.bind(item = item as UserInfoItem.TextItem)
            is SocialViewHolder -> holder.bind(item = item as UserInfoItem.SocialItem)
        }
    }

    override fun getItemCount(): Int = itemList.size

    override fun getItemViewType(position: Int): Int = itemList[position].type

}