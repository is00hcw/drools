/*
 * Copyright 2010 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.core.util.index;

import org.drools.core.common.InternalFactHandle;
import org.drools.core.util.AbstractHashTable.Index;
import org.drools.core.util.Entry;
import org.drools.core.util.FastIterator;
import org.drools.core.util.Iterator;
import org.drools.core.util.LinkedList;
import org.drools.core.reteoo.LeftTuple;
import org.drools.core.reteoo.RightTuple;
import org.drools.core.reteoo.RightTupleMemory;

public class RightTupleList
    implements
    RightTupleMemory,
        Entry {
    private static final long      serialVersionUID = 510l;

    public Entry                   previous;
    public Entry                   next;

    public RightTuple              first;
    public RightTuple              last;

    private final int              hashCode;
    private final Index            index;

    private TupleHashTableIterator iterator;
    
    private int                    size;    

    public RightTupleList() {
        // this is not an index bucket
        this.hashCode = 0;
        this.index = null;
    }

    public RightTupleList(final Index index,
                          final int hashCode) {
        this.index = index;
        this.hashCode = hashCode;
    }
    
    public RightTupleList(final Index index,
                          final int hashCode,
                          final Entry next) {
        this.index = index;
        this.hashCode = hashCode;
        this.next = next;
    }

    public RightTupleList(RightTupleList p, final Entry next) {
        this.index = p.index;
        this.hashCode = p.hashCode;
        this.next = next;
        this.first = p.first;
        this.last = p.last;
    }

    public RightTuple getFirst(LeftTuple leftTuple, InternalFactHandle factHandle, FastIterator rightTupleIterator) {
        return this.first;
    }
    
    public RightTuple getFirst() {
        return this.first;
    }

    public RightTuple getLast() {
        return this.last;
    }
    
    public void removeAdd(final RightTuple rightTuple) {
        remove(rightTuple);
        add(rightTuple);
    }
    
    public void split(final RightTuple rightTuple, int count) {
        this.first = rightTuple;
        rightTuple.setPrevious( null );
        size = size - count;
    }
    
    public void clear() {
        this.first = null;
        this.last = null;
        size = 0;
    }

    public void add(final RightTuple rightTuple) {
        if ( this.last != null ) {
            this.last.setNext( rightTuple );
            rightTuple.setPrevious( this.last );
            this.last = rightTuple;
        } else {
            this.first = rightTuple;
            this.last = rightTuple;
        }
        rightTuple.setMemory( this );
        this.size++;        
    }

    public void insertAfter(RightTuple rightTuple, RightTuple previous) {
        RightTuple next = (RightTuple) previous.getNext();
        previous.setNext(rightTuple);
        rightTuple.setPrevious(previous);
        if (next != null) {
            next.setPrevious(rightTuple);
            rightTuple.setNext(next);
        } else {
            this.last = rightTuple;
        }
        rightTuple.setMemory( this );
    }

    public void insertBefore(RightTuple rightTuple, RightTuple next) {
        RightTuple previous = (RightTuple) next.getPrevious();
        next.setPrevious(rightTuple);
        rightTuple.setNext(next);
        if (previous != null) {
            previous.setNext(rightTuple);
            rightTuple.setPrevious(previous);
        } else {
            this.first = rightTuple;
        }
        rightTuple.setMemory( this );
    }

    /**
     * We assume that this rightTuple is contained in this hash table
     */
    public void remove(final RightTuple rightTuple) {
        RightTuple previous = (RightTuple) rightTuple.getPrevious();
        RightTuple next = (RightTuple) rightTuple.getNext();

        if ( previous != null && next != null ) {
            // remove from middle
            previous.setNext( next );
            next.setPrevious( previous );
        } else if ( next != null ) {
            // remove from first
            this.first = next;
            next.setPrevious( null );
        } else if ( previous != null ) {
            // remove from end
            this.last = previous;
            previous.setNext( null );
        } else {
            // remove everything
            this.last = null;
            this.first = null;
        }
        rightTuple.clear();
        this.size--;
    }

    public RightTuple get(final InternalFactHandle handle) {
        RightTuple current = this.first;
        while ( current != null ) {
            if ( handle == current.getFactHandle() ) {
                return current;
            }
            current = (RightTuple) current.getNext();
        }
        return null;
    }

    public boolean contains(final InternalFactHandle handle) {
        return get( handle ) != null;
    }

    public RightTuple get(final RightTuple rightTuple) {
        InternalFactHandle handle = rightTuple.getFactHandle();
        RightTuple current = this.first;
        while ( current != null ) {
            if ( handle == current.getFactHandle() ) {
                return current;
            }
            current = (RightTuple) current.getNext();
        }
        return null;
    }

    public boolean contains(final RightTuple rightTuple) {
        return get( rightTuple ) != null;
    }

    public int size() {
        return this.size;
    }

    public FastIterator fastIterator() {
        return LinkedList.fastIterator; // contains no state, so ok to be static
    }
    
    public FastIterator fullFastIterator() {
        return LinkedList.fastIterator; // contains no state, so ok to be static
    }
    
    public FastIterator fullFastIterator(RightTuple rightTuple) {
        return LinkedList.fastIterator; // contains no state, so ok to be static
    }
    
    public Iterator iterator() {
        if ( this.iterator == null ) {
            this.iterator = new TupleHashTableIterator();
        }
        this.iterator.reset( this.first );
        return this.iterator;
    }

    public static class TupleHashTableIterator
        implements
        Iterator {
        private RightTuple current;

        public void reset(RightTuple first) {
            this.current = first;
        }

        public Object next() {
            if ( this.current != null ) {
                RightTuple returnValue = this.current;
                this.current = (RightTuple) current.getNext();
                return returnValue;
            } else {
                return null;
            }
        }

        public void remove() {
            // do nothing
        }
    }

    public boolean matches(final Object object,
                           final int objectHashCode) {
        return this.hashCode == objectHashCode && this.index.equal( this.first.getFactHandle().getObject(),
                                                                    object );
    }

    public boolean matches(final LeftTuple tuple,
                           final int tupleHashCode,
                           final InternalFactHandle factHandle) {
        if ( this.hashCode != tupleHashCode ) {
            return false;
        }
        
        if ( this.first.getFactHandle() == factHandle ) {
            RightTuple rightTuple = ( RightTuple ) this.first.getNext();
            if ( rightTuple != null ) {
                return this.index.equal( rightTuple.getFactHandle().getObject(),
                                         tuple );
            }
        }
        
        return this.index.equal( this.first.getFactHandle().getObject(),
                                                                   tuple );
    }
    
    public RightTuple[] toArray() {
        RightTuple[] tuples = new RightTuple[size];

        RightTuple current = first;
        for ( int i = 0; i < size; i++ ) {
            tuples[i] = current;
            current = (RightTuple) current.getNext();
        }

        return tuples;
    }

    public int hashCode() {
        return this.hashCode;
    }

    public boolean equals(final Object object) {
        final RightTupleList other = (RightTupleList) object;
        return this.hashCode == other.hashCode && this.index == other.index;
    }

    public Entry getNext() {
        return this.next;
    }

    public void setNext(final Entry next) {
        this.next = next;
    }

    public boolean isIndexed() {
        return (this.index != null);
    }
    
    public Index getIndex() {
        return this.index;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for ( RightTuple rightTuple = this.first; rightTuple != null; rightTuple = (RightTuple) rightTuple.getNext() ) {
            builder.append( rightTuple );
        }

        return builder.toString();
    }

    public IndexType getIndexType() {
        return IndexType.NONE;
    }

}
