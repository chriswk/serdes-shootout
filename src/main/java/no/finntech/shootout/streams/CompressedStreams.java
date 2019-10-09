/*
 *    Copyright 2015 FINN.no AS
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package no.finntech.shootout.streams;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.utils.IOUtils;

public abstract class CompressedStreams extends StreamsBase {
    private static final CompressorStreamFactory FACTORY = new CompressorStreamFactory();

    @Override
    protected void writeTo(OutputStream out) throws IOException, CompressorException {
        String json = objectMapper.writeValueAsString(getObject());
        CompressorOutputStream outputStream = FACTORY.createCompressorOutputStream(getCompressor(), out);
        outputStream.write(json.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    @Override
    protected String getJson() throws CompressorException, IOException {
        CompressorInputStream inputStream = FACTORY.createCompressorInputStream(getCompressor(), new ByteArrayInputStream(getBytes()));
        byte[] bytes = IOUtils.toByteArray(inputStream);
        return new String(bytes);
    }

    protected abstract String getCompressor();
}
