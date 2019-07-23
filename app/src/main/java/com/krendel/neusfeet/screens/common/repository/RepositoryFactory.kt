package com.krendel.neusfeet.screens.common.repository

import com.krendel.neusfeet.screens.common.repository.bookmark.BookmarksRepository
import com.krendel.neusfeet.screens.common.repository.everything.EverythingFetchConfiguration
import com.krendel.neusfeet.screens.common.repository.everything.EverythingRepository
import com.krendel.neusfeet.screens.common.repository.sources.SourcesFetchConfiguration
import com.krendel.neusfeet.screens.common.repository.sources.SourcesRepository
import com.krendel.neusfeet.screens.common.repository.topheadlines.TopHeadlinesFetchConfiguration
import com.krendel.neusfeet.screens.common.repository.topheadlines.TopHeadlinesRepository
import io.reactivex.disposables.CompositeDisposable

interface RepositoryFactory {

    fun bookmarksRepository(): BookmarksRepository

    fun topHeadlinesRepository(
        configuration: TopHeadlinesFetchConfiguration,
        compositeDisposable: CompositeDisposable
    ): TopHeadlinesRepository

    fun everythingRepository(
        configuration: EverythingFetchConfiguration,
        compositeDisposable: CompositeDisposable
    ): EverythingRepository

    fun sourcesRepository(
        configuration: SourcesFetchConfiguration,
        compositeDisposable: CompositeDisposable
    ): SourcesRepository

}