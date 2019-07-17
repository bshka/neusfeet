package com.krendel.neusfeet.screens.common.repository.bookmark

import com.google.common.truth.Truth.assertThat
import com.krendel.neusfeet.local.bookmarks.BookmarksDao
import com.krendel.neusfeet.model.articles.Article
import com.krendel.neusfeet.utils.TestSchedulersProvider
import com.nhaarman.mockitokotlin2.*
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import java.util.*

class AddBookmarkUseCaseTest {

    private lateinit var SUT: AddBookmarkUseCase
    private lateinit var bookmarksMock: BookmarksDao
    private val testSchedulerProvider = TestSchedulersProvider()

    @Before
    fun setUp() {
        bookmarksMock = mock()
        SUT = AddBookmarkUseCase(bookmarksMock, testSchedulerProvider)
    }

    @Test
    fun `add correct article provided to bookmarks dao add`() {
        argumentCaptor<Article> {
            val article = getArticle(Date())
            SUT.add(article).subscribe()

            testSchedulerProvider.triggerActions()
            verify(bookmarksMock).add(capture())

            assertThat(firstValue).isEqualTo(article)
        }
    }

    @Test
    fun `add success complete called`() {
        val testSubscriber = TestObserver<Unit>()
        SUT.add(getArticle(Date()))
            .subscribe(testSubscriber)

        testSchedulerProvider.triggerActions()

        assertThat(testSubscriber.assertComplete()).isEqualTo(testSubscriber)
    }

    @Test
    fun `add fails on error called`() {
        whenever(bookmarksMock.add(any()))
            .thenThrow(NullPointerException())

        val testSubscriber = TestObserver<Unit>()
        SUT.add(getArticle(Date()))
            .subscribe(testSubscriber)

        testSchedulerProvider.triggerActions()

        assertThat(testSubscriber.assertError(NullPointerException::class.java))
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