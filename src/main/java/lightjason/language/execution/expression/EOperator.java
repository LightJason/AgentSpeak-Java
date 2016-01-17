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

package lightjason.language.execution.expression;

/**
 * expression operator
 */
public enum EOperator
{
    PLUS( "+" ),
    MINUS( "-" ),

    MULTIPLY( "*" ),
    DIVIDE( "/" ),
    MODULO( "%" ),
    POW( "**" ),


    OR( "||" ),
    AND( "&&" ),
    XOR( "^" ),

    EQUAL( "==" ),
    NOTEQUAL( "\\==" ),

    LESS( "<" ),
    LESSEQUAL( "<=" ),
    GREATER( ">" ),
    GREATEREQUAL( ">=" );



    /**
     * text name of the enum
     */
    private final String m_name;

    /**
     * ctor
     *
     * @param p_name text name
     */
    EOperator( final String p_name )
    {
        m_name = p_name;
    }

    @Override
    public final String toString()
    {
        return m_name;
    }

    /**
     * check of a logical operator
     *
     * @return boolean for logical operator
     */
    public final boolean isLogical()
    {
        return ( this == AND ) || ( this == OR ) || ( this == XOR );
    }

    /**
     * check of a numeric operator
     *
     * @return boolean for numeric operator
     */
    public final boolean isNumerical()
    {
        return ( this == PLUS ) || ( this == MINUS ) || ( this == MULTIPLY ) || ( this == DIVIDE ) || ( this == MODULO ) || ( this == POW );
    }

    /**
     * check of a comparable operator
     *
     * @return boolean for comparable operator
     */
    public final boolean isComparable()
    {
        return ( this == EQUAL ) || ( this == NOTEQUAL );
    }

    /**
     * check of a relational operator
     *
     * @return boolean for relational operator
     */
    public final boolean isRelational()
    {
        return ( this == LESS ) || ( this == LESSEQUAL ) || ( this == GREATER ) || ( this == GREATEREQUAL );
    }
}