package com.krendel.neusfeet.screens.common.repository.bookmark

import com.krendel.neusfeet.model.articles.Article
import io.reactivex.Completable

interface AddBookmarkUseCase {
    fun add(article: Article): Completable
}