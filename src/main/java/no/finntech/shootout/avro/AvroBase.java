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

package no.finntech.shootout.avro;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import no.finntech.shootout.Case;
import no.finntech.shootout.Constants;
import no.finntech.shootout.Constants.Ad;
import no.finntech.shootout.Constants.AttributedTo;
import no.finntech.shootout.Constants.AvailableAt;
import no.finntech.shootout.Constants.Generator;
import no.finntech.shootout.Constants.Seller;
import no.finntech.shootout.Constants.Viewer;

import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.Encoder;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.commons.compress.compressors.CompressorException;
import org.openjdk.jmh.annotations.Benchmark;

public abstract class AvroBase extends Case<AvroView> {

    @Override
    protected AvroView buildObject() {
        return AvroView.newBuilder()
                .setPublished(Constants.PUBLISHED)
                .setActor(Person.newBuilder()
                        .setId(Viewer.ID)
                        .setUniqueVisitorId(Viewer.UNIQUE_ID)
                        .setSessionId(Viewer.SESSION_ID)
                        .setUserAgent(Viewer.USER_AGENT)
                        .setClientDevice(Viewer.CLIENT_DEVICE)
                        .setRemoteAddr(Viewer.REMOTE_ADDR)
                        .build())
                .setObject(Offer.newBuilder()
                        .setId(Ad.ID)
                        .setName(Ad.NAME)
                        .setCategory(Ad.CATEGORY)
                        .setSeller(Person.newBuilder()
                                .setId(Seller.ID)
                                .build())
                        .setAvailableAt(Place.newBuilder()
                                .setId(AvailableAt.ID)
                                .build())
                        .setPrice(Ad.PRICE)
                        .build())
                .setGenerator(Application.newBuilder()
                        .setId(Generator.ID)
                        .build())
                .setAttributedTo(Link.newBuilder()
                        .setHref(AttributedTo.HREF)
                        .setRel(AttributedTo.REL)
                        .build())
                .build();
    }

    @Override
    @Benchmark
    public ByteArrayOutputStream write() throws IOException, CompressorException {
        DatumWriter<AvroView> datumWriter = new SpecificDatumWriter<>(AvroView.class);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Encoder encoder = getEncoder(out);
        datumWriter.write(getObject(), encoder);
        encoder.flush();
        out.flush();
        out.close();
        return out;
    }

    @Override
    @Benchmark
    public AvroView read() throws IOException, CompressorException {
        DatumReader<AvroView> datumReader = new SpecificDatumReader<>(AvroView.class);
        Decoder decoder = getDecoder(getBytes());
        return datumReader.read(null, decoder);
    }

    protected abstract Encoder getEncoder(OutputStream out) throws IOException, CompressorException;

    protected abstract Decoder getDecoder(byte[] bytes) throws IOException, CompressorException;
}
