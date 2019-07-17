package com.krendel.neusfeet.screens.common.repository.bookmark

import com.google.common.truth.Truth
import com.krendel.neusfeet.local.bookmarks.BookmarksDao
import com.krendel.neusfeet.model.articles.Article
import com.krendel.neusfeet.utils.TestSchedulersProvider
import com.nhaarman.mockitokotlin2.*
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import java.util.*


class RemoveBookmarkUseCaseTest {

    private lateinit var SUT: RemoveBookmarkUseCase
    private lateinit var bookmarksMock: BookmarksDao
    private val testSchedulerProvider = TestSchedulersProvider()

    @Before
    fun setUp() {
        bookmarksMock = mock()
        SUT = RemoveBookmarkUseCase(bookmarksMock, testSchedulerProvider)
    }

    // add correct article provided to bookmarks dao add
    @Test
    fun `remove correct article provided to bookmarks dao delete`() {
        argumentCaptor<Article> {
            val article = getArticle(Date())
            SUT.remove(article).subscribe()

            testSchedulerProvider.triggerActions()
            verify(bookmarksMock).delete(capture())

            Truth.assertThat(firstValue).isEqualTo(article)
        }
    }

    // add success complete called
    @Test
    fun `remove success complete called`() {
        val testSubscriber = TestObserver<Unit>()
        SUT.remove(getArticle(Date()))
            .subscribe(testSubscriber)

        testSchedulerProvider.triggerActions()

        Truth.assertThat(testSubscriber.assertComplete()).isEqualTo(testSubscriber)
    }

    // add fails on error called
    @Test
    fun `add fails on error called`() {
        whenever(bookmarksMock.delete(any()))
            .thenThrow(NullPointerException())

        val testSubscriber = TestObserver<Unit>()
        SUT.remove(getArticle(Date()))
            .subscribe(testSubscriber)

        testSchedulerProvider.triggerActions()

        Truth.assertThat(testSubscriber.assertError(NullPointerException::class.java))
    }

    companion object {

        private fun getArticle(publishedAt: Date) = Article(
            localId = 123,
            content = "content",
            urlToImage = "url to image",
            title = "title",
            sourceId = "source id",
            sourceName = "source name",
            description = "description",
            url = "url",
            author = "author",
            publishedAt = publishedAt
        )
    }
}