/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp.kraus@tu-clausthal.de)               #
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

package lightjason.agent.action.buildin.collection.list;

import lightjason.agent.action.buildin.IBuildinAction;
import lightjason.language.CCommon;
import lightjason.language.CRawTerm;
import lightjason.language.ITerm;
import lightjason.language.execution.IContext;
import lightjason.language.execution.fuzzy.CBoolean;
import lightjason.language.execution.fuzzy.IFuzzyValue;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


/**
 * creates the intersection between lists
 */
public final class CIntersect extends IBuildinAction
{
    /**
     * ctor
     */
    public CIntersect()
    {
        super( 3 );
    }

    @Override
    public final int getMinimalArgumentNumber()
    {
        return 1;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext<?> p_context, final Boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        // all arguments must be lists (build unique list of all elements and check all collection if an element exists in each collection)
        p_return.add( CRawTerm.from(

                CCommon.flatList( p_argument ).stream().map( i -> CCommon.getRawValue( i ) ).collect( Collectors.toSet() ).parallelStream()
                       .filter(
                        i -> p_argument.stream()
                                       .map( j -> CCommon.<Collection<?>, ITerm>getRawValue( j )
                                               .parallelStream()
                                               .map( l -> CCommon.getRawValue( l ) )
                                               .collect( Collectors.toList() )
                                               .contains( i )
                                       )
                                       .allMatch( j -> j )
                ).collect( Collectors.<Object>toList() )

        ) );

        return CBoolean.from( true );
    }

}