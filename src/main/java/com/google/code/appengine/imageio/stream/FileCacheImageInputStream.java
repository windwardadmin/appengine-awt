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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;


import com.newatlanta.commons.vfs.provider.gae.GaeFileObject;
import com.newatlanta.commons.vfs.provider.gae.GaeFileSystemManager;
import com.newatlanta.commons.vfs.provider.gae.GaeRandomAccessContent;
import com.newatlanta.commons.vfs.provider.gae.GaeVFS;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.util.RandomAccessMode;
import org.apache.harmony.x.imageio.internal.nls.Messages;


public class FileCacheImageInputStream extends ImageInputStreamImpl {
    private InputStream is;
    private GaeFileObject file;
    private GaeRandomAccessContent rac;


    public FileCacheImageInputStream(InputStream stream, File cacheDir) throws IOException {
        if (stream == null) {
            throw new IllegalArgumentException(Messages.getString("imageio.0A"));
        }
        is = stream;

        // Retrieve the file system manager
        GaeFileSystemManager fsManager = GaeVFS.getManager();

        // Convert cacheDir to a FileObject
        GaeFileObject cacheDirObj = (cacheDir == null) ? null :
                (GaeFileObject)fsManager.resolveFile(cacheDir.getPath());

        // if cache dir is null, or if it is a directory
        if (cacheDirObj == null || cacheDirObj.getType() == FileType.FOLDER) {

            // original code:
            file = FileObjectUtils.createTempFileObject(fsManager,
                    FileCacheImageOutputStream.IIO_TEMP_FILE_PREFIX, null, cacheDirObj);
        } else {
            throw new IllegalArgumentException(Messages.getString("imageio.0B"));
        }

        rac = new GaeRandomAccessContent(file, RandomAccessMode.READWRITE);
    }

    @Override
    public int read() throws IOException {
        bitOffset = 0;

        if (streamPos >= rac.length()) {
            int b = is.read();

            if (b < 0) {
                return -1;
            }

            rac.seek(streamPos++);
            rac.write(b);
            return b;
        }

        rac.seek(streamPos++);
        return rac.read();
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        bitOffset = 0;

        if (streamPos >= rac.length()) {
            int nBytes = is.read(b, off, len);

            if (nBytes < 0) {
                return -1;
            }

            rac.seek(streamPos);
            rac.write(b, off, nBytes);
            streamPos += nBytes;
            return nBytes;
        }

        rac.seek(streamPos);
        int nBytes = rac.read(b, off, len);
        streamPos += nBytes;
        return nBytes;
    }

    @Override
    public boolean isCached() {
        return true;
    }

    @Override
    public boolean isCachedFile() {
        return true;
    }

    @Override
    public boolean isCachedMemory() {
        return false;
    }

    @Override
    public void close() throws IOException {
        super.close();
        rac.close();
        file.delete();
    }
}
