package com.graphqljava.tutorial.bookdetails

import graphql.schema.Coercing
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
        return EmailScalarType(input.toString())
    }

    override fun serialize(dataFetcherResult: Any): String {
        logger.info("serialize [{}] type is [{}]", dataFetcherResult.toString())

        return when (dataFetcherResult) {
            is String -> dataFetcherResult
            is EmailScalarType -> dataFetcherResult.rawValue
            else -> dataFetcherResult.toString()
        }
    }

}

val emailType: GraphQLScalarType = GraphQLScalarType.newScalar().name("Email").coercing(emailCoercing).build()
