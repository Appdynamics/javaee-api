/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
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

package javax.faces.render;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.application.StateManager;
import javax.faces.application.StateManager.SerializedView;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;




/**
 * <p><strong class="changed_modified_2_0 changed_modified_2_2">
 * ResponseStateManager</strong>
 * is the helper class to {@link javax.faces.application.StateManager}
 * that knows the specific rendering technology being used to generate
 * the response.  It is a singleton abstract class, vended by the {@link
 * RenderKit}.  This class knows the mechanics of saving state, whether
 * it be in hidden fields, session, or some combination of the two.</p>
 */

public abstract class ResponseStateManager {
    
    private static Logger log = Logger.getLogger("javax.faces.render");

    /**
     * <p>The name of the request parameter used by the default
     * implementation of {@link
     * javax.faces.application.ViewHandler#calculateRenderKitId} to
     * derive a RenderKit ID.</p>
     */
    public static final String RENDER_KIT_ID_PARAM =
            "javax.faces.RenderKitId";
            
    /**
     * <p><span class="changed_modified_2_0
     * changed_modified_2_2">Implementations</span> must use this
     * constant field value as the name of the client parameter in which
     * to save the state between requests. <span
     * class="changed_added_2_2">The <code>id</code> attribute must be a
     * concatenation of the return from {@link
     * javax.faces.component.UIViewRoot#getContainerClientId}, the
     * return from {@link
     * javax.faces.component.UINamingContainer#getSeparatorChar}, this
     * constant field value, the separator char, and a number that is 
     * guaranteed to be unique with respect to all the other instances of
     * this kind of client parameter in the view.</span>
     * 
     * </span></p>

     * <p class="changed_added_2_0">It is strongly recommend that
     * implementations guard against cross site scripting attacks by at
     * least making the value of this parameter difficult to
     * predict.</p>
     *
     * @since 1.2
     */

    public static final String VIEW_STATE_PARAM = "javax.faces.ViewState";
    
    /**
     * <p class="changed_added_2_2">The name of the hidden field that
     * refers to the encoded ClientWindow.  This field is only used if 
     * {@link javax.faces.lifecycle.ClientWindow#CLIENT_WINDOW_MODE_PARAM_NAME}
     * is not "none". The <code>id</code> attribute must be a
     * concatenation of the return from {@link
     * javax.faces.component.UIViewRoot#getContainerClientId}, the
     * return from {@link
     * javax.faces.component.UINamingContainer#getSeparatorChar}, this
     * constant field value, the separator char, and a number that is 
     * guaranteed to be unique with respect to all the other instances of
     * this kind of client parameter in the view.  The value of this parameter 
     * is the return from {@link javax.faces.lifecycle.ClientWindow#getId}.</p>
     * 
     * @since 2.2
     * 
     */
    
    public static final String CLIENT_WINDOW_PARAM = "javax.faces.ClientWindow";
    
    /**
     * <p class="changed_added_2_2">The name of the URL query parameter for transmitting
     * the client window id.  This parameter is only used if 
     * {@link javax.faces.lifecycle.ClientWindow#CLIENT_WINDOW_MODE_PARAM_NAME}
     * is not "none".  The name of the parameter is given by the constant value 
     * of this field.  The value of this parameter 
     * is the return from {@link javax.faces.lifecycle.ClientWindow#getId}.
     * </p>
     * 
     * @since 2.2
     */
    
    public static final String CLIENT_WINDOW_URL_PARAM = "jfwid";
    
    /**
     * <p class="changed_added_2_2">The value of this constant is taken
     * to be the name of a request parameter whose value is inspected
     * to verify the safety of an incoming non-postback request with respect
     * to the currently configured <code>Set</code> of protected views
     * for this application.</p>
     * 
     * @since 2.2
     */
    
    public static final String NON_POSTBACK_VIEW_TOKEN_PARAM = 
            "javax.faces.Token";
    
