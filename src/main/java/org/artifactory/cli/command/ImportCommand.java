/*
 * Artifactory is a binaries repository manager.
 * Copyright (C) 2011 JFrog Ltd.
 *
 * Artifactory is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Artifactory is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Artifactory.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.artifactory.cli.command;

import org.artifactory.cli.common.Command;
import org.artifactory.cli.common.Param;
import org.artifactory.cli.common.UrlBasedCommand;
import org.artifactory.cli.main.CliLog;
import org.artifactory.cli.main.CliOption;
import org.artifactory.cli.main.CommandDefinition;
import org.artifactory.cli.rest.RestClient;

import java.io.File;

/**
 * The "Import" command class
 *
 * @author Noam Tenne
 */
public class ImportCommand extends UrlBasedCommand implements Command {

    /**
     * Default constructor
     */
    public ImportCommand() {
        super(
                CommandDefinition.imp,
                CliOption.verbose,
                CliOption.noMetadata,
                CliOption.failOnError,
                CliOption.failIfEmpty);
    }

    /**
     * Executes the command
     *
     * @throws Exception
     */
    public int execute() throws Exception {
        String systemUri = getUrl() + RestClient.IMPORT_URL;
        Param commandParam = CommandDefinition.imp.getCommandParam();
        String pathValue = extractPathValue(commandParam);
        StringBuilder jsonPayload = new StringBuilder("{");
        jsonPayload.append("\"importPath\" : \"").append(pathValue).append("\",\n");
        jsonPayload.append("\"includeMetadata\" : ").append(!CliOption.noMetadata.isSet()).append(",\n");
        jsonPayload.append("\"verbose\" : ").append(CliOption.verbose.isSet()).append(",\n");
        jsonPayload.append("\"failOnError\" : ").append(CliOption.failOnError.isSet()).append(",\n");
        jsonPayload.append("\"failIfEmpty\" : ").append(CliOption.failIfEmpty.isSet()).append("\n");
        jsonPayload.append("}");

        CliLog.info("Sending import request to server from path: " + pathValue);

        post(systemUri, jsonPayload.toString().getBytes("UTF-8"),
                "application/vnd.org.jfrog.artifactory.system.ImportSettings+json", 200, null, true);
        return 0;
    }

    /**
     * Prints the usage of the command
     */
    public void usage() {
        defaultUsage();
    }
}
