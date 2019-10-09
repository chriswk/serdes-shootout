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

import java.io.IOException;
import java.io.OutputStream;

public class Streams extends StreamsBase {

    @Override
    protected void writeTo(OutputStream out) throws IOException {
        String json = objectMapper.writeValueAsString(getObject());
        out.write(json.getBytes());
    }

    @Override
    protected String getJson() {
        return new String(getBytes());
    }
}
