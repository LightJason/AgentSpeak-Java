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

package org.lightjason.agentspeak.error;

import org.junit.Test;

import java.util.NoSuchElementException;


/**
 * test exception
 */
public final class TestCException
{

    /**
     * test no-such-element exception
     */
    @Test( expected = NoSuchElementException.class )
    public void nosuchelementexception()
    {
        throw new CNoSuchElementException();
    }

    /**
     * test enum-not-present exception
     */
    @Test( expected = EnumConstantNotPresentException.class )
    public void enumnotpresentstring()
    {
        throw new CEnumConstantNotPresentException( ETestEnum.class, "MISSING" );
    }

    /**
     * test illegal-argument exception
     */
    @Test( expected = IllegalArgumentException.class )
    public void illegalargument()
    {
        throw new CIllegalArgumentException();
    }

    /**
     * test illegal-argument exception
     */
    @Test( expected = IllegalArgumentException.class )
    public void illegalargumentstring()
    {
        throw new CIllegalArgumentException( "illegealargument" );
    }

    /**
     * test illegal-argument exception
     */
    @Test( expected = IllegalArgumentException.class )
    public void illegalargumentstringexception()
    {
        throw new CIllegalArgumentException( "illegealargument", new RuntimeException() );
    }

    /**
     * test illegal-argument exception
     */
    @Test( expected = IllegalArgumentException.class )
    public void illegalargumentexception()
    {
        throw new CIllegalArgumentException( new RuntimeException() );
    }

    /**
     * test unmodifyable-exception
     */
    @Test( expected = IllegalStateException.class )
    public void unmodifyableexception()
    {
        throw new CUnmodifiableException();
    }

    /**
     * test unmodifyable-exception
     */
    @Test( expected = IllegalStateException.class )
    public void unmodifyableexceptionstringexception()
    {
        throw new CUnmodifiableException( "unmodifyable", new RuntimeException() );
    }

    /**
     * test unmodifyable-exception
     */
    @Test( expected = IllegalStateException.class )
    public void unmodifyableexceptionpassexception()
    {
        throw new CUnmodifiableException( new RuntimeException() );
    }

    /**
     * test illegal-state-exception
     */
    @Test( expected = IllegalStateException.class )
    public void illegalstateexception()
    {
        throw new CIllegalStateException();
    }

    /**
     * test illegal-state-exception
     */
    @Test( expected = IllegalStateException.class )
    public void illegalstateexceptionstring()
    {
        throw new CIllegalStateException( "illegal-state" );
    }

    /**
     * test illegal-state-exception
     */
    @Test( expected = IllegalStateException.class )
    public void illegalstateexceptionpassexception()
    {
        throw new CIllegalStateException( new RuntimeException() );
    }


    /**
     * test illegal-state-exception
     */
    @Test( expected = IllegalStateException.class )
    public void illegalstateexceptionstringexception()
    {
        throw new CIllegalStateException( "illegal-state", new RuntimeException() );
    }

    /**
     * test enum
     */
    private enum ETestEnum
    {
        FOO;
    }
}
