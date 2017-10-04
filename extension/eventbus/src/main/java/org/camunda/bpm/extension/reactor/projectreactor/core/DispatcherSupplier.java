/*
 * Copyright (c) 2011-2015 Pivotal Software Inc., Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.extension.reactor.projectreactor.core;

import org.camunda.bpm.extension.reactor.projectreactor.fn.Resource;
import java.util.function.Supplier;

/**
 * A Dispatcher {@link Supplier} with shutdown capacities to clean produced dispatchers.
 *
 * @author Stephane Maldini
 * @since 2.0
 */
@Deprecated
public interface DispatcherSupplier extends Resource, Supplier<Dispatcher> {
}
