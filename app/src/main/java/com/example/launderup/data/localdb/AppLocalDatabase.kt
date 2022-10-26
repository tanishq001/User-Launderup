package com.example.launderup.data.localdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.launderup.data.api.ClothesDataInterface
import com.example.launderup.data.models.ClothesDataClass

@Database(entities = [ClothesDataClass::class], version = 1)

abstract class AppLocalDatabase:RoomDatabase() {
    abstract fun clothesDao(): ClothesDataInterface
}