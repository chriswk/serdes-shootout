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

import com.fasterxml.jackson.databind.ObjectMapper;
import no.finntech.shootout.Case;
import no.finntech.shootout.Constants;
import no.finntech.shootout.Constants.Ad;
import no.finntech.shootout.Constants.AttributedTo;
import no.finntech.shootout.Constants.AvailableAt;
import no.finntech.shootout.Constants.Viewer;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.streams.data.util.RFC3339Utils;
import org.apache.streams.jackson.StreamsJacksonMapper;
import org.apache.streams.pojo.json.Activity;
import org.apache.streams.pojo.json.ActivityObject;
import org.apache.streams.pojo.json.Author;
import org.apache.streams.pojo.json.Generator;
import org.apache.streams.pojo.json.objectTypes.Offer;
import org.apache.streams.pojo.json.objectTypes.Person;
import org.apache.streams.pojo.json.objectTypes.Place;
import org.openjdk.jmh.annotations.Benchmark;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public abstract class StreamsBase extends Case<Activity> {
    protected static final ObjectMapper objectMapper = StreamsJacksonMapper.getInstance();
    @Override
    protected Activity buildObject() {
        return new Activity()
                .withPublished(RFC3339Utils.parseToUTC(Constants.PUBLISHED))
                .withActor(new Author()
                        .withObjectType("Person")
                        .withId(Viewer.ID)
                        .withAdditionalProperty("uniqueVisitorId", Viewer.UNIQUE_ID)
                        .withAdditionalProperty("sessionId", Viewer.SESSION_ID)
                        .withAdditionalProperty("userAgent", Viewer.USER_AGENT)
                        .withAdditionalProperty("clientDevice", Viewer.CLIENT_DEVICE)
                        .withAdditionalProperty("remoteAddr", Viewer.REMOTE_ADDR))
                .withObject(new Offer()
                        .withId(Ad.ID)
                        .withAdditionalProperty("name", Ad.NAME)
                        .withAdditionalProperty("category", Ad.CATEGORY)
                        .withAdditionalProperty("seller", new Person()
                                .withAdditionalProperty("id", Constants.Seller.ID))
                        .withAdditionalProperty("availableAt", new Place()
                                .withAdditionalProperty("id", AvailableAt.ID))
                        .withAdditionalProperty("proce", Ad.PRICE))
                .withGenerator((Generator) new Generator()
                        .withObjectType("Application")
                        .withId(Constants.Generator.ID))
                .withAdditionalProperty("attributedTo", new ActivityObject()
                        .withObjectType("Link")
                        .withAdditionalProperty("href", AttributedTo.HREF)
                        .withAdditionalProperty("rel", AttributedTo.REL));
    }

    @Override
    @Benchmark
    public ByteArrayOutputStream write() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        writeTo(baos);
        return baos;
    }

    protected abstract void writeTo(OutputStream out) throws IOException, CompressorException;

    @Override
    @Benchmark
    public Activity read() throws Exception {
        String json = getJson();
        return objectMapper.readValue(json, Activity.class);
    }

    protected abstract String getJson() throws CompressorException, IOException;
}
