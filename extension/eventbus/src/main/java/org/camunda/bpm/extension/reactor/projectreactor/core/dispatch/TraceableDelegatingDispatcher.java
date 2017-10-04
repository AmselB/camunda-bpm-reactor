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

package org.camunda.bpm.extension.reactor.projectreactor.core.dispatch;

import org.camunda.bpm.extension.reactor.projectreactor.core.Dispatcher;
import org.camunda.bpm.extension.reactor.projectreactor.core.support.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * An implementation of {@link org.camunda.bpm.extension.reactor.projectreactor.core.Dispatcher} that traces activity through it.
 *
 * @author Jon Brisbin
 */
public class TraceableDelegatingDispatcher implements Dispatcher {

  private final Dispatcher delegate;
  private final Logger log;

  public TraceableDelegatingDispatcher(Dispatcher delegate) {
    Assert.notNull(delegate, "Delegate Dispatcher cannot be null.");
    this.delegate = delegate;
    this.log = LoggerFactory.getLogger(delegate.getClass());
  }

  @Override
  public boolean alive() {
    return delegate.alive();
  }

  @Override
  public boolean awaitAndShutdown() {
    if (log.isTraceEnabled()) {
      log.trace("awaitAndShutdown()");
    }
    return delegate.awaitAndShutdown();
  }

  @Override
  public boolean awaitAndShutdown(long timeout, TimeUnit timeUnit) {
    if (log.isTraceEnabled()) {
      log.trace("awaitAndShutdown({}, {})", timeout, timeUnit);
    }
    return delegate.awaitAndShutdown(timeout, timeUnit);
  }

  @Override
  public void shutdown() {
    if (log.isTraceEnabled()) {
      log.trace("shutdown()");
    }
    delegate.shutdown();
  }

  @Override
  public void forceShutdown() {
    if (log.isTraceEnabled()) {
      log.trace("forceShutdown()");
    }
    delegate.forceShutdown();
  }

  @Override
  public <E> void tryDispatch(
    E event,
    Consumer<E> eventConsumer,
    Consumer<Throwable> errorConsumer) {
    dispatch(event, eventConsumer, errorConsumer);
  }

  @Override
  public <E> void dispatch(E event,
                           Consumer<E> consumer,
                           Consumer<Throwable> errorConsumer) {
    if (log.isTraceEnabled()) {
      log.trace("dispatch({}, {}, {})", event, consumer, errorConsumer);
    }
    delegate.dispatch(event, consumer, errorConsumer);
  }

  @Override
  public void execute(Runnable command) {
    delegate.execute(command);
  }


  @Override
  public boolean supportsOrdering() {
    return delegate.supportsOrdering();
  }

  @Override
  public long remainingSlots() {
    return delegate.remainingSlots();
  }

  @Override
  public long backlogSize() {
    return delegate.backlogSize();
  }

  @Override
  public boolean inContext() {
    return delegate.inContext();
  }
}
