/*
 * Copyright 2023 The Fury Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.fury.integration_tests.state;

import io.fury.benchmark.data.MediaContent;
import io.fury.benchmark.data.Sample;
import java.nio.ByteBuffer;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FlatBuffersStateTest {
  @Test
  public void testMediaContent() {
    Sample object = new Sample().populate(false);
    byte[] data = FlatBuffersState.serializeSample(object);
    Sample sample = FlatBuffersState.deserializeSample(ByteBuffer.wrap(data));
    Assert.assertEquals(sample, object);
  }

  @Test
  public void testSample() {
    MediaContent object = new MediaContent().populate(false);
    byte[] data = FlatBuffersState.serializeMediaContent(object).sizedByteArray();
    MediaContent mediaContent = FlatBuffersState.deserializeMediaContent(ByteBuffer.wrap(data));
    Assert.assertEquals(mediaContent, object);
  }
}