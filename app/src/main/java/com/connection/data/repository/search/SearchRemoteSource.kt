package com.connection.data.repository.search

import com.connection.data.remote.response.user.UserData
import com.connection.utils.common.Constants
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import timber.log.Timber
import javax.inject.Inject

class SearchRemoteSource @Inject constructor(
    private val db: FirebaseDatabase
) : SearchRepository.RemoteSource {


    /* --------------------------------------------------------------------------------------------
     * Override
     ---------------------------------------------------------------------------------------------*/
    /**
     * The character \uf8ff used in the query is a very high code point in the Unicode range.
     * Because it is after most regular characters in Unicode,
     * the query matches all values that start with searchText.
     */
    override suspend fun searchUsers(
        searchText: String,
        onSuccess: (List<UserData>) -> Unit,
        onFailure: () -> Unit
    ) {
        val users: MutableList<UserData> = mutableListOf()
        db.getReference(Constants.USERS)
            .orderByChild(Constants.USERNAME)
            .startAt(searchText)
            .endAt(searchText + Constants.SEARCH_POSTFIX)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (user in snapshot.children)
                        user.getValue(UserData::class.java)?.let { users.add(it) }
                    onSuccess(users)
                }

                override fun onCancelled(error: DatabaseError) {
                    Timber.e("Error occurred: ${error.message}")
                }
            })
    }
}