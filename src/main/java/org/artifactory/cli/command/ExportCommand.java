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
import org.artifactory.cli.common.UrlBasedCommand;
import org.artifactory.cli.main.CliLog;
import org.artifactory.cli.main.CliOption;
import org.artifactory.cli.main.CommandDefinition;
import org.artifactory.cli.rest.RestClient;

import java.io.File;

/**
 * The "Export" command class
 *
 * @author Noam Tenne
 */
public class ExportCommand extends UrlBasedCommand implements Command {

    /**
     * Default constructor
     */
    public ExportCommand() {
        super(
                CommandDefinition.export,
                CliOption.m2,
                CliOption.noMetadata,
                CliOption.verbose,
                CliOption.failOnError,
                CliOption.failIfEmpty,
                CliOption.bypassFiltering,
                CliOption.createArchive,
                CliOption.incremental,
                CliOption.excludeContent
        );
    }

    /**
     * Executes the command
     *
     * @throws Exception
     */
    public int execute() throws Exception {
        String systemUri = getUrl() + RestClient.EXPORT_URL;
        File exportTo = new File(CommandDefinition.export.getCommandParam().getValue());
        if (exportTo.exists()) {
            exportTo = new File(exportTo.getCanonicalPath());
        }
        StringBuilder jsonPayload = new StringBuilder("{");
        jsonPayload.append("\"exportPath\" : \"").append(exportTo.getPath()).append("\",\n");
        jsonPayload.append("\"includeMetadata\" : ").append(!CliOption.noMetadata.isSet()).append(",\n");
        jsonPayload.append("\"createArchive\" : ").append(CliOption.createArchive.isSet()).append(",\n");
        jsonPayload.append("\"bypassFiltering\" : ").append(CliOption.bypassFiltering.isSet()).append(",\n");
        jsonPayload.append("\"verbose\" : ").append(CliOption.verbose.isSet()).append(",\n");
        jsonPayload.append("\"failOnError\" : ").append(CliOption.failOnError.isSet()).append(",\n");
        jsonPayload.append("\"failIfEmpty\" : ").append(CliOption.failIfEmpty.isSet()).append(",\n");
        jsonPayload.append("\"m2\" : ").append(CliOption.m2.isSet()).append(",\n");
        jsonPayload.append("\"incremental\" : ").append(CliOption.incremental.isSet()).append(",\n");
        jsonPayload.append("\"excludeContent\" : ").append(CliOption.excludeContent.isSet()).append("\n");
        jsonPayload.append("}");

        CliLog.info("Sending export request to server path: " + exportTo.getPath());

        // TODO: The repo list
        post(systemUri, jsonPayload.toString().getBytes("UTF-8"),
                "application/vnd.org.jfrog.artifactory.system.ExportSettings+json", 200, null, true);
        return 0;
    }

    /**
     * Prints the usage of the command
     */
    public void usage() {
        defaultUsage();
    }
}