    /**       
     * <p><span class="changed_modified_2_2"">Take</span> the argument 
     * <code>state</code> and write it into the
     * output using the current {@link ResponseWriter}, which must be
     * correctly positioned already.</p>
     * 
     * <p class="changed_added_2_2">Call {@link FacesContext#getViewRoot()}.
     * If {@link javax.faces.component.UIComponent#isTransient()}
     * returns {@code true}, take implementation specific action so that the 
     * following call to {@link #isStateless} returns {@code true} and return.
     * Otherwise, proceed as follows.</p>
     *
     * <p>If the state is to be written out to hidden fields, the
     * implementation must take care to make all necessary character
     * replacements to make the Strings suitable for inclusion as an
     * HTTP request paramater.</p>
     *
     * <p>If the state saving method for this application is {@link
     * javax.faces.application.StateManager#STATE_SAVING_METHOD_CLIENT},
     * the implementation <span class="changed_modified_2_2">must</span> 
     * encrypt the state to be saved to the
     * client <span class="changed_modified_2_2">in a tamper evident 
     * manner</span>.</p>
     *
     * <p>If the state saving method for this application is {@link
     * javax.faces.application.StateManager#STATE_SAVING_METHOD_SERVER},
     * and the current request is an <code>Ajax</code> request
     * {@link javax.faces.context.PartialViewContext#isAjaxRequest} returns
     * <code>true</code>), use the current view state identifier if it is
     * available (do not generate a new identifier).</p>
     * 
     * <p>Write out the render kit identifier associated with this 
     * <code>ResponseStateManager</code> implementation with the name
     * as the value of the <code>String</code> constant 
     * <code>ResponseStateManager.RENDER_KIT_ID_PARAM</code>.  The  
     * render kit identifier must not be written if:</p>
     * <ul>
     * <li>it is the default render kit identifier as returned by 
     * {@link javax.faces.application.Application#getDefaultRenderKitId()} or</li>
     * <li>the render kit identfier is the value of 
     * <code>javax.faces.render.RenderKitFactory.HTML_BASIC_RENDER_KIT</code> and 
     * {@link javax.faces.application.Application#getDefaultRenderKitId()} returns <code>null</code>.
     * </li>
     * </ul> 
     *
     * <p>For backwards compatability with existing
     * <code>ResponseStateManager</code> implementations, the default
     * implementation of this method checks if the argument is an
     * instance of <code>SerializedView</code>.  If so, it calls through
     * to {@link
     * #writeState(javax.faces.context.FacesContext,javax.faces.application.StateManager.SerializedView)}.
     * If not, it expects the state to be a two element Object array.  It creates 
     * an instance of <code>SerializedView</code> and
     * stores the state as the treeStructure, and passes it to {@link
     * #writeState(javax.faces.context.FacesContext,javax.faces.application.StateManager.SerializedView)}.</p>
     * 
     * <p class="changed_added_2_2">The {@link
     * javax.faces.lifecycle.ClientWindow} must be written using these
     * steps.  Call {@link
     * javax.faces.context.ExternalContext#getClientWindow}.  If the
     * result is <code>null</code>, take no further action regarding the
     * <code>ClientWindow</code>.  If the result is
     * non-<code>null</code>, write a hidden field whose name is {@link
     * #CLIENT_WINDOW_PARAM} and whose id is
     * <code>&lt;VIEW_ROOT_CONTAINER_CLIENT_ID&gt;&lt;SEP&gt;javax.faces.ClientWindow&lt;SEP&gt;&lt;UNIQUE_PER_VIEW_NUMBER&gt;</code>
     * where &lt;SEP&gt; is the currently configured
     * <code>UINamingContainer.getSeparatorChar()</code>.
     * &lt;VIEW_ROOT_CONTAINER_CLIENT_ID&gt; is the return from
     * <code>UIViewRoot.getContainerClientId()</code> on the view from
     * whence this state originated.  &lt;UNIQUE_PER_VIEW_NUMBER&gt; is
     * a number that must be unique within this view, but must not be
     * included in the view state.  The value of the field is implementation
     * dependent but must uniquely identify this window within the user's session.</p>
     *
     *
     * @since 1.2
     *
     * @param context The {@link FacesContext} instance for the current request
     * @param state The serialized state information previously saved
     * @throws IOException if the state argument is not an array of length 2.
     *
     */
    public void writeState(FacesContext context,
        Object state) throws IOException {
        
	SerializedView view;
	if (state instanceof SerializedView) {
	    view = (SerializedView) state;
	}
	else {
            if (state instanceof Object[]) {
                Object[] stateArray = (Object[])state;
                if (2 == stateArray.length) {
	            StateManager stateManager = 
		        context.getApplication().getStateManager();
	            view = stateManager.new SerializedView(stateArray[0], 
                        stateArray[1]);
                } else {
                    //PENDING - I18N
                    if (log.isLoggable(Level.SEVERE)) {
                        log.log(Level.SEVERE, "State is not an expected array of length 2.");
                    }
                    throw new IOException("State is not an expected array of length 2.");
                }
            } else {
                //PENDING - I18N
                if (log.isLoggable(Level.SEVERE)) {
                    log.log(Level.SEVERE, "State is not an expected array of length 2.");
                }
                throw new IOException("State is not an expected array of length 2.");
            }
	}
	writeState(context, view);
    }

