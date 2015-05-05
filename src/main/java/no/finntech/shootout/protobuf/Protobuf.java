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

package no.finntech.shootout.protobuf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import no.finntech.shootout.Case;

import org.openjdk.jmh.annotations.Benchmark;

public class Protobuf extends Case<CaseProtos.ProtobufPost> {

    @Override
    protected CaseProtos.ProtobufPost buildPost() {
        return CaseProtos.ProtobufPost.newBuilder()
                .setPublished(PUBLISHED)
                .setActor(CaseProtos.Person.newBuilder()
                        .setId(PERSON_ID)
                        .setDisplayName(PERSON_NAME))
                .setObject(CaseProtos.Article.newBuilder()
                        .setId(ARTICLE_ID)
                        .setDisplayName(ARTICLE_NAME))
                .build();
    }

    @Override
    @Benchmark
    public ByteArrayOutputStream write() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        getPost().writeTo(baos);
        return baos;
    }

    @Override
    @Benchmark
    public CaseProtos.ProtobufPost read() throws Exception {
        return CaseProtos.ProtobufPost.parseFrom(getBytes());
    }
}
