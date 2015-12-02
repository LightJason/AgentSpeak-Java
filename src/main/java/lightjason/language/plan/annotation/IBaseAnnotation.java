/**
 * @cond LICENSE
 * ######################################################################################
 * # GPL License                                                                        #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015, Philipp Kraus (philipp.kraus@tu-clausthal.de)                  #
 * # This program is free software: you can redistribute it and/or modify               #
 * # it under the terms of the GNU General Public License as                            #
 * # published by the Free Software Foundation, either version 3 of the                 #
 * # License, or (at your option) any later version.                                    #
 * #                                                                                    #
 * # This program is distributed in the hope that it will be useful,                    #
 * # but WITHOUT ANY WARRANTY; without even the implied warranty of                     #
 * # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      #
 * # GNU General Public License for more details.                                       #
 * #                                                                                    #
 * # You should have received a copy of the GNU General Public License                  #
 * # along with this program. If not, see http://www.gnu.org/licenses/                  #
 * ######################################################################################
 * @endcond
 */

package lightjason.language.plan.annotation;


/**
 * annotation base
 */
public abstract class IBaseAnnotation<T> implements IAnnotation<T>
{
    /**
     * number data
     */
    protected final T m_data;
    /**
     * annotation type
     */
    protected final EType m_type;

    /**
     * ctor
     *
     * @param p_type type
     * @param p_data data
     */
    protected IBaseAnnotation( final EType p_type, final T p_data )
    {
        m_data = p_data;
        m_type = p_type;
    }

    @Override
    public final int hashCode()
    {
        return m_type.hashCode();
    }

    @Override
    public final EType getID()
    {
        return m_type;
    }

    @Override
    public final T getData()
    {
        return m_data;
    }
}
