package com.jmeter.typedb.stresstest;


//tag::code[]
//tag::import[]
import com.vaticle.typedb.driver.api.*;
import com.vaticle.typedb.driver.api.answer.ConceptMap;
import com.vaticle.typedb.driver.TypeDB;
import com.vaticle.typedb.driver.api.answer.JSON;
import com.vaticle.typedb.driver.api.query.QueryManager;

import com.vaticle.typedb.driver.common.exception.TypeDBDriverException;
import com.vaticle.typeql.lang.TypeQL;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
//end::import[]
//tag::class-main[]
public class StressTest_3 {
 // tag::constants[]
 private static final String DB_NAME = "mhsh-stress-test1";
 private static final String SERVER_ADDR = "127.0.0.1:1729";

 public enum Edition {
 CORE,
 CLOUD
 }

 private static final Edition TYPEDB_EDITION = Edition.CORE;
 private static final String CLOUD_USERNAME = "admin";
 private static final String CLOUD_PASSWORD = "password";
 // end::constants[]
 // tag::main[]
 public static void main(String[] args) {
	 StressTest_3 stressTestOBJ=new StressTest_3();
	 stressTestOBJ.processRequests();
 }
 // end::main[]
 // tag::queries[]

 public void processRequests()
 {
	 try (TypeDBDriver driver = connectToTypeDB(TYPEDB_EDITION, SERVER_ADDR)) {
      //   if (dbSetup(driver, DB_NAME, false)) {
             System.out.println("Setup complete.");
             queries(driver, DB_NAME);
        // } else {
        //     System.out.println("Setup failed.");
        // }
     } catch (TypeDBDriverException e) {
         e.printStackTrace();
     }
 }
 public static void queries(TypeDBDriver driver, String dbName) {
//     System.out.println("Request 1 of 6: Fetch all users as JSON objects with full names and emails");
     Stream<JSON> users = fetchAllUsers(driver, dbName);

    // System.out.println(users);
    /* String name = "Jack Keeper";
     String email = "jk@typedb.com";
     String secondRequestMessage = String.format("Request 2 of 6: Request 2 of 6: Add a new user with the full-name \"%s\" and email \"%s\"", name, email);
     System.out.println(secondRequestMessage);
     List<ConceptMap> newUser = insertNewUser(driver, dbName, name, email);

     String nameKevin = "Kevin Morrison";
     String thirdRequestMessage = String.format("Request 3 of 6: Find all files that the user \"%s\" has access to view (no inference)", nameKevin);
     System.out.println(thirdRequestMessage);
   //  List<ConceptMap> no_files = getFilesByUser(driver, dbName, nameKevin, false);
*/
 }
 // end::queries[]
 // tag::connection[]
 private static TypeDBDriver connectToTypeDB(Edition edition, String addr) {
	 System.out.println("CP-1");
     if (edition == Edition.CORE) {
    	 System.out.println("CP-2-"+addr);
         return TypeDB.coreDriver(addr);
     };
     
     System.out.println("CP-3");
     if (edition == Edition.CLOUD) {
         return TypeDB.cloudDriver(addr, new TypeDBCredential(CLOUD_USERNAME, CLOUD_PASSWORD, true ));
     };
     return null;
 }
 // end::connection[]
 // tag::fetch[]
 private static Stream<JSON> fetchAllUsers(TypeDBDriver driver, String dbName) {
	 
	 Stream<JSON>  answers = null;
     try (TypeDBSession session = driver.session(dbName, TypeDBSession.Type.DATA)) {
         try (TypeDBTransaction tx = session.transaction(TypeDBTransaction.Type.READ)) {
             String query = "match\r\n"
             		+ "$user isa user, has name \"Alex\";\r\n"
             		+ "\r\n"
             		+ "(subject: $user, action: $act) isa permission;\r\n"
             		+ "fetch\r\n"
             		+ "$act: name;\r\n"
             		+ "";
         //   System.out.println("CP-4ws3q");
         //    tx.query().fetch(query);
            // System.out.println("tx.query().fetch(query).count()"+tx.query().fetch(query).count());
          answers = tx.query().fetch(query);
           System.out.println("Last Call >>>-");
            
           // System.out.println("CP-5cnt"+answers.count());
			/*
			 * try { // List<JSON> list = answers.collect(Collectors.toList()); Path
			 * fileName = Path.of( "d:\\data.txt");
			 * 
			 * // Writing into the file Files.writeString(fileName, answers.toString()); }
			 * catch (IOException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); }
			 */
          
          
			/*
			 * // Write the stream to a file try (BufferedWriter writer =
			 * Files.newBufferedWriter(Paths.get("d:\\data1.txt"))) { answers.forEach(json
			 * -> { try { // Write each JSONObject to the file as a JSON string
			 * writer.write(json.toString()); writer.newLine(); // Add a newline after each
			 * JSON object } catch (IOException e) { e.printStackTrace(); // Handle the
			 * IOException } }); } catch (IOException e) { e.printStackTrace(); // Handle
			 * the IOException }
			 */
            
            
           
          answers.limit(100).forEach(json -> System.out.println("JSON: " + json.toString()));
           
          System.out.println("Completed ***-");
           
         }  
         catch(Exception ex)
         {
        	 System.out.println("ERROR 0----");
         }
         
     }
     
     catch(Exception ex)
     {
    	 System.out.println("ERROR 1----");
     }
     
     return answers;
 }
 // end::fetch[]
 // tag::insert[]
 public static List<ConceptMap> insertNewUser(TypeDBDriver driver, String dbName, String name, String email) {
     try (TypeDBSession session = driver.session(dbName, TypeDBSession.Type.DATA)) {
         try (TypeDBTransaction tx = session.transaction(TypeDBTransaction.Type.WRITE)) {
             String query = String.format(
                     "insert $p isa person, has full-name $fn, has email $e; $fn \"%s\"; $e \"%s\";", name, email);
             List<ConceptMap> response = (List<ConceptMap>) tx.query().insert(query);
             tx.commit();
             for (ConceptMap conceptMap : response) {
                 String fullName = conceptMap.get("fn").asAttribute().getValue().asString();
                 String emailAddress = conceptMap.get("e").asAttribute().getValue().asString();
                 System.out.println("Added new user. Name: " + fullName + ", E-mail: " + emailAddress);
             }
             return response;
         }
     }
 }
 

