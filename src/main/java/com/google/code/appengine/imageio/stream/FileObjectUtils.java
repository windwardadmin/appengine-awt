/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */


package com.google.code.appengine.imageio.stream;

import com.newatlanta.commons.vfs.provider.gae.GaeFileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.FileType;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Utility class for GaeFileObject.  Contains methods for things such as creating a temporary file object.
 */
public class FileObjectUtils {
    private FileObjectUtils(){}

    public final static class RandStringGenerator {
        private static SecureRandom random = new SecureRandom();

        public static String nextString() {
            return new BigInteger(130, random).toString(32);
        }
    }

    public static GaeFileObject createTempFileObject(FileSystemManager fsManager, String prefix, String suffix, GaeFileObject directory) throws FileSystemException {
        if (suffix == null) suffix = ".tmp";
        GaeFileObject file;

        if (directory == null || (directory.getType()== FileType.FOLDER)) {
            String name = RandStringGenerator.nextString();
            String filename = prefix + name + suffix;
            file = (GaeFileObject)fsManager.resolveFile(filename);
            while (file.exists()) {
                name = RandStringGenerator.nextString();
                filename = prefix + name + suffix;
                file = (GaeFileObject)fsManager.resolveFile(filename);
            }
            // guaranteed file doesn't exist at this point
            file.createFile();
        } else {
            throw new IllegalArgumentException("directory must be a directory!");
        }

        return file;
    }
}
