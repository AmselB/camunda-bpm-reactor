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
import org.camunda.bpm.extension.reactor.projectreactor.bus.Event;
import org.camunda.bpm.extension.reactor.projectreactor.bus.EventBus;
import org.camunda.bpm.extension.reactor.projectreactor.bus.filter.*;
import org.camunda.bpm.extension.reactor.projectreactor.bus.registry.Registries;
import org.camunda.bpm.extension.reactor.projectreactor.bus.registry.Registry;
import org.camunda.bpm.extension.reactor.projectreactor.bus.routing.ConsumerFilteringRouter;
import org.camunda.bpm.extension.reactor.projectreactor.bus.routing.Router;
import org.camunda.bpm.extension.reactor.projectreactor.bus.routing.TraceableDelegatingRouter;
import org.camunda.bpm.extension.reactor.projectreactor.core.Dispatcher;
import org.camunda.bpm.extension.reactor.projectreactor.core.dispatch.TraceableDelegatingDispatcher;
import org.camunda.bpm.extension.reactor.projectreactor.core.support.Assert;

import java.util.function.Consumer;


/**
 * A generic environment-aware class for specifying components that need to be configured with an {@link Environment},
 * {@link Dispatcher}, and {@link org.camunda.bpm.extension.reactor.projectreactor.bus.routing.Router}.
 *
 * @param <SPEC>
 * 		The DispatcherComponentSpec subclass
 * @param <TARGET>
 * 		The type that this spec will create
 *
 * @author Jon Brisbin
 */
