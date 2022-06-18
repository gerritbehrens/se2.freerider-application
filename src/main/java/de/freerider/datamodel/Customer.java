package de.freerider.datamodel;

import java.util.*;


/**
 * Class for entity type Customer. Customer is an individual who acts as holder of a business relationship.
 *
 * @since "0.1.0"
 * @version "0.1.0"
 * @author sgra64
 */

public class Customer {

    /**
     * id attribute, {@code < 0} invalid, can be set only once.
     */
    private long id = -1;

    /**
     * surname, never null, mapped to "" when empty.
     */
    private String lastName = "";

    /**
     * none-surname name parts, never null, mapped to "" when empty.
     */
    private String firstName = "";

    /**
     * contact information with multiple contact entries.
     */
    private List<String> contacts = new ArrayList<String>();

    /**
     * status information of a Customer.
     */
    private Status status = Status.New;


    /**
     * Definition of Customer Status states.
     */
    public enum Status {	// FIX: Customer.Status -> Status, move from end to top
        New,
        InRegistration,
        Active,
        Suspended,
        Deleted
    };


    /**
     * Default constructor
     */
    public Customer() { }


    /**
     * Id getter.
     *
     * @return customer id, may be invalid {@code < 0} if unassigned.
     */
    public long getId() {
        return id;
    }


    /**
     * Id setter. Id can only be set once, id is immutable after being set.
     *
     * @param id set id once if id is valid {@code >= 0} and id attribute
     * is still unassigned {@code < 0}.
     * @return chainable self-reference.
     */
    public Customer setId( long id ) {
        // set id only once; id cannot be changed afterwards
        this.id = ( this.id < 0 && id >= 0 )? id : this.id;
        return this;
    }


    /**
     * Getter that returns single-String name from lastName and firstName
     * attributes in format: {@code "lastName, firstName"} or {@code "lastName"}
     * if {@code firstName} is empty.
     *
     * @return single-String name.
     */
    public String getName() {
        return lastName + ( firstName.length() > 0? ", " + firstName : "" );
    }


    /**
     * FirstName getter.
     *
     * @return value of firstName attribute, never null, mapped to "" when empty.
     */
    public String getFirstName() {
        return firstName;
    }


    /**
     * LastName getter.
     * @return value of lastName attribute, never null, mapped to "" when empty.
     */
    public String getLastName() {
        return lastName;
    }


    /**
     * Setter that splits single-String name (e.g. "Eric Meyer") into first-
     * and lastName parts and assigns parts to corresponding first- and
     * lastName attributes.
     *
     * @param name single-String name to split into first- and lastName parts.
     * @return chainable self-reference.
     */
    public Customer setName( String name ) {
        splitName( name );	// set first and last name from single-string name
        return this;
    }


    /**
     * Name setter for first- and lastName attributes, which are only changed
     * when arguments are not null or not empty "".
     *
     * @param first assigned to firstName attribute when not null or empty "".
     * @param last assigned to lastName attribute when not null or empty "".
     * @return chainable self-reference.
     */
    public Customer setName( String first, String last ) {
        this.firstName = first != null? first.trim() : this.firstName;
        this.lastName = last != null? last.trim() : this.lastName;
        return this;
    }


    /**
     * Return number of contacts.
     *
     * @return number of contacts.
     */
    public int contactsCount() {
        return contacts.size();
    }


    /**
     * Contacts getter (as {@code Iterable<String>}).
     *
     * @return contacts as {@code Iterable<String>}.
     */
    public Iterable<String> getContacts() {
        return contacts;
    }


    /**
     * Add new contact. Only valid contacts (not null or "") are stored.
     * Duplicate contacts are ignored.
     *
     * @param contact contact to add, null, "" or duplicate contacts are ignored.
     * @return chainable self-reference.
     */
    public Customer addContact( String contact ) {
        if( contact != null && contact.length() > 0 ) {
            contact = contact.trim();
            // avoid duplicate entries
            if( ! contacts.contains( contact ) ) {
                contacts.add( contact );
            }
        }
        return this;
    }


    /**
     * Delete the i-th contact if {@code i >= 0} and {@code i < contacts.size()},
     * otherwise method has no effect.\
     *
     * @param i index of contact to delete.
     */
    public void deleteContact( int i ) {
        if( i >= 0 && i < contacts.size() ) {
            contacts.remove( i );
        }
    }


    /**
     * Delete all contacts.
     */
    public void deleteAllContacts() {
        contacts.clear();
    }


    /**
     * Status getter.
     *
     * @return status of customer as defined in enum Status.
     */
    public Customer.Status getStatus() {
        return status;
    }


    /**
     * Status setter.
     *
     * @param status customer status as defined in enum Status.
     * @return chainable self-reference.
     */
    public Customer setStatus( Customer.Status status ) {
        this.status = status;
        return this;
    }


    /*
     * private methods
     */

    /**
     * Split single-String name into first- and last name.
     * Examples:<pre>{@code
     * single-String name             -> lastName,           firstName
     * - "Eric Meyer"                 -> "Meyer",            "Eric"
     * - "Meyer, Anne"                -> "Meyer",            "Anne"
     * - "Meyer; Anne"                -> "Meyer",            "Anne"
     * - "Tim Schulz-Mueller"         -> "Schulz-Mueller",   "Tim"
     * - "Nadine Ulla Blumenfeld"     -> "Blumenfeld",       "Nadine Ulla"
     * - "Nadine-Ulla Blumenfeld"     -> "Blumenfeld",       "Nadine-Ulla"
     * - "Khaled Saad Mohamed Abdelalim" -> "Abdelalim",     "Khaled Saad Mohamed"
     *
     * special cases:
     * - "Meyer"                      -> "Meyer",            ""
     * - ""                           -> "",                 ""
     * - null                         -> "",                 ""
     * }</pre>
     * @param name single-String name split into first- and last name
     */

    /**
     * Split single-String name into first- and last name.
     * @param name single-String name split into first- and last name
     */
    private void splitName( String name ) {
        if( name==null )
            return;			// no change to first- and lastName attributes
        //
        String first = this.firstName;
        String last = this.lastName;
        String[] spl1 = name.split( "[,;]" );
        if( spl1.length > 1 ) {
            // name has separator: [,;]
            for( int i=0; i < spl1.length; i++ ) {
                if( i == 0 ) {
                    last = spl1[0];
                } else {
                    // first += (i > 1? " " : "" ) + spl1[ i ].trim();
                    first = spl1[ i ].trim();
                }
            }
            last = spl1[0].trim();
            first = spl1[ spl1.length -1 ].trim();
            //
        } else {
            // no separator [,;] -> split by white spaces;
            // collect firstNames in order and lastName as last
            for( String s : name.split( "\\s+" ) ) {
                if( last.length() > 0 ) {
                    first += ( first.length()==0? "" : " " ) + last;
                }
                last = s;
            }
        }
        setName( first, last );
    }

}
