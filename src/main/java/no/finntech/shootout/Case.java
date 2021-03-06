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

package no.finntech.shootout;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.BenchmarkParams;

/**
 * Harness to benchmark serialization/deserialization using a suitable mechanism.
 *
 * An object with a common structure and data should be created. Make sure it
 * carries the same information as the objects used in other Cases, so the
 * benchmarks are comparable.
 *
 * Use the constants defined in {@see Constants} to set fieldvalues.
 *
 * @param <T> The type of the object
 */
@State(Scope.Thread)
public abstract class Case<T> {
    private static final Map<String, Integer> SIZES = new ConcurrentHashMap<>();

    private T object;
    private byte[] bytes;

    /**
     * Return a complete object for use in serialization
     *
     * @return An object for serialization
     */
    protected abstract T buildObject();

    /**
     * Write the object to a ByteArrayOutputStream using the
     * serialization mechanism to test.
     *
     * Must be annotated with @Benchmark
     *
     * @return A completed ByteArrayOutputStream
     * @throws Exception
     */
    public abstract ByteArrayOutputStream write() throws Exception;

    /**
     * Load a pre-created byte[], using the appropriate deserialization
     * mechanism. Should return an object similar to the object
     * serialized in write.
     *
     * Must be annotated with @Benchmark
     *
     * @return object-object of suitable type
     * @throws Exception
     */
    public abstract T read() throws Exception;

    private static void saveSize(String benchmark, int size) {
        SIZES.put(benchmark, size);
    }

    public static Map<String, Integer> getSizes() {
        return SIZES;
    }

    @Setup
    public void prepare() throws Exception {
        object = buildObject();
        ByteArrayOutputStream baos = write();
        bytes = baos.toByteArray();
    }

    @Benchmark
    @Fork(0)
    @BenchmarkMode(Mode.SingleShotTime)
    @Warmup(iterations = 1)
    public void sizer(BenchmarkParams params) {
        saveSize(params.getBenchmark(), getSize());
    }

    public int getSize() {
        return bytes.length;
    }

    public T getObject() {
        return object;
    }

    public byte[] getBytes() {
        return bytes;
    }
}
