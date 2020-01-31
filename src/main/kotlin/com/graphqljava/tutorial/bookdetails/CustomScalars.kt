package com.graphqljava.tutorial.bookdetails

import graphql.GraphQLException
import graphql.language.StringValue
import graphql.schema.Coercing
import graphql.schema.CoercingParseLiteralException
import graphql.schema.GraphQLScalarType
import org.slf4j.LoggerFactory

data class EmailScalarType(val rawValue: String)

val emailCoercing = object : Coercing<EmailScalarType, String> {

    private val logger = LoggerFactory.getLogger("emailCoercing")

    override fun parseValue(input: Any): EmailScalarType {
        logger.info("parseValue {}", input.toString())
        return EmailScalarType(input.toString())
    }

    override fun parseLiteral(input: Any): EmailScalarType {
        logger.info("parseLiteral {}", input.toString())

        when (input) {
            // 入力された値はダウンキャストして使用する
            is StringValue -> return EmailScalarType(input.value)
            // Coercing用の例外クラスを用いることで、適切なエラーレスポンスになる
            else -> throw CoercingParseLiteralException("invalid email value")
        }
    }

    // Server to Client で値を渡すときに呼び出される
    override fun serialize(dataFetcherResult: Any): String {
        logger.info("serialize {}", dataFetcherResult.toString())

        return when (dataFetcherResult) {
            is String -> dataFetcherResult
            is EmailScalarType -> dataFetcherResult.rawValue
            else -> dataFetcherResult.toString()
        }
    }

}

val emailType: GraphQLScalarType = GraphQLScalarType.newScalar().name("Email").coercing(emailCoercing).build()
