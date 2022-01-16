package com.example.flow

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flow.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity :
    AppCompatActivity(),
    MainActivityAdapter.ItemCallBack,
    MainActivityMarkAdapter.MarkItemCallBack {

    private val model by viewModels<MainActivityViewModel>()
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: MainActivityAdapter
    lateinit var adapter2: MainActivity2Adapter
    lateinit var markAdapter: MainActivityMarkAdapter
    val TAG: String = "로그"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()

        lifecycleScope.launch {
            model.friendList.collect {
                Log.d(TAG, "collected FriendList - ${model.friendList.value} called")
                adapter.submitList(it)
            }
        }
        lifecycleScope.launch {
            model.markFriendList.collect {
                Log.d(TAG, "markAdapter - ${markAdapter.currentList.toList()}")
                Log.d(TAG, "it - ${it.toList()}")
                markAdapter.submitList(it)
            }
        }
        lifecycleScope.launch {
            model.selectedFriendList.collect {
                Log.d(TAG, "MainActivity - selectedFriendList collected")
                adapter2.submitList(it)
            }
        }
    }

    fun initUI() {

        binding.rvView.layoutManager = LinearLayoutManager(this)
        adapter = MainActivityAdapter(this)
        binding.rvView.adapter = adapter
        binding.rvView2.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter2 = MainActivity2Adapter(this)
        binding.rvView2.adapter = adapter2
        binding.rvMark.layoutManager = LinearLayoutManager(this)
        markAdapter = MainActivityMarkAdapter(this)
        binding.rvMark.adapter = markAdapter
    }
    override fun onItemClicked(friend: Friend, position: Int) {
        model.addFriendList(friend)
    }
}
