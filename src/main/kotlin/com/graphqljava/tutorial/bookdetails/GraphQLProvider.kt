package com.graphqljava.tutorial.bookdetails

import com.google.common.io.Resources
import graphql.GraphQL
import graphql.schema.GraphQLSchema
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import graphql.schema.idl.TypeRuntimeWiring
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class GraphQLProvider {

    private var graphQl: GraphQL? = null

    @Autowired
    private lateinit var graphQLDataFetchers: GraphQLDataFetchers

    @Bean
    fun graphQL(): GraphQL? {
        return graphQl
    }

    @PostConstruct
    @Suppress("UnstableApiUsage")
    fun init() {
        // schema.graphqls ファイルの読み込み
        val url = Resources.getResource("schema.graphqls")
        val sdl = Resources.toString(url, Charsets.UTF_8)

        // GraphQLSchema 及び GraphQL インスタンスの作成
        val graphQLSchema = buildSchema(sdl)
        this.graphQl = GraphQL.newGraphQL(graphQLSchema).build()
    }

    /**
     * GraphQLSchema インスタンスの作成
     */
    fun buildSchema(sdl: String): GraphQLSchema {
        val typeRegistry = SchemaParser().parse(sdl)
        val runtimeWiring = buildWiring()
        return SchemaGenerator().makeExecutableSchema(typeRegistry, runtimeWiring)
    }

    private fun buildWiring(): RuntimeWiring {
        return RuntimeWiring.newRuntimeWiring()
                .type(TypeRuntimeWiring.newTypeWiring("Query")
                        .dataFetcher("bookById", graphQLDataFetchers.getBookByIdDataFetcher())
                        .dataFetcher("hero", graphQLDataFetchers.getHeroDataFetcher()))
                .type(TypeRuntimeWiring.newTypeWiring("Book")
                        .dataFetcher("author", graphQLDataFetchers.getAuthorDataFetcher()))
                .build()
    }

}
