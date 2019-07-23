package com.krendel.neusfeet.screens.bookmarks.data

import com.google.common.truth.Truth.assertThat
import com.krendel.neusfeet.model.articles.Article
import com.krendel.neusfeet.screens.common.repository.bookmark.BookmarksRepository
import com.krendel.neusfeet.screens.common.repository.bookmark.RemoveBookmarkUseCase
import com.krendel.neusfeet.utils.FakeGenerator.getArticle
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Completable
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import java.util.*

class BookmarksDataInteractorImplTest {

    private lateinit var SUT: BookmarksDataInteractorImpl
    private lateinit var removeUseCaseMock: RemoveBookmarkUseCase
    private lateinit var bookmarkRepoMock: BookmarksRepository

    @Before
    fun setUp() {
        removeUseCaseMock = mock()
        bookmarkRepoMock = mock()
        SUT = BookmarksDataInteractorImpl(removeUseCaseMock, bookmarkRepoMock)
    }

    @Test
    fun `removeBookmark correct article passed to use case`() {
        argumentCaptor<Article> {
            val date = Date()

            SUT.removeBookmark(getArticle(date))
            verify(removeUseCaseMock).remove(capture())

            assertThat(firstValue).isEqualTo(getArticle(date))
        }
    }

    @Test
    fun `remove bookmark success complete called`() {
        whenever(removeUseCaseMock.remove(any()))
            .thenReturn(Completable.complete())

        val testSubscriber = TestObserver<Unit>()

        SUT.removeBookmark(getArticle(Date()))
            .subscribe(testSubscriber)

        assertThat(testSubscriber.assertComplete())
    }

    @Test
    fun `remove bookmark error on error called`() {
        whenever(removeUseCaseMock.remove(any()))
            .thenReturn(Completable.error(NullPointerException()))
        val testSubscriber = TestObserver<Unit>()

        SUT.removeBookmark(getArticle(Date()))
            .subscribe(testSubscriber)

        assertThat(testSubscriber.assertError(NullPointerException::class.java))
    }

    @Test
    fun `bookmark correct page size passed to repository`() {
        argumentCaptor<Int> {
            SUT.bookmarks(55)
            verify(bookmarkRepoMock).bookmarks(capture())

            assertThat(firstValue).isEqualTo(55)
        }
    }
}