@SuppressWarnings("unchecked")
public abstract class EventRoutingComponentSpec<SPEC extends EventRoutingComponentSpec<SPEC, TARGET>, TARGET> extends
                                                                                                              DispatcherComponentSpec<SPEC, TARGET> {




	private EventRoutingStrategy  eventRoutingStrategy;
	private Router                router;
	private Filter                eventFilter;
	private Consumer<Throwable> dispatchErrorHandler;
	private Consumer<Throwable>   uncaughtErrorHandler;
	private Registry<Object, Consumer<? extends Event<?>>> consumerRegistry;
	private boolean traceEventPath = false;


	/**
	 * Assigns the component's Filter
	 *
	 * @return {@code this}
	 */
	public final SPEC eventFilter(Filter filter) {
		Assert.isNull(router, "Cannot set both a filter and a router. Use one or the other.");
		this.eventFilter = filter;
		return (SPEC) this;
	}

	/**
	 * Assigns the component's EventRouter
	 *
	 * @return {@code this}
	 */
	public final SPEC eventRouter(Router router) {
		Assert.isNull(eventFilter, "Cannot set both a filter and a router. Use one or the other.");
		this.router = router;
		return (SPEC) this;
	}

	/**
	 * Configures the component's EventRouter to broadcast events to all matching consumers
	 *
	 * @return {@code this}
	 */
	public final SPEC broadcastEventRouting() {
		this.eventRoutingStrategy = EventRoutingStrategy.BROADCAST;
		return (SPEC) this;
	}

	/**
	 * Configures the component's EventRouter to route events to one consumer that's randomly selected from that matching
	 * consumers
	 *
	 * @return {@code this}
	 */
	public final SPEC randomEventRouting() {
		this.eventRoutingStrategy = EventRoutingStrategy.RANDOM;
		return (SPEC) this;
	}

	/**
	 * Configures the component's EventRouter to route events to the first of the matching consumers
	 *
	 * @return {@code this}
	 */
	public final SPEC firstEventRouting() {
		this.eventRoutingStrategy = EventRoutingStrategy.FIRST;
		return (SPEC) this;
	}

	/**
	 * Configures the component's EventRouter to route events to one consumer selected from the matching consumers using a
	 * round-robin algorithm consumers
	 *
	 * @return {@code this}
	 */
	public final SPEC roundRobinEventRouting() {
		this.eventRoutingStrategy = EventRoutingStrategy.ROUND_ROBIN;
		return (SPEC) this;
	}

	/**
	 * Configures the component's error handler for any errors occurring during dispatch (e.g. Exceptions resulting from
	 * calling a {@code Consumer#accept} method.
	 *
	 * @param dispatchErrorHandler
	 * 		the error handler for dispatching errors
	 *
	 * @return {@code this}
	 */
	public SPEC dispatchErrorHandler(Consumer<Throwable> dispatchErrorHandler) {
		this.dispatchErrorHandler = dispatchErrorHandler;
		return (SPEC) this;
	}

	/**
	 * Configures the component's uncaught error handler for any errors that get reported into this component but aren't a
	 * direct result of dispatching (e.g. errors that originate from another component).
	 *
	 * @param uncaughtErrorHandler
	 * 		the error handler for uncaught errors
	 *
	 * @return {@code this}
	 */
	public SPEC uncaughtErrorHandler(Consumer<Throwable> uncaughtErrorHandler) {
		this.uncaughtErrorHandler = uncaughtErrorHandler;
		return (SPEC) this;
	}

	/**
	 * Configures this component to provide event tracing when dispatching and routing an event.
	 *
	 * @return {@code this}
	 */
	public final SPEC traceEventPath() {
		return traceEventPath(true);
	}

	/**
	 * Configures this component to provide or not provide event tracing when dispatching and routing an event.
	 *
	 * @param b
	 * 		whether to trace the event path or not
	 *
	 * @return {@code this}
	 */
	public final SPEC traceEventPath(boolean b) {
		this.traceEventPath = b;
		return (SPEC) this;
	}

	/**
	 * Configures the {@link org.camunda.bpm.extension.reactor.projectreactor.bus.registry.Registry} to use when creating this component. Registries can be
	 * shared to reduce GC pressure and potentially be persisted across restarts.
	 *
	 * @param consumerRegistry
	 * 		the consumer registry to use
	 *
	 * @return {@code this}
	 */
	public SPEC consumerRegistry(Registry<Object, Consumer<? extends Event<?>>> consumerRegistry) {
		this.consumerRegistry = consumerRegistry;
		return (SPEC) this;
	}

	/**
	 * Configures the callback to invoke if a notification key is sent into this component and there are no consumers
	 * registered to respond to it.
	 *
	 * @param consumerNotFoundHandler
	 * 		the not found handler to use
	 *
	 * @return {@code this}
	 */
	public SPEC consumerNotFoundHandler(Consumer<Object> consumerNotFoundHandler) {
		this.consumerRegistry = Registries.create(true,true, consumerNotFoundHandler);
		return (SPEC) this;
	}

	protected abstract TARGET configure(EventBus reactor, Environment environment);

	@Override
	protected final TARGET configure(Dispatcher dispatcher, Environment environment) {
		return configure(createReactor(dispatcher), environment);
	}

	private EventBus createReactor(Dispatcher dispatcher) {
		if (traceEventPath) {
			dispatcher = new TraceableDelegatingDispatcher(dispatcher);
		}
		return new EventBus((consumerRegistry != null ? consumerRegistry : createRegistry()),
		                   dispatcher,
		                   (router != null ? router : createEventRouter()),
		                   dispatchErrorHandler,
		                   uncaughtErrorHandler);
	}

	private Router createEventRouter() {
		Router evr = new ConsumerFilteringRouter(
				eventFilter != null ? eventFilter : createFilter());
		if (traceEventPath) {
			return new TraceableDelegatingRouter(evr);
		} else {
			return evr;
		}
	}

	private Filter createFilter() {
		Filter filter;
		if (EventRoutingStrategy.ROUND_ROBIN == eventRoutingStrategy) {
			filter = new RoundRobinFilter();
		} else if (EventRoutingStrategy.RANDOM == eventRoutingStrategy) {
			filter = new RandomFilter();
		} else if (EventRoutingStrategy.FIRST == eventRoutingStrategy) {
			filter = new FirstFilter();
		} else {
			filter = new PassThroughFilter();
		}
		return (traceEventPath ? new TraceableDelegatingFilter(filter) : filter);
	}

	private Registry createRegistry() {
		return Registries.create();
	}

	protected enum EventRoutingStrategy {
		BROADCAST, RANDOM, ROUND_ROBIN, FIRST
	}

}
