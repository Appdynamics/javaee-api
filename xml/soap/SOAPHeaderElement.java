/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2004-2012 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package javax.xml.soap;

/**
 * An object representing the contents in the SOAP header part of the
 * SOAP envelope.
 * The immediate children of a <code>SOAPHeader</code> object can
 * be represented only as <code>SOAPHeaderElement</code> objects.
 * <P>
 * A <code>SOAPHeaderElement</code> object can have other
 * <code>SOAPElement</code> objects as its children.
 */
public interface SOAPHeaderElement extends SOAPElement {

    /**
     * Sets the actor associated with this <code>SOAPHeaderElement</code>
     * object to the specified actor. The default value of an actor is:
     *          <code>SOAPConstants.URI_SOAP_ACTOR_NEXT</code>
     * <P>
     * If this <code>SOAPHeaderElement</code> supports SOAP 1.2 then this call is
     * equivalent to {@link #setRole(String)}
     * 
     * @param  actorURI a <code>String</code> giving the URI of the actor
     *           to set
     *
     * @exception IllegalArgumentException if there is a problem in
     * setting the actor.
     *
     * @see #getActor
     */
    public void setActor(String actorURI);

    /**
     * Sets the <code>Role</code> associated with this <code>SOAPHeaderElement</code>
     * object to the specified <code>Role</code>.
     *
     * @param uri - the URI of the <code>Role</code>
     * 
     * @throws SOAPException if there is an error in setting the role
     * 
     * @exception UnsupportedOperationException if this message does not 
     *      support the SOAP 1.2 concept of Fault Role. 
     * 
     * @since SAAJ 1.3
     */
    public void setRole(String uri) throws SOAPException;

    /**
     * Returns the uri of the <i>actor</i> attribute of this
     * <code>SOAPHeaderElement</code>.
     *<P>
     * If this <code>SOAPHeaderElement</code> supports SOAP 1.2 then this call is
     * equivalent to {@link #getRole()}
     * @return  a <code>String</code> giving the URI of the actor
     * @see #setActor
     */
    public String getActor();

    /**
     * Returns the value of the <i>Role</i> attribute of this
     * <code>SOAPHeaderElement</code>.
     *
     * @return a <code>String</code> giving the URI of the <code>Role</code>
     * 
     * @exception UnsupportedOperationException if this message does not 
     *      support the SOAP 1.2 concept of Fault Role. 
     * 
     * @since SAAJ 1.3
     */
    public String getRole();

    /**
     * Sets the mustUnderstand attribute for this <code>SOAPHeaderElement</code>
     * object to be either true or false.
     * <P>
     * If the mustUnderstand attribute is on, the actor who receives the
     * <code>SOAPHeaderElement</code> must process it correctly. This
     * ensures, for example, that if the <code>SOAPHeaderElement</code>
     * object modifies the message, that the message is being modified correctly.
     *
     * @param mustUnderstand <code>true</code> to set the mustUnderstand
     *        attribute to true; <code>false</code> to set it to false
     *
     * @exception IllegalArgumentException if there is a problem in
     * setting the mustUnderstand attribute
     * @see #getMustUnderstand 
     * @see #setRelay
     */
    public void setMustUnderstand(boolean mustUnderstand);

    /**
     * Returns the boolean value of the mustUnderstand attribute for this
     * <code>SOAPHeaderElement</code>.
     *
     * @return <code>true</code> if the mustUnderstand attribute of this
     *        <code>SOAPHeaderElement</code> object is turned on; <code>false</code>
     *         otherwise
     */
    public boolean getMustUnderstand();
    
    /**
     * Sets the <i>relay</i> attribute for this <code>SOAPHeaderElement</code> to be 
     * either true or false.
     * <P>
     * The SOAP relay attribute is set to true to indicate that the SOAP header 
     * block must be relayed by any node that is targeted by the header block 
     * but not actually process it. This attribute is ignored on header blocks
     * whose mustUnderstand attribute is set to true or that are targeted at 
     * the ultimate reciever (which is the default). The default value of this 
     * attribute is <code>false</code>.
     * 
     * @param relay the new value of the <i>relay</i> attribute
     * 
     * @exception SOAPException if there is a problem in setting the 
     * relay attribute. 
     * @exception UnsupportedOperationException if this message does not 
     *      support the SOAP 1.2 concept of Relay attribute.
     * 
     * @see #setMustUnderstand 
     * @see #getRelay
     * 
     * @since SAAJ 1.3
     */
    public void setRelay(boolean relay) throws SOAPException;

    /**
     * Returns the boolean value of the <i>relay</i> attribute for this 
     * <code>SOAPHeaderElement</code>
     * 
     * @return <code>true</code> if the relay attribute is turned on; 
     * <code>false</code> otherwise
     *
     * @exception UnsupportedOperationException if this message does not 
     *      support the SOAP 1.2 concept of Relay attribute. 
     * 
     * @see #getMustUnderstand 
     * @see #setRelay
     * 
     * @since SAAJ 1.3
     */
    public boolean getRelay();    
}
