package com.awonar.android.shared.repos

import com.awonar.android.model.core.MessageSuccessResponse
import com.awonar.android.model.watchlist.*
import com.awonar.android.shared.api.WatchlistService
import com.awonar.android.shared.db.room.instrument.InstrumentDao
import com.awonar.android.shared.db.room.watchlist.WatchlistFolderDao
import com.molysulfur.library.network.DirectNetworkFlow
import com.molysulfur.library.network.NetworkFlow
import com.molysulfur.library.result.Result
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
                folder?.let {
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
                        val infos = folder.items?.map {
                            if (it.instrumentId ?: 0 > 0) {
                                val instrument = instrumentDao.loadById(it.instrumentId ?: 0)
                                WatchlistInfo(
                                    id = it.id,
                                    instrumentId = it.instrumentId ?: 0,
                                    uid = it.uid,
                                    image = instrument?.logo,
                                    title = instrument?.symbol,
                                    subTitle = "",
                                    type = it.type
                                )
                            } else {
                                WatchlistInfo(
                                    id = it.id,
                                    instrumentId = it.instrumentId ?: 0,
                                    uid = it.uid,
                                    image = it.trader?.image,
                                    title = if (it.trader?.displayFullName == true) "%s %s %s".format(
                                        it.trader?.firstName,
                                        it.trader?.middleName,
                                        it.trader?.lastName) else it.trader?.username,
                                    subTitle = it.trader?.username ?: "",
                                    risk = it.trader?.risk ?: 0,
                                    gain = it.trader?.gain ?: 0f,
                                    type = it.type
                                )
                            }
                        }
                        val entity = Folder(
                            id = folder.id ?: "",
                            name = folder.name,
                            totalItem = folder.totalItem,
                            static = folder.static,
                            default = folder.default,
                            recentlyInvested = folder.recentlyInvested,
                            infos = infos ?: arrayListOf()
                        )
                        folderEntities.add(entity)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                return folderEntities
            }

            override fun loadFromDb(): Flow<List<Folder>> = flow {
                emit(foldersDao.getAll())
            }

            override fun saveToDb(data: List<Folder>) {
                foldersDao.clear()
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
                    infos = arrayListOf()
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

    fun deleteWatchlist(folderId: String, itemId: String) =
        object : DirectNetworkFlow<String, List<Folder>, MessageSuccessResponse>() {
            override fun createCall(): Response<MessageSuccessResponse> =
                service.deleteItem(itemId).execute()

            override fun convertToResultType(response: MessageSuccessResponse): List<Folder> {
                val folder = foldersDao.loadById(folderId)
                folder?.let { item ->
                    item.infos = item.infos.filter { it.id != itemId }
                    foldersDao.update(item)
                }
                return foldersDao.getAll()
            }

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }

        }.asFlow()

    fun addWatchlist(request: AddWatchlistItemRequest) =
        object : DirectNetworkFlow<String, List<Folder>, WatchlistFolderItem>() {
            override fun createCall(): Response<WatchlistFolderItem> =
                service.addItem(request.folderId, request).execute()

            override fun convertToResultType(response: WatchlistFolderItem): List<Folder> {
                val instrument = instrumentDao.loadById(response.instrumentId ?: 0)
                val info = WatchlistInfo(
                    id = response.id,
                    instrumentId = response.instrumentId ?: 0,
                    uid = response.uid,
                    image = instrument?.logo,
                    title = instrument?.symbol,
                    subTitle = null,
                    type = response.type)
                val folder = foldersDao.loadById(request.folderId)
                folder?.let { item ->
                    item.infos = item.infos + info
                    foldersDao.update(item)
                }
                return foldersDao.getAll()
            }

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }

        }.asFlow()

    fun setDefault(folderId: String): Flow<Result<List<Folder>?>> =
        object : DirectNetworkFlow<String, List<Folder>, MessageSuccessResponse>() {
            override fun createCall(): Response<MessageSuccessResponse> =
                service.setDefault(folderId).execute()

            override fun convertToResultType(response: MessageSuccessResponse): List<Folder> {
                val newFolder = foldersDao.getAll().map {
                    it.default = it.id == folderId
                    it
                }
                foldersDao.updateFolders(newFolder)
                return foldersDao.getAll()
            }

            override fun onFetchFailed(errorMessage: String) {
                println(errorMessage)
            }

        }.asFlow()
}