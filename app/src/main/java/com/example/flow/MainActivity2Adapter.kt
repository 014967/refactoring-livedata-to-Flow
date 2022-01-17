package com.example.flow

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.flow.databinding.ItemView2Binding

/**
 * @author boris
 * @created 2022/01/16
 */
class MainActivity2Adapter constructor(
    callBack: MainActivity
) : ListAdapter<Friend, RecyclerView.ViewHolder>(FriendDiffCallback) {

    interface ItemSelectedCallBack {
        fun onItemSelectedClicked(friend: Friend)
    }
    var removeCallBack = callBack

    companion object {
        val TAG: String = "로그"
    }
    object FriendDiffCallback : DiffUtil.ItemCallback<Friend>() {
        override fun areItemsTheSame(oldItem: Friend, newItem: Friend): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: Friend, newItem: Friend): Boolean {
            return oldItem.selected == newItem.selected
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view2, parent, false)
        return ItemViewHolder(ItemView2Binding.bind(view))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemViewHolder).bind(getItem(position))
    }

    inner class ItemViewHolder(val binding: ItemView2Binding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(friend: Friend) {
            binding.layoutInviteRoomSelected.setOnClickListener(
                ItemClickListener(friend)
            )
            binding.tvInviteRoomSelectedUserName.text = friend.nickname
        }
    }

    inner class ItemClickListener(
        var friend: Friend
    ) : View.OnClickListener {
        override fun onClick(view: View?) {
            when (view?.id) {
                R.id.layout_invite_room_selected ->
                    {
                        removeCallBack?.onItemSelectedClicked(friend)
                    }
            }
        }
    }
}
