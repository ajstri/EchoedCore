/*
 *  Copyright 2020 EchoedAJ
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package echoedcore.utilities;

import echoedcore.core.EchoedCore;
import echoedcore.utilities.exceptions.NoConfigurationFileException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;

/**
 * FileUtilities class of the EchoedCore project
 *
 * @author EchoedAJ
 * @since April 2020
 */
@SuppressWarnings("unused")
public class FileUtilities {

    /**
     * Adds a JSON Array to the Configuration File.
     *
     * @param obj JSONObject to add to file
     */
    @SuppressWarnings("Duplicates")
    public static int writeToFile(JSONObject obj, String fileName) {
        // Write to the file.
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            fileWriter.write(
                    obj.toString()
                            .replace("[", "[\n")
                            .replace("{", "{\n")
                            .replace(",", ",\n")
                            .replace("}", "\n}")
                            .replace("]", "\n]")
            );
            fileWriter.flush();
            fileWriter.close();
            return Constants.WRITE_TO_FILE_SUCCESS;
        }
        catch (IOException ioe) {
            EchoedCore.getLog().error("Unable to write to file.", ioe);
            return Constants.WRITE_TO_FILE_FAIL;
        }
    }

    /**
     * Adds a JSON Array to the Configuration File.
     *
     * @param object JSONObject to add to file
     */
    @SuppressWarnings("Duplicates")
    public static int writeToFile(JSONArray object, String fileName) {
        // Write to the file.
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            fileWriter.write(
                    object.toString()
                            .replace("[", "[\n")
                            .replace("{", "{\n")
                            .replace(",", ",\n")
                            .replace("}", "\n}")
                            .replace("]", "\n]")
            );
            fileWriter.flush();
            fileWriter.close();
            return Constants.WRITE_TO_FILE_SUCCESS;
        }
        catch (IOException ioe) {
            EchoedCore.getLog().error("Unable to write to file.", ioe);
            return Constants.WRITE_TO_FILE_FAIL;
        }
    }

    /**
     * Retrieves a specific value by key in a given file.
     *
     * @param fileName Name of the file to retrieve key from
     * @param key Key to retrieve
     * @param arrayLocation Array the key is located in
     * @return Value of the key
     */
    @SuppressWarnings("ConstantConditions")
    public static String getValueByKey(String fileName, String key, String arrayLocation) {
        JSONArray object = getJSONFileArray(fileName);

        if (object.equals(null)) {
            EchoedCore.getLog().error(key + " is null.", new NoConfigurationFileException("Failed to grab " + key));
            return  "" + Constants.STATUS_NO_CONFIG;
        }

        JSONObject jsonObject = object.getJSONObject(0);
        JSONObject array = (JSONObject)jsonObject.opt(arrayLocation);

        return array.optString(key);
    }

    /**
     * Retrieves the JSONObject to read the JSON File.
     *
     * @return JSONObject
     */
    public static JSONArray getJSONFileArray(String fileName) {
        JSONObject obj;
        JSONArray array = new JSONArray();
        StringBuilder sb = new StringBuilder();

        try {
            FileReader reader = new FileReader(fileName);

            int i;
            while ((i = reader.read()) != -1) {
                sb.append((char) i);
            }

            obj = new JSONObject(sb.toString().replace("[", "").replace("]", ""));
            array.put(obj);

        }
        catch (FileNotFoundException fnfe) {
            EchoedCore.getLog().error("File not found.", fnfe);
            EchoedCore.shutdown(Constants.STATUS_NO_CONFIG);
        }
        catch (Exception e) {
            EchoedCore.getLog().error("File could not be read.", e);
            EchoedCore.shutdown(Constants.STATUS_NO_CONFIG);
        }

        return array;
    }

    /**
     * Retrieves the JSONObject to read the JSON File.
     * @return JSONObject
     */
    public static JSONObject getJSONFileObject(String fileName) {
        JSONObject obj;
        StringBuilder sb = new StringBuilder();

        try {
            FileReader reader = new FileReader(fileName);

            int i;
            while ((i = reader.read()) != -1) {
                sb.append((char) i);
            }

            obj = new JSONObject(sb.toString().replace("[", "").replace("]", ""));

            return obj;
        }
        catch (FileNotFoundException fnfe) {
            EchoedCore.getLog().error("File not found.", fnfe);
            EchoedCore.shutdown(Constants.STATUS_NO_CONFIG);
        }
        catch (Exception e) {
            EchoedCore.getLog().error("File could not be read.", e);
            EchoedCore.shutdown(Constants.STATUS_NO_CONFIG);
        }

        return null;
    }

    /**
     * Checks if a given file exists
     * @param directory directory of file
     * @return true if it exists, false if not
     */
    public static boolean checkIfFileExists(String directory) {
        File temp = new File(directory);
        return temp.exists();
    }

}