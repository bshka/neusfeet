package com.krendel.neusfeet.screens.common.repository.everything

import androidx.paging.PageKeyedDataSource
import com.google.common.truth.Truth.assertThat
import com.krendel.neusfeet.model.articles.Article
import com.krendel.neusfeet.networking.NewsApi
import com.krendel.neusfeet.screens.common.repository.common.DataSourceActions
import com.krendel.neusfeet.utils.FakeGenerator.getArticlesSchema
import com.krendel.neusfeet.utils.SimplePagedLoadCallback
import com.krendel.neusfeet.utils.SimplePagedLoadInitialCallback
import com.krendel.neusfeet.utils.TestSchedulersProvider
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test

class EverythingDataSourceTest {

    private lateinit var SUT: EverythingDataSource

    private lateinit var newsApiMock: NewsApi
    private val configuration = EverythingFetchConfiguration(pageSize = 1)
    private val initialCallback: SimplePagedLoadInitialCallback<Int, Article> = mock()
    private val loadCallback: SimplePagedLoadCallback<Int, Article> = mock()

    private val disposable = CompositeDisposable()
    private val testSchedulersProvider = TestSchedulersProvider()

    @Before
    fun setUp() {
        newsApiMock = mock()
        SUT = EverythingDataSource(newsApiMock, testSchedulersProvider, configuration, disposable)
    }

    // loadInitial correct page size passed to news api everything
    @Test
    fun `loadInitial correct page size passed to news api everything`() {
        argumentCaptor<Int> {
            success()

            val params = PageKeyedDataSource.LoadInitialParams<Int>(PAGE_SIZE, false)

            SUT.loadInitial(params, initialCallback)
            testSchedulersProvider.triggerActions()

            verify(newsApiMock).everything(any(), capture(), any())

            assertThat(firstValue).isEqualTo(PAGE_SIZE)
        }
    }

    // loadInitial success callback on result called params placeholders
    @Test
    fun `loadInitial success callback on result called params placeholders`() {
        success()

        val params = PageKeyedDataSource.LoadInitialParams<Int>(PAGE_SIZE, true)
        SUT.loadInitial(params, initialCallback)
        testSchedulersProvider.triggerActions()

        verify(initialCallback, atLeastOnce()).onResult(any(), any(), any(), anyOrNull(), anyOrNull())
        verify(initialCallback, never()).onResult(any(), anyOrNull(), anyOrNull())
    }

    // loadInitial success callback on result called params without placeholders
    @Test
    fun `loadInitial success callback on result called params without placeholders`() {
        success()

        val params = PageKeyedDataSource.LoadInitialParams<Int>(PAGE_SIZE, false)
        SUT.loadInitial(params, initialCallback)
        testSchedulersProvider.triggerActions()

        verify(initialCallback, never()).onResult(any(), any(), any(), anyOrNull(), anyOrNull())
        verify(initialCallback, atLeastOnce()).onResult(any(), anyOrNull(), anyOrNull())
    }

    // loadInitial success result prev page is null
    @Test
    fun `loadInitial success result prev page is null`() {
        argumentCaptor<Int> {
            success()

            val params = PageKeyedDataSource.LoadInitialParams<Int>(PAGE_SIZE, true)
            SUT.loadInitial(params, initialCallback)
            testSchedulersProvider.triggerActions()

            verify(initialCallback).onResult(any(), any(), any(), capture(), anyOrNull())
            assertThat(firstValue).isNull()
        }
    }

    // loadInitial success items count more than one page next page returned
    @Test
    fun `loadInitial success items count more than one page next page returned`() {
        argumentCaptor<Int> {
            success(PAGE_SIZE * 2)

            val params = PageKeyedDataSource.LoadInitialParams<Int>(PAGE_SIZE, false)
            SUT.loadInitial(params, initialCallback)
            testSchedulersProvider.triggerActions()

            verify(initialCallback).onResult(any(), anyOrNull(), capture())
            assertThat(firstValue).isNotNull()
        }
    }

