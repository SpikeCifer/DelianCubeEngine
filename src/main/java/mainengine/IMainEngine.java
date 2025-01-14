/*
*    DelianCubeEngine. A simple cube query engine.
*    Copyright (C) 2018  Panos Vassiliadis
*
*    This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU Affero General Public License as published
*    by the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU Affero General Public License for more details.
*
*    You should have received a copy of the GNU Affero General Public License
*    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*
*/


package mainengine;

import java.io.File;
import java.io.FileNotFoundException;
//import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



/**
 * @author pvassil
 *
 */
public interface IMainEngine extends IServer {

	/**
	 * Use initializeConnection as the first step between a CLIENT and any implementation of the IMainEngine.
	 * 
	 * @param typeOfConnection	Must be one of the below -options.
	 * @param userInputList		Whatever the type connection is you will need the
	 * 							required variables written below.
	 * 
	 * - RDBMS userInputList
	 * @param schemaName  	The name of the database 
	 * @param login  		The user login
	 * @param passwd  		The user passwd
	 * @param inputFolder	The folder where the cube description lies (includes a dbc.ini for the connection and cubde descriptions too)
	 * @param cubeName		The name of the cube + the name of the file where the cube is described = <cubename>.ini
	 * 
	 * - Spark userInputList
	 * @param schemaName	The name of the spark folder containing all...
	 * @param inputFolder 	Same...
	 * @param cubeName 		Same...
	 * 
	 * @throws RemoteException
	 */
	void initializeConnection(String typeOfConnection, HashMap<String, String> userInputList) throws RemoteException;

	/**
	 * Has same functionality as initializeConnection and also initializes the only Interestingness Manager object
	 * Used only by the Interestingness Client.
	 * 
	 * See the initializeConnection
	 * 
	 * @param typeOfConnection	Must be one of the below -options.
	 * @param userInputList		Whatever the type connection is you will need the
	 * 							required variables written below.
	 * 
	 * - RDBMS userInputList
	 * @param schemaName  	The name of the database 
	 * @param login  		The user login
	 * @param passwd  		The user passwd
	 * @param inputFolder	The folder where the cube description lies (includes a dbc.ini for the connection and cubde descriptions too)
	 * @param cubeName		The name of the cube + the name of the file where the cube is described = <cubename>.ini
	 * 
	 * - Spark userInputList
	 * @param schemaName	The name of the spark folder containing all...
	 * @param inputFolder 	Same...
	 * @param cubeName 		Same...
	 * 
	 * 
	 * @param historyFolder   Path to the History folder
	 * @param expValuesFolder Path to the Expected Values folder
	 * @param expLabelsFolder Path to the Expected Labels folder
	 * @param BeliefFolder	  Path to the Cell Beliefs folder
	 * @param k               The kth neighbor
	 * 
	 * @throws RemoteException
	 * @see initializeConnection
	 */
	void initializeConnectionWithIntrMng(String typeOfConnection, HashMap<String, String> userInputList, String historyFolder, 
			  String expValuesFolder, String expLabelsFolder, String beliefFolder, int k) throws RemoteException;
	
	
	/**
	 * Answers a query as prescribed in a String
	 * 
	 * An exemplary query definition is as follows:
	 * <p>
	 * <code>CubeName:loan</code>
	 * <code>Name:CubeQueryLoan2</code>
	 * <code>AggrFunc:Avg</code>
	 * <code>Measure:amount</code>
	 * <code>Gamma:account_dim.lvl2,date_dim.lvl2</code>
	 * <code>Sigma:account_dim.lvl1='Liberec',status_dim.lvl0='Running Contract/OK'</code>
	 * <p>
	 * Observe:
	 * The query has a name, in the example,CubeQueryLoan2
	 * The query comes with two groupers
	 * The selection condition Sigma, comes as a COMMA-SEPARATED list of atomic expressions of the form Level=value
	 * 
	 * TODO: test with less/more groupers + with alternative selection atoms
	 * 
	 * @param queryRawString	A String with the query definition
	 * @return the path of the result file
	 * @throws RemoteException
	 * 
	 */
	String answerCubeQueryFromString(String queryRawString) throws RemoteException;
	
	
	


	
	/**
	 * This is the method to call for answering several queries from a single file.
	 *  
	 * Inside the file, queries are separated by a '@'
	 * 
	 * @param file	The PATH of the filename containing the queries
	 * @return	An ArrayList of Strings with the names of the files containing the results of the queries.
	 * @throws RemoteException
	 * @see answerCubeQueryFromString
	 */
	ArrayList<String> answerCubeQueriesFromFile(File file) throws RemoteException;
	
	
	/**
	 * Answers a query as prescribed in a String and returns (a) a file with the results of the query and (b) an info file with metadata
	 * 
	 * See the answerCubeQueryFromString
	 * 
	 * @param queryRawString	A String with the query definition
	 * @return	A ResultFileMetadata object containing the respective String values on the location of the two produced files and their parent folder 
	 * @throws RemoteException
	 * @see answerCubeQueryFromString
	 */
	ResultFileMetadata answerCubeQueryFromStringWithMetadata(String queryRawString) throws RemoteException;

	
	
	/**
	 * Answers a query as prescribed in a String and returns a file with the results of the query and a set of models produced over the result of the query 
	 * 
	 * See the answerCubeQueryFromString
	 * 
	 * @param queryRawString	A String with the query definition
	 * @param modelsToGenerate	An array of Strings with the model names to generate; if empty, a list of default models are applied
	 * @return	A ResultFileMetadata object containing the respective String values on the location of the produced files and their parent folder 
	 * @throws RemoteException
	 * @see answerCubeQueryFromString
	 */
	ResultFileMetadata answerCubeQueryFromStringWithModels(String queryRawString, String [] modelsToGenerate) throws RemoteException;
	
	
	
