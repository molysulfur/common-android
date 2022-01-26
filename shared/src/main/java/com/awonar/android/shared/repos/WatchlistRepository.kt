package com.awonar.android.shared.repos

import com.awonar.android.model.core.MessageSuccessResponse
import com.awonar.android.model.watchlist.AddWatchlistRequest
import com.awonar.android.model.watchlist.Folder
import com.awonar.android.model.watchlist.WatchlistFolder
import com.awonar.android.shared.api.WatchlistService
import com.awonar.android.shared.db.room.instrument.InstrumentDao
import com.awonar.android.shared.db.room.watchlist.WatchlistFolderDao
import com.molysulfur.library.network.DirectNetworkFlow
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

    fun deleteFolder(id: String) =
        object : DirectNetworkFlow<String, List<Folder>, MessageSuccessResponse>() {
            override fun createCall(): Response<MessageSuccessResponse> =
                service.deleteFolder(id).execute()

            override fun convertToResultType(response: MessageSuccessResponse): List<Folder> {
                val folder = foldersDao.loadById(id)
                folder.let {
                    foldersDao.delete(it)
                }
                return foldersDao.getAll()
            }

            override fun onFetchFailed(errorMessage: String) {
            }

        }.asFlow()


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
                        val images = folder.items?.map {
                            if (it.instrumentId ?: 0 > 0) {
                                val instrument = instrumentDao.loadById(it.instrumentId ?: 0)
                                instrument?.logo ?: ""
                            } else {
                                ""
                            }
                        }
                        val entity = Folder(
                            id = folder.id ?: "",
                            name = folder.name,
                            totalItem = folder.totalItem,
                            static = folder.static,
                            default = folder.default,
                            recentlyInvested = folder.recentlyInvested,
                            images = images ?: arrayListOf()
                        )
                        folderEntities.add(entity)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Timber.e(e.message)
                    }
                }
                folderEntities.forEach {
                    Timber.e("${it.id} ${it.name}")
                }
                return folderEntities
            }

            override fun loadFromDb(): Flow<List<Folder>> = flow {
                emit(foldersDao.getAll())
            }

            override fun saveToDb(data: List<Folder>) {
                data.forEach {
                    Timber.e("${it.id} ${it.name}")
                }
                foldersDao.insertAll(data)
            }

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }

        }.asFlow()

    fun addFolder(request: String) =
        object : NetworkFlow<String, List<Folder>, WatchlistFolder>() {
            override fun shouldFresh(data: List<Folder>?): Boolean = true

            override fun createCall(): Response<WatchlistFolder> =
                service.addFolder(AddWatchlistRequest(request)).execute()

            override fun convertToResultType(response: WatchlistFolder): List<Folder> {
                val entity = Folder(
                    id = response.id ?: "",
                    name = response.name,
                    totalItem = response.totalItem,
                    static = response.static,
                    default = response.default,
                    recentlyInvested = response.recentlyInvested,
                    images = arrayListOf()
                )
                return listOf(entity)
            }

            override fun loadFromDb(): Flow<List<Folder>> = flow {
                emit(foldersDao.getAll())
            }

            override fun saveToDb(data: List<Folder>) {
                if (data.isNotEmpty()) {
                    foldersDao.insert(data[0])
                }
            }

            override fun onFetchFailed(errorMessage: String) {
            }

        }.asFlow()
}