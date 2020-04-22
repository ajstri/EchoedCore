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
package utilities.exceptions;

/**
 * NoConfigurationFileException class of the EchoedDungeons project
 *
 * Simple exception to note that the Configuration File is not usable
 * in its current state.
 */
public class NoConfigurationFileException extends Exception {
    public NoConfigurationFileException (String errorMessage) {
        super(errorMessage);
    }
}
