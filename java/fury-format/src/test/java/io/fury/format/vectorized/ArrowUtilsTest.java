package io.fury.format.vectorized;

import io.fury.memory.MemoryBuffer;
import io.fury.memory.MemoryUtils;
import org.apache.arrow.vector.BitVector;
import org.apache.arrow.vector.FieldVector;
import org.apache.arrow.vector.VarCharVector;
import org.apache.arrow.vector.VectorSchemaRoot;
import org.apache.arrow.vector.VectorUnloader;
import org.apache.arrow.vector.ipc.message.ArrowRecordBatch;
import org.apache.arrow.vector.types.pojo.Field;
import org.testng.annotations.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.*;

public class ArrowUtilsTest {
  public static VectorSchemaRoot createVectorSchemaRoot(int size) {
    BitVector bitVector = new BitVector("boolean", ArrowUtils.allocator);
    VarCharVector varCharVector = new VarCharVector("varchar", ArrowUtils.allocator);
    for (int i = 0; i < size; i++) {
      bitVector.setSafe(i, i % 2 == 0 ? 0 : 1);
      varCharVector.setSafe(i, ("test" + i).getBytes(StandardCharsets.UTF_8));
    }
    bitVector.setValueCount(size);
    varCharVector.setValueCount(size);
    List<Field> fields = Arrays.asList(bitVector.getField(), varCharVector.getField());
    List<FieldVector> vectors = Arrays.asList(bitVector, varCharVector);
    return new VectorSchemaRoot(fields, vectors);
  }

  @Test
  public void testSerializeRecordBatch() {
    VectorSchemaRoot vectorSchemaRoot = createVectorSchemaRoot(2);
    VectorUnloader unloader = new VectorUnloader(vectorSchemaRoot);
    ArrowRecordBatch recordBatch = unloader.getRecordBatch();
    MemoryBuffer buffer = MemoryUtils.buffer(32);
    ArrowUtils.serializeRecordBatch(recordBatch, buffer);
    try(ArrowRecordBatch batch = ArrowUtils.deserializeRecordBatch(buffer)) {
      System.out.println("newRecordBatch " + batch);
      assertEquals(batch.getLength(), recordBatch.getLength());
      assertEquals(batch.computeBodyLength(), recordBatch.computeBodyLength());
      assertEquals(batch.getMessageType(), recordBatch.getMessageType());
    }
  }

}