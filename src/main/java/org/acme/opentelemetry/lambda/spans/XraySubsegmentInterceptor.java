//package org.acme.opentelemetry.lambda.spans;
//
//import com.amazonaws.xray.AWSXRay;
//import com.amazonaws.xray.entities.Subsegment;
//
//import java.lang.reflect.Method;
//import java.lang.reflect.Parameter;
//import java.util.Arrays;
//import java.util.Optional;
//
//import org.jboss.logging.Logger;
//
//import jakarta.annotation.Priority;
//import jakarta.interceptor.AroundInvoke;
//import jakarta.interceptor.Interceptor;
//import jakarta.interceptor.InvocationContext;
//
//@WithXraySubsegment
//@Priority(2020)
//@Interceptor
//public class XraySubsegmentInterceptor {
//
//  private static final Logger LOG = Logger.getLogger(XraySubsegmentInterceptor.class);
//
//  @AroundInvoke
//  Object withSubsegment(InvocationContext context) throws Exception {
//    Method method = context.getMethod();
//    Subsegment subsegment = AWSXRay.beginSubsegment(segmentName(method));
//    try {
//      params(subsegment, method, context.getParameters());
//      Object proceed = context.proceed();
//      commonTags(subsegment);
//      LOG.info("Results: " + proceed);
//      return proceed;
//    } catch (Exception e) {
//      subsegment.setError(true);
//      subsegment.addException(e);
//      LOG.info("Ex: " + e.getLocalizedMessage());
//      throw e;
//    } finally {
//      AWSXRay.endSubsegment();
//    }
//  }
//
//  private void params(Subsegment subsegment, Method method, Object[] parameters) {
//    Parameter[] params = method.getParameters();
//    LOG.info("Params: " + Arrays.toString(parameters));
//    for (int i = 0; i < parameters.length; i++) {
//      Parameter param = params[i];
//
//      LOG.info("Annotations: " + Arrays.toString(param.getAnnotations()));
//      Optional<String> value = Arrays.stream(param.getAnnotations())
//          .filter(a -> a.annotationType().equals(SubsegmentAttribute.class))
//          .map(a -> (SubsegmentAttribute) a)
//          .map(SubsegmentAttribute::value)
//          .findFirst();
//
//      if (value.isPresent()) {
//        LOG.info("Attr: " + value.get() + " value: " + parameters[i]);
//        subsegment.putMetadata(value.get(), parameters[i]);
//      }
//    }
//  }
//
//  private void commonTags(Subsegment subsegment) {
//    subsegment.putMetadata("tm.product_code", "prd3293");
//  }
//
//  private String segmentName(Method method) {
//    String v = method.getDeclaringClass().getSimpleName() + "." + method.getName();
//    LOG.info("Method name: " + v);
//    return v;
//  }
//}
