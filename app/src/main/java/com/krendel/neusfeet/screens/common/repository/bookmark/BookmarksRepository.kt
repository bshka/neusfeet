package com.krendel.neusfeet.screens.common.repository.bookmark

import com.krendel.neusfeet.screens.common.repository.common.PagedListing
import com.krendel.neusfeet.screens.common.views.articles.ArticleItemViewModel

interface BookmarksRepository {
    fun bookmarks(pageSize: Int): PagedListing<ArticleItemViewModel>
}