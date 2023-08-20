package com.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.core.dao.DigitoonDao
import com.core.dto.batman.Batman

/**
 * Main database description.
 */
@Database(
    entities =
    [
        Batman::class
    ],
    version = 1, exportSchema = false
)
abstract class DigitoonDb : RoomDatabase() {

    abstract fun userInfoDao(): DigitoonDao

}
