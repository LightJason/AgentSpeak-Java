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

package org.lightjason.agentspeak.action.builtin.datetime;


import javax.annotation.Nonnull;
import java.time.ZonedDateTime;
import java.util.stream.Stream;


/**
 * subtract or adds a number of days.
 * The action adds / subtracts a number of
 * days to the date-time objects, the first
 * argument is a string with minus or plus,
 * the second argument the number and all other
 * arguments are date-time objects, the action
 * returns the modified date-time objects, the action
 * never fails
 *
 * {@code [O1|O2] = .datetime/applydays( "minus|plus" 3, DateTime1, DateTime2 );}
 */
public final class CApplyDays extends IPlusMinus
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -2885812047623467290L;

    @Nonnull
    @Override
    protected Stream<?> applyminus( @Nonnull final Stream<ZonedDateTime> p_datetime, final long p_value )
    {
        return p_datetime.map( i -> i.minusDays( p_value ) );
    }

    @Nonnull
    @Override
    protected Stream<?> applyplus( @Nonnull final Stream<ZonedDateTime> p_datetime, final long p_value )
    {
        return p_datetime.map( i -> i.plusDays( p_value ) );
    }

}
