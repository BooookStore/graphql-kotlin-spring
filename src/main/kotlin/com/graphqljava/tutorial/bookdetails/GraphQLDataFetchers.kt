package com.graphqljava.tutorial.bookdetails

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class GraphQLDataFetchers {

    private val logger = LoggerFactory.getLogger(GraphQLDataFetchers::class.java)

    data class Book(
            val id: String,
            val name: String,
            val pageCount: Int,
            val authorId: String
    )

    private val books = listOf(
            Book("book-1", "Harry Potter and the Philosopher's Stone", 223, "author-1"),
            Book("book-2", "Moby Dick", 635, "author-2"),
            Book("book-3", "Interview with the vampire", 371, "author-3")
    )

    private val authors = listOf(
            mapOf("id" to "author-1",
                    "firstName" to "Joanne",
                    "lastName" to "Rowling",
                    "birthDay" to LocalDate.of(1990, 1, 1),
                    "email" to EmailScalarType("sample@demo.co.jp")),
            mapOf("id" to "author-2",
                    "firsName" to "Herman",
                    "lastName" to "Melville",
                    "birthDay" to LocalDate.of(1990, 2, 2)),
            mapOf("id" to "author-3",
                    "firstName" to "Anne",
                    "lastName" to "Rice",
                    "birthDay" to LocalDate.of(1990, 3, 3))
    )

    fun getBookByIdDataFetcher(): DataFetcher<Book?> {
        return DataFetcher { dataFetchingEnvironment ->
            val bookId = dataFetchingEnvironment.getArgument<String>("id")
            books.stream().filter { it.id == bookId }.findFirst().orElse(null)
        }
    }

    fun getAuthorDataFetcher(): DataFetcher<Map<String, Any>?> {
        return DataFetcher { dataFetchingEnvironment ->
            val book = dataFetchingEnvironment.getSource<Book>()
            authors.stream().filter { it["id"] == book.authorId }.findFirst().orElse(null)
        }
    }

    // getterを用意することでレスポンスとして使用できる
    data class Character(val name: String, val appearsIn: List<String>) {

        private val logger = LoggerFactory.getLogger(Character::class.java)

        // 引数にenvironmentを宣言することで、メソッド内でアクセスできる
        fun getAppearsIn(environment: DataFetchingEnvironment): List<String> {
            logger.info("argument is {}", environment.getSource() as Character)
            return appearsIn
        }

    }

    fun getHeroDataFetcher(): DataFetcher<Character> {
        return DataFetcher { dataFetchingEnvironment ->
            // schemaで定義されているenum値は文字列として取得できる
            logger.info("argument is {}", dataFetchingEnvironment.getArgument("episode") as String)
            Character("Anakin Skywalker", listOf("NEW_HOPE", "EMPIRE", "JEDI"))
        }
    }

}