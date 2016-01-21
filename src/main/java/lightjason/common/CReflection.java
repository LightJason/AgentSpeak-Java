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

package lightjason.common;

import lightjason.error.CIllegalArgumentException;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;


/**
 * reflection access
 */
public final class CReflection
{

    /**
     * ctor
     */
    private CReflection()
    {
    }

    /**
     * get a class field of the class or super classes and returns getter / setter handles
     *
     * @param p_class class
     * @param p_field fieldname
     * @return getter / setter handle object
     */
    public static CGetSet getClassField( final Class<?> p_class, final String p_field )
    throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException
    {
        Field l_field = null;
        for ( Class<?> l_class = p_class; ( l_field == null ) && ( l_class != null ); l_class = l_class.getSuperclass() )
            l_field = l_class.getDeclaredField( p_field );

        if ( l_field == null )
            throw new CIllegalArgumentException( CCommon.getLanguageString( CReflection.class, "fieldnotfound", p_field, p_class.getCanonicalName() ) );

        l_field.setAccessible( true );
        return new CGetSet( l_field, MethodHandles.lookup().unreflectGetter( l_field ), MethodHandles.lookup().unreflectSetter( l_field ) );
    }

    /**
     * returns all fields of a class and the super classes
     *
     * @param p_class class
     * @return map with field name and getter / setter handle
     */
    public static Map<String, CGetSet> getClassFields( final Class<?> p_class ) throws IllegalAccessException
    {
        return getClassFields( p_class, null );
    }

    /**
     * returns filtered fields of a class and the super classes
     *
     * @param p_class class
     * @param p_filter filtering object
     * @return map with field name and getter / setter handle
     */
    public static Map<String, CGetSet> getClassFields( final Class<?> p_class, final IFilter<Field> p_filter ) throws IllegalAccessException
    {
        final Map<String, CGetSet> l_fields = new HashMap<>();
        for ( Class<?> l_class = p_class; l_class != null; l_class = l_class.getSuperclass() )
            for ( final Field l_field : l_class.getDeclaredFields() )
            {
                l_field.setAccessible( true );
                if ( ( p_filter != null ) && ( !p_filter.filter( l_field ) ) )
                    continue;

                l_fields.put(
                        l_field.getName(), new CGetSet(
                                l_field, MethodHandles.lookup().unreflectGetter( l_field ), MethodHandles.lookup().unreflectSetter( l_field )
                        )
                );

            }
        return l_fields;
    }


    /**
     * returns a void-method from a class
     *
     * @param p_class class
     * @param p_method methodname
     * @return method
     */
    public static CMethod getClassMethod( final Class<?> p_class, final String p_method )
    throws IllegalArgumentException, IllegalAccessException, NoSuchMethodException
    {
        return getClassMethod( p_class, p_method, null );
    }


    /**
     * returns a void-method from a class
     *
     * @param p_class class
     * @param p_method methodname
     * @param p_parameter array with type-classes to define method parameter e.g. new Class[]{Integer.TYPE,
     * Integer.TYPE};
     * @return method
     */
    public static CMethod getClassMethod( final Class<?> p_class, final String p_method, final Class<?>[] p_parameter )
    throws IllegalArgumentException, IllegalAccessException, NoSuchMethodException
    {
        Method l_method = null;
        for ( Class<?> l_class = p_class; ( l_method == null ) && ( l_class != null ); l_class = l_class.getSuperclass() )
            l_method = l_class.getDeclaredMethod( p_method, p_parameter );

        if ( l_method == null )
            throw new CIllegalArgumentException( CCommon.getLanguageString( CReflection.class, "methodnotfound", p_method, p_class.getCanonicalName() ) );

        l_method.setAccessible( true );
        return new CMethod( l_method );
    }


    /**
     * returns filtered methods of a class and the super classes
     *
     * @param p_class class
     * @param p_filter filtering object
     * @return map with method name
     */
    public static Map<String, CMethod> getClassMethods( final Class<?> p_class, final IFilter<Method> p_filter ) throws IllegalAccessException
    {
        final Map<String, CMethod> l_methods = new HashMap<>();
        for ( Class<?> l_class = p_class; l_class != null; l_class = l_class.getSuperclass() )
            for ( final Method l_method : l_class.getDeclaredMethods() )
            {
                l_method.setAccessible( true );
                if ( ( p_filter != null ) && ( !p_filter.filter( l_method ) ) )
                    continue;

                l_methods.put( l_method.getName(), new CMethod( l_method ) );
            }
        return l_methods;
    }



    /**
     * interface for filtering
     * accessable class elements
     */
    public interface IFilter<T extends AccessibleObject>
    {

        /**
         * filter method
         *
         * @param p_element accessable element
         * @return true field will be added, false method will be ignored
         */
        boolean filter( final T p_element );
    }



    /**
     * structure for getter and setter method handles
     */
    public static class CGetSet
    {

        /**
         * field of the property
         */
        private final Field m_field;
        /**
         * getter method handle *
         */
        private final MethodHandle m_getter;
        /**
         * setter method handle *
         */
        private final MethodHandle m_setter;

        /**
         * ctor
         *
         * @param p_field field of the getter / setter
         * @param p_getter getter handle or null
         * @param p_setter setter handle or null
         */
        public CGetSet( final Field p_field, final MethodHandle p_getter, final MethodHandle p_setter )
        {
            m_field = p_field;
            m_getter = p_getter;
            m_setter = p_setter;
        }

        /**
         * checks transient flag of the field
         *
         * @return boolean if field is transient
         */
        public final boolean isTransient()
        {
            return Modifier.isTransient( m_field.getModifiers() );
        }

        /**
         * returns the field of the bind
         *
         * @return field
         */
        public final Field getField()
        {
            return m_field;
        }

        /**
         * returns the getter
         *
         * @return handle
         */
        public final MethodHandle getGetter()
        {
            return m_getter;
        }

        /**
         * returns the setter
         *
         * @return handle
         */
        public final MethodHandle getSetter()
        {
            return m_setter;
        }

        /**
         * check getter exist
         *
         * @return bool flag
         */
        public final boolean hasGetter()
        {
            return m_getter != null;
        }


        /**
         * check setter exist
         *
         * @return bool flag
         */
        public final boolean hasSetter()
        {
            return m_setter != null;
        }

    }



    /**
     * class for storing method access
     */
    public static class CMethod
    {
        /**
         * method handle *
         */
        private final MethodHandle m_handle;
        /**
         * method object *
         */
        private final Method m_method;

        /**
         * ctor
         *
         * @param p_method method object
         */
        public CMethod( final Method p_method ) throws IllegalAccessException
        {
            m_method = p_method;
            m_handle = MethodHandles.lookup().unreflect( m_method );
        }

        /**
         * returns the method handle object
         *
         * @return handle object
         */
        public final MethodHandle getHandle()
        {
            return m_handle;
        }

        /**
         * returns the method object
         *
         * @return method object
         */
        public final Method getMethod()
        {
            return m_method;
        }

    }

}
