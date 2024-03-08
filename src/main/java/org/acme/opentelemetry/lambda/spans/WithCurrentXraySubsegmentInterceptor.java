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
//@WithCurrentSubsegment
//@Priority(2020)
//@Interceptor
//public class WithCurrentXraySubsegmentInterceptor {
//
//  private static final Logger LOG = Logger.getLogger(WithCurrentXraySubsegmentInterceptor.class);
//
//  @AroundInvoke
//  Object withSubsegment(InvocationContext context) throws Exception {
//    Method method = context.getMethod();
//    Subsegment subsegment = AWSXRay.getCurrentSubsegment();
//    boolean isNew = false;
//    if (subsegment == null) {
//      LOG.info("New subsegment");
//      isNew = true;
//      subsegment = AWSXRay.beginSubsegment(segmentName(method));
//    }
//    try {
//      params(subsegment, method, context.getParameters());
//      return context.proceed();
//    } catch (Exception e) {
//      subsegment.setError(true);
//      subsegment.addException(e);
//      LOG.info("Ex: " + e.getLocalizedMessage());
//      throw e;
//    } finally {
//      if (isNew) {
//        AWSXRay.endSubsegment();
//      }
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
//  private String segmentName(Method method) {
//    String v = method.getDeclaringClass().getSimpleName() + "." + method.getName();
//    LOG.info("Method name: " + v);
//    return v;
//  }
//}