    // loadInitial success items count less or equal for one page next page is null
    @Test
    fun `loadInitial success items count less or equal for one page next page is null`() {
        argumentCaptor<Int> {
            success(1)

            val params = PageKeyedDataSource.LoadInitialParams<Int>(PAGE_SIZE, false)
            SUT.loadInitial(params, initialCallback)
            testSchedulersProvider.triggerActions()

            verify(initialCallback).onResult(any(), anyOrNull(), capture())
            assertThat(firstValue).isNull()
        }
    }

    // loadInitial error callback on error called
    @Test
    fun `loadInitial error callback loading stopped and on error called`() {
        error()

        val testObserver = TestObserver<DataSourceActions>()
        SUT.eventsObservable.subscribe(testObserver)

        val params = PageKeyedDataSource.LoadInitialParams<Int>(PAGE_SIZE, false)
        SUT.loadInitial(params, initialCallback)
        testSchedulersProvider.triggerActions()

        // first is loading, second is error
        assertThat(testObserver.assertValueAt(0) { t ->
            when (t) {
                is DataSourceActions.Loading -> true
                else -> false
            }
        })
        assertThat(testObserver.assertValueAt(1) { t ->
            when (t) {
                is DataSourceActions.Loading -> true
                else -> false
            }
        })
        assertThat(testObserver.assertValueAt(2) { t ->
            when (t) {
                is DataSourceActions.Error -> true
                else -> false
            }
        })

    }

