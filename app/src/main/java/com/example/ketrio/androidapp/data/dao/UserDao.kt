package com.example.ketrio.androidapp.data.dao

import androidx.room.*
import com.example.ketrio.androidapp.data.entity.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user LIMIT 1")
    fun get(): User?

    @Update
    fun update(user: User)

    @Insert
    fun insert(user: User)

}