	/**
	 * Performs the Roll-Up operator and executes the new query returning the results in a file
	 * 
	 * @param cubeQueryName 	The name of the query the user wants to perform Roll-Up on
	 * @param newQueryName 		The name of the query that will be produced after the performance of the Roll-Up operator
	 * @param dimensionName		The dimension that the Roll-Up operator will be performed on
	 * @param targetLevelName	The level that is the target of the Roll-Up operator
	 * @return 	A ResultFileMetadata object containing the respective String values on the location of the produced files or a message if any errors are found
	 * @throws RemoteException
	 */
	ResultFileMetadata rollUp(String cubeQueryName, String newQueryName, String dimensionName, String targetLevelName) throws RemoteException;
	
	
	
	/**
	 * Performs the Drill-Down operator and executes the new query returning the results in a file
	 * 
	 * @param cubeQueryName 	The name of the query the user wants to perform Drill-Down on
	 * @param newQueryName 		The name of the query that will be produced after the performance of the Drill-Down operator
	 * @param dimensionName		The dimension that the Drill-Down operator will be performed on
	 * @param targetLevelName	The level that is the target of the Drill-Down operator
	 * @return 	A ResultFileMetadata object containing the respective String values on the location of the produced files or a message if any errors are found
	 * @throws RemoteException
	 */
	ResultFileMetadata drillDown(String cubeQueryName, String newQueryName, String dimensionName, String targetLevelName) throws RemoteException;
	
	
	/**
	 * 
	 * @param incomingExpression The String that defines an intentional query for the analyze operator
	 * @returnA ResultFileMetadata object containing the respective String values on the location of the produced files or a message if any errors are found
	 * @throws RemoteException
	 */
	ResultFileMetadata analyze(String incomingExpression) throws RemoteException;
	
	
	/**
	 * 
	 * @param incomingExpression The String that defines an intentional query for the assess operator
	 * @returnA ResultFileMetadata object containing the respective String values on the location of the produced files or a message if any errors are found
	 * @throws RemoteException
	 */
	ResultFileMetadata assess(String incomingExpression) throws RemoteException;
	
	
	
	/**
	 * Answers a natural language query as prescribed in a String
	 * 
	 * An example of the the query form is as it follows:
	 * <p>
	 * Describe the avg of loan amount per account_dim.lvl1,date_dim.lvl2 for account_dim.lvl2='north Moravia' as LoanQuery11_S1_CG-Prtl
	 * <p>
	 * @param queryRawString A String with the query
	 * @return the path of the result file
	 * @throws RemoteException
	 */
	String answerCubeQueryFromNLString(String queryRawString) throws RemoteException;


	/**
	 * Answers a natural language query as prescribed in a String and returns (a) a file with the results of the query and (b) an info file with metadata
	 * 
	 * See the answerCubeQueryFromNLString
	 * 
	 * @param queryRawString	A String with the query definition
	 * @return	A ResultFileMetadata object containing the respective String values on the location of the two produced files and their parent folder 
	 * @throws RemoteException
	 * @see {@link #answerCubeQueryFromNLString(String)}
	 */
	ResultFileMetadata answerCubeQueryFromNLStringWithMetadata(String queryRawString) throws RemoteException;
	
	/**
	 * Gets the natural language query from a string, passes it to a NLQValidator object, which parses it and produces error messages if any errors where found.
	 * The errors are returned to the client as a ErrorChecking.txt file, so the client can produce the error message to the end-user.
	 *
	 *@param queryString A String with the natural language query given by the user.
	 *@return a ResultFileMetadata object containing info on the location of the error checking file at the server
	 *@throws RemoteException
	 *@author DimosGkitsakis
	 */
	ResultFileMetadata prepareCubeQuery(String queryString) throws RemoteException;
	
	/**
	 * Answers a query as prescribed in a String and computes the chosen measures 
	 * 
	 * See the answerCubeQueryFromString
	 * @param queryString A String with the query
	 * @param measures    A List with the chosen measures
	 * @return A String Array with the results of all the measures
	 * @throws RemoteException
	 * @see answerCubeQueryFromString
	 */
    String[] answerCubeQueryWithInterestMeasures(String queryString, List<String> measures) throws RemoteException;
    
	/**
	 * Answers a query as prescribed in a String and computes the chosen measures. Mainly used for FamilyBasedRelevance metric 
	 * 
	 * See the answerCubeQueryFromString
	 * @param queryString A String with a query
	 * @param queryString1 A String with another query
	 * @param measures    A List with the chosen measures
	 * @return A String Array with the results of all the measures
	 * @throws RemoteException
	 * @throws FileNotFoundException 
	 * @see answerCubeQueryFromString
	 */
    String[] answerCubeQueryWithInterestMeasures(String queryString, String queryString1, List<String> measures) throws RemoteException, FileNotFoundException;

	
	/**
	 * @deprecated	Not to be used in the context of the DelianCubeEngine project.
	 * This is an old-to-be-removed method, from Cinecubes, that turns on/off audio and MS word generation
	 * 
	 * @param audio	To turn on/off audio generation
	 * @param word	To turn on/off MS Word generation
	 * @throws RemoteException
	 */
//	void optionsChoice(boolean audio, boolean word)throws RemoteException;
	
	

}