    // loadAfter correct page size passed to news api everything
    @Test
    fun `loadAfter correct page size passed to news api everything`() {
        argumentCaptor<Int> {

            success(PAGE_SIZE * 2)

            val params = PageKeyedDataSource.LoadParams(2, PAGE_SIZE)

            SUT.loadAfter(params, loadCallback)
            testSchedulersProvider.triggerActions()

            verify(newsApiMock).everything(any(), capture(), any())

            assertThat(firstValue).isEqualTo(PAGE_SIZE)

        }
    }
    // loadAfter correct page passed to news api everything
    @Test
    fun `loadAfter correct page passed to news api everything`() {
        argumentCaptor<Int> {

            success(PAGE_SIZE * 2)

            val params = PageKeyedDataSource.LoadParams(3, PAGE_SIZE)

            SUT.loadAfter(params, loadCallback)
            testSchedulersProvider.triggerActions()

            verify(newsApiMock).everything(capture(), any(), any())

            assertThat(firstValue).isEqualTo(3)

        }
    }
    // loadAfter success items count more than one page next page returned
    @Test
    fun `loadAfter success items count more than one page next page returned`() {
        argumentCaptor<Int> {
            success(45)

            val params = PageKeyedDataSource.LoadParams(2, PAGE_SIZE)

            SUT.loadAfter(params, loadCallback)
            testSchedulersProvider.triggerActions()

            verify(loadCallback).onResult(any(), capture())
            assertThat(firstValue).isEqualTo(3)
        }
    }
    // loadAfter success items count less or equal for one page next page is null
    @Test
    fun `loadAfter success items count less or equal for one page next page is null`() {
        argumentCaptor<Int> {
            success(45)

            val params = PageKeyedDataSource.LoadParams(4, PAGE_SIZE)

            SUT.loadAfter(params, loadCallback)
            testSchedulersProvider.triggerActions()

            verify(loadCallback).onResult(any(), capture())
            assertThat(firstValue).isNull()
        }
    }
    // loadAfter error callback on error called
    @Test
    fun `loadAfter error callback on error called`() {
        error()

        val testObserver = TestObserver<DataSourceActions>()
        SUT.eventsObservable.subscribe(testObserver)

        val params = PageKeyedDataSource.LoadParams(1, PAGE_SIZE)
        SUT.loadAfter(params, loadCallback)
        testSchedulersProvider.triggerActions()

        // start loading
        assertThat(testObserver.assertValueAt(0) { t ->
            when (t) {
                is DataSourceActions.Loading -> true
                else -> false
            }
        })
        // stop loading
        assertThat(testObserver.assertValueAt(1) { t ->
            when (t) {
                is DataSourceActions.Loading -> true
                else -> false
            }
        })
        // error
        assertThat(testObserver.assertValueAt(2) { t ->
            when (t) {
                is DataSourceActions.Error -> true
                else -> false
            }
        })
    }
    // loadBefore correct page size passed to news api everything
    @Test
    fun `loadBefore correct page size passed to news api everything`() {
        argumentCaptor<Int> {

            success(PAGE_SIZE * 2)

            val params = PageKeyedDataSource.LoadParams(2, PAGE_SIZE)

            SUT.loadBefore(params, loadCallback)
            testSchedulersProvider.triggerActions()

            verify(newsApiMock).everything(any(), capture(), any())

            assertThat(firstValue).isEqualTo(PAGE_SIZE)

        }
    }
    // loadBefore correct page passed to news api everything
    @Test
    fun `loadBefore correct page passed to news api everything`() {
        argumentCaptor<Int> {

            success(PAGE_SIZE * 2)

            val params = PageKeyedDataSource.LoadParams(2, PAGE_SIZE)

            SUT.loadBefore(params, loadCallback)
            testSchedulersProvider.triggerActions()

            verify(newsApiMock).everything(capture(), any(), any())

            assertThat(firstValue).isEqualTo(2)
        }
    }
    // loadBefore success current page more than one prev page returned
    @Test
    fun `loadBefore success current page more than one prev page returned`() {
        argumentCaptor<Int> {

            success(PAGE_SIZE * 2)

            val params = PageKeyedDataSource.LoadParams(2, PAGE_SIZE)

            SUT.loadBefore(params, loadCallback)
            testSchedulersProvider.triggerActions()

            verify(loadCallback).onResult(any(), capture())

            assertThat(firstValue).isEqualTo(1)
        }
    }
    // loadBefore success current page less or equal one prev page is null
    @Test
    fun `loadBefore success current page less or equal one prev page is null`() {
        argumentCaptor<Int> {

            success(PAGE_SIZE * 2)

            val params = PageKeyedDataSource.LoadParams(1, PAGE_SIZE)

            SUT.loadBefore(params, loadCallback)
            testSchedulersProvider.triggerActions()

            verify(loadCallback).onResult(any(), capture())

            assertThat(firstValue).isNull()
        }
    }
    // loadBefore error callback on error called
    @Test
    fun `loadBefore error callback on error called`() {
        error()

        val testObserver = TestObserver<DataSourceActions>()
        SUT.eventsObservable.subscribe(testObserver)

        val params = PageKeyedDataSource.LoadParams(1, PAGE_SIZE)
        SUT.loadBefore(params, loadCallback)
        testSchedulersProvider.triggerActions()

        // start loading
        assertThat(testObserver.assertValueAt(0) { t ->
            when (t) {
                is DataSourceActions.Loading -> true
                else -> false
            }
        })
        // stop loading
        assertThat(testObserver.assertValueAt(1) { t ->
            when (t) {
                is DataSourceActions.Loading -> true
                else -> false
            }
        })
        // error
        assertThat(testObserver.assertValueAt(2) { t ->
            when (t) {
                is DataSourceActions.Error -> true
                else -> false
            }
        })
    }

    // helpers
    private fun success(itemsCount: Int = 1) {
        whenever(newsApiMock.everything(any(), any(), any()))
            .thenReturn(Single.just(getArticlesSchema(itemsCount)))
    }

    private fun error() {
        whenever(newsApiMock.everything(any(), any(), any()))
            .thenReturn(Single.error(IllegalStateException()))
    }

    companion object {
        private const val PAGE_SIZE = 20
    }

}
