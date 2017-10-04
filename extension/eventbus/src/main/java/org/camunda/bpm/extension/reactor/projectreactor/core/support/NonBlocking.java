/*
 * Copyright (c) 2011-2014 Pivotal Software, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.camunda.bpm.extension.reactor.projectreactor.core.support;

import org.camunda.bpm.extension.reactor.projectreactor.core.Dispatcher;

/**
 * A dispatcher aware component
 *
 * @author Stephane Maldini
 */
public interface NonBlocking {

  /**
   * Get the assigned {@link org.camunda.bpm.extension.reactor.projectreactor.core.Dispatcher}.
   *
   * @return true if the component wishes to use a back-pressure ready message-passing (e.g., ReactiveSubscription)
   */
  boolean isReactivePull(Dispatcher dispatcher, long producerCapacity);

  /**
   * Return defined element capacity, used to drive new {@link org.reactivestreams.Subscription}
   * request needs. This is the maximum in-flight data allowed to transit to this elements.
   *
   * @return long capacity
   */
  long getCapacity();
}