    /**
     * <p>Take the argument <code>state</code> and write it into
     * the output using the current {@link ResponseWriter}, which
     * must be correctly positioned already.</p>
     *
     * <p>If the {@link
     * javax.faces.application.StateManager.SerializedView} is to be
     * written out to hidden fields, the implementation must take care
     * to make all necessary character replacements to make the Strings
     * suitable for inclusion as an HTTP request paramater.</p>
     *
     * <p>If the state saving method for this application is {@link
     * javax.faces.application.StateManager#STATE_SAVING_METHOD_CLIENT},
     * the implementation may encrypt the state to be saved to the
     * client.  We recommend that the state be unreadable by the client,
     * and also be tamper evident.  The reference implementation follows
     * these recommendations.  </p>
     *
     * @deprecated This method has been replaced by {@link
     * #writeState(javax.faces.context.FacesContext,java.lang.Object)}.
     * The default implementation creates a two element
     * <code>Object</code> array with the first element being the return
     * from calling {@link SerializedView#getStructure}, and the second
     * being the return from {@link SerializedView#getState}.  It then
     * passes this <code>Object</code> array to {@link #writeState}.
     * 
     * @param context The {@link FacesContext} instance for the current request
     * @param state The serialized state information previously saved
     *
     */
    public void writeState(FacesContext context,
                           SerializedView state) throws IOException {

        if (state != null) {
            writeState(context, new Object[]{state.getStructure(),
                                             state.getState()});
        }
        
    }
    
    /**
     * <p class="changed_added_2_2">If the preceding call to {@link #writeState(javax.faces.context.FacesContext, java.lang.Object)}
     * was stateless, return {@code true}.  If the preceding call to {@code writeState()} was
     * stateful, return {@code false}.  Otherwise throw {@code IllegalStateException}.</p>
     * 
     * <div class="changed_added_2_2">
     * 
     * <p>To preserve backward compatibility
     * with custom implementations that may have extended from an earlier
     * version of this class, an implementation is provided that returns 
     * <code>false</code>.  A compliant implementation must override this 
     * method to take the specified action.</p>
     * 
     * </div>
     * 
     * @param context The {@link FacesContext} instance for the current request
     * @param viewId View identifier of the view to be restored
     * @throws NullPointerException if the argument {@code context} is {@code null}.
     * @throws IllegalStateException if this method is invoked and the statefulness
     * of the preceding call to {@link #writeState(javax.faces.context.FacesContext, java.lang.Object)}
     * cannot be determined.
     * 
     * @since 2.2
     * 
     *  
     */
    
    public boolean isStateless(FacesContext context, String viewId) {
        return false;
    }
    
