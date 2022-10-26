package com.example.launderup.data.api

import androidx.room.Dao
import androidx.room.Query
import com.example.launderup.data.models.ClothesDataClass

@Dao
interface ClothesDataInterface {
    @Query("Select * from clothesdataclass")
    fun getAll():List<ClothesDataClass>
}