package com.example.flow

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.flow.databinding.ItemView3Binding

/**
 * @author boris
 * @created 2022/01/16
 */
class MainActivityMarkAdapter constructor(
    callback: MainActivity?
) : ListAdapter<Friend, RecyclerView.ViewHolder>(diffUtils) {

    interface MarkItemCallBack {
        fun onMarkItemClicked(friend: Friend, position: Int, adapter: MainActivityMarkAdapter)
    }

    var itemCallBack = callback
    companion object {
        val TAG: String = "로그"
        val diffUtils = object : DiffUtil.ItemCallback<Friend>() {
            override fun areItemsTheSame(oldItem: Friend, newItem: Friend): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Friend, newItem: Friend): Boolean {

                Log.d(TAG, "mark - oldItem : ${oldItem.hashCode()} - newItem : ${newItem.hashCode()}")
                return oldItem.selected == newItem.selected
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return ItemViewHolder(ItemView3Binding.bind(view))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemViewHolder).bind(getItem(position), position)
    }

    inner class ItemViewHolder(val binding: ItemView3Binding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(friend: Friend, position: Int) {
            binding.ivInviteRoomRadioButton.isActivated = friend.selected != "0"
            binding.itemChatRoomList.setOnClickListener(
                ItemClickListener(friend, position)
            )

            binding.tvChatRoomListUserName.text = friend.nickname
        }
    }
    inner class ItemClickListener(
        var friend: Friend,
        var position: Int
    ) : View.OnClickListener {
        override fun onClick(view: View?) {
            when (view?.id) {
                R.id.item_chat_room_list ->
                    {
                        itemCallBack?.onMarkItemClicked(friend, position, this@MainActivityMarkAdapter)
                    }
            }
        }
    }
    fun putActivate(position: Int) {
        var list: MutableList<Friend> = currentList.toMutableList()
        var friend: Friend = currentList[position].copy()
        friend.selected = "1"
        list[position] = friend
        submitList(list)
    }
    fun removeActivate(position: Int) {
        var list: MutableList<Friend> = currentList.toMutableList()
        var friend: Friend = currentList[position].copy()
        friend.selected = "0"
        list[position] = friend
        submitList(list)
    }
}
