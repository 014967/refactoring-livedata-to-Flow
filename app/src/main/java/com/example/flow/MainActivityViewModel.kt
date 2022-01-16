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
    private var _friendList = MutableStateFlow<List<Friend>> (emptyList())
    private var _markFriendList = MutableStateFlow<List<Friend>>(emptyList())
    private var _selectedFriendList = MutableStateFlow<List<Friend>> (emptyList())

    private var selectedMap = HashMap<String, Int>(0)
    private var selectedList: MutableList<Friend> = arrayListOf()

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
                friend = Friend(i, "$i", "더미$i", 0, 0)
                List.add(friend)
            }

            emit(List)
        }
    }

    fun initMarkList(): Flow<List<Friend>> = flow {
        var markList: MutableList<Friend> = arrayListOf()
        var friend: Friend

        for (i in 0 until 2) {
            friend = Friend(i, "$i", "더미$i", 1, 0)
            markList.add(friend)
        }
        emit(markList)
    }

    fun addFriendList(friend: Friend) {
        var list: List<Friend>
        Log.d(TAG, "friend - $friend called")
        Log.d(TAG, "selectList before add ${selectedList.toList()}")
        if (selectedList.indexOf(friend) == -1) {

            if (_friendList.value.indexOf(friend) == -1) {

                // list = _markFriendList.value.map { it.copy() }
                list = _markFriendList.value.map {
                    if (it.equals(friend)) {
                        it.copy(selected = 1)
                    } else {
                        it.copy()
                    }
                }

                // list[_markFriendList.value.indexOf(friend)].selected = 1
                _markFriendList.value = ArrayList(list)
            } else {
                list = _friendList.value.map { it.copy() }
                list[_friendList.value.indexOf(friend)].selected = 1
                _friendList.value = list.toMutableList()
            }
            friend.selected = 1
            selectedList.add(friend)
            _selectedFriendList.value = selectedList.toMutableList()
        } else {

            if (_friendList.value.indexOf(friend) == -1) {

                list = _markFriendList.value.map { it.copy() }
                list[_markFriendList.value.indexOf(friend)].selected = 0
                _markFriendList.value = list.toMutableList()
            } else {

                list = _friendList.value.map { it.copy() }
                list[_friendList.value.indexOf(friend)].selected = 0
                _friendList.value = list.toMutableList()
            }

            list = _selectedFriendList.value.map { it.copy() }.toMutableList()
            list.remove(friend)
            selectedList.remove(friend)
            _selectedFriendList.value = list
        }
    }
}