 // tag::delete[]
 public static boolean deleteFile(TypeDBDriver driver, String dbName, String path) {
     try (TypeDBSession session = driver.session(dbName, TypeDBSession.Type.DATA);
          TypeDBTransaction tx = session.transaction(TypeDBTransaction.Type.WRITE)) {

         String query = String.format("match $f isa file, has path '%s'; get;", path);
         List<ConceptMap> response = (List<ConceptMap>) tx.query().get(query);

         if (response.size() == 1) {
             tx.query().delete(String.format("match $f isa file, has path '%s'; delete $f isa file;", path)).resolve();
             tx.commit();
             System.out.println("The file has been deleted.");
             return true;
         } else if (response.size() > 1) {
             System.out.println("Matched more than one file with the same path. No files were deleted.");
             return false;
         } else {
             System.out.println("No files matched in the database. No files were deleted.");
             return false;
         }
     } catch (TypeDBDriverException e) {
         e.printStackTrace();
         return false;
     }
 }
 // end::delete[]
 // tag::db-setup[]
 private static boolean dbSetup(TypeDBDriver driver, String dbName, boolean dbReset) {
     System.out.println("Setting up the database: " + dbName);
     if (driver.databases().contains(dbName)) {
         if (dbReset) {
             if (!replaceDatabase(driver, dbName)) {
                 return false;
             }
         } else{
             System.out.println("Found a pre-existing database. Do you want to replace it? (Y/N) ");
             String answer;
             try {
                 BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                 answer = reader.readLine();
             } catch (IOException e) {
                 throw new RuntimeException("Failed to read user input.", e);
             }
             if (answer.equalsIgnoreCase("y")) {
                 if (!replaceDatabase(driver, dbName)) {
                     return false;
                 }
             } else {
                 System.out.println("Reusing an existing database.");
             }
         }
     } else { // No such database found on the server
         if (!createDatabase(driver,dbName)) {
             System.out.println("Failed to create a new database. Terminating...");
             return false;
         }
     }
     if (driver.databases().contains(dbName)) {
         try (TypeDBSession session = driver.session(dbName, TypeDBSession.Type.DATA)) {
             return dbCheck(session);
         }
     } else {
         System.out.println("Database not found. Terminating...");
         return false;
     }
 }
 // end::db-setup[]
 // tag::create_new_db[]
 private static boolean createDatabase(TypeDBDriver driver, String dbName) {
     System.out.print("Creating a new database...");
     driver.databases().create(dbName);
     System.out.println("OK");
     try (TypeDBSession session = driver.session(dbName, TypeDBSession.Type.SCHEMA)) {
         dbSchemaSetup(session);
     }
     try (TypeDBSession session = driver.session(dbName, TypeDBSession.Type.DATA)) {
         dbDatasetSetup(session);
     }
     return true;
 }
 // end::create_new_db[]
 // tag::replace_db[]
 private static boolean replaceDatabase(TypeDBDriver driver, String dbName) {
     System.out.print("Deleting an existing database...");
     driver.databases().get(dbName).delete();  // Delete the database if it exists already
     System.out.println("OK");
     if (createDatabase(driver,dbName)) {
         return true;
     } else {
         System.out.println("Failed to create a new database. Terminating...");
         return false;
     }
 }
 // end::replace_db[]
 // tag::db-schema-setup[]
 private static void dbSchemaSetup(TypeDBSession session) {
     String schemaFile = "D:\\eclipse-workspace\\javaparser-maven-sample-master\\src\\main\\resources\\define-1.tql";
     try (TypeDBTransaction tx = session.transaction(TypeDBTransaction.Type.WRITE)) {
         String defineQuery = new String(Files.readAllBytes(Paths.get(schemaFile)));
         System.out.print("Defining schema...");
         tx.query().define(defineQuery).resolve();
         tx.commit();
         System.out.println("OK");
     } catch (IOException e) {
         throw new RuntimeException("Failed to read the schema file.", e);
     }
 }
 // end::db-schema-setup[]
 // tag::db-dataset-setup[]
 private static void dbDatasetSetup(TypeDBSession session) {
     String dataFile = "D:\\eclipse-workspace\\javaparser-maven-sample-master\\src\\main\\resources\\data-1.tql";
     try (TypeDBTransaction tx = session.transaction(TypeDBTransaction.Type.WRITE)) {
         String insertQuery = new String(Files.readAllBytes(Paths.get(dataFile)));
         System.out.print("Loading data...");
         tx.query().insert(insertQuery);
         tx.commit();
         System.out.println("OK");
     } catch (IOException e) {
         throw new RuntimeException("Failed to read the data file.", e);
     }
 }
 // end::db-dataset-setup[]
 // tag::test-db[]
 private static boolean dbCheck(TypeDBSession session) {
     try (TypeDBTransaction transaction = session.transaction(TypeDBTransaction.Type.READ)) {
         String testQuery = "match $u isa user; get $u; count;";
         System.out.print("Testing the database...");
         long result = transaction.query().getAggregate(testQuery).resolve().get().asLong();
         if (result >= 2) {
             System.out.println("Passed");
             return true;
         } else {
             System.out.println("Failed the test with the result: " + result + "\n Expected result: 3.");
             return false;
         }
     }
 }
 // end::test-db[]
}
//tag::class-main[]
//tag::code[]