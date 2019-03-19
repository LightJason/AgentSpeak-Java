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

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Objects;
import java.util.Queue;
import java.util.function.BiFunction;
import java.util.stream.Stream;


/**
 * base routing structure
 */
public abstract class IBaseRouting implements IRouting
{
    /**
     * distance
     */
    protected final IDistance m_distance;
    /**
     * search direction
     */
    private final ISearchDirection m_searchdirection;
    /**
     * walkable function
     */
    private final BiFunction<ObjectMatrix2D, DoubleMatrix1D, Boolean> m_walkable;

    /**
     * ctor
     * @param p_distance distance
     * @param p_searchdirection search direction
     */
    protected IBaseRouting( @Nonnull final IDistance p_distance, @Nonnull final ISearchDirection p_searchdirection )
    {
        this( p_distance, p_searchdirection, ( g, p ) -> Objects.isNull( g.getQuick( (int) p.getQuick( 0 ), (int) p.getQuick( 1 ) ) ) );
    }

    /**
     * ctor
     *  @param p_distance distance
     * @param p_searchdirection search direction
     * @param p_walkable walkable function
     */
    protected IBaseRouting( @Nonnull final IDistance p_distance, @Nonnull final ISearchDirection p_searchdirection,
                            @NonNull final BiFunction<ObjectMatrix2D, DoubleMatrix1D, Boolean> p_walkable
    )
    {
        m_distance = p_distance;
        m_walkable = p_walkable;
        m_searchdirection = p_searchdirection;
    }

    /**
     * returns a stream of neighbour positions
     *
     * @param p_grid grid
     * @param p_current current position
     * @return position stream
     */
    protected final Stream<DoubleMatrix1D> neighbour( @Nonnull final ObjectMatrix2D p_grid, @Nonnull final DoubleMatrix1D p_current )
    {
        return m_searchdirection.apply( p_grid, p_current, m_walkable );
    }

    /**
     * builds the path recursive on the node structure
     *
     * @param p_end final node (target position)
     * @return position stream
     */
    protected static Stream<DoubleMatrix1D> constructpath( @Nonnull final INode p_end )
    {
        return Stream.concat(
            Objects.isNull( p_end.get() )
            ? Stream.of()
            : constructpath( p_end.get() ),
            Stream.of( p_end.position() )
        );
    }

    /**
     * reorganize a priority queue
     *
     * @param p_queue queue
     */
    protected static void reorganizequeue( @Nonnull final Queue<INode> p_queue )
    {
        final INode[] l_nodes = p_queue.toArray( INode[]::new );
        p_queue.clear();
        p_queue.addAll( Arrays.asList( l_nodes ) );
    }

}
