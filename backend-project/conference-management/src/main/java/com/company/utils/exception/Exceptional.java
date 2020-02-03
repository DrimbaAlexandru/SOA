package com.company.utils.exception;

import java.util.function.Consumer;

/**
 * Created by AlexandruD on 06-Jun-17.
 */
public class Exceptional< T >
{

    private enum State
    {
        OK, EXCEPTION
    }

    private State state;
    private T data;
    private Exception exception;

    private Exceptional( State state, T data, Exception exception )
    {
        this.state = state;
        this.data = data;
        this.exception = exception;
    }

    public static < F > Exceptional< F > OK( F data )
    {
        return new Exceptional<>( State.OK, data, null );
    }

    public static < F > Exceptional< F > Error( Exception exception )
    {
        return new Exceptional<>( State.EXCEPTION, null, exception );
    }

    public boolean isException()
    {
        return state.equals( State.EXCEPTION );
    }

    public boolean isOK()
    {
        return state.equals( State.OK );
    }

//    public Exceptional< T > ok( Consumer< T > consumer )
//    {
//        if( isException() )
//        {
//            return this;
//        }
//        consumer.accept( this.data );
//        return this;
//    }
//
//    public Exceptional< T > error( Consumer< Exception > consumer )
//    {
//        if( isOK() )
//        {
//            return this;
//        }
//        consumer.accept( this.exception );
//        return this;
//    }

    public Exception getException()
    {
        if( isException() )
        {
            return exception;
        }
        else
        {
            return null;
        }
    }

    public T getData()
    {
        if( isOK() )
        {
            return data;
        }
        else
        {
            return null;
        }
    }
}
