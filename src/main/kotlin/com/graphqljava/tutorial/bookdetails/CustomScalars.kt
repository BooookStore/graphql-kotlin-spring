package com.graphqljava.tutorial.bookdetails

import graphql.schema.Coercing
import graphql.schema.GraphQLScalarType
import org.slf4j.LoggerFactory

val emailCoercing = object : Coercing<String, String> {

    private val logger = LoggerFactory.getLogger("emailCoercing")

    override fun parseValue(input: Any): String {
        logger.info("parseValue {}", input.toString())
        return input.toString()
    }

    override fun parseLiteral(input: Any): String {
        logger.info("parseLiteral {}", input.toString())
        return input.toString()
    }

    override fun serialize(dataFetcherResult: Any): String {
        logger.info("serialize {}", dataFetcherResult.toString())
        return dataFetcherResult.toString()
    }

}

val emailType: GraphQLScalarType = GraphQLScalarType.newScalar().name("Email").coercing(emailCoercing).build()
