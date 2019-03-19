/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010-2011 Oracle and/or its affiliates. All rights reserved.
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
package javax.enterprise.concurrent;

import java.util.Map;

/**
 * The ContextService provides methods for creating dynamic proxy objects
 * (as defined by {@link java.lang.reflect.Proxy java.lang.reflect.Proxy}) with
 * the addition of context typically associated with applications executing in a
 * Java&trade; EE environment. 
 * Examples of such context are classloading, namespace, security, etc.
 * <p>
 *
 * The proxy objects follow the same rules as defined for the
 * {@link java.lang.reflect.Proxy java.lang.reflect.Proxy} class with the following additions:
 * <ul>
 * <li>The proxy instance will retain the context of the creator's
 *     thread.
 * <li>The proxy instance will implement all of the interfaces specified on the
 *     {@code createContextualProxy} methods.
 * <li>The object to have a proxy instance created for should not be a 
 *     component managed by the Java&trade; EE Product Provider, such as a web
 *     component or an EJB.
 * <li>All interface method invocations on a proxy instance run in the
 *     creator's context with the exception of {@code hashCode}, 
 *     {@code equals}, {@code toString} and all other methods declared in 
 *     {@link java.lang.Object}.
 * <li>The proxy instance must implement {@link java.io.Serializable}.
 * <li>The proxied object instance must implement
 *     {@link java.io.Serializable} if the proxy instance is serialized.
 * <li>Execution properties can be stored with the proxy instance. Custom
 *     property keys must not begin with "javax.enterprise.concurrent.".
 * <li>Execution properties are to be used for controlling how various contextual
 *     information is retrieved and applied to the thread. Although application
 *     components can store arbitrary property keys and values, it is not
 *     recommended. Java&trade; EE product providers may impose limits to the 
 *     size of the keys and values.
 * </ul>
 * <P>
 * 
 * @since 1.0
 */
public interface ContextService {

  /**
   * Creates a new contextual object proxy for the input object instance.
   * <p>
   * Each method invocation will have the context of the application component
   * instance that created the contextual object proxy.
   * <p>
   * The contextual object is useful when developing or using Java&trade; SE
   * threading mechanisms propagating events to other component instances.
   * <p>
   * If the application component that created the proxy is not started or
   * deployed, all methods on reflected interfaces may throw an
   * {@link java.lang.IllegalStateException}.
   *  <p>
   * For example, to execute a Runnable which is contextualized with the 
   * creator's context using a Java&trade; SE ExecutorService:
   * <P>
   * <pre>
   *  public class MyRunnable implements Runnable {
   *      public void run() {
   *          System.out.println(&quot;MyRunnable.run with Java EE Context available.&quot;);
   *      }
   *  }
   *  
   *  InitialContext ctx = new InitialContext();
   *  ThreadFactory threadFactory = (ThreadFactory) ctx
   *           .lookup(&quot;java:comp/env/concurrent/ThreadFactory&quot;);
   *           
   *  ContextService ctxService = (ContextService) ctx
   *           .lookup(&quot;java:comp/env/concurrent/ContextService&quot;);
   *
   *  MyRunnable myRunnableInstance = ...;
   * 
   *  Runnable rProxy = ctxService.createContextualProxy(myRunnableInstance, Runnable.class);
   *
   *  ExecutorService exSvc = Executors.newThreadPool(10, threadFactory);
   *
   *  Future f = exSvc.submit(rProxy);
   * </pre>
   * <P>
   * 
   * @param instance the instance of the object to proxy.
   * @param intf the interface that the proxy should implement.
   * @return a proxy for the input object that implements the specified interface.
   * @throws java.lang.IllegalArgumentException - if the {@code intf} argument 
   * is null or the instance does not implement the specified 
   * interface.
   *         
   */
  public <T> T createContextualProxy(T instance, Class<T> intf);

  /**
   * Creates a new contextual object proxy for the input object instance.
   * <p>
   * This method is similar to {@code <T> T createContextualProxy(T instance, Class<T> intf)}
   * except that this method can be used if the proxy has to support multiple
   * interfaces.
   *  <p>
   * Example:
   * <P>
   * <pre>
   *  public class MyRunnableWork implements Runnable, SomeWorkInterface {
   *      public void run() {
   *          System.out.println(&quot;MyRunnableWork.run with Java EE Context available.&quot;);
   *      }
   *      public void someWorkInterfaceMethod() {
   *          ...
   *      }
   *  }
   *  
   *  ThreadFactory threadFactory = ...;
   *           
   *  ContextService ctxService = ...;
   *
   *  MyRunnableWork myRunnableWorkInstance = ...;
   * 
   *  Object proxy = ctxService.createContextualProxy(myRunnableWorkInstance, 
   *                                   Runnable.class, SomeWorkInterface.class);
   *
   *  // call SomeWorkInterface method on the proxy
   *  ((SomeWorkInterface) proxy).someWorkInterfaceMethod();
   * 
   *  ExecutorService exSvc = Executors.newThreadPool(10, threadFactory);
   *
   *  // submit the proxy as a Runnable to the ExecutorService 
   *  Future f = exSvc.submit( (Runnable)proxy);
   * </pre>
   * <P>
   * 
   * @param instance the instance of the object to proxy.
   * @param interfaces the interfaces that the proxy should implement.
   * @return a proxy for the input object that implements all of the specified
   *         interfaces.
   * @throws java.lang.IllegalArgumentException - if the {@code interfaces}
   * argument is null or the instance does not implement
   * all the specified interfaces.
   *         
   */
  public Object createContextualProxy(Object instance, Class<?>... interfaces);

