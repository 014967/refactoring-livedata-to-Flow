package com.example.flow

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.flow.databinding.ItemViewBinding

/**
 * @author boris
 * @created 2022/01/16
 */
class MainActivityAdapter constructor(
    callback: MainActivity
) : ListAdapter<Friend, RecyclerView.ViewHolder>(diffUtils) {

    interface ItemCallBack {
        fun onItemClicked(friend: Friend, position: Int)
    }

    var itemCallBack = callback
    companion object {
        val TAG: String = "로그"
        val diffUtils = object : DiffUtil.ItemCallback<Friend>() {
            override fun areItemsTheSame(oldItem: Friend, newItem: Friend): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Friend, newItem: Friend): Boolean {
                Log.d(TAG, "normal - oldItem : ${oldItem.hashCode()} newItem : ${newItem.hashCode()}")
                return oldItem.selected == newItem.selected
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return ItemViewHolder(ItemViewBinding.bind(view))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemViewHolder).bind(getItem(position), position)
    }

    inner class ItemViewHolder(val binding: ItemViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(friend: Friend, position: Int) {
            if (friend.selected == 0) {
                Log.d(TAG,"01 - bind() called")
                binding.ivInviteRoomRadioButtonFill.visibility = View.GONE
                binding.ivInviteRoomRadioButton.visibility = View.VISIBLE
            } else {
                Log.d(TAG,"02 - bind() called")
                binding.ivInviteRoomRadioButtonFill.visibility = View.VISIBLE
                binding.ivInviteRoomRadioButton.visibility = View.GONE
            }

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
                        Log.d(TAG, "ItemClickListener - onClick() called")
                        itemCallBack?.onItemClicked(friend, position)
                    }
            }
        }
    }
}
