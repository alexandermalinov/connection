package com.connection.data.repository.search

import com.connection.data.remote.response.user.UserData
import com.connection.utils.common.Constants
import com.connection.utils.responsehandler.Either
import com.connection.utils.responsehandler.HttpError
import com.connection.utils.responsehandler.toHttpError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
        block: (Either<HttpError, List<UserData>>) -> Unit
    ) {
        val users: MutableList<UserData> = mutableListOf()
        db.getReference(Constants.USERS)
            .orderByChild(Constants.USERNAME)
            .startAt(searchText)
            .endAt(searchText + Constants.SEARCH_POSTFIX)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    block.invoke(
                        if (snapshot.exists()) {
                            for (user in snapshot.children)
                                user.getValue(UserData::class.java)?.let { users.add(it) }
                            Either.right(users)
                        } else
                            Either.left(HttpError())
                    )
                }

                override fun onCancelled(error: DatabaseError) {
                    Either.left(error.toHttpError())
                }
            })
    }
}