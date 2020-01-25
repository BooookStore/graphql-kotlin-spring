package com.graphqljava.tutorial.bookdetails

import graphql.schema.DataFetcher
import org.springframework.stereotype.Component

@Component
class GraphQLDataFetchers {

    private val books = listOf(
            mapOf("id" to "book-1",
                    "name" to "Harry Potter and the Philosopher's Stone",
                    "pageCount" to "223",
                    "authorId" to "author-1"),
            mapOf("id" to "book-2",
                    "name" to "Moby Dick",
                    "pageCount" to "635",
                    "authorId" to "author-3"),
            mapOf("id" to "book-3",
                    "name" to "Interview with the vampire",
                    "pageCount" to "371",
                    "authorId" to "author-3")
    )

    private val authors = listOf(
            mapOf("id" to "author-1",
                    "firstName" to "Joanne",
                    "lastName" to "Rowling"),
            mapOf("id" to "author-2",
                    "firsName" to "Herman",
                    "lastName" to "Melville"),
            mapOf("id" to "author-3",
                    "firstName" to "Anne",
                    "lastName" to "Rice")
    )

    fun getBookByIdDataFetcher(): DataFetcher<Map<String, String>?> {
        return DataFetcher { dataFetchingEnvironment ->
            val bookId = dataFetchingEnvironment.getArgument<String>("id")
            books.stream().filter { it["id"] == bookId }.findFirst().orElse(null)
        }
    }

    fun getAuthorDataFetcher(): DataFetcher<Map<String, String>?> {
        return DataFetcher { dataFetchingEnvironment ->
            val book = dataFetchingEnvironment.getSource<Map<String, String>>()
            val authorId = book["authorId"]
            authors.stream().filter { it["id"] == authorId }.findFirst().orElse(null)
        }
    }

}