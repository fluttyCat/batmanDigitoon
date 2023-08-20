package com.core.repository

import com.core.base.BaseRepository
import com.core.dao.DigitoonDao

abstract class LocalRepository : BaseRepository {

}

class LocalRepositoryImpl(
    private val digitoonDao: DigitoonDao
) : LocalRepository() {

}