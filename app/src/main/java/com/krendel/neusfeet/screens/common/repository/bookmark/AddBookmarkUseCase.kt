package com.krendel.neusfeet.screens.common.repository.bookmark

import com.krendel.neusfeet.local.bookmarks.BookmarksDao
import com.krendel.neusfeet.model.articles.Article
import com.krendel.neusfeet.networking.schedulers.SchedulersProvider
import io.reactivex.Completable

class AddBookmarkUseCase(
    private val bookmarksDao: BookmarksDao,
    private val schedulersProvider: SchedulersProvider
) {

    fun add(article: Article): Completable =
        Completable.create {
            try {
                bookmarksDao.add(article)
                it.onComplete()
            } catch (e: Exception) {
                it.tryOnError(e)
            }
        }.subscribeOn(schedulersProvider.io())

}