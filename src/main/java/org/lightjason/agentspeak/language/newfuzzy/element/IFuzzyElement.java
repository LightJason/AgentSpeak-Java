/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-19, LightJason (info@lightjason.org)                            #
 * # This program is free software: you can redistribute it and/or modify               #
 * # it under the terms of the GNU Lesser General Public License as                     #
 * # published by the Free Software Foundation, either version 3 of the                 #
 * # License, or (at your option) any later version.                                    #
 * #                                                                                    #
 * # This program is distributed in the hope that it will be useful,                    #
 * # but WITHOUT ANY WARRANTY; without even the implied warranty of                     #
 * # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      #
 * # GNU Lesser General Public License for more details.                                #
 * #                                                                                    #
 * # You should have received a copy of the GNU Lesser General Public License           #
 * # along with this program. If not, see http://www.gnu.org/licenses/                  #
 * ######################################################################################
 * @endcond
 */

package org.lightjason.agentspeak.language.newfuzzy.element;

import org.lightjason.agentspeak.language.newfuzzy.value.IFuzzyValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;
import java.util.stream.Stream;


/**
 * fuzzy set
 *
 * @tparam T enum type
 */
public interface IFuzzyElement<T extends Enum<?>> extends Function<Number, IFuzzyValue<T>>
{

    /**
     * returns a raw value if it exist
     *
     * @tparam V raw value of the fuzzy set element
     * @return raw value
     */
    @Nullable
    <V> V raw();

    /**
     * returns the definition of success
     *
     * @return success fuzzy value stream
     */
    @Nonnull
    Stream<IFuzzyValue<T>> success();

    /**
     * returns the definition of fail
     *
     * @return fail fuzzy value stream
     */
    @Nonnull
    Stream<IFuzzyValue<T>> fail();

}
