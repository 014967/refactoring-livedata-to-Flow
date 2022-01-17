package com.example.flow

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/01/16
 */
class MainActivityViewModel @Inject constructor() : ViewModel() {
    val TAG: String = "로그"
    private var _friendList = MutableStateFlow<List<Friend>>(emptyList())
    private var _markFriendList = MutableStateFlow<List<Friend>>(emptyList())
    private var _selectedFriendList = MutableStateFlow<List<Friend>>(emptyList())

    private var friendAdapter: MainActivityAdapter? = null
    private var markAdapter: MainActivityMarkAdapter? = null

    data class SelectedTable(var id: String, var position: Int, var adapterFlag: Int)
    private var selectedMap = HashMap<String, SelectedTable>()
    // private var selectedList: MutableList<Friend> = arrayListOf()

    var friendList = _friendList.asStateFlow()
        get() = _friendList

    var markFriendList = _markFriendList.asStateFlow()
        get() = _markFriendList

    var selectedFriendList = _selectedFriendList.asStateFlow()
        get() = _selectedFriendList

    init {
        viewModelScope.launch {
            initFriendList().collect {
                _friendList.value = it
            }
        }
        viewModelScope.launch {
            initMarkList().collect {
                _markFriendList.value = it
            }
        }
    }

    fun initFriendList(): Flow<List<Friend>> = flow {
        var List: MutableList<Friend> = arrayListOf()

        var friend: Friend
        for (i in 0 until 20) {
            if (i < 2) {
            } else {
                friend = Friend("$i", "$i", "더미$i", 0)
                List.add(friend)
            }

            emit(List)
        }
    }

    fun initMarkList(): Flow<List<Friend>> = flow {
        var markList: MutableList<Friend> = arrayListOf()
        var friend: Friend

        for (i in 0 until 2) {
            friend = Friend("$i", "$i", "더미$i", 1)
            markList.add(friend)
        }
        emit(markList)
    }

    fun addFriendList(friend: Friend, position: Int, adapter: MainActivityAdapter) {
        var list: List<Friend>
        if (!selectedMap.containsKey(friend.id)) {

            var selectedTable = SelectedTable(friend.id, position, 0)
            selectedMap.put(friend.id, selectedTable)
            // Log.d(TAG,"selectedMap - $selectedMap called")
            adapter.putActivate(position)
            list = _selectedFriendList.value.map { it.copy() }
            var newList = list.toMutableList()
            newList.add(friend)
            this.friendAdapter = adapter
            _selectedFriendList.value = newList
        } else {
            var newList: MutableList<Friend> = _selectedFriendList.value.toMutableList()
            newList.removeIf {
                it.id == friend.id
            }
            selectedMap.remove(friend.id)
            Log.d(TAG, "$adapter")
            this.friendAdapter = adapter
            adapter.removeActivate(position)
            _selectedFriendList.value = newList
        }
    }

    fun addMarkList(friend: Friend, position: Int, adapter: MainActivityMarkAdapter) {
        var list: List<Friend>
        if (!selectedMap.containsKey(friend.id)) {

            var selectedTable = SelectedTable(friend.id, position, 1)
            selectedMap.put(friend.id, selectedTable)
            adapter.putActivate(position)
            list = _selectedFriendList.value.map { it.copy() }
            var newList = list.toMutableList()
            newList.add(friend)
            this.markAdapter = adapter
            _selectedFriendList.value = newList
        } else {

            var newList: MutableList<Friend> = _selectedFriendList.value.toMutableList()
            newList.removeIf {
                it.id == friend.id
            }
            selectedMap.remove(friend.id)
            Log.d(TAG, "$adapter")
            this.markAdapter = adapter
            adapter.removeActivate(position)
            _selectedFriendList.value = newList
        }
    }

    fun removeSelectList(friend: Friend) {
        var newTable: SelectedTable = selectedMap.get(friend.id)!!
        var newPosition = newTable.position
        var newList: MutableList<Friend> = _selectedFriendList.value.toMutableList()
        newList.removeIf {
            it.id == friend.id
        }
        selectedMap.remove(friend.id)
        _selectedFriendList.value = newList
        if (newTable.adapterFlag == 0) {
            this.friendAdapter?.removeActivate(newPosition)
        } else {
            this.markAdapter?.removeActivate(newPosition)
        }
    }
}
