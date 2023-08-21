/**
 * Copyright (c) 2010-2023 Contributors to the openHAB project
 * <p>
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 * <p>
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 * <p>
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.panasonicviera.internal;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * The {@link PanasonicVieraConfiguration} class contains fields mapping thing configuration parameters.
 *
 * @author Christian Boseck - Initial contribution
 */
@NonNullByDefault
public class PanasonicVieraConfiguration {

    public String hostname = "";
    public int port = 55000;
    public int refreshInterval = 600;
}