    /**
     * <p><span class="changed_modified_2_2">The</span> implementation must 
     * inspect the current request and return
     * an Object representing the tree structure and component state
     * passed in to a previous invocation of {@link
     * #writeState(javax.faces.context.FacesContext,java.lang.Object)}.</p>
     * 
     * <p class="changed_added_2_2">If the state saving method for this 
     * application is {@link
     * javax.faces.application.StateManager#STATE_SAVING_METHOD_CLIENT},
     * <code>writeState()</code> will have encrypted the state in a tamper
     * evident manner.  If the state fails to decrypt, or decrypts but 
     * indicates evidence of tampering, a 
     * {@link javax.faces.application.ProtectedViewException} must be thrown.</p>
     *
     * <p>For backwards compatability with existing
     * <code>ResponseStateManager</code> implementations, the default
     * implementation of this method calls {@link
     * #getTreeStructureToRestore} and {@link
     * #getComponentStateToRestore} and creates and returns a two
     * element <code>Object</code> array with element zero containing
     * the <code>structure</code> property and element one containing
     * the <code>state</code> property of the
     * <code>SerializedView</code>.</p>
     *
     * @since 1.2
     *
     * @param context The {@link FacesContext} instance for the current request
     * @param viewId View identifier of the view to be restored
     *
     * @return the tree structure and component state Object passed in
     * to <code>writeState</code>.  If this is an initial request, this
     * method returns <code>null</code>.
     */
 
    public Object getState(FacesContext context, String viewId) {
	Object stateArray[] = { getTreeStructureToRestore(context, viewId),
				getComponentStateToRestore(context) };
	return stateArray;
    }


    /**
     * <p>The implementation must inspect the current request and return
     * the tree structure Object passed to it on a previous invocation of
     * <code>writeState()</code>.</p>
     *
     * @deprecated This method has been replaced by {@link #getState}.
     * The default implementation returns <code>null</code>.
     *
     * @param context The {@link FacesContext} instance for the current request
     * @param viewId View identifier of the view to be restored
     *
     */
    public Object getTreeStructureToRestore(FacesContext context, 
					    String viewId) {
	return null;
    }


    /**
     * <p>The implementation must inspect the current request and return
     * the component state Object passed to it on a previous invocation
     * of <code>writeState()</code>.</p>
     *
     * @deprecated This method has been replaced by {@link #getState}.
     * The default implementation returns <code>null</code>.
     *
     * @param context The {@link FacesContext} instance for the current request
     *
     */
    public Object getComponentStateToRestore(FacesContext context) {
	return null;
    }

    /**
     * <p>Return true if the current request is a postback.  This method
     * is leveraged from the <i>Restore View Phase</i> to determine if
     * {@link javax.faces.application.ViewHandler#restoreView} or {@link
     * javax.faces.application.ViewHandler#createView} should be called.
     * The default implementation must return <code>true</code> if this
     * <code>ResponseStateManager</code> instance wrote out state on a
     * previous request to which this request is a postback,
     * <code>false</code> otherwise.</p>
     *
     * <p>The implementation of this method for the Standard HTML
     * RenderKit must consult the {@link
     * javax.faces.context.ExternalContext}'s
     * <code>requestParameterMap</code> and return <code>true</code> if
     * and only if there is a key equal to the value of the symbolic
     * constant {@link #VIEW_STATE_PARAM}.</p>
     *
     * <p>For backwards compatability with implementations of
     * <code>ResponseStateManager</code> prior to JSF 1.2, a default
     * implementation is provided that consults the {@link
     * javax.faces.context.ExternalContext}'s <code>requestParameterMap</code> and return
     * <code>true</code> if its size is greater than 0.</p>
     *
     * @since 1.2
     */

    public boolean isPostback(FacesContext context) {
        return (!context.getExternalContext().getRequestParameterMap().isEmpty());
    }

    /**
     * <p>
     * Return the specified state as a <code>String</code> without any markup
     * related to the rendering technology supported by this ResponseStateManager.
     * </p>
     * 
     * @param context the {@link FacesContext} for the current request
     * @param state the state from which the String version will be generated
     *  from
     * @return the view state for this request without any markup specifics
     *
     * @since 2.0
     */
    public String getViewState(FacesContext context, Object state) {
        return null;
    }
    
    /**
     * <p class="changed_added_2_2">Compliant implementations must return a 
     * cryptographically strong token for use to protect views in this 
     * application. For backwards compatability with earlier revisions, a
     * default implementation is provided that simply returns <code>null</code>.
     * </p>
     * 
     * @param context the {@link FacesContext} for the current request
     * 
     * @return a cryptographically strong value
     *
     * @since 2.2
     */
    public String getCryptographicallyStrongTokenFromSession(FacesContext context) {
        return null;
    }
}
