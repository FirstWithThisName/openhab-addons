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
import org.openhab.core.thing.ThingTypeUID;

/**
 * The {@link PanasonicVieraBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Christian Boseck - Initial contribution
 */
@NonNullByDefault
public class PanasonicVieraBindingConstants {

    private static final String BINDING_ID = "panasonicviera";

    // List of all Thing Type UIDs
    public static final ThingTypeUID THING_TYPE_TV = new ThingTypeUID(BINDING_ID, "tv");

    // List of all Channel ids
    public static final String CHANNEL_POWER = "power";
}
