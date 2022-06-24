package cz.ackee.testtask.rm.framework.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import cz.ackee.testtask.rm.data.RemoteCharacterDataSource
import cz.ackee.testtask.rm.domain.Character
import cz.ackee.testtask.rm.framework.CharacterRepositoryImpl.Companion.NETWORK_PAGE_SIZE
import okio.IOException
import retrofit2.HttpException

private const val RICK_AND_MORTY_STARTING_PAGE_INDEX = 1

class GetFilteredCharactersPagingSource(
    private val query: String,
    private val remoteDataSource: RemoteCharacterDataSource,
) : PagingSource<Int, Character>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        val position = params.key ?: RICK_AND_MORTY_STARTING_PAGE_INDEX
        return try {
            val characters = remoteDataSource.filterCharacters(position, query)
            val nextKey = if (characters.isEmpty()) {
                null
            } else {
                position + (params.loadSize / NETWORK_PAGE_SIZE)
            }
            LoadResult.Page(
                data = characters,
                prevKey = if (position == RICK_AND_MORTY_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