  /**
   * Creates a new contextual object proxy for the input object instance.
   * <p>
   * The contextual object is useful when developing or using Java&trade; SE
   * threading mechanisms propagating events to other component instances.
   * <p>
   * If the application component that created the proxy is not started or
   * deployed, all methods on reflected interfaces may throw an
   * {@link java.lang.IllegalStateException}.
   * <p>
   * This method accepts a {@code Map} object which allows the
   * contextual object creator to define what contexts or behaviors to capture
   * when creating the contextual object. The specified properties will remain
   * with the contextual object.
   * <p>
   * 
   * For example, to call a Message Driven Bean (MDB) with the sender's
   * context, but within the MDB's transaction:
   * <P>
   * <pre>
   * public class MyServlet ... {
   *     public void doPost() throws NamingException, JMSException {
   *        InitialContext ctx = new InitialContext();
   *     
   *        // Get the ContextService that only propagates
   *        // security context.
   *        ContextService ctxSvc = (ContextService)
   *            ctx.lookup(&quot;java:comp/env/SecurityContext&quot;);
   *
   *        // Set any custom context data through execution properties
   *        Map&lt;String, String&gt; execProps = new HashMap&lt;&gt;();
   *        execProps.put(&quot;vendor_a.security.tokenexpiration&quot;, &quot;15000&quot;);
   *        // Specify that contextual object should run inside the current 
   *        // transaction.  If we have a failure, we don't want to consume
   *        // the message.
   *        execProps.put(ManagedTask.TRANSACTION, &quot;USE_TRANSACTION_OF_EXECUTION_THREAD&quot;);
   *
   *        ProcessMessage msgProcessor =
   *            ctxSvc.createContextualProxy(new MessageProcessor(), execProps,
   *            ProcessMessage.class);
   *
   *        ConnectionFactory cf = (ConnectionFactory)
   *             ctx.lookup(&quot;java:comp/env/MyTopicConnectionFactory&quot;);
   *        Destination dest = (Destination) ctx.lookup(&quot;java:comp/env/MyTopic&quot;);
   *        Connection con = cf.createConnection();
   *
   *        Session session = con.createSession(true, Session.AUTO_ACKNOWLEDGE);
   *        MessageProducer producer = session.createProducer(dest);
   *
   *        Message msg = session.createObjectMessage((Serializable)msgProcessor);
   *        producer.send(dest, msg);
   *        ...
   *
   *    }
   *
   *  public class MyMDB ... {
   *    public void onMessage(Message msg) {
   *        // Get the ProcessMessage contextual object from the message.
   *        ObjectMessage omsg = (ObjectMessage)msg;
   *        ProcessMessage msgProcessor = (ProcessMessage)omsg.getObject();
   *        
   *        // Process the message in the specified context.
   *        msgProcessor.processMessage(msg);
   *    }
   *  }
   *
   *  public interface  ProcessMessage {
   *      public void processMessage(Message msg);
   *  }
   *
   *  public class MessageProcessor implements ProcessMessage, Serializable {
   *      public void processMessage(Message msg) {
   *          // Process the message with the application container
   *          // context that sent the message.
   *
   *      }
   *  }
   *</pre>
   *<P>
   *
   * @param instance the instance of the object to proxy.
   * @param executionProperties the properties to use when creating and running the context
   *                          object.
   * @param intf the interface that the proxy should implement.
   * @return a proxy for the input object that implements the specified interface.
   *
   * @throws java.lang.IllegalArgumentException - if the {@code intf} argument
   * null or the instance does not implement the specified interface.
   */
  public <T> T createContextualProxy(T instance,
                                     Map<String, String> executionProperties,
                                     Class<T> intf);
  
   /**
   * Creates a new contextual object proxy for the input object instance.
   * <p>
   * This method is similar to {@code <T> T createContextualProxy(T instance, Map<String, String> executionProperties, Class<T> intf)}
   * except that this method can be used if the proxy has to support multiple
   * interfaces.
   *
   * @param instance the instance of the object to proxy.
   * @param executionProperties the properties to use when creating and running the context
   *                          object.
   * @param interfaces the interfaces that the proxy should implement.
   * @return a proxy for the input object that implements all of the specified
   *         interfaces.
   *
   * @throws java.lang.IllegalArgumentException - if the {@code interfaces}
   * argument is null or the instance does not implement all the specified 
   * interfaces.
   */
   public Object createContextualProxy(Object instance,
                                       Map<String, String> executionProperties,
                                       Class<?>... interfaces);
  /**
   * Gets the current execution properties on the context proxy instance.
   * 
   * @param contextualProxy the contextual proxy instance to retrieve the execution properties.
   * @return A copy of the current contextual object execution properties, or null if
   *         the contextualProxy is created without specifying any execution properties.
   * 
   * @throws java.lang.IllegalArgumentException thrown if the input contextualProxy is not a valid 
   *                                            contextual object proxy created with the 
   *                                            {@code createContextualProxy} method.
   */
  public Map<String, String> getExecutionProperties(Object contextualProxy);
}
