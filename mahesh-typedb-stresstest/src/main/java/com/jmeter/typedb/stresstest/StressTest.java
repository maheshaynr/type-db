package com.jmeter.typedb.stresstest;


import com.vaticle.typedb.driver.api.*;
import com.vaticle.typedb.driver.api.answer.ConceptMap;
import com.vaticle.typedb.driver.TypeDB;
import com.vaticle.typedb.driver.api.answer.JSON;
import com.vaticle.typedb.driver.common.exception.TypeDBDriverException;
import com.vaticle.typeql.lang.TypeQL;
import com.vaticle.typeql.lang.common.TypeQLArg;
import com.vaticle.typeql.lang.common.TypeQLToken;
import com.vaticle.typeql.lang.query.TypeQLDefine;
import com.vaticle.typeql.lang.query.TypeQLFetch;
import com.vaticle.typeql.lang.query.TypeQLInsert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;



public class StressTest {
	/*public static void main(String[] args) {
        final String DB_NAME = "access-management-db";
        final String SERVER_ADDR = "127.0.0.1:1729";

        try (TypeDBDriver driver = TypeDB.coreDriver(SERVER_ADDR)) {

            if (driver.databases().contains(DB_NAME)) {
                driver.databases().get(DB_NAME).delete();
            }
            driver.databases().create(DB_NAME);

            try (TypeDBSession session = driver.session(DB_NAME, TypeDBSession.Type.SCHEMA)) {
                try (TypeDBTransaction tx = session.transaction(TypeDBTransaction.Type.WRITE)) {
                    TypeQLDefine defineQuery = TypeQL.define(
                                    TypeQL.type("person").sub(TypeQLToken.Type.ENTITY).owns("name"),
                                    TypeQL.type("name").sub(TypeQLToken.Type.ATTRIBUTE).value(TypeQLArg.ValueType.STRING)
                    );
                    tx.query().define(defineQuery);
                    tx.commit();
                }
            }
            try (TypeDBSession session = driver.session(DB_NAME, TypeDBSession.Type.DATA)) {
                try (TypeDBTransaction tx = session.transaction(TypeDBTransaction.Type.WRITE)) {
                    TypeQLInsert insertQuery = TypeQL.insert(
                            TypeQL.cVar("p1").isa("person").has("name", "Alice"),
                            TypeQL.cVar("p2").isa("person").has("name","Bob")
                    );
                    tx.query().insert(insertQuery);
                    tx.commit();
                    }
                try (TypeDBTransaction tx = session.transaction(TypeDBTransaction.Type.READ)) {
                    TypeQLFetch fetchQuery = TypeQL.match(
                            TypeQL.cVar("p").isa("person")
                    ).fetch(TypeQL.cVar("p").map("name"));
                    tx.query().fetch(fetchQuery).forEach(result -> System.out.println(result.toString()));
                }
            }
        }
    }
    */
}
