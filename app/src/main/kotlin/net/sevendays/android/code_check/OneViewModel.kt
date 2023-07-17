/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package net.sevendays.android.code_check

import android.content.Context
import android.os.Parcelable
import androidx.lifecycle.ViewModel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.sevendays.android.code_check.TopActivity.Companion.lastSearchDate
import kotlinx.parcelize.Parcelize
import org.json.JSONObject
import java.util.*

class OneViewModel(
    private val context: Context
) : ViewModel() {
    suspend fun searchResults(inputText: String): List<item> {
        return withContext(Dispatchers.IO) {
            val fetcher = SearchResultFetcher(context)
            return@withContext fetcher.fetch(inputText)
        }
    }
}

object HttpClientProvider {
    val httpClient: HttpClient
        get() = HttpClient(Android)
}

object ItemFormatter {
    fun formatLanguage(context: Context, language: String): String {
        return context.getString(R.string.written_language, language)
    }
}

class SearchResultFetcher(private val context: Context) {
    private val httpClient = HttpClientProvider.httpClient

    suspend fun fetch(inputText: String): List<item> {
        val response: HttpResponse = httpClient.get("https://api.github.com/search/repositories") {
            header("Accept", "application/vnd.github.v3+json")
            parameter("q", inputText)
        }
        val jsonBody = JSONObject(response.receive<String>())
        val jsonItems = jsonBody.optJSONArray("items") ?: return emptyList()
        val items = mutableListOf<item>()
        for (i in 0 until jsonItems.length()) {
            val jsonItem = jsonItems.optJSONObject(i) ?: continue

            val name = jsonItem.optString("full_name")
            val ownerIconUrl = jsonItem.optJSONObject("owner")?.optString("avatar_url")
            val language = ItemFormatter.formatLanguage(context, jsonItem.optString("language"))
            val stargazersCount = jsonItem.optLong("stargazers_count")
            val watchersCount = jsonItem.optLong("watchers_count")
            val forksCount = jsonItem.optLong("forks_count")
            val openIssuesCount = jsonItem.optLong("open_issues_count")

            items.add(
                item(
                    name = name,
                    ownerIconUrl = ownerIconUrl,
                    language = context.getString(R.string.written_language, language),
                    stargazersCount = stargazersCount,
                    watchersCount = watchersCount,
                    forksCount = forksCount,
                    openIssuesCount = openIssuesCount
                )
            )
        }
        lastSearchDate = Date()
        return items
    }
}

@Parcelize
data class item(
    val name: String,
    val ownerIconUrl: String?,
    val language: String,
    val stargazersCount: Long,
    val watchersCount: Long,
    val forksCount: Long,
    val openIssuesCount: Long
) : Parcelable