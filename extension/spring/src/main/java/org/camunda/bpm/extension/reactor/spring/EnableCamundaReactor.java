package org.camunda.bpm.extension.reactor.spring;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(CamundaReactorConfiguration.class)
@Documented
@Inherited
public @interface EnableCamundaReactor {
  String value() default "";
}
