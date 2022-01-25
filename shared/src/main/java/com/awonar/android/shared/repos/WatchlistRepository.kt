package com.awonar.android.shared.repos

import com.awonar.android.model.watchlist.Folder
import com.awonar.android.model.watchlist.WatchlistFolder
import com.awonar.android.shared.api.WatchlistService
import com.awonar.android.shared.db.room.instrument.InstrumentDao
import com.awonar.android.shared.db.room.watchlist.WatchlistFolderDao
import com.molysulfur.library.network.NetworkFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class WatchlistRepository @Inject constructor(
    private val service: WatchlistService,
    private val foldersDao: WatchlistFolderDao,
    private val instrumentDao: InstrumentDao,
) {


    fun getFolders(needFresh: Boolean) =
        object : NetworkFlow<Unit, List<Folder>, List<WatchlistFolder>>() {
            override fun shouldFresh(data: List<Folder>?): Boolean =
                data == null || needFresh

            override fun createCall(): Response<List<WatchlistFolder>> =
                service.getFolders().execute()

            override fun convertToResultType(response: List<WatchlistFolder>): List<Folder> {
                val folderEntities = mutableListOf<Folder>()
                response.forEach { folder ->
                    try {
                        Timber.e("$folder")
                        val images = folder.items.map {
                            val instrument = instrumentDao.loadById(it.instrumentId)
                            Timber.e("$instrument")
                            instrument.logo ?: ""
                        }
                        val entity = Folder(
                            id = folder.id ?: "",
                            name = folder.name,
                            totalItem = folder.totalItem,
                            static = folder.static,
                            default = folder.default,
                            recentlyInvested = folder.recentlyInvested,
                            images = images
                        )
                        Timber.e("$entity $folder")
                        folderEntities.add(entity)
                    } catch (e: Exception) {
                        Timber.e(e.message)
                    }
                }
                Timber.e("entity : $folderEntities")
                return folderEntities
            }

            override fun loadFromDb(): Flow<List<Folder>> = flow {
                emit(foldersDao.getAll())
            }

            override fun saveToDb(data: List<Folder>) {
                Timber.e("$data")
                foldersDao.insertAll(data)
            }

            override fun onFetchFailed(errorMessage: String) {
            }

        }.asFlow()
}