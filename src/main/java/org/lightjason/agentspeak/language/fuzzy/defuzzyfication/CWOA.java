/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason                                                #
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

package org.lightjason.agentspeak.language.fuzzy.defuzzyfication;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.membership.IFuzzyMembership;
import org.lightjason.agentspeak.language.fuzzy.set.IFuzzySet;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.stream.Stream;


/**
 * defuzzification with weighted-average-method
 */
public final class CWOA<E extends Enum<?>> extends IBaseDefuzzification<E>
{

    /**
     * ctor
     *
     * @param p_class fuzzy set class
     * @param p_membership membership function
     * @param p_default fuzzy enum type
     */
    public CWOA( @NonNull final Class<? extends IFuzzySet<E>> p_class, @NonNull final IFuzzyMembership<E> p_membership,
                 @NonNull final IFuzzyValue<?> p_default )
    {
        super( p_class, p_membership, p_default );
    }

    @Nonnull
    @Override
    public Number apply( @Nonnull final Stream<IFuzzyValue<?>> p_value )
    {
        final IFuzzyValue<?>[] l_values = p_value.toArray( IFuzzyValue<?>[]::new );
        if ( l_values.length < 2 )
            return l_values.length == 0 ? m_default.fuzzy() : l_values[0].fuzzy();

        return Arrays.stream( l_values ).mapToDouble( i -> i.fuzzy().doubleValue() * ( i.get().ordinal() ) ).sum()
               / Arrays.stream( l_values ).mapToDouble( i -> i.fuzzy().doubleValue() ).sum();
    }

    @Override
    public boolean success( @NonNull final Number p_value )
    {
        // scale the gravity on the maximum to the enum result
        final double l_max = this.maximum().orElse( 1 );
        return p_value.doubleValue() / ( CCommon.floatingequal( l_max, 0, CCommon.FLOATINGPRECISION ) ? 1 : l_max ) > 0.5;
    }

}
