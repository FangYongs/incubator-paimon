/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.paimon.utils;

import org.apache.paimon.Snapshot;
import org.apache.paimon.fs.FileIO;
import org.apache.paimon.fs.Path;
import org.apache.paimon.schema.SchemaManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.apache.paimon.utils.Preconditions.checkArgument;

/** Manager for {@code branch} */
public class BranchManager {

    private static final Logger LOG = LoggerFactory.getLogger(BranchManager.class);

    private static final String BRANCH_PREFIX = "branch-";
    private static final String MASTER_FILE = "master";

    private final FileIO fileIO;
    private final Path tablePath;

    public BranchManager(FileIO fileIO, Path tablePath) {
        this.fileIO = fileIO;
        this.tablePath = tablePath;
    }

    /** Get the main branch name for the table. */
    public String getMainBranch() {
        try {
            Path masterFilePath = new Path(tablePath, MASTER_FILE);
            if (fileIO.exists(masterFilePath)) {
                return fileIO.readFileUtf8(masterFilePath);
            } else {
                // Use table root path as the default main path
                return "";
            }
        } catch (IOException e) {
            LOG.error("Get main branch failed", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Create branch with given branch name from specified tag.
     *
     * @param tagName The tag to create branch.
     * @param branchName The name for the branch.
     */
    public void createBranch(String tagName, String branchName) {
        checkArgument(!StringUtils.isBlank(branchName), "Branch name '%s' is blank.", branchName);
        checkArgument(!branchExists(branchName), "Branch name '%s' already exists.", branchName);
        checkArgument(
                !branchName.chars().allMatch(Character::isDigit),
                "Branch name cannot be pure numeric string but is '%s'.",
                branchName);

        Snapshot snapshot = new TagManager(fileIO, tablePath).taggedSnapshot(tagName);
        Path branchPath = branchPath(branchName);
        SchemaManager schemaManager = new SchemaManager(fileIO, branchPath);
        try {
            schemaManager.commit(new SchemaManager(fileIO, tablePath).schema(snapshot.schemaId()));
        } catch (Exception e) {
            throw new RuntimeException(
                    String.format(
                            "Exception occurs when committing schema '%s' (path %s). "
                                    + "Cannot clean up because we can't determine the success.",
                            branchName, branchPath),
                    e);
        }

        Path branchSnapshotPath =
                new SnapshotManager(fileIO, branchPath).snapshotPath(snapshot.id());
        try {
            fileIO.writeFileUtf8(branchSnapshotPath, snapshot.toJson());
        } catch (IOException e) {
            throw new RuntimeException(
                    String.format(
                            "Exception occurs when committing snapshot '%s' (path %s). "
                                    + "Cannot clean up because we can't determine the success.",
                            branchName, branchSnapshotPath),
                    e);
        }
    }

    /**
     * Check if the specify branch exists.
     *
     * @param branchName The name of the branch.
     */
    public boolean branchExists(String branchName) {
        Path path = branchPath(branchName);
        try {
            return fileIO.exists(path);
        } catch (IOException e) {
            throw new RuntimeException(
                    String.format(
                            "Failed to determine if branch '%s' exists in path %s.",
                            branchName, path),
                    e);
        }
    }

    /** Return the path of a branch. */
    public Path branchPath(String branchName) {
        return new Path(tablePath + "/branch/" + BRANCH_PREFIX + branchName);
    }

    /**
     * Delete branch with given branch name.
     *
     * @param branchName The name of the branch to be deleted.
     */
    public void deleteBranch(String branchName) {
        throw new UnsupportedOperationException();
    }

    /**
     * Merge given branch into main and the branch will be still exist.
     *
     * @param branchName The name of the branch to be merged to main.
     */
    public void mergeBranch(String branchName) {
        throw new UnsupportedOperationException();
    }

    /**
     * Replace main branch with specified branch name and the previous main branch will be deleted.
     *
     * @param branchName The name of the branch to be used as main.
     */
    public void replaceMain(String branchName) {
        throw new UnsupportedOperationException();
    }
}
