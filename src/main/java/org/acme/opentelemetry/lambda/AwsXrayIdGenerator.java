//package org.acme.opentelemetry.lambda;
//
//import java.util.Random;
//import java.util.concurrent.ThreadLocalRandom;
//import java.util.concurrent.TimeUnit;
//
//import org.jboss.logging.Logger;
//
//import io.opentelemetry.api.trace.TraceId;
//import io.opentelemetry.sdk.trace.IdGenerator;
//
//public class AwsXrayIdGenerator implements IdGenerator {
//  private static final Logger LOG = Logger.getLogger(AwsXrayIdGenerator.class);
//
//  private static final IdGenerator RANDOM_ID_GENERATOR = IdGenerator.random();
//
//  @Override
//  public String generateSpanId() {
//    return RANDOM_ID_GENERATOR.generateSpanId();
//  }
//
//  @Override
//  public String generateTraceId() {
//    // hi - 4 bytes timestamp, 4 bytes random
//    // low - 8 bytes random.
//    // Since we include timestamp, impossible to be invalid.
//
//    Random random = ThreadLocalRandom.current();
//    long timestampSecs = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
//    long hiRandom = random.nextInt() & 0xFFFFFFFFL;
//
//    long lowRandom = random.nextLong();
//
//    String s = TraceId.fromLongs(timestampSecs << 32 | hiRandom, lowRandom);
//    LOG.infof("Here! With id: %s", s);
//    return s;
//  }
//
//}
