/*
    Copyright 2020 EchoedAJ

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at:

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */
package utilities;

import core.EchoedCore;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtilities {

    /**
     * Adds a JSON Array to the Configuration File.
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
            EchoedCore.log.error("Unable to write to file.", ioe);
            return Constants.WRITE_TO_FILE_FAIL;
        }
    }

    /**
     * Adds a JSON Array to the Configuration File.
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
            EchoedCore.log.error("Unable to write to file.", ioe);
            return Constants.WRITE_TO_FILE_FAIL;
        }
    }

    @SuppressWarnings("ConstantConditions")
    public static String getValueByKey(String fileName, String key, String arrayLocation) {
        JSONArray object = getJSONFile(fileName);

        if (object.equals(null)) {
            EchoedCore.log.error(key + " is null.", new Exception("Failed to grab " + key));
            return  "" + Constants.STATUS_NO_CONFIG;
        }

        JSONObject jsonObject = object.getJSONObject(0);
        JSONObject array = (JSONObject)jsonObject.opt(arrayLocation);

        return array.optString(key);
    }

    /**
     * Retrieves the JSONObject to read the JSON File.
     * @return JSONObject
     */
    public static JSONArray getJSONFile(String fileName) {
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
            EchoedCore.log.error("File not found.", fnfe);
            EchoedCore.shutdown(Constants.STATUS_NO_CONFIG);
        }
        catch (Exception e) {
            EchoedCore.log.error("File could not be read.", e);
            EchoedCore.shutdown(Constants.STATUS_NO_CONFIG);
        }

        return array;

    }

}