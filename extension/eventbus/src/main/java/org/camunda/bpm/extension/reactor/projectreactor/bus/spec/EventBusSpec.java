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
package org.camunda.bpm.extension.reactor.projectreactor.bus.spec;

import org.camunda.bpm.extension.reactor.projectreactor.Environment;
import org.camunda.bpm.extension.reactor.projectreactor.bus.EventBus;

/**
 * A helper class for configuring a new {@link org.camunda.bpm.extension.reactor.projectreactor.bus.EventBus}.
 *
 * @author Jon Brisbin
 */
public class EventBusSpec extends EventRoutingComponentSpec<EventBusSpec, EventBus> {

  @Override
  protected final EventBus configure(EventBus reactor, Environment environment) {
    return reactor;
  }

}
