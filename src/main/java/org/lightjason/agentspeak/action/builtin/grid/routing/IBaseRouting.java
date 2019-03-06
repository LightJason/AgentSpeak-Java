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

package org.lightjason.agentspeak.action.builtin.grid.routing;

import cern.colt.matrix.tdouble.DoubleMatrix1D;
import cern.colt.matrix.tobject.ObjectMatrix2D;
import edu.umd.cs.findbugs.annotations.NonNull;

import java.util.Objects;
import java.util.function.BiFunction;


/**
 * base routing structure
 */
public abstract class IBaseRouting implements IRouting
{
    /**
     * walkable function
     */
    protected final BiFunction<ObjectMatrix2D, DoubleMatrix1D, Boolean> m_walkable;

    /**
     * ctor
     */
    protected IBaseRouting()
    {
        this( ( g, p ) -> Objects.nonNull( g.getQuick( (int) p.getQuick( 0 ), (int) p.getQuick( 1 ) ) ) );
    }

    /**
     * ctor
     *
     * @param p_walkable walkable function
     */
    protected IBaseRouting( @NonNull final BiFunction<ObjectMatrix2D, DoubleMatrix1D, Boolean> p_walkable )
    {
        m_walkable = p_walkable;
    }


}
